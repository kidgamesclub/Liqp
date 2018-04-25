package liqp

import liqp.config.MutableParseSettings
import liqp.config.ParseSettingsSpec
import liqp.config.RenderSettings
import liqp.node.LTemplate
import java.io.File

interface LParser : ParseSettingsSpec {
  fun parse(template: String): LTemplate
  fun parseFile(file: File): LTemplate
  fun toRenderSettings(): RenderSettings
}
