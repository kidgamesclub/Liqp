package liqp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.exceptions.MissingVariableException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class LiquidRendererSettingsTest {
  @Test
  public void renderstrictVariables1() {
    try {
      LiquidParser.newBuilder().strictVariables(true)
            .toParser()
            .parse("{{mu}}")

            .render();
    } catch (RuntimeException ex) {
      MissingVariableException e = (MissingVariableException) TestUtils.getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("mu"));
    }
  }

  @Test
  public void renderstrictVariables2() {
    Assertions.assertThatCode(() -> LiquidParser.newBuilder()
          .strictVariables(true)
          .toParser()
          .parse("{{mu}} {{qwe.asd.zxc}}")
          .render("mu", "muValue"))
          .describedAs("Should throw missing variable exception")
          .isInstanceOf(MissingVariableException.class)
          .matches(ex -> {
            Assertions.assertThat(((MissingVariableException) ex).getVariableName()).isEqualToIgnoringCase("qwe.asd.zxc");
            return true;
          });
  }

  @Test
  public void renderstrictVariablesInCondition1() {
    LiquidParser.newBuilder().strictVariables(true)
          .toParser()
          .parse("{% if mu == \"somethingElse\" %}{{ badVariableName }}{% endif %}")
          .render("mu", "muValue");
  }

  @Test
  public void renderstrictVariablesInCondition2() {
    try {
      LiquidParser.newBuilder().strictVariables(true)
            .toParser()
            .parse("{% if mu == \"muValue\" %}{{ badVariableName }}{% endif %}")

            .render("mu", "muValue");
    } catch (RuntimeException ex) {
      MissingVariableException e = (MissingVariableException) TestUtils.getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("badVariableName"));
    }
  }

  @Test
  public void renderstrictVariablesInAnd1() {
    try {
      LiquidParser.newBuilder().strictVariables(true)
            .toParser()
            .parse("{% if mu == \"muValue\" and checkThis %}{{ badVariableName }}{% endif %}")
            .render("mu", "muValue");
    } catch (RuntimeException ex) {
      MissingVariableException e = (MissingVariableException) TestUtils.getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("checkThis"));
    }
  }

  @Test
  public void renderstrictVariablesInAnd2() {
    LiquidParser.newBuilder().strictVariables(true)
          .toParser()
          .parse("{% if mu == \"somethingElse\" and doNotCheckThis %}{{ badVariableName " +
                "}}{% endif %}")

          .render("mu", "muValue");
  }

  @Test
  public void renderstrictVariablesInOr1() {
    try {
      LiquidParser.newBuilder().strictVariables(true)
            .toParser()
            .parse("{% if mu == \"muValue\" or doNotCheckThis %}{{ badVariableName }}{% endif %}")

            .render("mu", "muValue");
    } catch (RuntimeException ex) {
      MissingVariableException e = (MissingVariableException) TestUtils.getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("badVariableName"));
    }
  }

  @Test
  public void renderstrictVariablesInOr2() {
    try {
      LiquidParser.newBuilder().strictVariables(true)
            .toParser()
            .parse("{% if mu == \"somethingElse\" or checkThis %}{{ badVariableName }}{% endif %}")
            .render("mu", "muValue");
    } catch (RuntimeException ex) {
      MissingVariableException e = (MissingVariableException) TestUtils.getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("checkThis"));
    }
  }
}
