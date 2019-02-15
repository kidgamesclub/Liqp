package liqp.node

import liqp.EmptyNode
import liqp.LRenderer
import liqp.Liquify.Companion.provider
import liqp.parseJSON
import java.time.ZoneId
import java.util.*

interface LTemplate {
  val rootNode: LNode
  val renderer: LRenderer

  val defaultLocale get() = renderer.renderSettings.defaultLocale
  val defaultTimezone get() = renderer.renderSettings.defaultTimezone

  // These overloads can't be generated on interfaces
  // That's why they're spelled out instead of using default arguments

  fun render() = render(defaultLocale, defaultTimezone)
  fun render(locale: Locale, timezone: ZoneId): String = render(null, locale, timezone)
  fun render(inputData: Any?) = render(inputData, defaultLocale, defaultTimezone)
  fun render(inputData: Any?, locale: Locale, timezone: ZoneId): String = renderer.render(this, locale, timezone, inputData)
  fun renderJson(inputData: String, locale: Locale, timezone: ZoneId): String = render(inputData.parseJSON(), locale, timezone)
  fun renderJson(inputData: String) = renderJson(inputData, defaultLocale, defaultTimezone)
}

object EmptyTemplate : LTemplate {
  override fun render(locale: Locale, timezone: ZoneId): String {
    return ""
  }

  override fun render(inputData: Any?, locale: Locale, timezone: ZoneId): String {
    return ""
  }

  override fun renderJson(inputData: String, locale: Locale, timezone: ZoneId): String {
    return ""
  }

  override val rootNode: LNode = EmptyNode
  val parser = provider.createParser()
  override val renderer: LRenderer = provider.createRenderer(parser, parser.toRenderSettings())
}
