package liqp.tags

import liqp.ControlResult
import liqp.context.LContext
import liqp.node.LNode
import liqp.tag.LTag

const val TEMP_SET_KEY = "@ifchanged"

class Ifchanged : LTag() {

  /**
  {% for product in products %}

  {% ifchanged %}<h3>{{ product.created_at | date:"%w" }}</h3>{% endifchanged %}

  <p>{{ product.title }} </p>

  ...

  {% endfor %}
   */
  override fun render(context: LContext, vararg nodes: LNode): Any? {

    if (nodes.isEmpty()) {
      return ControlResult.NO_CONTENT
    }

    val forLoopContext = context.loopState
    val isFirst = forLoopContext.isFirst
    val isLast = forLoopContext.isLast
    val alreadySeen: MutableSet<Any>

    if (isFirst) {
      // This is the first value in the FOR loop, store the Set that keeps track of all the unique
      // values in this context

      alreadySeen = mutableSetOf()
      context[TEMP_SET_KEY] = alreadySeen
    } else {
      // Retrieve the Set that keeps track of all the unique values
      alreadySeen = context[TEMP_SET_KEY]!!
    }

    val rendered = nodes[0].render(context)
    val returned = when {
      rendered == null -> null
      alreadySeen.add(rendered) -> null
      else -> rendered
    }

    if (isLast) {
      // We're done iterating, remove the temporary Set that keeps track of all the unique values
      context.remove(TEMP_SET_KEY)
    }

    return returned
  }
}
