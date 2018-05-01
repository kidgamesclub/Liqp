package liqp.tags

import liqp.ControlResult.BREAK
import liqp.context.LContext
import liqp.exceptions.ExceededMaxIterationsException
import liqp.exceptions.LiquidRenderingException
import liqp.node.LNode
import liqp.safeSubList
import liqp.tag.LTag

private const val OFFSET = "offset"
private const val CONTINUE = "continue"

class For : LTag() {

  /**
   * forloop.length      # => length of the entire for loop
   * forloop.index       # => index of the current iteration
   * forloop.index0      # => index of the current iteration (zero based)
   * forloop.rindex      # => how many items are still left?
   * forloop.rindex0     # => how many items are still left? (zero based)
   * forloop.first       # => is this the first iteration?
   * forloop.last        # => is this the last iteration?
   */
  override fun render(context: LContext, vararg nodes: LNode): Any? {

    // The first node in the array denotes whether this is a for-tag
    // over an array, `for item in array ...`, or a for-tag over a
    // range, `for i in (4..item.length)`.
    val isArray:Boolean = nodes[0].execute(context)
    val id:String = nodes[1].execute(context)

    // Each for tag has its own context that keeps track of its own variables (scope)
    context.pushFrame()

    val rendered = if (isArray) renderArray(id, context, *nodes) else renderRange(id, context, *nodes)

    context.popFrame()
    return rendered
  }

  private fun renderArray(id: String, context: LContext, vararg tokens: LNode): Any? {

    val results = mutableListOf<Any?>()

    // attributes start from index 5
    val attributes = getAttributes(5, context, *tokens)

    val offset = attributes[OFFSET] ?: 0
    val limit = attributes[LIMIT] ?: 0

    val iterated:List<Any>? = tokens[2].executeOrNull(context)

    val block = tokens[3]
    val blockIfEmptyOrNull = tokens[4]

    if (iterated?.isEmpty() == true) {
      return blockIfEmptyOrNull.render(context)
    }

    val length = Math.min(limit, iterated!!.size)

    val loopName = id + "-" + tokens[2].toString()
    context.startLoop(length, loopName)

    var continueIndex = offset

    var i = offset
    iterated.safeSubList(offset, offset + limit).forEachIndexed outer@{ n, item->
      context.incrementIterations()

      continueIndex = i
      context[id] = item

      val children = block.children
      inner@for (node in children) {
        val value = node.render(context)

        if (value == CONTINUE) {
          // break from this inner loop: equals continue outer loop!
          break@inner
        }

        if (value == BREAK) {
          return@outer
        }

        results.addAll(context.asIterable(value))
      }
      i++
    }

    context.setRoot(CONTINUE, continueIndex + 1)
    return results
  }

  private fun renderRange(id: String, context: LContext, vararg tokens: LNode): Any {

    val results = mutableListOf<Any?>()


    // attributes start from index 5
    val attributes = getAttributes(5, context, *tokens)

    val offset = attributes[OFFSET] ?: 0
    val limit = attributes[LIMIT] ?: throw LiquidRenderingException("Limit required")

    val block = tokens[4]

    try {
      val from:Int = tokens[2].execute(context)
      val to:Int = tokens[3].execute(context)

      val length = to - from

      context.startLoop(length, null)
      var continueIndex = from + offset

      var i = from + offset
      var n = 0
      outer@while (i <= to && n < limit) {

        context.incrementIterations()
        continueIndex = i
        context[id] = i

        val children = block.children
        inner@for (node in children) {

          val value = node.render(context) ?: continue@inner

          if (value === CONTINUE) {
            // break from this inner loop: equals continue outer loop!
            break@inner
          }

          if (value === BREAK) {
            // break from outer loop
            break@outer
          }

          if (context.isIterable(value)) {
            results.addAll(context.asIterable(value))
          } else {
            results.add(value)
          }
        }
        i++
        n++
      }

      context[CONTINUE] = continueIndex + 1
    } catch (e: ExceededMaxIterationsException) {
      throw e
    } catch (e: Exception) {
      /* just ignore incorrect expressions */
    }

    return results
  }

  private fun getAttributes(fromIndex: Int, context: LContext, vararg tokens: LNode): Map<String, Int> {

    val attributes = mutableMapOf<String, Int>()

    attributes[OFFSET] = 0
    attributes[LIMIT] = Integer.MAX_VALUE

    for (i in fromIndex until tokens.size) {

      val attribute:List<Any?> = tokens[i].execute(context)

      try {
        val key = attribute[0].toString()
        attributes[key] = context.asInteger(attribute[1])?:0
      } catch (e: Exception) {
        /* just ignore incorrect attributes */
      }
    }

    return attributes
  }


}
