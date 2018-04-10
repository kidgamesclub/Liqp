package liqp.tags;

import liqp.nodes.LNode;
import liqp.nodes.RenderContext;
import org.apache.commons.lang.BooleanUtils;

/*
    increment

    Creates a new number variable, and increases its value by 1 every time increment is
    called on the variable. The counter's initial value is 0.

    Here, an increment counter is used to create a unique numbered class for each list
    item:

    Input
        <ul>
          <li class="item-{% increment counter %}">apples</li>
          <li class="item-{% increment counter %}">oranges</li>
          <li class="item-{% increment counter %}">peaches</li>
          <li class="item-{% increment counter %}">plums</li>
        </ul>

    Output
        <ul>
          <li class="item-0">apples</li>
          <li class="item-1">oranges</li>
          <li class="item-2">peaches</li>
          <li class="item-3">plums</li>
        </ul>

    Variables created using increment are separate from variables created using assign
    or capture.
*/
public class Increment extends Tag {

  private static final Long INITIAL = 0L;

  @Override
  public Object render(RenderContext context, LNode... nodes) {

    String variable = super.asString(nodes[0].render(context));
    String incrementVariable = String.format("@increment_%s", variable);
    String variableExistsFlag = String.format("@variable_%s_exists", variable);

    Long value = context.get(incrementVariable);
    if (value == null) {
      value = INITIAL;
    }

    Long nextValue = value + 1;

    if (value.equals(INITIAL)) {
      // If this is the first 'increment' tag, check if the variable exists in the outer scope.
      context.set(variableExistsFlag, context.hasVar(variable));
    }

    final Boolean varExists = context.get(variableExistsFlag);
    if (!BooleanUtils.isTrue(varExists)) {
      // Set the 'variable' to the next value, only if it was flagged as not being defined in the outer scope
      //todo:Ericm This seems really odd - a weird side effect
      context.set(variable, nextValue);
    }

    // Store the nextValue
    context.set(incrementVariable, nextValue);

    return value;
  }
}
