package liqp

import liqp.config.ParseSettings
import liqp.config.RenderSettings
import liqp.node.LTemplate
import java.io.File

interface LParser {
  fun parse(template: String): LTemplate
  fun parseFile(file: File): LTemplate
  fun toRenderSettings(): RenderSettings
  val settings: ParseSettings
}
