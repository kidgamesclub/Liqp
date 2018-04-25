package liqp.nodes;

import static liqp.TestUtils.getNode;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import liqp.Template;
import liqp.LiquidRenderer;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Before;
import org.junit.Test;

public class LookupNodeTest {

  private LiquidRenderer engine;
  private RenderContext context;

  @Before
  public void setUp() throws Exception {
    engine = new LiquidRenderer();
    context = createTestContext();
  }

  @Test
  public void renderTest() throws RecognitionException {

    String json = "{\"a\" : { \"b\" : { \"c\" : 42 } } }";

    String[][] tests = {
          {"{{a.b.c.d}}", ""},
          {"{{a.b.c}}", "42"},
    };

    for (String[] test : tests) {

      Template template = LiquidParser.newInstance().parse(test[0]);
      String rendered = String.valueOf(template.render(json));

      assertThat(rendered, is(test[1]));
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
  public void lengthQueryTest() throws Exception {

    context.set("numbers", new Integer[]{1, 2, 3, 4});

    assertThat(getNode("numbers.size", "expr").render(context), is((Object) 4));

    context.set("numbers", new HashMap<Object, Object>() {{
      put(1, 1);
      put(2, 2);
      put(3, 3);
      put(4, 4);
    }});
    assertThat(getNode("numbers.size", "expr").render(context), is((Object) 4));

    context.set("numbers", new HashMap<Object, Object>() {{
      put(1, 1);
      put(2, 2);
      put(3, 3);
      put(4, 4);
      put("size", 1000);
    }});
    assertThat(getNode("numbers.size", "expr").render(context), is((Object) 1000));
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
  public void tryFirstTest() throws Exception {

    context.set("test", new Integer[]{1, 2, 3, 4, 5});
    assertThat(getNode("test.first", "expr").render(context), is((Object) 1));
    assertThat(getNode("test.last", "expr").render(context), is((Object) 5));

    RenderContext context2 = this.createTestContext();
    context2.set("test", context);
    assertThat(getNode("test.test.first", "expr").render(context2), is((Object) 1));
    assertThat(getNode("test.test.last", "expr").render(context2), is((Object) 5));
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
  public void accessHashesWithHashNotationTest() throws Exception {

    Map<String, Object> products = new HashMap<String, Object>();
    Map<String, Object> product = new HashMap<String, Object>();

    products.put("count", 5);
    products.put("tags", new String[]{"deepsnow", "freestyle"});

    product.put("variants", new HashMap[]{
                new HashMap<String, Object>() {{
                  put("title", "draft151cm");
                }},
                new HashMap<String, Object>() {{
                  put("title", "element151cm");
                }}
          }
    );

    context.set("products", products);
    context.set("product", product);

    assertThat(getNode("products[\"count\"]", "expr").render(context), is((Object) 5));
    assertThat(getNode("products[\"tags\"][0]", "expr").render(context), is((Object) "deepsnow"));
    assertThat(getNode("products[\"tags\"].first", "expr").render(context), is((Object) "deepsnow"));
    assertThat(getNode("product[\"variants\"][0][\"title\"]", "expr").render(context), is((Object) "draft151cm"));
    assertThat(getNode("product[\"variants\"][1][\"title\"]", "expr").render(context), is((Object) "element151cm"));
    assertThat(getNode("product[\"variants\"][0][\"title\"]", "expr").render(context), is((Object) "draft151cm"));
    assertThat(getNode("product[\"variants\"].last[\"title\"]", "expr").render(context), is((Object) "element151cm"));
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
  public void accessVariableWithHashNotationTest() throws Exception {

    context.set("foo", "baz");
    context.set("bar", "foo");

    assertThat(getNode("[\"foo\"]", "expr").render(context), is((Object) "baz"));
    assertThat(getNode("[bar]", "expr").render(context), is((Object) "baz"));
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
  public void accessHashesWithHashAccessVariablesTest() throws Exception {

    context.set("var", "tags");
    context.set("nested", new HashMap<String, Object>() {{
      put("var", "tags");
    }});
    context.set("products", new HashMap<String, Object>() {{
      put("count", 5);
      put("tags", new String[]{"deepsnow", "freestyle"});
    }});

    assertThat(getNode("products[var].first", "expr").render(context), is((Object) "deepsnow"));
    assertThat(getNode("products[nested.var].last", "expr").render(context), is((Object) "freestyle"));
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
  public void hashNotationOnlyForHashAccessTest() throws Exception {

    context.set("array", new Integer[]{1, 2, 3, 4, 5});
    context.set("hash", new HashMap<String, Object>() {{
      put("first", "Hello");
    }});

    assertThat(getNode("array.first", "expr").render(context), is((Object) 1));
    assertThat(getNode("array[\"first\"]", "expr").render(context), is((Object) null));
    assertThat(getNode("hash[\"first\"]", "expr").render(context), is((Object) "Hello"));
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
  public void firstCanAppearInMiddleOfCallChainTest() throws Exception {

    context.set("product", new HashMap<String, Object>() {{
      put("variants", new HashMap[]{
            new HashMap<String, Object>() {{
              put("title", "draft151cm");
            }},
            new HashMap<String, Object>() {{
              put("title", "element151cm");
            }}
      });
    }});

    assertThat(getNode("product.variants[0].title", "expr").render(context), is((Object) "draft151cm"));
    assertThat(getNode("product.variants[1].title", "expr").render(context), is((Object) "element151cm"));
    assertThat(getNode("product.variants.first.title", "expr").render(context), is((Object) "draft151cm"));
    assertThat(getNode("product.variants.last.title", "expr").render(context), is((Object) "element151cm"));
  }

  @Test
  public void resolvesGetterAndPropertyContainer() throws Exception {

    context.set("props", new RecursivePropertyContainer());

    assertThat(getNode("props.foo.bar.title", "expr").render(context), is((Object) "Lord of the Grapes"));
    assertThat(getNode("props.foo.bar", "expr").render(context), is(instanceOf(RecursivePropertyContainer.class)));
  }

  /*
   * def test_size_of_array
   *   assigns = {"array" => [1,2,3,4]}
   *   assert_template_result('array has 4 elements', "array has {{ array.size }} elements", assigns)
   * end
   */
  @Test
  public void size_of_arrayTest() throws Exception {

    String assigns = "{ \"array\" : [1,2,3,4] }";

    assertThat(LiquidParser.newBuilder()
          .strictVariables(true)
          .toParser()
          .parse("array has {{ array.size }} elements")
          .render(assigns), is("array has 4 elements"));
  }

  /*
   * def test_size_of_hash
   *   assigns = {"hash" => {:a => 1, :b => 2, :c=> 3, :d => 4}}
   *   assert_template_result('hash has 4 elements', "hash has {{ hash.size }} elements", assigns)
   * end
   */
  @Test
  public void size_of_hashTest() throws Exception {

    String assigns = "{ \"hash\" : { \"a\" : 1, \"b\" : 2, \"c\" : 3, \"d\" : 4 } }";

    assertThat(LiquidParser.newInstance().parse("hash has {{ hash.size }} elements").render(assigns), is("hash has" +
          " 4 elements"));
  }

  private RenderContext createTestContext() {
    return engine.createRenderContext(null);
  }
}
