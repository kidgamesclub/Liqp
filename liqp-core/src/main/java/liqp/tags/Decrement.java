package liqp.tags;

import liqp.tag.LTag;
import liqp.node.LNode;
import liqp.context.LContext;

/*
    decrement

    Creates a new number variable, and decreases its value by 1 every time decrement
    is called on the variable. The counter's initial value is -1.

    Input
        {% decrement variable %}
        {% decrement variable %}
        {% decrement variable %}

    Output
        -1
        -2
        -3

    Like increment, variables declared using decrement are independent from variables
    created using assign or capture.
*/
public class Decrement extends LTag {

    private static final Long INITIAL = -1L;

    @Override
    public Object render(LContext context, LNode... nodes) {

        Long value = INITIAL;
        String variable = super.asString(nodes[0].render(context));
        String decrementVariable = String.format("@decrement_%s", variable);
        String variableExistsFlag = String.format("@variable_%s_exists", variable);

        if (context.hasVar(decrementVariable)) {
            // Retrieve the old 'decrement' value
            value = context.get(decrementVariable);
        }

        if (value.equals(INITIAL)) {
            // If this is the first 'decrement' tag, check if the variable exists in the outer scope.
            context.set(variableExistsFlag, context.hasVar(variable));
        }

        if (!((Boolean) context.get(variableExistsFlag))) {
            // Set the 'variable' to the current value, only if it was flagged as not being defined in the outer scope
            context.set(variable, value);
        }

        // Store the nextValue
        context.set(decrementVariable, value - 1);

        return value;
    }
}
