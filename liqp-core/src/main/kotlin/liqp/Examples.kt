package liqp

import java.util.Collections.singletonMap

import com.google.common.collect.ImmutableList
import liqp.context.LContext
import liqp.filter.FilterParams
import liqp.filter.LFilter

/**
 * A class holding some examples of how to use Liqp.
 */
object Examples {

  private val engine = LiquidRenderer.defaultInstance

  private fun demoGuards() {

    val templateCtx = LiquidParser.newBuilder()
        .maxTemplateSize(300L)
        .maxTemplateSize(100L)
        .toParser()

    val rendered = templateCtx
        .parse("{% for i in (1..10) %}{{ text }}{% endfor %}")
        .render(mapOf("text" to "abcdefghijklmnopqrstuvwxyz"))

    println(rendered)
  }

  private fun demoSimple() {

    System.out.println(LiquidParser.newInstance()
        .parse("hi {{name}}")
        .render("name" to "tobi"))

    System.out.println(LiquidParser.newInstance().parse("hi {{name}}")
        .render("name" to "tobi"))
  }

  private fun demoCustomStrongFilter() {

    // first register your custom filter
    val ctx = LiquidParser.newInstance()
        .withFilters(object : LFilter("b") {
          override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
            // create a string from the  value
            val text = context.asString(value)

            // replace and return *...* with <strong>...</strong>
            return text?.replace("\\*(\\w(.*?\\w)?)\\*".toRegex(), "<strong>$1</strong>")
          }
        })

    // use your filter
    val template = ctx.parse("{{ wiki | b }}")
    val rendered = template.render(singletonMap("wiki", "Some *bold* text *in here*."))
    println(rendered)
  }

  private fun demoCustomRepeatFilter() {

    // first register your custom filter
    val ctx = LiquidParser.newInstance()
        .withFilters(object : LFilter("repeat") {
          override fun onFilterAction(context: LContext, value: Any?, vararg params: Any?): Any? {
            context.run {

              // check if an optional parameter is provided
              val times = context.asInteger(params[0]) ?: 1
              // get the text of the value
              val text = asString(value)
              return text?.repeat(times)
            }
          }
        })

    // use your filter
    val template = ctx.parse("{{ 'a' | repeat }}\n{{ 'b' | repeat:5 }}")
    val rendered = template.render()
    println(rendered)
  }

  private fun demoCustomSumFilter() {

    val ctx = LiquidParser.newInstance()
        .withFilters(object : LFilter("sum") {
          override fun onFilterAction(context: LContext,
                                      value: Any?,
                                      params: FilterParams): Any? {

            val numbers = context.asIterable(value)
            var sum = 0.0
            for (obj in numbers) {
              sum += (context.asNumber(obj)?.toDouble() ?: 0.0)
            }

            return sum
          }

        })

    val template = ctx.parse("{{ numbers | sum }}")
    val rendered = template.render(singletonMap("numbers", ImmutableList.of(1, 2, 3, 4, 5)))
    println(rendered)
  }

  //  private static void customLoopTag() {
  //
  //    Tag.registerTag(new Tag("loop") {
  //      @Override
  //      public Object render(TemplateFactory context, LNode... nodes) {
  //
  //        int n = super.asNumber(nodes[0].render(context)).intValue();
  //        LNode block = nodes[1];
  //
  //        StringBuilder builder = new StringBuilder();
  //
  //        while (n-- > 0) {
  //          builder.append(super.asString(block.render(context)));
  //        }
  //
  //        return builder.toString();
  //      }
  //    });
  //
  //    String source = "{% loop 5 %}looping!\n{% endloop %}";
  //
  //    Template template = Template.parse(source);
  //
  //    String rendered = template.render();
  //
  //    System.out.println(rendered);
  //  }
  //
  //  public static void instanceTag() {
  //
  //    String source = "{% loop 5 %}looping!\n{% endloop %}";
  //
  //    Template template = Template.parse(source).with(new Tag("loop") {
  //      @Override
  //      public Object render(TemplateFactory context, LNode... nodes) {
  //
  //        int n = super.asNumber(nodes[0].render(context)).intValue();
  //        LNode block = nodes[1];
  //
  //        StringBuilder builder = new StringBuilder();
  //
  //        while (n-- > 0) {
  //          builder.append(super.asString(block.render(context)));
  //        }
  //
  //        return builder.toString();
  //      }
  //    });
  //
  //    String rendered = template.render();
  //
  //    System.out.println(rendered);
  //  }
  //
  //  public static void instanceFilter() {
  //
  //    Template template = Template.parse("{{ numbers | sum }}").with(new Filter("sum") {
  //      @Override
  //      public Object apply(Object value, Object... params) {
  //
  //        Object[] numbers = super.asArray(value);
  //
  //        double sum = 0;
  //
  //        for (Object obj : numbers) {
  //          sum += super.asNumber(obj).doubleValue();
  //        }
  //
  //        return sum;
  //      }
  //    });
  //
  //    String rendered = template.render("{\"numbers\" : [1, 2, 3, 4, 5]}");
  //    System.out.println(rendered);
  //  }
  //
  //  public static void demoStrictVariables() {
  //    try {
  //      Template.parse("{{mu}}")
  //            .withRenderSettings(new RenderSettings.Builder().withStrictVariables(true).build())
  //            .render();
  //    } catch (RuntimeException ex) {
  //      System.out.println("Caught an exception for strict variables");
  //    }
  //  }

  @Throws(Exception::class)
  @JvmStatic
  fun main(args: Array<String>) {

    println("running liqp.Examples")

    println("\n=== demoSimple() ===")
    demoSimple()

    println("\n=== demoCustomStrongFilter() ===")
    demoCustomStrongFilter()

    println("\n=== demoCustomRepeatFilter() ===")
    demoCustomRepeatFilter()

    println("\n=== demoCustomSumFilter() ===")
    demoCustomSumFilter()

    //    System.out.println("\n=== customLoopTag() ===");
    //    customLoopTag();
    //
    //    System.out.println("\n=== instanceTag() ===");
    //    instanceTag();
    //
    //    System.out.println("\n=== instanceFilter() ===");
    //    instanceFilter();
    //
    //    System.out.println("\n=== demoStrictVariables() ===");
    //    demoStrictVariables();

    println("Done!")
  }
}
