package liqp;

import static liqp.AssertsKt.mapOf;
import static liqp.TestUtils.INSTANCE;
import static liqp.TestUtils.getExceptionRootCause;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.ZoneId;
import java.util.Collections;
import java.util.Locale;
import liqp.exceptions.MissingVariableException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class LiquidRendererSettingsTest {
  Liquify liquify = Liquify.getProvider();

  @Test
  public void renderstrictVariables1() {
    try {
      liquify.createParser()
            .parse("{{mu}}")

            .render();
    } catch (RuntimeException ex) {
      MissingVariableException e = (MissingVariableException) getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("mu"));
    }
  }

  @Test
  public void renderstrictVariables2() {
    Assertions.assertThatCode(() -> liquify.createParserJvm(settings -> settings.setStrictVariables(true))
          .parse("{{mu}} {{qwe.asd.zxc}}")
          .render(Collections.singletonMap("mu", "muValue"), Locale.US, ZoneId.systemDefault()))
          .describedAs("Should throw missing variable exception")
          .isInstanceOf(MissingVariableException.class)
          .matches(ex -> {
            Assertions.assertThat(((MissingVariableException) ex).getVariableName()).isEqualToIgnoringCase("qwe.asd" +
                  ".zxc");
            return true;
          });
  }

  @Test
  public void renderstrictVariablesInCondition1() {
    liquify.createParserJvm(builder -> builder.strictVariables(true))
          .parse("{% if mu == \"somethingElse\" %}{{ badVariableName }}{% endif %}")
          .render(mapOf("mu", "muValue"));
  }

  @Test
  public void renderstrictVariablesInCondition2() {
    try {
      liquify.createParserJvm(builder -> builder.setStrictVariables(true))
            .parse("{% if mu == \"muValue\" %}{{ badVariableName }}{% endif %}")
            .render(mapOf("mu", "muValue"));
    } catch (RuntimeException ex) {
      MissingVariableException e = (MissingVariableException) INSTANCE.getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("badVariableName"));
    }
  }

  @Test
  public void renderstrictVariablesInAnd1() {
    try {
      liquify.createParserJvm(builder -> builder.setStrictVariables(true))
            .parse("{% if mu == \"muValue\" and checkThis %}{{ badVariableName }}{% endif %}")
            .render(mapOf("mu", "muValue"));
    } catch (RuntimeException ex) {
      MissingVariableException e = (MissingVariableException) getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("checkThis"));
    }
  }

  @Test
  public void renderstrictVariablesInAnd2() {
    liquify.createParserJvm(builder -> builder.setStrictVariables(true))
          .parse("{% if mu == \"somethingElse\" and doNotCheckThis %}{{ badVariableName " +
                "}}{% endif %}")
          .render(mapOf("mu", "muValue"));
  }

  @Test
  public void renderstrictVariablesInOr1() {
    try {
      liquify.createParserJvm(builder -> builder.setStrictVariables(true))
            .parse("{% if mu == \"muValue\" or doNotCheckThis %}{{ badVariableName }}{% endif %}")
            .render(Collections.singletonMap("mu", "muValue"), Locale.US, ZoneId.systemDefault());
    } catch (RuntimeException ex) {
      MissingVariableException e = (MissingVariableException) INSTANCE.getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("badVariableName"));
    }
  }

  @Test
  public void renderstrictVariablesInOr2() {
    try {
      liquify.createParserJvm(builder -> builder.setStrictVariables(true))
            .parse("{% if mu == \"somethingElse\" or checkThis %}{{ badVariableName }}{% endif %}")
            .render(Collections.singletonMap("mu", "muValue"), Locale.US, ZoneId.systemDefault());
    } catch (RuntimeException ex) {
      MissingVariableException e = (MissingVariableException) getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("checkThis"));
    }
  }
}
