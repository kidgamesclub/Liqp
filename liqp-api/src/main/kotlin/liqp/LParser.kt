package liqp

import liqp.config.LParseSettings
import liqp.config.RenderSettings
import liqp.node.LTemplate
import java.io.File

interface LParser : LParseSettings {
  fun parse(template: String): LTemplate
  fun parseFile(file: File): LTemplate
  fun toRenderSettings(): RenderSettings
}
