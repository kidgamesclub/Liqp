package liqp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.exceptions.VariableNotExistException;
import org.junit.Test;

public class TemplateEngineSettingsTest {
  @Test
  public void renderstrictVariables1() {
    try {
      TemplateFactory.newBuilder().strictVariables(true)
            .parse("{{mu}}")

            .render();
    } catch (RuntimeException ex) {
      VariableNotExistException e = (VariableNotExistException) TestUtils.getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("mu"));
    }
  }

  @Test
  public void renderstrictVariables2() {
    try {
      TemplateFactory.newBuilder().strictVariables(true)
            .parse("{{mu}} {{qwe.asd.zxc}}")

            .render("mu", "muValue");
    } catch (RuntimeException ex) {
      VariableNotExistException e = (VariableNotExistException) TestUtils.getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("qwe.asd.zxc"));
    }
  }

  @Test
  public void renderstrictVariablesInCondition1() {
    TemplateFactory.newBuilder().strictVariables(true)
          .parse("{% if mu == \"somethingElse\" %}{{ badVariableName }}{% endif %}")

          .render("mu", "muValue");
  }

  @Test
  public void renderstrictVariablesInCondition2() {
    try {
      TemplateFactory.newBuilder().strictVariables(true)
            .parse("{% if mu == \"muValue\" %}{{ badVariableName }}{% endif %}")

            .render("mu", "muValue");
    } catch (RuntimeException ex) {
      VariableNotExistException e = (VariableNotExistException) TestUtils.getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("badVariableName"));
    }
  }

  @Test
  public void renderstrictVariablesInAnd1() {
    try {
      TemplateFactory.newBuilder().strictVariables(true)
            .parse("{% if mu == \"muValue\" and checkThis %}{{ badVariableName }}{% endif %}")

            .render("mu", "muValue");
    } catch (RuntimeException ex) {
      VariableNotExistException e = (VariableNotExistException) TestUtils.getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("checkThis"));
    }
  }

  @Test
  public void renderstrictVariablesInAnd2() {
    TemplateFactory.newBuilder().strictVariables(true)
          .parse("{% if mu == \"somethingElse\" and doNotCheckThis %}{{ badVariableName " +
                "}}{% endif %}")

          .render("mu", "muValue");
  }

  @Test
  public void renderstrictVariablesInOr1() {
    try {
      TemplateFactory.newBuilder().strictVariables(true)
            .parse("{% if mu == \"muValue\" or doNotCheckThis %}{{ badVariableName }}{% endif %}")

            .render("mu", "muValue");
    } catch (RuntimeException ex) {
      VariableNotExistException e = (VariableNotExistException) TestUtils.getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("badVariableName"));
    }
  }

  @Test
  public void renderstrictVariablesInOr2() {
    try {
      TemplateFactory.newBuilder().strictVariables(true)
            .parse("{% if mu == \"somethingElse\" or checkThis %}{{ badVariableName }}{% endif %}")
            .render("mu", "muValue");
    } catch (RuntimeException ex) {
      VariableNotExistException e = (VariableNotExistException) TestUtils.getExceptionRootCause(ex);
      assertThat(e.getVariableName(), is("checkThis"));
    }
  }
}
