package liqp;

import static java.util.Collections.singletonMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import liqp.filter.PostFilter;
import liqp.context.LContext;

/**
 * A class holding some examples of how to use Liqp.
 */
public class Examples {

  private static LiquidRenderer engine = LiquidRenderer.getDefaultInstance();

  private static void demoGuards() {

    final LiquidParser templateCtx = LiquidParser.newBuilder()
          .maxTemplateSize(300L)
          .maxTemplateSize(100L)
          .toParser();

    String rendered = templateCtx.parse("{% for i in (1..10) %}{{ text }}{% endfor %}")
          .render(ImmutableMap.of("text", "abcdefghijklmnopqrstuvwxyz"));

    System.out.println(rendered);
  }

  private static void demoSimple() {

    System.out.println(LiquidParser.newInstance()
          .parse("hi {{name}}")
          .render(singletonMap("name", "tobi")));

    System.out.println(LiquidParser.newInstance().parse("hi {{name}}")
          .render("name", "tobi"));
  }

  private static void demoCustomStrongFilter() {

    // first register your custom filter
    final LiquidParser ctx = LiquidParser.newInstance()
          .withFilters(new PostFilter("b") {
            @Override
            public Object apply(LContext context, Object value, Object... params) {
              // create a string from the  value
              String text = super.asString(value);

              // replace and return *...* with <strong>...</strong>
              return text.replaceAll("\\*(\\w(.*?\\w)?)\\*", "<strong>$1</strong>");
            }
          });

    // use your filter
    Template template = ctx.parse("{{ wiki | b }}");
    String rendered = template.render(singletonMap("wiki", "Some *bold* text *in here*."));
    System.out.println(rendered);
  }

  private static void demoCustomRepeatFilter() {

    // first register your custom filter
    final LiquidParser ctx = LiquidParser.newInstance()
          .withFilters(new PostFilter("repeat") {
            @Override
            public Object apply(LContext context, Object value, Object... params) {

              // check if an optional parameter is provided
              int times = params.length == 0 ? 1 : super.asNumber(params[0]).intValue();

              // get the text of the value
              String text = super.asString(value);

              StringBuilder builder = new StringBuilder();

              while (times-- > 0) {
                builder.append(text);
              }

              return builder.toString();
            }
          });

    // use your filter
    Template template = ctx.parse("{{ 'a' | repeat }}\n{{ 'b' | repeat:5 }}");
    String rendered = template.render();
    System.out.println(rendered);
  }

  private static void demoCustomSumFilter() {

    final LiquidParser ctx = LiquidParser.newInstance()
          .withFilters(new PostFilter("sum") {
            @Override
            public Object apply(LContext context, Object value, Object... params) {

              Object[] numbers = super.asArray(value);

              double sum = 0;

              for (Object obj : numbers) {
                sum += super.asNumber(obj).doubleValue();
              }

              return sum;
            }
          });

    Template template = ctx.parse("{{ numbers | sum }}");
    String rendered = template.render(singletonMap("numbers", ImmutableList.of(1, 2, 3, 4, 5)));
    System.out.println(rendered);
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

  public static void main(String[] args) throws Exception {

    System.out.println("running liqp.Examples");

    System.out.println("\n=== demoSimple() ===");
    demoSimple();

    System.out.println("\n=== demoCustomStrongFilter() ===");
    demoCustomStrongFilter();

    System.out.println("\n=== demoCustomRepeatFilter() ===");
    demoCustomRepeatFilter();

    System.out.println("\n=== demoCustomSumFilter() ===");
    demoCustomSumFilter();

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

    System.out.println("Done!");
  }
}
