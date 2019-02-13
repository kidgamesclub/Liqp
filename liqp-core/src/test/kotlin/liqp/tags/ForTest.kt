package liqp.tags

import liqp.LiquidParser
import liqp.assertThat
import liqp.parameterized.LiquifyNoInputTest
import org.antlr.runtime.RecognitionException
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runners.Parameterized
import java.util.*

class ForTests(template: String, expected: String) : LiquifyNoInputTest(template, expected, "{\"array\" : [1,2,3,4,5,6,7,8,9,10], \"item\" : {\"quantity\" : 5} }") {
  companion object {
    @JvmStatic @Parameterized.Parameters
    fun testParams() = arrayOf(
        arrayOf("{% for item in array %}{{ item }}{% endfor %}", "12345678910"),
        arrayOf("{% for item in array limit:8.5 %}{{ item }}{% endfor %}", "12345678"),
        arrayOf("{% for item in array limit:8.5 offset:6 %}{{ item }}{% endfor %}", "78910"),
        arrayOf("{% for item in array limit:2 offset:6 %}{{ item }}{% endfor %}", "78"),
        arrayOf("{% for i in (1..item.quantity) %}{{ i }}{% endfor %}", "12345"),
        arrayOf("{% for i in (1..3) %}{{ i }}{% endfor %}", "123"),
        arrayOf("{% for i in (1..nil) %}{{ i }}{% endfor %}", ""),
        arrayOf("{% for i in (XYZ .. 7) %}{{ i }}{% endfor %}", ""),
        arrayOf("{% for i in (1 .. item.quantity) offset:2 %}{{ i }}{% endfor %}", "345"),
        arrayOf("{% for i in (1.. item.quantity) offset:nil %}{{ i }}{% endfor %}", "12345"),
        arrayOf("{% for i in (1 ..item.quantity) limit:4 OFFSET:2 %}{{ i }}{% endfor %}", "1234"),
        arrayOf("{% for i in (1..item.quantity) limit:4 offset:20 %}{{ i }}{% endfor %}", ""),
        arrayOf("{% for i in (1..item.quantity) limit:0 offset:2 %}{{ i }}{% endfor %}", ""),
        arrayOf("{% for i in (1..5) limit:4 OFFSET:2 %}{{forloop.length}}{% endfor %}", "4444"),
        arrayOf("{% for i in array limit:4 OFFSET:2 %}{{forloop.length}}{% endfor %}", "4444"),
        arrayOf("{% for i in (1..5) limit:4 OFFSET:2 %}{{forloop.first}}{% endfor %}", "truefalsefalsefalse"),
        arrayOf("{% for i in array limit:4 OFFSET:2 %}{{forloop.first}}{% endfor %}", "truefalsefalsefalse"),
        arrayOf("{% for i in (1..5) limit:4 OFFSET:2 %}{{forloop.last}}{% endfor %}", "falsefalsefalsetrue"),
        arrayOf("{% for i in array limit:4 OFFSET:2 %}{{forloop.last}}{% endfor %}", "falsefalsefalsetrue"),
        arrayOf("{% for i in (1..5) limit:4 OFFSET:2 %}{{forloop.index}}{% endfor %}", "1234"),
        arrayOf("{% for i in array limit:4 OFFSET:2 %}{{forloop.index}}{% endfor %}", "1234"),
        arrayOf("{% for i in (1..5) limit:4 OFFSET:2 %}{{forloop.index0}}{% endfor %}", "0123"),
        arrayOf("{% for i in array limit:4 OFFSET:2 %}{{forloop.index0}}{% endfor %}", "0123"),
        arrayOf("{% for i in (1..5) limit:4 OFFSET:2 %}{{forloop.rindex}}{% endfor %}", "4321"),
        arrayOf("{% for i in array limit:4 OFFSET:2 %}{{forloop.rindex}}{% endfor %}", "4321"),
        arrayOf("{% for i in (1..5) limit:4 OFFSET:2 %}{{forloop.rindex0}}{% endfor %}", "3210"),
        arrayOf("{% for i in array limit:4 OFFSET:2 %}{{forloop.rindex0}}{% endfor %}", "3210"))
  }
}

class ForTest {
    /*
     * def test_for
     *   assert_template_result(' yo  yo  yo  yo ','{%for item in array%} yo {%endfor%}','array' => [1,2,3,4])
     *   assert_template_result('yoyo','{%for item in array%}yo{%endfor%}','array' => [1,2])
     *   assert_template_result(' yo ','{%for item in array%} yo {%endfor%}','array' => [1])
     *   assert_template_result('','{%for item in array%}{%endfor%}','array' => [1,2])
     *   expected = <<HERE
     *
     *     yo
     *
     *     yo
     *
     *     yo
     *
     *   HERE
     *   template = <<HERE
     *   {%for item in array%}
     *     yo
     *   {%endfor%}
     *   HERE
     *   assert_template_result(expected,template,'array' => [1,2,3])
     * end
     */
    @Test
    @Throws(RecognitionException::class)
    fun forTest() {

      assertThat(LiquidParser.newInstance().parse("{%for item in array%} yo {%endfor%}").renderJson("{\"array\":[1,2,3," + "4]}"), `is`(" yo  yo  yo  yo "))
      assertThat(LiquidParser.newInstance().parse("{%for item in array%}yo{%endfor%}").renderJson("{\"array\":[1,2]}"),
          `is`("yoyo"))
      assertThat(LiquidParser.newInstance().parse("{%for item in array%} yo {%endfor%}").renderJson("{\"array\":[1]}"),
          `is`(" yo "))
      assertThat(LiquidParser.newInstance().parse("{%for item in array%}{%endfor%}").renderJson("{\"array\":[1,2]}"), `is`(""))
    }

    /*
     * def test_for_with_range
     *   assert_template_result(' 1  2  3 ','{%for item in (1..3) %} {{item}} {%endfor%}')
     * end
     */
    @Test
    @Throws(RecognitionException::class)
    fun forWithRangeTest() {

      assertThat(LiquidParser.newInstance().parse("{%for item in (1..3) %} {{item}} {%endfor%}").render(), `is`(" 1  2" + "  3 "))
    }

    /*
     * def test_for_with_variable
     *   assert_template_result(' 1  2  3 ','{%for item in array%} {{item}} {%endfor%}','array' => [1,2,3])
     *   assert_template_result('123','{%for item in array%}{{item}}{%endfor%}','array' => [1,2,3])
     *   assert_template_result('123','{% for item in array %}{{item}}{% endfor %}','array' => [1,2,3])
     *   assert_template_result('abcd','{%for item in array%}{{item}}{%endfor%}','array' => ['a','b','c','d'])
     *   assert_template_result('a b c','{%for item in array%}{{item}}{%endfor%}','array' => ['a',' ','b',' ','c'])
     *   assert_template_result('abc','{%for item in array%}{{item}}{%endfor%}','array' => ['a','','b','','c'])
     * end
     */
    @Test
    @Throws(RecognitionException::class)
    fun forWithVariableTest() {

      assertThat(LiquidParser.newInstance().parse("{%for item in array%} {{item}} {%endfor%}").renderJson("{\"array\":[1,2,3]}"), `is`(" 1  2  3 "))
      assertThat(LiquidParser.newInstance().parse("{%for item in array%}{{item}}{%endfor%}").renderJson("{\"array\":[1," + "2,3]}"), `is`("123"))
      assertThat(LiquidParser.newInstance().parse("{% for item in array %}{{item}}{% endfor %}").renderJson("{\"array\":[1,2,3]}"), `is`("123"))
      assertThat(LiquidParser.newInstance().parse("{%for item in array%}{{item}}{%endfor%}").renderJson("{\"array\":[\"a\",\"b\",\"c\",\"d\"]}"), `is`("abcd"))
      assertThat(LiquidParser.newInstance().parse("{%for item in array%}{{item}}{%endfor%}").renderJson("{\"array\":[\"a\",\" \",\"b\",\" \",\"c\"]}"), `is`("a b c"))
      assertThat(LiquidParser.newInstance().parse("{%for item in array%}{{item}}{%endfor%}").renderJson("{\"array\":[\"a\",\"\",\"b\",\"\",\"c\"]}"), `is`("abc"))
    }

    /*
     * def test_for_helpers
     *   assigns = {'array' => [1,2,3] }
     *   assert_template_result(' 1/3  2/3  3/3 ',
     *                          '{%for item in array%} {{forloop.index}}/{{forloop.length}} {%endfor%}',
     *                          assigns)
     *   assert_template_result(' 1  2  3 ', '{%for item in array%} {{forloop.index}} {%endfor%}', assigns)
     *   assert_template_result(' 0  1  2 ', '{%for item in array%} {{forloop.index0}} {%endfor%}', assigns)
     *   assert_template_result(' 2  1  0 ', '{%for item in array%} {{forloop.rindex0}} {%endfor%}', assigns)
     *   assert_template_result(' 3  2  1 ', '{%for item in array%} {{forloop.rindex}} {%endfor%}', assigns)
     *   assert_template_result(' true  false  false ', '{%for item in array%} {{forloop.first}} {%endfor%}', assigns)
     *   assert_template_result(' false  false  true ', '{%for item in array%} {{forloop.last}} {%endfor%}', assigns)
     * end
     */
    @Test
    @Throws(RecognitionException::class)
    fun forHelpersTest() {

      val assigns = "{\"array\":[1,2,3]}"

      assertThat(LiquidParser.newInstance().parse("{%for item in array%} {{forloop.index}}/{{forloop.length}} " + "{%endfor%}").renderJson(assigns), `is`(" 1/3  2/3  3/3 "))
      assertThat(LiquidParser.newInstance().parse("{%for item in array%} {{forloop.index}} {%endfor%}").renderJson(assigns), `is`(" 1  2  3 "))
      assertThat(LiquidParser.newInstance().parse("{%for item in array%} {{forloop.index0}} {%endfor%}").renderJson(assigns), `is`(" 0  1  2 "))
      assertThat(LiquidParser.newInstance().parse("{%for item in array%} {{forloop.rindex0}} {%endfor%}").renderJson(assigns), `is`(" 2  1  0 "))
      assertThat(LiquidParser.newInstance().parse("{%for item in array%} {{forloop.rindex}} {%endfor%}").renderJson(assigns), `is`(" 3  2  1 "))
      assertThat(LiquidParser.newInstance().parse("{%for item in array%} {{forloop.first}} {%endfor%}").renderJson(assigns), `is`(" true  false  false "))
      assertThat(LiquidParser.newInstance().parse("{%for item in array%} {{forloop.last}} {%endfor%}").renderJson(assigns), `is`(" false  false  true "))
    }

    /*
     * def test_for_and_if
     *   assigns = {'array' => [1,2,3] }
     *   assert_template_result('+--',
     *                          '{%for item in array%}{% if forloop.first %}+{% else %}-{% endif %}{%endfor%}',
     *                          assigns)
     * end
     */
    @Test
    @Throws(RecognitionException::class)
    fun forAndIfTest() {

      val assigns = "{\"array\":[1,2,3]}"

      assertThat(LiquidParser.newInstance().parse("{%for item in array%}{% if forloop.first %}+{% else %}-{% endif " + "%}{%endfor%}").renderJson(assigns), `is`("+--"))
    }

    /*
     * def test_for_else
     *   assert_template_result('+++', '{%for item in array%}+{%else%}-{%endfor%}', 'array'=>[1,2,3])
     *   assert_template_result('-',   '{%for item in array%}+{%else%}-{%endfor%}', 'array'=>[])
     *   assert_template_result('-',   '{%for item in array%}+{%else%}-{%endfor%}', 'array'=>nil)
     * end
     */
    @Test
    @Throws(RecognitionException::class)
    fun forElseTest() {

      assertThat(LiquidParser.newInstance().parse("{%for item in array%}+{%else%}-{%endfor%}").renderJson("{\"array\":[1,2,3]}"), `is`("+++"))
      assertThat(LiquidParser.newInstance().parse("{%for item in array%}+{%else%}-{%endfor%}").renderJson("{\"array\":[]}"), `is`("-"))
      assertThat(LiquidParser.newInstance().parse("{%for item in array%}+{%else%}-{%endfor%}").renderJson("{\"array\":null}"), `is`("-"))
    }

    /*
     * def test_limiting
     *   assigns = {'array' => [1,2,3,4,5,6,7,8,9,0]}
     *   assert_template_result('12', '{%for i in array limit:2 %}{{ i }}{%endfor%}', assigns)
     *   assert_template_result('1234', '{%for i in array limit:4 %}{{ i }}{%endfor%}', assigns)
     *   assert_template_result('3456', '{%for i in array limit:4 offset:2 %}{{ i }}{%endfor%}', assigns)
     *   assert_template_result('3456', '{%for i in array limit: 4 offset: 2 %}{{ i }}{%endfor%}', assigns)
     * end
     */
    @Test
    fun limitingTest() {

      val assigns = "{\"array\":[1,2,3,4,5,6,7,8,9,0]}"

      assertThat(LiquidParser.newInstance().parse("{%for i in array limit:2 %}{{ i }}{%endfor%}").renderJson(assigns),
          `is`("12"))
      assertThat(LiquidParser.newInstance().parse("{%for i in array limit:4 %}{{ i }}{%endfor%}").renderJson(assigns),
          `is`("1234"))
      assertThat(LiquidParser.newInstance().parse("{%for i in array limit:4 offset:2 %}{{ i }}{%endfor%}").renderJson(assigns), `is`("3456"))
      assertThat(LiquidParser.newInstance().parse("{%for i in array limit: 4 offset: 2 %}{{ i }}{%endfor%}").renderJson(assigns), `is`("3456"))
    }

    /*
     * def test_dynamic_variable_limiting
     *   assigns = {'array' => [1,2,3,4,5,6,7,8,9,0]}
     *   assigns['limit'] = 2
     *   assigns['offset'] = 2
     *
     *   assert_template_result('34', '{%for i in array limit: limit offset: offset %}{{ i }}{%endfor%}', assigns)
     * end
     */
    @Test
    fun dynamicVariableLimitingTest() {

      LiquidParser.newInstance()
          .assertThat()
          .withTemplateString("{%for i in array limit: limit offset: offset %}{{ i }}{%endfor%}")
          .rendering(mapOf(
              "array" to arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0),
              "limit" to 2,
              "offset" to 2
          ))
          .containsExactly(3, 4)
    }

    /*
     * def test_nested_for
     *   assigns = {'array' => [[1,2],[3,4],[5,6]] }
     *   assert_template_result('123456', '{%for item in array%}{%for i in item%}{{ i }}{%endfor%}{%endfor%}', assigns)
     * end
     */
    @Test
    @Throws(RecognitionException::class)
    fun nestedForTest() {

      val assigns = "{ \"array\":[[1,2], [3,4], [5,6]] }"

      assertThat(LiquidParser.newInstance().parse("{%for item in array%}{%for i in item%}{{ i " + "}}{%endfor%}{%endfor%}").renderJson(assigns), `is`("123456"))
    }

    /*
     * def test_offset_only
     *   assigns = {'array' => [1,2,3,4,5,6,7,8,9,0]}
     *   assert_template_result('890', '{%for i in array offset:7 %}{{ i }}{%endfor%}', assigns)
     * end
     */
    @Test
    @Throws(RecognitionException::class)
    fun offsetOnlyTest() {

      val assigns = "{ \"array\":[1,2,3,4,5,6,7,8,9,0] }"

      assertThat(LiquidParser.newInstance().parse("{%for i in array offset:7 %}{{ i }}{%endfor%}").renderJson(assigns),
          `is`("890"))
    }

    /*
     * def test_pause_resume
     *   assigns = {'array' => {'items' => [1,2,3,4,5,6,7,8,9,0]}}
     *   markup = <<-MKUP
     *     {%for i in array.items limit: 3 %}{{i}}{%endfor%}
     *     next
     *     {%for i in array.items offset:continue limit: 3 %}{{i}}{%endfor%}
     *     next
     *     {%for i in array.items offset:continue limit: 3 %}{{i}}{%endfor%}
     *     MKUP
     *   expected = <<-XPCTD
     *     123
     *     next
     *     456
     *     next
     *     789
     *     XPCTD
     *   assert_template_result(expected,markup,assigns)
     * end
     */
    @Test
    @Throws(RecognitionException::class)
    fun pauseResumeTest() {

      val assigns = "{ \"array\": { \"items\":[1,2,3,4,5,6,7,8,9,0] } }"

      val markup = "{%for i in array.items limit: 3 %}{{i}}{%endfor%}\n" +
          "next\n" +
          "{%for i in array.items offset:continue limit: 3 %}{{i}}{%endfor%}\n" +
          "next\n" +
          "{%for i in array.items offset:continue limit: 3 %}{{i}}{%endfor%}"

      val expected = "123\n" +
          "next\n" +
          "456\n" +
          "next\n" +
          "789"

      assertThat(LiquidParser.newInstance().parse(markup).renderJson(assigns), `is`(expected))
    }

    /*
     * def test_pause_resume_limit
     *   assigns = {'array' => {'items' => [1,2,3,4,5,6,7,8,9,0]}}
     *   markup = <<-MKUP
     *     {%for i in array.items limit:3 %}{{i}}{%endfor%}
     *     next
     *     {%for i in array.items offset:continue limit:3 %}{{i}}{%endfor%}
     *     next
     *     {%for i in array.items offset:continue limit:1 %}{{i}}{%endfor%}
     *     MKUP
     *   expected = <<-XPCTD
     *     123
     *     next
     *     456
     *     next
     *     7
     *     XPCTD
     *   assert_template_result(expected,markup,assigns)
     * end
     */
    @Test
    @Throws(RecognitionException::class)
    fun pauseResumeLimitTest() {

      LiquidParser.newInstance()
          .assertThat()
          .withTemplateString("{%for i in array.items limit:3 %}{{i}}{%endfor%}\n" +
              "next\n" +
              "{%for i in array.items offset:continue limit:3 %}{{i}}{%endfor%}\n" +
              "next\n" +
              "{%for i in array.items offset:continue limit:1 %}{{i}}{%endfor%}")
          .rendering("{ \"array\": { \"items\":[1,2,3,4,5,6,7,8,9,0] } }")
          .isEqualTo("123\n" +
              "next\n" +
              "456\n" +
              "next\n" +
              "7")
    }

    /*
     * def test_pause_resume_BIG_limit
     *   assigns = {'array' => {'items' => [1,2,3,4,5,6,7,8,9,0]}}
     *   markup = <<-MKUP
     *     {%for i in array.items limit:3 %}{{i}}{%endfor%}
     *     next
     *     {%for i in array.items offset:continue limit:3 %}{{i}}{%endfor%}
     *     next
     *     {%for i in array.items offset:continue limit:1000 %}{{i}}{%endfor%}
     *     MKUP
     *   expected = <<-XPCTD
     *     123
     *     next
     *     456
     *     next
     *     7890
     *     XPCTD
     *     assert_template_result(expected,markup,assigns)
     * end
     */
    @Test
    @Throws(RecognitionException::class)
    fun pauseResumeBigLimitTest() {

      val assigns = "{ \"array\": { \"items\":[1,2,3,4,5,6,7,8,9,0] } }"

      val markup = "{%for i in array.items limit:3 %}{{i}}{%endfor%}\n" +
          "next\n" +
          "{%for i in array.items offset:continue limit:3 %}{{i}}{%endfor%}\n" +
          "next\n" +
          "{%for i in array.items offset:continue limit:1000 %}{{i}}{%endfor%}"

      val expected = "123\n" +
          "next\n" +
          "456\n" +
          "next\n" +
          "7890"

      assertThat(LiquidParser.newInstance().parse(markup).renderJson(assigns), `is`(expected))
    }

    /*
     * def test_pause_resume_BIG_offset
     *   assigns = {'array' => {'items' => [1,2,3,4,5,6,7,8,9,0]}}
     *   markup = %q({%for i in array.items limit:3 %}{{i}}{%endfor%}
     *     next
     *     {%for i in array.items offset:continue limit:3 %}{{i}}{%endfor%}
     *     next
     *     {%for i in array.items offset:continue limit:3 offset:1000 %}{{i}}{%endfor%})
     *   expected = %q(123
     *     next
     *     456
     *     next
     *     )
     *     assert_template_result(expected,markup,assigns)
     * end
     */
    @Test
    @Throws(RecognitionException::class)
    fun pauseResumeBigOffsetTest() {

      val assigns = "{ \"array\": { \"items\":[1,2,3,4,5,6,7,8,9,0] } }"

      val markup = "{%for i in array.items limit:3 %}{{i}}{%endfor%}\n" +
          "next\n" +
          "{%for i in array.items offset:continue limit:3 %}{{i}}{%endfor%}\n" +
          "next\n" +
          "{%for i in array.items offset:continue limit:3 offset:1000 %}{{i}}{%endfor%}"

      val expected = "123\n" +
          "next\n" +
          "456\n" +
          "next\n"

      assertThat(LiquidParser.newInstance().parse(markup).renderJson(assigns), `is`(expected))
    }

    /*
     * def test_for_with_break
     *   assigns = {'array' => {'items' => [1,2,3,4,5,6,7,8,9,10]}}
     *
     *   markup = '{% for i in array.items %}{% break %}{% endfor %}'
     *   expected = ""
     *   assert_template_result(expected,markup,assigns)
     *
     *   markup = '{% for i in array.items %}{{ i }}{% break %}{% endfor %}'
     *   expected = "1"
     *   assert_template_result(expected,markup,assigns)
     *
     *   markup = '{% for i in array.items %}{% break %}{{ i }}{% endfor %}'
     *   expected = ""
     *   assert_template_result(expected,markup,assigns)
     *
     *   markup = '{% for i in array.items %}{{ i }}{% if i > 3 %}{% break %}{% endif %}{% endfor %}'
     *   expected = "1234"
     *   assert_template_result(expected,markup,assigns)
     *
     *   # tests to ensure it only breaks out of the local for loop
     *   # and not all of them.
     *   assigns = {'array' => [[1,2],[3,4],[5,6]] }
     *   markup = '{% for item in array %}' +
     *              '{% for i in item %}' +
     *                '{% if i == 1 %}' +
     *                  '{% break %}' +
     *                '{% endif %}' +
     *                '{{ i }}' +
     *              '{% endfor %}' +
     *            '{% endfor %}'
     *   expected = '3456'
     *   assert_template_result(expected, markup, assigns)
     *
     *   # test break does nothing when unreached
     *   assigns = {'array' => {'items' => [1,2,3,4,5]}}
     *   markup = '{% for i in array.items %}{% if i == 9999 %}{% break %}{% endif %}{{ i }}{% endfor %}'
     *   expected = '12345'
     *   assert_template_result(expected, markup, assigns)
     * end
     */
    @Test
    @Throws(RecognitionException::class)
    fun forWithBreakTest() {

      var assigns = "{ \"array\": { \"items\":[1,2,3,4,5,6,7,8,9,0] } }"

      var markup = "{% for i in array.items %}{% break %}{% endfor %}"
      var expected = ""
      assertThat(LiquidParser.newInstance().parse(markup).renderJson(assigns), `is`(expected))

      markup = "{% for i in array.items %}{{ i }}{% break %}{% endfor %}"
      expected = "1"
      assertThat(LiquidParser.newInstance().parse(markup).renderJson(assigns), `is`(expected))

      markup = "{% for i in array.items %}{% break %}{{ i }}{% endfor %}"
      expected = ""
      assertThat(LiquidParser.newInstance().parse(markup).renderJson(assigns), `is`(expected))

      markup = "{% for i in array.items %}{{ i }}{% if i > 3 %}{% break %}{% endif %}{% endfor %}"
      expected = "1234"
      assertThat(LiquidParser.newInstance().parse(markup).renderJson(assigns), `is`(expected))

      assigns = "{ \"array\":[[1,2],[3,4],[5,6]] }"

      markup = "{% for item in array %}" +
          "{% for i in item %}" +
          "{% if i == 1 %}" +
          "{% break %}" +
          "{% endif %}" +
          "{{ i }}" +
          "{% endfor %}" +
          "{% endfor %}"
      expected = "3456"
      assertThat(LiquidParser.newInstance().parse(markup).renderJson(assigns), `is`(expected))

      assigns = "{ \"array\": { \"items\":[1,2,3,4,5] } }"

      markup = "{% for i in array.items %}{% if i == 9999 %}{% break %}{% endif %}{{ i }}{% endfor %}"
      expected = "12345"
      assertThat(LiquidParser.newInstance().parse(markup).renderJson(assigns), `is`(expected))
    }

    /*
     * def test_for_with_continue
     *   assigns = {'array' => {'items' => [1,2,3,4,5]}}
     *
     *   markup = '{% for i in array.items %}{% continue %}{% endfor %}'
     *   expected = ""
     *   assert_template_result(expected,markup,assigns)
     *
     *   markup = '{% for i in array.items %}{{ i }}{% continue %}{% endfor %}'
     *   expected = "12345"
     *   assert_template_result(expected,markup,assigns)
     *
     *   markup = '{% for i in array.items %}{% continue %}{{ i }}{% endfor %}'
     *   expected = ""
     *   assert_template_result(expected,markup,assigns)
     *
     *   markup = '{% for i in array.items %}{% if i > 3 %}{% continue %}{% endif %}{{ i }}{% endfor %}'
     *   expected = "123"
     *   assert_template_result(expected,markup,assigns)
     *
     *   markup = '{% for i in array.items %}{% if i == 3 %}{% continue %}{% else %}{{ i }}{% endif %}{% endfor %}'
     *   expected = "1245"
     *   assert_template_result(expected,markup,assigns)
     *
     *   # tests to ensure it only continues the local for loop and not all of them.
     *   assigns = {'array' => [[1,2],[3,4],[5,6]] }
     *   markup = '{% for item in array %}' +
     *              '{% for i in item %}' +
     *                '{% if i == 1 %}' +
     *                  '{% continue %}' +
     *                '{% endif %}' +
     *                '{{ i }}' +
     *              '{% endfor %}' +
     *            '{% endfor %}'
     *   expected = '23456'
     *   assert_template_result(expected, markup, assigns)
     *
     *   # test continue does nothing when unreached
     *   assigns = {'array' => {'items' => [1,2,3,4,5]}}
     *   markup = '{% for i in array.items %}{% if i == 9999 %}{% continue %}{% endif %}{{ i }}{% endfor %}'
     *   expected = '12345'
     *   assert_template_result(expected, markup, assigns)
     * end
     */
    @Test
    fun forWithContinueTest() {

      var markup = "{% for i in array.items %}{% continue %}{% endfor %}"
      var expected = ""
      LiquidParser.newInstance().parse(markup)
          .assertThat()
          .rendering("{ \"array\": { \"items\":[1,2,3,4,5] } }")
          .isEqualTo(expected)

      markup = "{% for i in array.items %}{{ i }}{% continue %}{% endfor %}"
      expected = "12345"
      LiquidParser.newInstance().parse(markup)
          .assertThat()
          .rendering("{ \"array\": { \"items\":[1,2,3,4,5] } }")
          .isEqualTo(expected)

      LiquidParser.newInstance().parse("{% for i in array.items %}{% continue %}{{ i }}{% endfor %}")
          .assertThat()
          .rendering("{ \"array\": { \"items\":[1,2,3,4,5] } }")
          .isEqualTo("")

      markup = "{% for i in array.items %}{% if i > 3 %}{% continue %}{% endif %}{{ i }}{% endfor %}"
      expected = "123"
      LiquidParser.newInstance().parse(markup)
          .assertThat()
          .rendering("{ \"array\": { \"items\":[1,2,3,4,5] } }")
          .isEqualTo(expected)

      markup = "{% for i in array.items %}{% if i == 3 %}{% continue %}{% else %}{{ i }}{% endif %}{% endfor %}"
      expected = "1245"
      LiquidParser.newInstance().parse(markup)
          .assertThat()
          .rendering("{ \"array\": { \"items\":[1,2,3,4,5] } }")
          .isEqualTo(expected)

      expected = "23456"
      LiquidParser.newInstance().parse("{% for item in array %}" +
          "{% for i in item %}" +
          "{% if i == 1 %}" +
          "{% continue %}" +
          "{% endif %}" +
          "{{ i }}" +
          "{% endfor %}" +
          "{% endfor %}")
          .assertThat()
          .rendering("{ \"array\":[[1,2],[3,4],[5,6]] }")
          .isEqualTo(expected)

      LiquidParser.newInstance().parse("{% for i in array.items %}{% if i == 9999 %}{% continue %}{% endif %}{{ i }}{% endfor %}")
          .assertThat()
          .rendering("{ \"array\": { \"items\":[1,2,3,4,5] } }")
          .isEqualTo("12345")
    }

    /*
     * def test_for_tag_string
     *   # ruby 1.8.7 "String".each => Enumerator with single "String" element.
     *   # ruby 1.9.3 no longer supports .each on String though we mimic
     *   # the functionality for backwards compatibility
     *
     *   assert_template_result('test string',
     *               '{%for val in string%}{{val}}{%endfor%}',
     *               'string' => "test string")
     *
     *   assert_template_result('test string',
     *               '{%for val in string limit:1%}{{val}}{%endfor%}',
     *               'string' => "test string")
     *
     *   assert_template_result('val-string-1-1-0-1-0-true-true-test string',
     *               '{%for val in string%}' +
     *               '{{forloop.name}}-' +
     *               '{{forloop.index}}-' +
     *               '{{forloop.length}}-' +
     *               '{{forloop.index0}}-' +
     *               '{{forloop.rindex}}-' +
     *               '{{forloop.rindex0}}-' +
     *               '{{forloop.first}}-' +
     *               '{{forloop.last}}-' +
     *               '{{val}}{%endfor%}',
     *               'string' => "test string")
     * end
     */
    @Test
    @Throws(RecognitionException::class)
    fun forTagStringTest() {

      var json = "{ \"string\":\"test string\" }"

      assertThat(LiquidParser.newInstance().parse("{%for val in string%}{{val}}{%endfor%}").renderJson(json), `is`("test " + "string"))

      assertThat(LiquidParser.newInstance().parse("{%for val in string limit:1%}{{val}}{%endfor%}").renderJson(json), `is`("test string"))

      assertThat(LiquidParser.newInstance().parse("{%for val in string%}" +
          "{{forloop.name}}-" +
          "{{forloop.index}}-" +
          "{{forloop.length}}-" +
          "{{forloop.index0}}-" +
          "{{forloop.rindex}}-" +
          "{{forloop.rindex0}}-" +
          "{{forloop.first}}-" +
          "{{forloop.last}}-" +
          "{{val}}{%endfor%}").renderJson(json), `is`("val-string-1-1-0-1-0-true-true-test string"))

      // extra `name` testjson = "{ \"string\":\"test string\" }";
      json = "{ \"X\": [ { \"Y\":\"foo\"}, \"test string\" ] }"
      assertThat(LiquidParser.newInstance().parse("{% for x in X[0].Y %}{{forloop.name}}-{{x}}{%endfor%}").renderJson(json),
          `is`("x-X[0].Y-foo"))
    }

    /*
     * def test_blank_string_not_iterable
     *   assert_template_result('', "{% for char in characters %}I WILL NOT BE OUTPUT{% endfor %}", 'characters' => '')
     * end
     */
    @Test
    @Throws(RecognitionException::class)
    fun blankStringNotIterableTest() {

      LiquidParser.newInstance()
          .parse("{% for char in characters %}I WILL NOT BE OUTPUT{% endfor %}")
          .assertThat()
          .rendering()
          .isEqualTo("")
    }

    /*
     * Verified with the following Ruby code:
     *
     * require 'liquid'
     *
     * template = <<-HEREDOC
     * `{% for c1 in chars %}
     *   {{ forloop.index }}
     *   {% for c2 in chars %}
     *     {{ forloop.index }} {{ c1 }} {{ c2 }}
     *   {% endfor %}
     * {% endfor %}`
     * HEREDOC
     *
     * @template = Liquid::TemplateFactory.newInstance().parse(template)
     * rendered = @template.render('chars' => %w[a b c])
     *
     * puts(rendered)
     */
    @Test
    fun nestedTest() {

      val variables = HashMap<String, Any>()

      variables["chars"] = arrayOf("a", "b", "c")

      val template = "`{% for c1 in chars %}\n" +
          "  {{ forloop.index }}\n" +
          "  {% for c2 in chars %}\n" +
          "    {{ forloop.index }} {{ c1 }} {{ c2 }}\n" +
          "  {% endfor %}\n" +
          "{% endfor %}`"

      val expected = "`\n" +
          "  1\n" +
          "  \n" +
          "    1 a a\n" +
          "  \n" +
          "    2 a b\n" +
          "  \n" +
          "    3 a c\n" +
          "  \n" +
          "\n" +
          "  2\n" +
          "  \n" +
          "    1 b a\n" +
          "  \n" +
          "    2 b b\n" +
          "  \n" +
          "    3 b c\n" +
          "  \n" +
          "\n" +
          "  3\n" +
          "  \n" +
          "    1 c a\n" +
          "  \n" +
          "    2 c b\n" +
          "  \n" +
          "    3 c c\n" +
          "  \n" +
          "`"

      assertThat(LiquidParser.newInstance().parse(template).render(variables), `is`(expected))
    }
  }
