package liqp.nodes

import liqp.Getter
import liqp.LLogic
import liqp.LParser
import liqp.LRenderer
import liqp.PropertyContainer
import liqp.RenderFrame
import liqp.config.MutableRenderSettings
import liqp.config.ParseSettingsSpec
import liqp.config.RenderSettings
import liqp.config.RenderSettingsSpec
import liqp.context.LContext
import liqp.context.LoopState
import liqp.exceptions.ExceededMaxIterationsException
import liqp.exceptions.LiquidRenderingException
import liqp.isFalsy
import liqp.isTruthy
import liqp.lookup.HasProperties
import liqp.lookup.PropertyAccessors
import liqp.lookup.propertyContainer
import liqp.node.LTemplate
import liqp.parseJSON
import java.io.File
import java.time.ZoneId
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

val FORLOOP = "forloop"

@Suppress("UNCHECKED_CAST")
data class RenderContext
@JvmOverloads constructor(private val rawInputData: Any?,
                          override var result:Any? = null,
                          val logic:LLogic,
                          val parser: LParser,
                          val renderer: LRenderer,
                          val accessors: PropertyAccessors = PropertyAccessors.newInstance(),
                          val settings: RenderSettings = renderer.settings)
  : RenderSettingsSpec by settings,
    ParseSettingsSpec by parser,
    LContext, LLogic by logic {

  override val includesDir = settings.includesDir
  override val isStrictVariables = settings.isStrictVariables
  override val baseDir = settings.baseDir
  private var iterationCount: Int = 0

  override val logs = mutableListOf<Any>()
  override var locale: Locale by delegated(Locale.US)
  override var zoneId: ZoneId by delegated(ZoneId.systemDefault())
  private val stack: Deque<RenderFrame> by lazy { ArrayDeque<RenderFrame>() }

  val inputData: PropertyContainer by lazy {
    when (rawInputData) {
      is String -> rawInputData.parseJSON()::get
      is HasProperties -> { prop -> rawInputData.get(prop) }
      is Map<*, *> -> (rawInputData as Map<String, Any>)::get
      is Pair<*, *> -> propertyContainer(rawInputData.first, rawInputData.second)
      else -> accessors.propertyContainer(isStrictVariables, rawInputData)
    }
  }

  private var _current: RenderFrame? = null
  override var current: RenderFrame
    get() {
      if (this._current == null) {
        this._current = RenderFrame(mutableSetOf())
        stack.push(this._current)
      }
      return this._current!!
    }
    set(value) {
      _current = value
    }

  override fun incrementIterations() {
    if (++iterationCount > maxIterations) {
      throw ExceededMaxIterationsException(maxIterations)
    }
    loopState.increment()
  }

  override fun isTrue(value: Any?): Boolean {
    return when {
      isUseTruthyChecks -> value.isTruthy()
      else -> logic.isTrue(value)
    }
  }

  override fun isFalse(value: Any?): Boolean {
    return when {
      isUseTruthyChecks -> value.isFalsy()
      else -> logic.isTrue(value)
    }
  }

  override fun applyRenderSettings(configure: MutableRenderSettings.() -> Unit): LContext {
    val renderSettings = settings.toMutableRenderSettings().apply(configure).build()
    return this.copy(renderer = renderer.withRenderSettings(renderSettings),
        settings = renderSettings)
  }

  override fun withRenderSettings(configurer: (MutableRenderSettings) -> MutableRenderSettings): LContext {
    val renderSettings = configurer(settings.toMutableRenderSettings()).build()
    return this.copy(renderer = renderer.withRenderSettings(renderSettings),
        settings = renderSettings)
  }

  override fun pushFrame(): RenderFrame {
    if (stack.size + 1 > maxStackSize) {
      throw LiquidRenderingException("Stack limit exceeded: $maxStackSize")
    }
    val frame = RenderFrame(current.allVars)
    this.current = frame
    stack.push(frame)
    return frame
  }

  override fun popFrame(): RenderFrame {
    val popped = stack.pop()
    this.current = stack.peek()
    return popped
  }

  override operator fun set(varName: String, value: Any?) {
    when {
      current.hasVar(varName) -> current.set(varName, value)
      current.hasScopedVar(varName) -> {
        val frames = stack.iterator()
        frames.next() //Already looked at the head frame
        while (frames.hasNext()) {
          val frame = frames.next()
          if (frame.hasVar(varName)) {
            frame.set(varName, value)
            return
          }
        }
      }
      else -> current.set(varName, value)
    }
  }

  fun <T:Any> delegated(default: T?): ReadWriteProperty<RenderContext, T> {
    return object : ReadWriteProperty<RenderContext, T> {
      override fun getValue(thisRef: RenderContext, property: KProperty<*>): T {
        val value: T? = thisRef[property.name]
        return value
            ?: default
            ?: throw NullPointerException("Value for ${property.name} is null, with no default specified")
      }

      override fun setValue(thisRef: RenderContext, property: KProperty<*>, value: T) {
        thisRef[property.name] = value
      }
    }
  }

  override operator fun <T:Any> get(propName: String, supplier: () -> T): T? {
    return current[propName, supplier] as T
  }

  override operator fun <T:Any> get(propName: String): T? {
    if (FORLOOP == propName) {
      return current.loop as T
    }

    val frameVal = current[propName]
    if (frameVal != null) {
      return frameVal as T
    }

    if (current.hasScopedVar(propName)) {
      val frames = stack.iterator()
      frames.next() //Already looked at the head frame
      while (frames.hasNext()) {
        val frame = frames.next()
        if (frame.hasVar(propName)) {
          return frame.get(propName) as T
        }
      }
    }

    val inputVal = inputData.invoke(propName)
    if (inputVal != null) {
      return inputVal as T
    }

    return null
  }

  override fun startLoop(length: Int, name: String?) {
    current.loop = LoopState(length, name)
  }

  override fun setRoot(varName: String, value: Any) {
    val frames = stack.iterator()
    var frame = current
    while (frames.hasNext()) {
      frame = frames.next()
      if (frames.hasNext()) {
        frame.addScopedVar(varName)
      }
    }
    frame.set(varName, value)
  }

  override val loopState: LoopState get() = current.loop
  override val root: RenderFrame get() = stack.last()

  override fun endLoop() = current.endLoop()
  override fun <T:Any> getValue(ctx: LContext, prop: KProperty<*>): T? = ctx[prop.name]
  override fun hasVar(name: String): Boolean = current.hasVar(name) || current.hasScopedVar(name)
  override fun remove(varName: String): Any? = current.remove(varName)
  override fun parseFile(file: File): LTemplate = parser.parseFile(file)
  override fun render(template: LTemplate): String = renderer.render(template, this)
  override fun invoke(propName: String): Any? = get(propName)
  override fun getAccessor(container: Any, prop: String): Getter<Any> = renderer.getAccessor(container, prop)

  override fun withFrame(block: () -> Unit) {
    pushFrame()
    try {
      block()
    } finally {
      popFrame()
    }
  }
}


