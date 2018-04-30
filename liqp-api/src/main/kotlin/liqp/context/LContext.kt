package liqp.context

import liqp.Getter
import liqp.LLogic
import liqp.PropertyContainer
import liqp.config.MutableRenderSettings
import liqp.config.ParseSettingsSpec
import liqp.config.RenderSettingsSpec
import liqp.node.LTemplate
import java.awt.image.renderable.RenderContext
import java.io.File
import java.time.ZoneId
import java.util.*
import kotlin.reflect.KProperty

interface LContext : LLogic, PropertyContainer, ParseSettingsSpec, RenderSettingsSpec {
  var result: Any?
  val logs: MutableList<Any>
  var locale: Locale
  var zoneId: ZoneId
  val current: LFrame
  val loopState: LoopState
  val root: LFrame

  fun applyRenderSettings(configure: MutableRenderSettings.() -> Unit): LContext
  fun withRenderSettings(configurer: (MutableRenderSettings) -> MutableRenderSettings): LContext
  fun pushFrame(): LFrame
  fun popFrame(): LFrame
  fun withFrame(block: ()->Unit)

  operator fun set(varName: String, value: Any?)
  operator fun <T:Any> get(propName: String): T?
  operator fun <T:Any> get(propName: String, supplier:()->T): T?
  operator fun <T:Any> getValue(ctx: LContext, prop: KProperty<*>): T?

  fun startLoop(length: Int, name: String? = null)
  fun incrementIterations()
  fun endLoop()

  fun setRoot(varName: String, value: Any)
  fun hasVar(name: String): Boolean
  fun remove(varName: String): Any?
  fun parseFile(file: File): LTemplate
  fun render(template: LTemplate): String
  fun getAccessor(container: Any, prop: String): Getter<Any>
}
