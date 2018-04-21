package liqp;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

public class TemplateTest {

  @Test
  public void renderObjectTest() {

    // `a` is public
    assertThat(LiquidParser.newInstance().parse("{{foo.a}}").render("foo", new Foo()), is("A"));

    // there is a public `getB()` method that exposes `b`
    assertThat(LiquidParser.newInstance().parse("{{foo.b}}").render("foo", new Foo()), is("B"));

    // `c` is not accessible
    assertThat(LiquidParser.newInstance().parse("{{foo.c}}").render("foo", new Foo()), is(""));
  }

  @Test
  public void renderJSONStringTest() {

    final String expected = "Hey";

    String rendered = LiquidParser.newInstance().parse("{{mu}}").render("{\"mu\" : \"" + expected + "\"}");
    assertThat(rendered, is(expected));
  }

  @Test
  public void renderJSONStringTestInvalidJSON_NotAccessed() {
    assertThatCode(() -> LiquidParser.newInstance().parse("mu").render("{\"key : \"value\"}"))
          .doesNotThrowAnyException();
  }

  @Test
  public void renderJSONStringTestInvalidJSON_Accessed() {
    assertThatCode(() -> LiquidParser.newInstance().parse("{{ key }}").render("{\"key : \"value\"}"))
          .isInstanceOf(JsonParseException.class);
  }

  @Test
  public void renderVarArgsTest() {

    final String expected = "Hey";

    String rendered = LiquidParser.newInstance().parse("{{mu}}").render("mu", expected);
    assertThat(rendered, is(expected));

    rendered = LiquidParser.newInstance().parse("{{a}}{{b}}{{c}}").render(ImmutableMap.of(
          "a", expected,
          "b", expected,
          "c", ""
          )
    );
    assertThat(rendered, is(expected + expected));

    rendered = LiquidParser.newInstance().parse("{{a}}{{b}}{{c}}").render(
          ImmutableMap.of("a", expected,
                "b", expected,
                "c", "") /* no value */
    );
    assertThat(rendered, is(expected + expected));

    rendered = LiquidParser.newInstance().parse("{{a}}{{b}}{{c}}").render(
          ImmutableMap.of("a", "A",
                "b", "B",
                "c", "C")
    );
    assertThat(rendered, is("ABC"));
  }

  @Test
  public void renderVarArgsTestInvalidKey2() {
    assertThatCode(() -> {
      LiquidParser.newInstance().parse("mu").render(null, 456);
    }).isInstanceOf(IllegalArgumentException.class);
  }

  static class Foo {

    public String a = "A";
    private String b = "B";
    private String c = "C";

    public String getB() {
      return b;
    }
  }
}
