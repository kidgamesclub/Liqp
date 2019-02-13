package liqp.tags;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.AssertsKt;
import liqp.LiquidParser;
import liqp.parameterized.LiquifyNoInputTest;
import org.antlr.runtime.RecognitionException;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runners.Parameterized;

public class UnlessTest extends LiquifyNoInputTest {

  @Parameterized.Parameters
  public static Object[] testParams() {

    return new String[][]{
          {"{% unless user.name == 'tobi' %}X{% endunless %}", ""},
          {"{% unless user.name == 'bob' %}X{% endunless %}", "X"},
          {"{% unless user.name == 'tobi' %}X{% else %}Y{% endunless %}", "Y"},
    };
  }

  public UnlessTest(@NotNull String templateString,
                    @NotNull String expectedResult) {
    super(templateString, expectedResult, "{\"user\" : {\"name\" : \"tobi\", \"age\" : 42} }");
  }

  /*
   * def test_unless
   *   assert_template_result('  ',' {% unless true %} this text should not go into the output {% endunless %} ')
   *   assert_template_result('  this text should go into the output  ',
   *                          ' {% unless false %} this text should go into the output {% endunless %} ')
   *   assert_template_result('  you rock ?','{% unless true %} you suck {% endunless %} {% unless false %} you rock
   *   {% endunless %}?')
   * end
   */
  @Test
  public void unlessTest() throws RecognitionException {

    assertThat(LiquidParser.newInstance().parse(" {% unless true %} this text should not go into the output {% " +
          "endunless %} ").render(), is("  "));
    assertThat(LiquidParser.newInstance().parse(" {% unless false %} this text should go into the output {% endunless" +
          " %} ").render(), is("  this text should go into the output  "));
    assertThat(LiquidParser.newInstance().parse("{% unless true %} you suck {% endunless %} {% unless false %} you " +
          "rock {% endunless %}?").render(), is("  you rock ?"));
  }

  /*
   * def test_unless_else
   *   assert_template_result(' YES ','{% unless true %} NO {% else %} YES {% endunless %}')
   *   assert_template_result(' YES ','{% unless false %} YES {% else %} NO {% endunless %}')
   *   assert_template_result(' YES ','{% unless "foo" %} NO {% else %} YES {% endunless %}')
   * end
   */
  @Test
  public void unless_elseTest() throws RecognitionException {

    AssertsKt.assertThat(LiquidParser.newInstance())
          .withTemplateString("{% unless true %} NO {% else %} YES {% endunless %}")
          .rendering()
          .isEqualTo(" YES ");

    AssertsKt.assertThat(LiquidParser.newInstance())
          .withTemplateString("{% unless false %} YES {% else %} NO {% endunless %}")
          .rendering()
          .isEqualTo(" YES ");

    AssertsKt.assertThat(LiquidParser.newInstance())
          .withTemplateString("{% unless \"foo\" %} NO {% else %} YES {% endunless %}")
          .rendering()
          .isEqualTo(" YES ");
  }

  /*
   * def test_unless_in_loop
   *   assert_template_result '23', '{% for i in choices %}{% unless i %}{{ forloop.index }}{% endunless %}{% endfor
   *   %}', 'choices' => [1, nil, false]
   * end
   */
  @Test
  public void unless_in_loopTest() throws RecognitionException {

    assertThat(
          LiquidParser.newInstance().parse("{% for i in choices %}{% unless i %}{{ forloop.index }}{% endunless %}{% " +
                "endfor %}")
                .render("{ \"choices\" : [1, null, false] }"),
          is("23"));
  }

  /*
   * def test_unless_else_in_loop
   *   assert_template_result ' TRUE  2  3 ', '{% for i in choices %}{% unless i %} {{ forloop.index }} {% else %}
   *   TRUE {% endunless %}{% endfor %}', 'choices' => [1, nil, false]
   * end
   */
  @Test
  public void unless_else_in_loopTest() throws RecognitionException {

    assertThat(
          LiquidParser.newInstance().parse("{% for i in choices %}{% unless i %} {{ forloop.index }} {% else %} TRUE " +
                "{% endunless %}{% endfor %}")
                .render("{ \"choices\" : [1, null, false] }"),
          is(" TRUE  2  3 "));
  }
}
