package liqp.nodes

import liqp.LoopState
import liqp.RenderFrame
import liqp.Template
import liqp.TemplateEngine
import liqp.TemplateFactory
import liqp.exceptions.ExceededMaxIterationsException
import liqp.exceptions.LiquidRenderingException
import liqp.lookup.HasProperties
import liqp.lookup.PropertyAccessors
import liqp.lookup.PropertyContainer
import liqp.lookup.propertyContainer
import liqp.parseJSON
import liqp.tags.Tag
import java.util.*

val FORLOOP = "forloop"
val stack = "stack"

class RenderContext(input: Any?,
                    val accessors: PropertyAccessors = PropertyAccessors.newInstance(),
                    val maxIterations: Int = Integer.MAX_VALUE,
                    val templateFactory: TemplateFactory,
                    val engine: TemplateEngine,
                    val isStrictVariables: Boolean = false,
                    val maxStackSize: Int = 100,
                    val maxSizeRenderedString:Int = Integer.MAX_VALUE): HasProperties {

  val inputData: PropertyContainer by lazy {
    when (input) {
      is String-> input.parseJSON()::get
      is HasProperties -> (input as HasProperties)::getProperty
      is Map<*, *> -> (input as Map<String, Any>)::get
      is Pair<*, *> -> propertyContainer(input.first, input.second)
      else -> accessors.propertyContainer(isStrictVariables, input)
    }
  }

  fun <I> getTagStack(tag: Tag):Deque<I> {
    val varName = "stack:${tag.name}"
    val stack = rootFrame.get(varName) as Deque<I>?
    return when {
      stack != null -> stack
      else-> {
        val stack = ArrayDeque<I>()
        rootFrame.set(varName, stack)
        stack
      }
    }
  }

  fun <I> pushTagStack(tag: Tag, i:I):Deque<I> {
    return getTagStack<I>(tag).also { it.addLast(i) }
  }

  fun <I> popTagStack(tag: Tag):Deque<I> {
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

  fun addFrame(): RenderFrame {
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

  fun set(varName: String, value: Any?): Unit {
    if (current.hasVar(varName)) {
      current.set(varName, value)
    } else if (current.hasScopedVar(varName)) {
      val frames = stack.iterator()
      frames.next() //Already looked at the head frame
      while (frames.hasNext()) {
        val frame = frames.next()
        if (frame.hasVar(varName)) {
          frame.set(varName, value)
          return
        }
      }
    } else {
      current.set(varName, value)
    }
  }

  override fun getProperty(propName: String): Any? {
    return this.get(propName)
  }

  fun <T> get(varName: String): T? {
    if (FORLOOP == varName) {
      return current.loop as T
    }

    val frameVal = current.get(varName)
    if (frameVal != null) {
      return frameVal as T
    }

    if (current.hasScopedVar(varName)) {
      val frames = stack.iterator()
      frames.next() //Already looked at the head frame
      while (frames.hasNext()) {
        val frame = frames.next()
        if (frame.hasVar(varName)) {
          return frame.get(varName) as T
        }
      }
    }

    val inputVal = inputData.invoke(varName)
    if (inputVal != null) {
      return inputVal as T
    }

    return null
  }

  val loopState: LoopState
    get() = current.loop

  fun startLoop(length: Int, name:String? = null) {
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
