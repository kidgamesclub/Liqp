package liqp.tags

import assertk.assert
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import liqp.LiquidParser
import org.junit.Before
import org.junit.Test
import java.io.File

class IncludeTest {

  lateinit var baseDir: File

  @Before
  fun setup() {
    val base = File("./")
    val rootDir = if (ModulePath in base.absolutePath)
      base
    else
      File(ModulePath)

    baseDir = File(rootDir, "src/test/included")
  }

  @Test
  fun renderTest() {

    val source = "{% assign shape = 'circle' %}\n" +
        "{% include 'color' %}\n" +
        "{% include 'color' with 'red' %}\n" +
        "{% include 'color' with 'blue' %}\n" +
        "{% assign shape = 'square' %}\n" +
        "{% include 'color' with 'red' %}"

    val template = LiquidParser.newBuilder()
        .baseDir(baseDir)
        .forLiquid()
        .strictIncludes(true)
        .toParser()
        .parse(source)

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
    val index = File(baseDir, "index_with_quotes.html")
    val parser = LiquidParser.newBuilder()
        .baseDir(baseDir)
        .strictIncludes(true)
        .forLiquid()
        .toParser()
    val template = parser.parseFile(index)

    val result = template.render()
    assert(result).contains("HEADER")
  }

  @Test
  fun renderTestWithIncludeDirectorySpecifiedInContextJekyllFlavor() {

    val index = File(baseDir, "index_without_quotes.html")
    val template = LiquidParser.newBuilder()
        .forJekyll()
        .baseDir(baseDir)
        .toParser()
        .parseFile(index)

    val result = template.render()
    assert(result).contains("HEADER")
  }

  @Test
  fun renderTestWithIncludeDirectorySpecifiedInJekyllFlavor() {
    val index = File(baseDir, "index_without_quotes.html")
    val template = LiquidParser.newBuilder()
        .forJekyll()
        .baseDir(baseDir)
        .toParser()
        .parseFile(index)
    val result = template.render()
    assert(result).contains("HEADER")
  }

  @Test
  fun renderTestWithIncludeDirectorySpecifiedInLiquidFlavor() {
    val index = File(baseDir, "index_with_quotes.html")
    val template = LiquidParser.newBuilder()
        .forLiquid()
        .baseDir(baseDir)
        .toParser().parseFile(index)
    val result = template.render()
    assert(result).contains("HEADER")
  }

  // https://github.com/bkiers/Liqp/issues/75
  @Test
  fun expressionInIncludeTagJekyll() {
    val source = "{% assign variable = 'header.html' %}{% include {{variable}} %}"

    val rendered = LiquidParser.newBuilder()
        .forJekyll()
        //          .flavor(Flavor.JEKYLL)

        .baseDir(baseDir)
        .toParser()
        .parse(source).render()

    assert(rendered).contains("HEADER")
  }

  // https://github.com/bkiers/Liqp/issues/75
  @Test
  fun expressionInIncludeTagLiquidThrowsException() {

    val source = "{% assign variable = 'header.html' %}{% include {{variable}} %}"
    val rendered = LiquidParser.newBuilder()
        .baseDir(baseDir)
        .toParser().parse(source).render()
    assert(rendered).contains("LIQUID_HEADER")
  }

  // https://github.com/bkiers/Liqp/issues/75
  @Test
  fun expressionInIncludeTagDefaultFlavorThrowsException() {
    val source = "{% assign variable = 'header.html' %}{% include {{variable}} %}"

    LiquidParser.newBuilder().toParser().parse(source).render()
  }

  companion object {
    private const val ModulePath = "liqp-core"
  }
}
