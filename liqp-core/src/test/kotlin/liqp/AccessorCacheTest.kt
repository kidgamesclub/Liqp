package liqp

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test
import java.time.ZoneId
import java.util.*

class AccessorCacheTest {

  @Test fun testCachedAccessors() {
    val engine = Liquify.provider.createEngineJvm()
    val template = engine.parse("{{ contact.name }}")
    val template2 = engine.parse(" {{ contact.name }} ")
    val result = engine.renderer.render(template, Locale.CANADA, ZoneId.systemDefault(), mapOf("contact", "12345"))
    val result2 = engine.renderer.render(template, Locale.CANADA, ZoneId.systemDefault(), mapOf("contact", mapOf("name", "Bob Jones")))
    val result2Alt = engine.renderer.render(template2, Locale.CANADA, ZoneId.systemDefault(), mapOf("contact", mapOf("name", "Bob Jones")))
    assertThat(result).isEqualTo("")
    assertThat(result2Alt).isEqualTo(" Bob Jones ")
    assertThat(result2).isEqualTo("Bob Jones")
  }


}
