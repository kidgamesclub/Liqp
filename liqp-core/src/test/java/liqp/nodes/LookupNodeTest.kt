package liqp.nodes

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import liqp.LiquidParser
import liqp.LiquidRenderer
import liqp.TestUtils.getNode
import org.junit.Before
import org.junit.Test
import java.util.*

class LookupNodeTest {

  private lateinit var engine: LiquidRenderer
  private lateinit var context: RenderContext

  @Before fun setUp() {
    engine = LiquidRenderer()
    context = createTestContext()
  }

  @Test
  fun renderTest() {
    val json = "{\"a\" : { \"b\" : { \"c\" : 42 } } }"

    val tests = arrayOf(arrayOf("{{a.b.c.d}}", ""), arrayOf("{{a.b.c}}", "42"))

    for (test in tests) {

      val template = LiquidParser.newInstance().parse(test[0])
      val rendered = template.renderJson(json)

      assert(rendered).isEqualTo(test[1])
    }
  }

  /*
   * def test_length_query
   *
   *   @context['numbers'] = [1,2,3,4]
   *
   *   assert_equal 4, @context['numbers.size']
   *
   *   @context['numbers'] = {1 => 1,2 => 2,3 => 3,4 => 4}
   *
   *   assert_equal 4, @context['numbers.size']
   *
   *   @context['numbers'] = {1 => 1,2 => 2,3 => 3,4 => 4, 'size' => 1000}
   *
   *   assert_equal 1000, @context['numbers.size']
   *
   * end
   */
  @Test
  @Throws(Exception::class)
  fun lengthQueryTest() {

    context["numbers"] = arrayOf(1, 2, 3, 4)

    assert(getNode("numbers.size", "expr").render(context)).isEqualTo(4 as Any)

    context["numbers"] = object : HashMap<Any, Any>() {
      init {
        put(1, 1)
        put(2, 2)
        put(3, 3)
        put(4, 4)
      }
    }
    assert(getNode("numbers.size", "expr").render(context)).isEqualTo(4)

    context["numbers"] = object : HashMap<Any, Any>() {
      init {
        put(1, 1)
        put(2, 2)
        put(3, 3)
        put(4, 4)
        put("size", 1000)
      }
    }
    assert(getNode("numbers.size", "expr").render(context)).isEqualTo(1000)
  }

  /*
   * def test_try_first
   *   @context['test'] = [1,2,3,4,5]
   *
   *   assert_equal 1, @context['test.first']
   *   assert_equal 5, @context['test.last']
   *
   *   @context['test'] = {'test' => [1,2,3,4,5]}
   *
   *   assert_equal 1, @context['test.test.first']
   *   assert_equal 5, @context['test.test.last']
   *
   *   @context['test'] = [1]
   *   assert_equal 1, @context['test.first']
   *   assert_equal 1, @context['test.last']
   * end
   */
  @Test
  fun tryFirstTest() {
    context["test"] = arrayOf(1, 2, 3, 4, 5)
    assert(getNode("test.first", "expr").render(context)).isEqualTo(1)
    assert(getNode("test.last", "expr").render(context)).isEqualTo(5)

    val context2 = this.createTestContext()
    context2["test"] = context
    assert(getNode("test.test.first", "expr").render(context2)).isEqualTo(1)
    assert(getNode("test.test.last", "expr").render(context2)).isEqualTo(5)
  }

  /*
   * def test_access_hashes_with_hash_notation
   *   @context['products'] = {'count' => 5, 'tags' => ['deepsnow', 'freestyle'] }
   *   @context['product'] = {'variants' => [ {'title' => 'draft151cm'}, {'title' => 'element151cm'}  ]}
   *
   *   assert_equal 5, @context['products["count"]']
   *   assert_equal 'deepsnow', @context['products["tags"][0]']
   *   assert_equal 'deepsnow', @context['products["tags"].first']
   *   assert_equal 'draft151cm', @context['product["variants"][0]["title"]']
   *   assert_equal 'element151cm', @context['product["variants"][1]["title"]']
   *   assert_equal 'draft151cm', @context['product["variants"][0]["title"]']
   *   assert_equal 'element151cm', @context['product["variants"].last["title"]']
   * end
   */
  @Test
  @Throws(Exception::class)
  fun accessHashesWithHashNotationTest() {

    val products = HashMap<String, Any>()
    val product = HashMap<String, Any>()

    products["count"] = 5
    products["tags"] = arrayOf("deepsnow", "freestyle")

    product["variants"] = arrayOf<HashMap<*, *>>(object : HashMap<String, Any>() {
      init {
        put("title", "draft151cm")
      }
    }, object : HashMap<String, Any>() {
      init {
        put("title", "element151cm")
      }
    })

    context["products"] = products
    context["product"] = product

    assert(getNode("products[\"count\"]", "expr").render(context)).isEqualTo(5)
    assert(getNode("products[\"tags\"][0]", "expr").render(context)).isEqualTo("deepsnow")
    assert(getNode("products[\"tags\"].first", "expr").render(context)).isEqualTo("deepsnow")
    assert(getNode("product[\"variants\"][0][\"title\"]", "expr").render(context)).isEqualTo("draft151cm")
    assert(getNode("product[\"variants\"][1][\"title\"]", "expr").render(context)).isEqualTo("element151cm")
    assert(getNode("product[\"variants\"][0][\"title\"]", "expr").render(context)).isEqualTo("draft151cm")
    assert(getNode("product[\"variants\"].last[\"title\"]", "expr").render(context)).isEqualTo("element151cm")
  }

  /*
   * def test_access_variable_with_hash_notation
   *   @context['foo'] = 'baz'
   *   @context['bar'] = 'foo'
   *
   *   assert_equal 'baz', @context['["foo"]']
   *   assert_equal 'baz', @context['[bar]']
   * end
   */
  @Test
  @Throws(Exception::class)
  fun accessVariableWithHashNotationTest() {

    context["foo"] = "baz"
    context["bar"] = "foo"

    assert(getNode("[\"foo\"]", "expr").render(context)).isEqualTo("baz")
    assert(getNode("[bar]", "expr").render(context)).isEqualTo("baz")
  }

  /*
   * def test_access_hashes_with_hash_access_variables
   *
   *   @context['var'] = 'tags'
   *   @context['nested'] = {'var' => 'tags'}
   *   @context['products'] = {'count' => 5, 'tags' => ['deepsnow', 'freestyle'] }
   *
   *   assert_equal 'deepsnow', @context['products[var].first']
   *   assert_equal 'freestyle', @context['products[nested.var].last']
   * end
   */
  @Test
  @Throws(Exception::class)
  fun accessHashesWithHashAccessVariablesTest() {

    context["var"] = "tags"
    context["nested"] = object : HashMap<String, Any>() {
      init {
        put("var", "tags")
      }
    }
    context["products"] = object : HashMap<String, Any>() {
      init {
        put("count", 5)
        put("tags", arrayOf("deepsnow", "freestyle"))
      }
    }

    assert(getNode("products[var].first", "expr").render(context)).isEqualTo("deepsnow")
    assert(getNode("products[nested.var].last", "expr").render(context)).isEqualTo("freestyle")
  }

  /*
   * def test_hash_notation_only_for_hash_access
   *   @context['array'] = [1,2,3,4,5]
   *   @context['hash'] = {'first' => 'Hello'}
   *
   *   assert_equal 1, @context['array.first']
   *   assert_equal nil, @context['array["first"]']
   *   assert_equal 'Hello', @context['hash["first"]']
   * end
   */
  @Test
  @Throws(Exception::class)
  fun hashNotationOnlyForHashAccessTest() {

    context["array"] = arrayOf(1, 2, 3, 4, 5)
    context["hash"] = object : HashMap<String, Any>() {
      init {
        put("first", "Hello")
      }
    }

    assert(getNode("array.first", "expr").render(context)).isEqualTo(1)
    assert(getNode("array[\"first\"]", "expr").render(context)).isNull()
    assert(getNode("hash[\"first\"]", "expr").render(context)).isEqualTo("Hello")
  }

  /*
   * def test_first_can_appear_in_middle_of_callchain
   *
   *   @context['product'] = {'variants' => [ {'title' => 'draft151cm'}, {'title' => 'element151cm'}  ]}
   *
   *   assert_equal 'draft151cm', @context['product.variants[0].title']
   *   assert_equal 'element151cm', @context['product.variants[1].title']
   *   assert_equal 'draft151cm', @context['product.variants.first.title']
   *   assert_equal 'element151cm', @context['product.variants.last.title']
   *
   * end
   */
  @Test
  @Throws(Exception::class)
  fun firstCanAppearInMiddleOfCallChainTest() {

    context["product"] = object : HashMap<String, Any>() {
      init {
        put("variants", arrayOf<HashMap<*, *>>(object : HashMap<String, Any>() {
          init {
            put("title", "draft151cm")
          }
        }, object : HashMap<String, Any>() {
          init {
            put("title", "element151cm")
          }
        }))
      }
    }

    assert(getNode("product.variants[0].title", "expr").render(context)).isEqualTo("draft151cm")
    assert(getNode("product.variants[1].title", "expr").render(context)).isEqualTo("element151cm")
    assert(getNode("product.variants.first.title", "expr").render(context)).isEqualTo("draft151cm")
    assert(getNode("product.variants.last.title", "expr").render(context)).isEqualTo("element151cm")
  }

  @Test
  @Throws(Exception::class)
  fun resolvesGetterAndPropertyContainer() {
    context["props"] = RecursivePropertyContainer()
    assert(getNode("props.foo.bar.title", "expr").render(context)).isEqualTo("Lord of the Grapes")
    assert(getNode("props.foo.bar", "expr").render(context)).isNotNull {
      it.isInstanceOf(RecursivePropertyContainer::class)
    }
  }

  /*
   * def test_size_of_array
   *   assigns = {"array" => [1,2,3,4]}
   *   assert_template_result('array has 4 elements', "array has {{ array.size }} elements", assigns)
   * end
   */
  @Test
  @Throws(Exception::class)
  fun size_of_arrayTest() {

    val assigns = "{ \"array\" : [1,2,3,4] }"

    assert(LiquidParser.newBuilder()
        .strictVariables(true)
        .toParser()
        .parse("array has {{ array.size }} elements")
        .renderJson(assigns)).isEqualTo("array has 4 elements")
  }

  /*
   * def test_size_of_hash
   *   assigns = {"hash" => {:a => 1, :b => 2, :c=> 3, :d => 4}}
   *   assert_template_result('hash has 4 elements', "hash has {{ hash.size }} elements", assigns)
   * end
   */
  @Test
  @Throws(Exception::class)
  fun size_of_hashTest() {

    val assigns = "{ \"hash\" : { \"a\" : 1, \"b\" : 2, \"c\" : 3, \"d\" : 4 } }"

    assert(LiquidParser.newInstance().parse("hash has {{ hash.size }} elements").renderJson(assigns)).isEqualTo("hash has" + " 4 elements")
  }

  private fun createTestContext(): RenderContext {
    return engine.createRenderContext(null)
  }
}

