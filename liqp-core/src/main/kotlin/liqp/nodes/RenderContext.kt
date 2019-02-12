package liqp.nodes

import lang.exception.illegalState
import lang.json.JsrObject
import liqp.Getter
import liqp.LLogic
import liqp.LParser
import liqp.LRenderer
import liqp.PropertyGetter
import liqp.RenderFrame
import liqp.TypeCoersion
import liqp.child
import liqp.config.MutableRenderSettings
import liqp.config.ParseSettings
import liqp.config.RenderSettings
import liqp.context.LContext
import liqp.context.LoopState
import liqp.exceptions.ExceededMaxIterationsException
import liqp.exceptions.LiquidRenderingException
import liqp.isFalsy
import liqp.isTruthy
import liqp.lookup.JsonPropertyGetter
import liqp.lookup.MapPropertyGetter
import liqp.lookup.PropertyAccessors
import liqp.lookup.propertyContainer
import liqp.node.LTemplate
import liqp.parseJSON
import java.io.File
import java.time.ZoneId
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

const val FORLOOP = "forloop"

@Suppress("UNCHECKED_CAST")
data class RenderContext @JvmOverloads constructor(private val rawInputData: Any?,
                                                   override var result: Any? = null,
                                                   val logic: LLogic,
                                                   val parser: LParser,
                                                   val renderer: LRenderer,
                                                   val accessors: PropertyAccessors = PropertyAccessors.newInstance(),
                                                   override val renderSettings: RenderSettings = renderer.settings)
  : LContext, LLogic by logic {

  override val parseSettings: ParseSettings = parser.settings
  override val coersion: TypeCoersion = TypeCoersion(logic, logic)
  override val includeFile = parseSettings.baseDir.child(parseSettings.includesDir)
  private var iterationCount: Int = 0
  override val logs = mutableListOf<Any>()
  override var locale: Locale by delegated(Locale.US)
  override var zoneId: ZoneId by delegated(ZoneId.systemDefault())
  private val stack: Deque<RenderFrame> by lazy { ArrayDeque<RenderFrame>() }

  private val inputData: Any? by lazy {
    when (rawInputData) {
      null -> null
      is String -> rawInputData.parseJSON()
      else -> rawInputData
    }
  }

  private val inputProperties: PropertyGetter by lazy {
    when (val input = inputData) {
      is PropertyGetter -> input
      is JsrObject -> JsonPropertyGetter
      is Map<*, *> -> MapPropertyGetter
      is Pair<*, *> -> propertyContainer(input.first.toString(), input.second)
      else -> accessors.propertyContainer(this, input)
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
    if (++iterationCount > renderSettings.maxIterations) {
      throw ExceededMaxIterationsException(renderSettings.maxIterations)
    }
    loopState.increment()
  }

  override fun isTrue(t: Any?): Boolean {
    return when {
      renderSettings.isUseTruthyChecks -> t.isTruthy()
      else -> logic.isTrue(t)
    }
  }

  override fun isFalse(t: Any?): Boolean {
    return when {
      renderSettings.isUseTruthyChecks -> t.isFalsy()
      else -> logic.isFalse(t)
    }
  }

  override fun applyRenderSettings(configure: MutableRenderSettings.() -> Unit): LContext {
    val renderSettings = renderSettings.toMutableRenderSettings().apply(configure).build()
    return this.copy(renderer = renderer.withRenderSettings(renderSettings),
        renderSettings = renderSettings)
  }

  override fun withRenderSettings(configurer: (MutableRenderSettings) -> MutableRenderSettings): LContext {
    val renderSettings = configurer(renderSettings.toMutableRenderSettings()).build()
    return this.copy(renderer = renderer.withRenderSettings(renderSettings),
        renderSettings = renderSettings)
  }

  override fun pushFrame(): RenderFrame {
    if (stack.size + 1 > renderSettings.maxStackSize) {
      throw LiquidRenderingException("Stack limit exceeded: ${renderSettings.maxStackSize}")
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
      current.hasVar(varName) -> current[varName] = value
      current.hasScopedVar(varName) -> {
        val frames = stack.iterator()
        frames.next() //Already looked at the head frame
        while (frames.hasNext()) {
          val frame = frames.next()
          if (frame.hasVar(varName)) {
            frame[varName] = value
            return
          }
        }
      }
      else -> current[varName] = value
    }
  }

  fun <T : Any> delegated(default: T?): ReadWriteProperty<RenderContext, T> {
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

  override operator fun <T : Any> get(propName: String, supplier: () -> T): T {
    return current[propName, supplier] as T
  }

  override fun <T : Any> getValue(propName: String): T? {
    return this[propName]
  }

  override operator fun <T : Any> get(propName: String): T? {
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
          return frame[propName] as T
        }
      }
    }
    return when (val input = inputData) {
      null -> null
      else -> inputProperties.getterOrNull(propName)?.invoke(input) as T?
    }
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
    frame[varName] = value
  }

  override val loopState: LoopState get() = current.loop
  override val root: RenderFrame get() = stack.last()

  override fun endLoop() = current.endLoop()
  override fun <T : Any> getValue(ctx: LContext, prop: KProperty<*>): T? = ctx[prop.name]
  override fun hasVar(name: String): Boolean = current.hasVar(name) || current.hasScopedVar(name)
  override fun remove(varName: String): Any? = current.remove(varName)
  override fun parseFile(file: File): LTemplate = parser.parseFile(file)
  override fun render(template: LTemplate): String = renderer.render(template, this)
  override fun getAccessor(container: Any, prop: String): Getter<Any> = renderer.getAccessor(this, container, prop)

  override fun withFrame(block: () -> Any?): Any? {
    pushFrame()
    try {
      return block()
    } finally {
      popFrame()
    }
  }

  override fun reset(): LContext = this.copy(result = null)
}


