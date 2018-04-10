package liqp;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.common.collect.ImmutableMap;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class TemplateTest {

  @Test
  public void renderObjectTest() throws RecognitionException {

    // `a` is public
    assertThat(TemplateFactory.newBuilder().parse("{{foo.a}}").render("foo", new Foo()), is("A"));

    // there is a public `getB()` method that exposes `b`
    assertThat(TemplateFactory.newBuilder().parse("{{foo.b}}").render("foo", new Foo()), is("B"));

    // `c` is not accessible
    assertThat(TemplateFactory.newBuilder().parse("{{foo.c}}").render("foo", new Foo()), is(""));
  }

  @Test
  public void renderJSONStringTest() throws RecognitionException {

    final String expected = "Hey";

    String rendered = TemplateFactory.newBuilder().parse("{{mu}}").render("{\"mu\" : \"" + expected + "\"}");
    assertThat(rendered, is(expected));
  }

  public void renderJSONStringTestInvalidJSON() throws RecognitionException {
    assertThatCode(
          () -> {
            final String rendered = TemplateFactory.newBuilder().parse("mu").render("{\"key : \"value\"}"
            );
            System.out.println(rendered);
          })
          .isInstanceOf(JsonParseException.class);
    // missing quote after `key`

  }

  @Test
  public void renderVarArgsTest() throws RecognitionException {

    final String expected = "Hey";

    String rendered = TemplateFactory.newBuilder().parse("{{mu}}").render("mu", expected);
    assertThat(rendered, is(expected));

    rendered = TemplateFactory.newBuilder().parse("{{a}}{{b}}{{c}}").render(ImmutableMap.of(
          "a", expected,
          "b", expected,
          "c", ""
          )
    );
    assertThat(rendered, is(expected + expected));

    rendered = TemplateFactory.newBuilder().parse("{{a}}{{b}}{{c}}").render(
          ImmutableMap.of("a", expected,
                "b", expected,
                "c", "") /* no value */
    );
    assertThat(rendered, is(expected + expected));

    rendered = TemplateFactory.newBuilder().parse("{{a}}{{b}}{{c}}").render(
          ImmutableMap.of("a", "A",
                "b", "B",
                "c", "C")
    );
    assertThat(rendered, is("ABC"));
  }

  public void renderVarArgsTestInvalidKey2() throws RecognitionException {
    assertThatCode(() -> {
      TemplateFactory.newBuilder().parse("mu").render(null, 456);
    }).isInstanceOf(NullPointerException.class);
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
