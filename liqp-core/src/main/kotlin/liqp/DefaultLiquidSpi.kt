package liqp

import liqp.config.*
import liqp.filter.Filters
import liqp.parser.Flavor
import liqp.tag.Tags
import java.io.File
import java.util.function.Consumer

class DefaultLiquidSpi : Liquify {

    override val defaultFilters: Filters = LiquidDefaults.defaultFilters
    override val defaultTags: Tags = LiquidDefaults.defaultTags

    override val defaultParseSettings: LParseSettings = ParseSettings(
        defaultTags,
        defaultFilters,
        baseDir = File("./"),
        includesDir = Flavor.LIQUID.includesDirName
    )

    override fun createParser(parseSettings: LParseSettings): LParser = LiquidParser(parseSettings)
    override fun createRenderer(parser: LParser, renderSettings: LRenderSettings): LRenderer =
        LiquidRenderer(parser = parser, renderSettings = renderSettings)

    override fun createEngineJvm(
        configureParser: Consumer<MutableParseSettings>?,
        configureRenderer: Consumer<MutableRenderSettings>?
    ): LEngine {
        val parseSettings = defaultParseSettings.reconfigure {
            configureParser?.accept(this)
        }

        val renderSettings = RenderSettings(parseSettings).reconfigure {
            configureRenderer?.accept(this)
        }

        return LiquidEngine(parseSettings, renderSettings)

    }

    override fun createEngine(
        configureParser: Consumer<MutableParseSettings>?,
        configureRenderer: Consumer<MutableRenderSettings>?
    ): LEngine {
        val parseSettings = defaultParseSettings.reconfigure {
            configureParser?.accept(this)
        }

        val renderSettings = RenderSettings(parseSettings).reconfigure {
            configureRenderer?.accept(this)
        }

        return LiquidEngine(parseSettings, renderSettings)

    }
}
