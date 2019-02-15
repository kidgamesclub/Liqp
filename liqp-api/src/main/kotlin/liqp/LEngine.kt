package liqp

import liqp.config.LParseSettings
import liqp.config.LRenderSettings
import liqp.config.MutableParseSettings
import liqp.config.MutableRenderSettings

interface LEngine : LParser, LRenderer {
  override val parseSettings: LParseSettings
  override val renderSettings: LRenderSettings
  fun configureParser(block: MutableParseSettings.() -> Unit)
  fun configureRenderer(block: MutableRenderSettings.() -> Unit)
  val parser: LParser
  val renderer: LRenderer
  fun reset()
}
