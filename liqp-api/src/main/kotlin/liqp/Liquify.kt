package liqp

import liqp.config.*
import liqp.filter.Filters
import liqp.tag.Tags
import java.util.*
import java.util.function.Consumer

interface Liquify {
    fun createParser(parseSettings: LParseSettings): LParser
    fun createParser(): LParser = createParser(defaultParseSettings)
    fun createParser(configure: MutableParseSettings.() -> Unit): LParser =
        createParser(defaultParseSettings.reconfigure(configure))

    fun createParserJvm(configure: Consumer<MutableParseSettings>): LParser =
        createParser(defaultParseSettings.reconfigure { configure.accept(this) })

    fun createRendererJvm(parser: LParser, configure: Consumer<MutableRenderSettings>): LRenderer =
        createRenderer(parser, RenderSettings(parser.parseSettings).reconfigure { configure.accept(this) })

    fun createEngineJvm(
        configureParser: Consumer<MutableParseSettings>? = null,
        configureRenderer: Consumer<MutableRenderSettings>? = null,
    ): LEngine

    fun createEngine(
        configureParser: Consumer<MutableParseSettings>?=null,
        configureRenderer: Consumer<MutableRenderSettings>?=null
    ): LEngine

    fun createRenderer(parser: LParser, renderSettings: LRenderSettings = defaultRenderSettings): LRenderer

    val defaultRenderSettings: LRenderSettings get() = RenderSettings(defaultParseSettings)
    val defaultParseSettings: LParseSettings

    val defaultFilters: Filters
    val defaultTags: Tags

    companion object {
        @JvmStatic
        val provider: Liquify by lazy {
            ServiceLoader.load(Liquify::class.java).firstOrNull()
                ?: throw Error("No Liquify instance could be determined")
        }
    }
}


