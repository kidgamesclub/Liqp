package liqp.nodes

import liqp.LoopState
import liqp.RenderFrame
import liqp.RenderSettings
import liqp.RenderSettingsSpec
import liqp.Template
import liqp.LiquidRenderer
import liqp.LiquidParser
import liqp.exceptions.ExceededMaxIterationsException
import liqp.exceptions.LiquidRenderingException
import liqp.isFalse
import liqp.isFalsy
import liqp.isTrue
import liqp.isTruthy
import liqp.lookup.HasProperties
import liqp.lookup.PropertyAccessors
import liqp.lookup.PropertyContainer
import liqp.lookup.propertyContainer
import liqp.parseJSON
import liqp.tags.Tag
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

val FORLOOP = "forloop"

@Suppress("UNCHECKED_CAST")
class RenderContext
@JvmOverloads constructor(inputData: Any?,
                          val parser: LiquidParser,
                          val engine: LiquidRenderer,
                          val accessors: PropertyAccessors = PropertyAccessors.newInstance(),
                          val settings:RenderSettings = engine.settings)
  : HasProperties, RenderSettingsSpec by settings {

  val inputData: PropertyContainer by lazy {
    when (inputData) {
      is String -> inputData.parseJSON()::get
      is HasProperties -> { prop -> inputData.get(prop) }
      is Map<*, *> -> (inputData as Map<String, Any>)::get
      is Pair<*, *> -> propertyContainer(inputData.first, inputData.second)
      else -> accessors.propertyContainer(isStrictVariables, inputData)
    }
  }

  var locale:Locale by delegated(Locale.US)

  fun <I> getTagStack(tag: Tag): Deque<I> {
    val varName = "stack:${tag.name}"
    val stack = rootFrame.get(varName) as Deque<I>?
    return when {
      stack != null -> stack
      else -> ArrayDeque<I>().apply { rootFrame.set(varName, this) }
    }
  }

  fun <I> pushTagStack(tag: Tag, i: I): Deque<I> {
    return getTagStack<I>(tag).also { it.addLast(i) }
  }

  fun <I> popTagStack(tag: Tag): Deque<I> {
    return getTagStack<I>(tag).also { it.removeLast() }
  }

  private val stack: Deque<RenderFrame> by lazy {
    ArrayDeque<RenderFrame>()
  }

  private var _current: RenderFrame? = null
  var current: RenderFrame
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

  private var iterationCount: Int = 0

  fun incrementIterations() {
    if (++iterationCount > maxIterations) {
      throw ExceededMaxIterationsException(maxIterations)
    }

    loopState.increment()
  }

  fun isTrue(value: Any?): Boolean {
    return when {
      isUseTruthyChecks -> value.isTruthy()
      else -> value.isTrue()
    }
  }

  fun isFalse(value: Any?): Boolean {
    return when {
      isUseTruthyChecks -> value.isFalsy()
      else -> value.isFalse()
    }
  }

  fun pushFrame(): RenderFrame {
    if (stack.size + 1 > maxStackSize) {
      throw LiquidRenderingException("Stack limit exceeded: $maxStackSize")
    }
    val frame = RenderFrame(current.allVars)
    this.current = frame
    stack.push(frame)
    return frame
  }

  fun popFrame(): RenderFrame {
    val popped = stack.pop()
    this.current = stack.peek()
    return popped
  }

  operator fun set(varName: String, value: Any?) {
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

  private fun <T>  delegated(default:T? = null):ReadWriteProperty<RenderContext, T> {
    return object:ReadWriteProperty<RenderContext, T> {
      override fun getValue(thisRef: RenderContext, property: KProperty<*>): T {
        return thisRef[property.name] ?: default ?: throw NullPointerException("Value for ${property.name} is null, with no default specified")
      }

      override fun setValue(thisRef: RenderContext, property: KProperty<*>, value: T) {
        thisRef[property.name] = value
      }
    }
  }

  operator fun <T> getValue(ctx:RenderContext, prop:KProperty<*>): T? {
    return ctx[prop.name]
  }

  override operator fun <T> get(propName: String): T? {
    if (FORLOOP == propName) {
      return current.loop as T
    }

    val frameVal = current.get(propName)
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

  val loopState: LoopState
    get() = current.loop

  fun startLoop(length: Int, name: String? = null) {
    current.loop = LoopState(length, name)
  }

  fun endLoop() = current.endLoop()

  fun setRoot(varName: String, value: Any) {
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

  val rootFrame: RenderFrame
    get() {
      return stack.last()
    }


  fun hasVar(name: String): Boolean {
    return current.hasVar(name) || current.hasScopedVar(name)
  }

  fun remove(varName: String): Any? {
    return current.remove(varName)
  }

  fun render(template: Template) = engine.render(template, this)
}


