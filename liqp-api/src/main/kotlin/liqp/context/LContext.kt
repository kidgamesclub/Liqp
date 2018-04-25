package liqp.context

import liqp.LLogic
import liqp.PropertyContainer
import liqp.config.MutableRenderSettings
import liqp.config.RenderSettingsSpec
import liqp.node.LTemplate
import java.awt.image.renderable.RenderContext
import java.util.*
import kotlin.reflect.KProperty

interface LContext : LLogic, PropertyContainer, RenderSettingsSpec {
  var result: Any?
  val logs: MutableList<Any>
  var locale: Locale
  val current: LFrame
  val loopState: LoopState
  val root: LFrame

  fun applyRenderSettings(configure: MutableRenderSettings.() -> Unit): RenderContext
  fun withRenderSettings(configurer: (MutableRenderSettings) -> MutableRenderSettings): RenderContext
  fun pushFrame(): LFrame
  fun popFrame(): LFrame
  fun withFrame(block: ()->Unit)

  operator fun set(varName: String, value: Any?)
  operator fun <T> get(propName: String): T?
  operator fun <T> getValue(ctx: LContext, prop: KProperty<*>): T?

  fun startLoop(length: Int, name: String? = null)
  fun incrementIterations()
  fun endLoop()

  fun setRoot(varName: String, value: Any)
  fun hasVar(name: String): Boolean
  fun remove(varName: String): Any?
  fun render(template: LTemplate): String
}
