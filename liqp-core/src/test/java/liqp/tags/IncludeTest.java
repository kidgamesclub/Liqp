package liqp.tags;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import liqp.LiquidParser;
import liqp.Template;
import liqp.parser.Flavor;
import org.antlr.runtime.RecognitionException;
import org.junit.Before;
import org.junit.Test;

public class IncludeTest {

  private static final String MODULE_PATH = "liqp-core";

  private File included;

  @Before
  public void setup() {
    final File base = new File("./");
    final File basePath = base.getAbsolutePath().contains(MODULE_PATH)
          ? base
          : new File(MODULE_PATH);

    included = new File(basePath, "src/test/included");
  }

  @Test
  public void renderTest() throws RecognitionException {

    String source =
          "{% assign shape = 'circle' %}\n" +
                "{% include 'color' %}\n" +
                "{% include 'color' with 'red' %}\n" +
                "{% include 'color' with 'blue' %}\n" +
                "{% assign shape = 'square' %}\n" +
                "{% include 'color' with 'red' %}";

    Template template = LiquidParser.newBuilder()
          .baseDir(included)
          .parse(source);

    String rendered = template.render();

    assertThat(rendered, is("\n" +
          "color: ''\n" +
          "shape: 'circle'\n" +
          "color: 'red'\n" +
          "shape: 'circle'\n" +
          "color: 'blue'\n" +
          "shape: 'circle'\n" +
          "\n" +
          "color: 'red'\n" +
          "shape: 'square'"));
  }

  @Test
  public void renderTestWithIncludeDirectorySpecifiedInContextLiquidFlavor() throws Exception {
    File index = new File(included, "index_with_quotes.html");
    Template template = LiquidParser.newBuilder().baseDir(included).parseFile(index);
    String result = template.render();
    assertTrue(result.contains("HEADER"));
  }

  @Test
  public void renderTestWithIncludeDirectorySpecifiedInContextJekyllFlavor() throws Exception {

    File index = new File(included, "index_without_quotes.html");
    Template template = LiquidParser.newBuilder()
          .flavor(Flavor.JEKYLL)
          .baseDir(included)
          .toParser().parseFile(index);

    String result = template.render();
    assertTrue(result.contains("HEADER"));
  }

  @Test
  public void renderTestWithIncludeDirectorySpecifiedInJekyllFlavor() throws Exception {
    File index = new File(included, "index_without_quotes.html");
    Template template = LiquidParser.newBuilder()
          .flavor(Flavor.JEKYLL)
          .baseDir(included)
          .toParser().parseFile(index);
    String result = template.render();
    assertTrue(result.contains("HEADER"));
  }

  @Test
  public void renderTestWithIncludeDirectorySpecifiedInLiquidFlavor() throws Exception {
    File index = new File(included, "index_with_quotes.html");
    Template template = LiquidParser.newBuilder()
          .flavor(Flavor.LIQUID)
          .baseDir(included)
          .toParser().parseFile(index);
    String result = template.render();
    assertTrue(result.contains("HEADER"));
  }

  // https://github.com/bkiers/Liqp/issues/75
  @Test
  public void expressionInIncludeTagJekyll() {

    String source = "{% assign variable = 'header.html' %}{% include {{variable}} %}";

    String rendered = LiquidParser.newBuilder()
          .flavor(Flavor.JEKYLL)
          .baseDir(included)
          .toParser()
          .parse(source).render();

    assertTrue(rendered.contains("HEADER"));
  }

  // https://github.com/bkiers/Liqp/issues/75
  @Test(expected = RuntimeException.class)
  public void expressionInIncludeTagLiquidThrowsException() {

    String source = "{% assign variable = 'header.html' %}{% include {{variable}} %}";

    LiquidParser.newBuilder().flavor(Flavor.LIQUID)
          .baseDir(included)
          .toParser().parse(source).render();
  }

  // https://github.com/bkiers/Liqp/issues/75
  @Test(expected = RuntimeException.class)
  public void expressionInIncludeTagDefaultFlavorThrowsException() {

    String source = "{% assign variable = 'header.html' %}{% include {{variable}} %}";

    LiquidParser.newBuilder().flavor(Flavor.LIQUID).toParser().parse(source).render();
  }
}
