package liqp.tags

import liqp.tag.LTag
import liqp.node.LNode
import liqp.context.LContext
import liqp.exceptions.LiquidRenderingException

const val INITIAL_DECR_VALUE:Long = 0
/**
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
class Decrement : LTag() {

  override fun render(context: LContext, vararg nodes: LNode): Any? {
    context.run {
      val variable = nodes[0].render(context)?.toString()
          ?: throw LiquidRenderingException("Invalid variable")
      val decrementVar = "@decrement_$variable"
      val variableExistsFlag = "@variable_${variable}_exists"

      val value: Long = context[decrementVar] ?: INITIAL_DECR_VALUE
      val nextValue = value - 1

      if (value == INITIAL_DECR_VALUE) {
        // If this is the first 'decrement' tag, check if the variable exists in the outer scope.
        context[variableExistsFlag] = context.hasVar(variable)
      }

      val varExists = context.getValue<Boolean>(variableExistsFlag)
      if (varExists != true) {
        // Set the 'variable' to the next value, only if it was flagged as not being defined in the outer scope
        //todo:ericm Shouldn't this variable shadow the outer scope variable?
        context[variable] = nextValue
      }

      // Store the nextValue
      context[decrementVar] = nextValue

      return nextValue
    }
  }
}
