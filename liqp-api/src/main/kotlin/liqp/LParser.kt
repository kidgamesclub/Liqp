package liqp

import liqp.Liquify.Companion.provider
import liqp.config.LParseSettings
import liqp.config.LRenderSettings
import liqp.config.MutableParseSettings
import liqp.config.ParseSettings
import liqp.config.RenderSettings
import liqp.node.LTemplate
import java.io.File

interface LParser {
  fun parse(template: String): LTemplate
  fun parseFile(file: File): LTemplate
  fun toRenderSettings(): LRenderSettings
  val parseSettings: LParseSettings
  fun reconfigure(block:MutableParseSettings.()->Unit):LParser = parseSettings.toMutableSettings().build(block).toParser()
  fun toRenderer() = provider.createRenderer(this, this.toRenderSettings())
}
