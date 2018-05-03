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
    try {
      if (isFirst) {
        // This is the first value in the FOR loop, store the Set that keeps track of all the unique
        // values in this context

        alreadySeen = mutableSetOf()
        context[TEMP_SET_KEY] = alreadySeen
      } else {
        // Retrieve the Set that keeps track of all the unique values
        alreadySeen = context[TEMP_SET_KEY, { mutableSetOf<Any>() }]
      }

      val rendered: Any? = nodes[0].render(context)

      // We store numbers as doubles so that 1.0, 1L and 1(Int) match
      val hashed = rendered?.run { (this as? Number)?.toDouble() ?: this }
      return when {
        hashed == null -> null
        alreadySeen.add(hashed) -> rendered
        else -> null
      }
    } finally {
      if (isLast) {
        // We're done iterating, remove the temporary Set that keeps track of all the unique values
        context.remove(TEMP_SET_KEY)
      }
    }
  }
}
