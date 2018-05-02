package liqp.tags

import liqp.context.LContext
import liqp.node.LNode
import liqp.tag.LTag

class Tablerow : LTag() {

  /**
   * Tables
   */
  override fun render(context: LContext, vararg nodes: LNode): Any? {
    context.run {
      val valueName: String = nodes[0].execute(context)
      val collection = asIterable(nodes[1].render(context)).toList()
      val block = nodes[2]
      val attributes = getAttributes(collection, 3, context, *nodes)

      val cols = attributes[COLS] ?: 0
      val limit = attributes[LIMIT] ?: 0

      val tablerowloopContext = mutableMapOf<String, Any>()

      tablerowloopContext[LENGTH] = collection.size

      context.pushFrame()
      context[TABLEROWLOOP] = tablerowloopContext

      context.startLoop(collection.size, null)

      val builder = StringBuilder()

      val total = Math.min(collection.size, limit)

      if (total == 0) {

        builder.append("<tr class=\"row1\">\n</tr>\n")
      } else {

        var i = 0
        var c = 1
        var r = 0
        while (i < total) {

          context.incrementIterations()

          context[valueName] = collection[i]

          tablerowloopContext[INDEX0] = i
          tablerowloopContext[INDEX] = i + 1
          tablerowloopContext[RINDEX0] = total - i - 1
          tablerowloopContext[RINDEX] = total - i
          tablerowloopContext[FIRST] = i == 0
          tablerowloopContext[LAST] = i == total - 1
          tablerowloopContext[COL0] = c - 1
          tablerowloopContext[COL] = c
          tablerowloopContext[COL_FIRST] = c == 1
          tablerowloopContext[COL_LAST] = c == cols

          if (c == 1) {
            r++
            builder.append("<tr class=\"row").append(r).append("\">").append(if (r == 1) "\n" else "")
          }

          builder.append("<td class=\"col").append(c).append("\">")
          builder.append(asString(block.render(context)))
          builder.append("</td>")

          if (c == cols || i == total - 1) {
            builder.append("</tr>\n")
            c = 0
          }
          i++
          c++
        }
      }

      context.popFrame()
      return builder.toString()
    }
  }

  private fun getAttributes(collection: List<Any>,
                            fromIndex: Int,
                            context: LContext,
                            vararg tokens: LNode): Map<String, Int> {

    context.run {
      val attributes = mutableMapOf<String, Int>()

      attributes[COLS] = collection.size
      attributes[LIMIT] = Integer.MAX_VALUE

      for (i in fromIndex until tokens.size) {

        val attribute = asIterable(tokens[i].render(context)).toList()

        try {
          attributes[asString(attribute[0])!!] = asInteger(attribute[1])!!
        } catch (e: Exception) {
          /* just ignore incorrect attributes */
        }
      }
      return attributes
    }
  }
}

