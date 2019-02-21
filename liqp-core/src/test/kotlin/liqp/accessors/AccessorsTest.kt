package liqp.accessors

import assertk.assertions.isEqualTo
import lang.json.JsrObject
import lang.json.jsrObject
import liqp.createTestParser
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class AccessorsTest(val name: String, val template: String, val input: Any?, val expected: Any?) {

  @Test fun run() {
    val template = createTestParser {}.parse(template)
    val rendered = template.render(input)
    assertk.assert(rendered).isEqualTo(expected)
  }

  companion object {
    @JvmStatic @Parameterized.Parameters(name = "{0}") fun params(): Array<Array<Any>> {
      val mockJimObj = mockDude("Jim", 34, mapOf("facebook" to "smarty"))
      val mockJimJson = mockDudeJson("Jim", 34, mapOf("facebook" to "smarty"))
      val mockJimMap = mockDudeMap("Jim", 34, mapOf("facebook" to "smarty"))

      val mockOliverObj = mockDude("Oliver", 22, mapOf("facebook" to "ollie"))
      val mockOliverJson = mockDudeJson("Oliver", 22, mapOf("facebook" to "ollie"))
      val mockOliverMap = mockDudeMap("Oliver", 22, mapOf("facebook" to "ollie"))

      val mockGroupObj = mockGroup(mockJimObj, mockOliverObj)
      val mockGroupJson = mockGroupJson(mockJimJson, mockOliverJson)
      val mockGroupMap = mockGroupMap(mockJimMap, mockOliverMap)
      return arrayOf(
          arrayOf("single:obj", mockTemplate1, mockJimObj, mockTemplateExpect),
          arrayOf("single:json", mockTemplate1, mockJimJson, mockTemplateExpect),
          arrayOf("single:map", mockTemplate1, mockJimMap, mockTemplateExpect),

          arrayOf("group:obj", mockTemplate2, mockGroupObj, mockTemplateExpect2),
          arrayOf("group:json", mockTemplate2, mockGroupJson, mockTemplateExpect2),
          arrayOf("group:map", mockTemplate2, mockGroupMap, mockTemplateExpect2)
      )
    }

    val mockTemplate1 = "{{ name }} {%if age > 30%}Oldie{%endif%} -> {{ social.facebook }}"
    val mockTemplateExpect = "Jim Oldie -> smarty"

    val mockTemplate2 = "{% for dude in dudes %}{{ dude.name }} {%if dude.age > 30%}Oldie{%endif%} -> {{ dude.social.facebook }}\n{%endfor%}"
    val mockTemplateExpect2 = "Jim Oldie -> smarty\nOliver  -> ollie\n"

    fun mockGroup(vararg dudes: Dude): Group = Group(dudes.toList())
    fun mockGroupJson(vararg dudes: JsrObject) = jsrObject {
      "dudes" *= dudes.toList()
    }

    fun mockGroupMap(vararg dudes: Map<String, Any?>): Map<String, Any?> = mapOf(
        "dudes" to dudes
    )

    fun mockDude(name: String, age: Int, social: Map<String, Any>): Dude = Dude(name, age, social)

    fun mockDudeJson(name: String, age: Int, social: Map<String, Any>): JsrObject {
      return jsrObject {
        "name" *= name
        "age" *= age
        "social" *= social
      }
    }

    fun mockDudeMap(name: String, age: Int, social: Map<String, Any>): Map<String, Any?> {
      return mapOf(
          "name" to name,
          "age" to age,
          "social" to social
      )
    }
  }

  data class Dude(val name: String, val age: Int, val social: Map<String, Any>)
  data class Group(val dudes: List<Dude>) {
    constructor(vararg dudes: Dude) : this(dudes.toList())
  }
}

class AccessorsMapFilterIteratorTest {
  @Test fun testMapElements() {
    val input = mapOf("a" to 1, "b" to 2, "c" to 3)
    val template = createTestParser {}.parse("{% for pair in _ %}{{ pair.key }}->{{ pair.value}}" +
        "{% unless last %},{% endunless %}" +
        "{% endfor %}")
    val rendered = template.render(input)
    assertk.assert(rendered).isEqualTo("a->1,b->2,c->3,")
  }
}
