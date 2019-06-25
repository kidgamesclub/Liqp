package liqp.context

import liqp.Getter
import liqp.HasProperties
import liqp.LLogic
import liqp.TypeCoercion
import liqp.config.LParseSettings
import liqp.config.LRenderSettings
import liqp.config.MutableRenderSettings
import liqp.node.LTemplate
import java.io.File
import java.time.ZoneId
import java.util.*
import kotlin.reflect.KProperty

interface LContext : LLogic, HasProperties {
  val inputData: Any?
  val parseSettings: LParseSettings
  val renderSettings: LRenderSettings
  val coercion: TypeCoercion
  var result: Any?
  val logs: MutableList<Any>
  val locale: Locale
  val timezone: ZoneId
  val current: LFrame
  val loopState: LoopState
  val root: LFrame
  val includeFile: File
  fun reconfigure(configure: MutableRenderSettings.() -> Unit): LContext
  fun pushFrame(): LFrame
  fun popFrame(): LFrame
  fun withFrame(block: () -> Any?): Any?

  operator fun set(varName: String, value: Any?)
  operator fun <T : Any> get(propName: String): T?
  operator fun <T : Any> get(propName: String, supplier: () -> T): T
  operator fun <T : Any> getValue(ctx: LContext, prop: KProperty<*>): T?

  fun startLoop(length: Int, name: String? = null)
  fun incrementIterations()
  fun endLoop()

  fun setRoot(varName: String, value: Any?)
  fun hasVar(name: String): Boolean
  fun remove(varName: String): Any?
  fun parseFile(file: File): LTemplate
  fun render(template: LTemplate): String
  fun getAccessor(container: Any, prop: String): Getter<Any>
  fun reset(): LContext

  operator fun <R> invoke(block: LContext.() -> R): R = block()
}


