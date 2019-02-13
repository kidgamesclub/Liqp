package liqp.tags;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.AssertsKt;
import liqp.LiquidParser;
import liqp.parameterized.LiquifyNoInputTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runners.Parameterized;

public class CycleTest extends LiquifyNoInputTest {

  @Parameterized.Parameters
  public static Object[] testParams() {
    String[][] tests = {
          {
                "{% cycle 'o', 't' %}\n" +
                      "{% cycle 33: 'one', 'two', 'three' %}\n" +
                      "{% cycle 33: 'one', 'two', 'three' %}\n" +
                      "{% cycle 3: '1', '2' %}\n" +
                      "{% cycle 33: 'one', 'two' %}\n" +
                      "{% cycle 33: 'one', 'two' %}\n" +
                      "{% cycle 3: '1', '2' %}\n" +
                      "{% cycle 3: '1', '2' %}\n" +
                      "{% cycle 'o', 't' %}\n" +
                      "{% cycle 'o', 't' %}",
                "o\n" +
                      "one\n" +
                      "two\n" +
                      "1\n" +
                      "one\n" +
                      "two\n" +
                      "2\n" +
                      "1\n" +
                      "t\n" +
                      "o"
          },
          {
                "{% cycle 'o', 'p' %}\n" +
                      "{% cycle 'o' %}\n" +
                      "{% cycle 'o' %}\n" +
                      "{% cycle 'o', 'p' %}\n" +
                      "{% cycle 'o', 'p' %}",
                "o\n" +
                      "o\n" +
                      "o\n" +
                      "p\n" +
                      "o"
          },
          {
                "{% cycle 'one', 'two', 'three' %}\n" +
                      "{% cycle 'one', 'two', 'three' %}\n" +
                      "{% cycle 'one', 'two' %}\n" +
                      "{% cycle 'one', 'two', 'three' %}\n" +
                      "{% cycle 'one' %}",
                "one\n" +
                      "two\n" +
                      "one\n" +
                      "three\n" +
                      "one"
          }
    };
    return tests;
  }

  public CycleTest(@NotNull String templateString,
                   @NotNull String expectedResult) {
    super(templateString, expectedResult, null);
  }

  /*
   * def test_cycle
   *   assert_template_result('one','{%cycle "one", "two"%}')
   *   assert_template_result('one two','{%cycle "one", "two"%} {%cycle "one", "two"%}')
   *   assert_template_result(' two','{%cycle "", "two"%} {%cycle "", "two"%}')
   *
   *   assert_template_result('one two one','{%cycle "one", "two"%} {%cycle "one", "two"%} {%cycle "one", "two"%}')
   *
   *   assert_template_result('text-align: left text-align: right',
   *     '{%cycle "text-align: left", "text-align: right" %} {%cycle "text-align: left", "text-align: right"%}')
   * end
   */
  @Test
  public void cycleTest() throws Exception {

    AssertsKt.assertThat(LiquidParser.newInstance())
          .withTemplateString("{%cycle \"one\", \"two\"%}")
          .rendering()
          .isEqualTo("one");

    AssertsKt.assertThat(LiquidParser.newInstance())
          .withTemplateString("{%cycle \"one\", \"two\"%} {%cycle \"one\", \"two\"%}")
          .rendering()
          .isEqualTo("one two");

    AssertsKt.assertThat(LiquidParser.newInstance())
          .withTemplateString("{%cycle \"\", \"two\"%} {%cycle \"\", \"two\"%}")
          .rendering()
          .isEqualTo(" two");

    AssertsKt.assertThat(LiquidParser.newInstance())
          .withTemplateString("{%cycle \"one\", \"two\"%} {%cycle \"one\", \"two\"%} {%cycle \"one\", \"two\"%}")
          .rendering()
          .isEqualTo("one two one");

    AssertsKt.assertThat(LiquidParser.newInstance())
          .withTemplateString("{%cycle \"text-align: left\", \"text-align: right\" %} {%cycle " +
                "\"text-align: left\", \"text-align: right\"%}")
          .rendering()
          .isEqualTo("text-align: left text-align: right");
  }

  /*
   * def test_multiple_cycles
   *   assert_template_result('1 2 1 1 2 3 1',
   *     '{%cycle 1,2%} {%cycle 1,2%} {%cycle 1,2%} {%cycle 1,2,3%} {%cycle 1,2,3%} {%cycle 1,2,3%} {%cycle 1,2,3%}')
   * end
   */
  @Test
  public void multiple_cyclesTest() throws Exception {

    assertThat(
          LiquidParser.newInstance().parse(
                "{%cycle 1,2%} " +
                      "{%cycle 1,2%} " +
                      "{%cycle 1,2%} " +
                      "{%cycle 1,2,3%} " +
                      "{%cycle 1,2,3%} " +
                      "{%cycle 1,2,3%} " +
                      "{%cycle 1,2,3%}").render(),
          is("1 2 1 1 2 3 1"));
  }

  /*
   * def test_multiple_named_cycles
   *   assert_template_result('one one two two one one',
   *     '{%cycle 1: "one", "two" %} {%cycle 2: "one", "two" %} {%cycle 1: "one", "two" %} {%cycle 2: "one", "two" %}
   *     {%cycle 1: "one", "two" %} {%cycle 2: "one", "two" %}')
   * end
   */
  @Test
  public void multiple_named_cyclesTest() throws Exception {

    assertThat(
          LiquidParser.newInstance().parse(
                "{%cycle 1: \"one\", \"two\" %} {%cycle 2: \"one\", \"two\" %} " +
                      "{%cycle 1: \"one\", \"two\" %} {%cycle 2: \"one\", \"two\" %} " +
                      "{%cycle 1: \"one\", \"two\" %} {%cycle 2: \"one\", \"two\" %}").render(),
          is("one one two two one one"));
  }

  /*
   * def test_multiple_named_cycles_with_names_from_context
   *   assigns = {"var1" => 1, "var2" => 2 }
   *   assert_template_result('one one two two one one',
   *     '{%cycle var1: "one", "two" %} {%cycle var2: "one", "two" %} {%cycle var1: "one", "two" %} {%cycle var2:
   *     "one", "two" %} {%cycle var1: "one", "two" %} {%cycle var2: "one", "two" %}', assigns)
   * end
   */
  @Test
  public void multiple_named_cycles_with_names_from_contextTest() throws Exception {

    String assigns = "{\"var1\" : 1, \"var2\" : 2 }";

    assertThat(
          LiquidParser.newInstance().parse(
                "{%cycle var1: \"one\", \"two\" %} {%cycle var2: \"one\", \"two\" %} " +
                      "{%cycle var1: \"one\", \"two\" %} {%cycle var2: \"one\", \"two\" %} " +
                      "{%cycle var1: \"one\", \"two\" %} {%cycle var2: \"one\", \"two\" %}").render(assigns),
          is("one one two two one one"));
  }
}
