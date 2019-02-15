package liqp.tags

import assertk.assert
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import liqp.Liquify
import liqp.createParseSettings
import liqp.toParser
import org.junit.Before
import org.junit.Test
import java.io.File

class IncludeTest {

  lateinit var testBaseDir: File

  @Before
  fun setup() {
    val base = File("./")
    val rootDir = if (ModulePath in base.absolutePath)
      base
    else
      File(ModulePath)

    testBaseDir = File(rootDir, "src/test/included")
  }



  @Test
  fun renderTest() {

    val source = "{% assign shape = 'circle' %}\n" +
        "{% include 'color' %}\n" +
        "{% include 'color' with 'red' %}\n" +
        "{% include 'color' with 'blue' %}\n" +
        "{% assign shape = 'square' %}\n" +
        "{% include 'color' with 'red' %}"

    val parser = Liquify.provider.createParser {
      baseDir(testBaseDir)
      forLiquid()
      strictIncludes(true)
    }
    val template = parser.parse(source)

    val rendered = template.render()

    assert(rendered).isEqualTo("\n" +
        "color: ''\n" +
        "shape: 'circle'\n" +
        "color: 'red'\n" +
        "shape: 'circle'\n" +
        "color: 'blue'\n" +
        "shape: 'circle'\n" +
        "\n" +
        "color: 'red'\n" +
        "shape: 'square'")
  }

  @Test
  fun renderTestWithIncludeDirectorySpecifiedInContextLiquidFlavor() {
    val index = File(testBaseDir, "index_with_quotes.html")
    val parser = createParseSettings()
        .baseDir(testBaseDir)
        .strictIncludes(true)
        .forLiquid()
        .build()
        .toParser()
    val template = parser.parseFile(index)

    val result = template.render()
    assert(result).contains("HEADER")
  }

  @Test
  fun renderTestWithIncludeDirectorySpecifiedInContextJekyllFlavor() {

    val index = File(testBaseDir, "index_without_quotes.html")
    val template = createParseSettings()
        .forJekyll()
        .baseDir(testBaseDir)
        .toParser()
        .parseFile(index)

    val result = template.render()
    assert(result).contains("HEADER")
  }

  @Test
  fun renderTestWithIncludeDirectorySpecifiedInJekyllFlavor() {
    val index = File(testBaseDir, "index_without_quotes.html")
    val template = createParseSettings()
        .forJekyll()
        .baseDir(testBaseDir)
        .toParser()
        .parseFile(index)
    val result = template.render()
    assert(result).contains("HEADER")
  }

  @Test
  fun renderTestWithIncludeDirectorySpecifiedInLiquidFlavor() {
    val index = File(testBaseDir, "index_with_quotes.html")
    val template = createParseSettings()
        .forLiquid()
        .baseDir(testBaseDir)
        .toParser().parseFile(index)
    val result = template.render()
    assert(result).contains("HEADER")
  }

  // https://github.com/bkiers/Liqp/issues/75
  @Test
  fun expressionInIncludeTagJekyll() {
    val source = "{% assign variable = 'header.html' %}{% include {{variable}} %}"

    val rendered = createParseSettings()
        .forJekyll()
        //          .flavor(Flavor.JEKYLL)

        .baseDir(testBaseDir)
        .toParser()
        .parse(source).render()

    assert(rendered).contains("HEADER")
  }

  // https://github.com/bkiers/Liqp/issues/75
  @Test
  fun expressionInIncludeTagLiquidThrowsException() {

    val source = "{% assign variable = 'header.html' %}{% include {{variable}} %}"
    val rendered = createParseSettings()
        .baseDir(testBaseDir)
        .toParser().parse(source).render()
    assert(rendered).contains("LIQUID_HEADER")
  }

  // https://github.com/bkiers/Liqp/issues/75
  @Test
  fun expressionInIncludeTagDefaultFlavorThrowsException() {
    val source = "{% assign variable = 'header.html' %}{% include {{variable}} %}"

    createParseSettings().toParser().parse(source).render()
  }

  companion object {
    private const val ModulePath = "liqp-core"
  }
}
