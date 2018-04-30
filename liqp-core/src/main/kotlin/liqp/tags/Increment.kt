package liqp.tags

import liqp.context.LContext
import liqp.exceptions.LiquidRenderingException
import liqp.node.LNode
import liqp.tag.LTag

const val INITIAL:Long = 0

/**
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

Variables created using increment are separate from variables created using assign or capture.
 */

class Increment : LTag() {

  override fun render(context: LContext, vararg nodes: LNode): Any? {
    context.run {
      val variable = nodes[0].render(context)?.toString()
          ?: throw LiquidRenderingException("Invalid variable")
      val incrementVariable = "@increment_$variable"
      val variableExistsFlag = "@variable_${variable}_exists"

      val value: Long = context[incrementVariable] ?: INITIAL
      val nextValue = value + 1

      if (value == INITIAL) {
        // If this is the first 'increment' tag, check if the variable exists in the outer scope.
        context[variableExistsFlag] = context.hasVar(variable)
      }

      val varExists = context.get<Boolean>(variableExistsFlag)
      if (varExists == null || varExists == false) {
        // Set the 'variable' to the next value, only if it was flagged as not being defined in the outer scope
        //todo:Ericm This seems really odd - a weird side effect
        context[variable] = nextValue
      }

      // Store the nextValue
      context[incrementVariable] = nextValue

      return value
    }
  }
}
