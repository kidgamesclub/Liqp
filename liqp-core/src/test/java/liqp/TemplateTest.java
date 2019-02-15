package liqp;

import static java.util.Collections.singletonMap;
import static liqp.AssertsKt.*;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.common.collect.ImmutableMap;
import javax.json.stream.JsonParsingException;
import org.junit.Test;

public class TemplateTest {

  @Test
  public void renderObjectTest() {

    // `a` is public
    assertThat(createTestParser().parse("{{foo.a}}").render(singletonMap("foo", new Foo())), is("A"));

    // there is a public `getB()` method that exposes `b`
    assertThat(createTestParser().parse("{{foo.b}}").render(singletonMap("foo", new Foo())), is("B"));

    // `c` is not accessible
    assertThat(createTestParser().parse("{{foo.c}}").render(singletonMap("foo", new Foo())), is(""));
  }

  @Test
  public void renderJSONStringTest() {

    final String expected = "Hey";

    String rendered = createTestParser().parse("{{mu}}").renderJson("{\"mu\" : \"" + expected + "\"}");
    assertThat(rendered, is(expected));
  }

  @Test
  public void renderJSONStringTestInvalidJSON_NotAccessed() {
    assertThatCode(() -> createTestParser().parse("mu").renderJson("{\"key : \"value\"}"))
          .isInstanceOf(JsonParsingException.class);
  }

  @Test
  public void renderJSONStringTestInvalidJSON_Accessed() {
    assertThatCode(() -> createTestParser().parse("{{ key }}").renderJson("{\"key : \"value\"}"))
          .isInstanceOf(JsonParsingException.class);
  }

  @Test
  public void renderVarArgsTest() {

    final String expected = "Hey";

    String rendered = createTestParser().parse("{{mu}}").render(singletonMap("mu", expected));
    assertThat(rendered, is(expected));

    rendered = createTestParser().parse("{{a}}{{b}}{{c}}").render(ImmutableMap.of(
          "a", expected,
          "b", expected,
          "c", ""
          )
    );
    assertThat(rendered, is(expected + expected));

    rendered = createTestParser().parse("{{a}}{{b}}{{c}}").render(
          ImmutableMap.of("a", expected,
                "b", expected,
                "c", "") /* no value */
    );
    assertThat(rendered, is(expected + expected));

    rendered = createTestParser().parse("{{a}}{{b}}{{c}}").render(
          ImmutableMap.of("a", "A",
                "b", "B",
                "c", "C")
    );
    assertThat(rendered, is("ABC"));
  }

  @Test
  public void renderVarArgsTestInvalidKey2() {
    assertThatCode(() -> {
      createTestParser().parse("mu").render(singletonMap(null, 456));
    }).doesNotThrowAnyException();
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
