package liqp.tags

import liqp.ControlResult.BREAK
import liqp.ControlResult.CONTINUE
import liqp.context.LContext
import liqp.node.LNode
import liqp.params.ResolvableNamedParams
import liqp.safeSlice
import liqp.tag.LTag

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


    // attributes start from index 5
    val attributes = ResolvableNamedParams(context,
        paramNodes = nodes.slice(5..nodes.lastIndex)
    )

    val offset:Int = attributes[OFFSET, 0]
    val limit:Int = attributes[LIMIT, Integer.MAX_VALUE]

    // Each for tag has its own context that keeps track of its own variables (scope)
    return context.withFrame {
      when (isArray) {
        true -> renderArray(id, context, offset, limit, *nodes)
        false -> renderRange(id, context, offset, limit, *nodes)
      }
    }
  }

  private fun renderArray(id: String, context: LContext,
                          offset:Int, limit:Int,
                          vararg tokens: LNode): Any? {

    val results = mutableListOf<Any?>()

    val iterated:List<Any> = tokens[2].executeOrNull(context) ?: emptyList()

    val block = tokens[3]
    val blockIfEmptyOrNull = tokens[4]

    if (iterated.isEmpty()) {
      return blockIfEmptyOrNull.render(context)
    }

    val length = Math.min(limit, iterated.size)

    val loopName = id + "-" + tokens[2].toString()
    context.startLoop(length, loopName)

    var continueIndex = offset

    val adjustedLimit = when (limit) {
      Integer.MAX_VALUE -> iterated.size
      else -> offset + limit
    }

    var i = offset
    val sliced = iterated.safeSlice(offset, adjustedLimit)
    outer@for (item in sliced) {
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
          break@outer
        }

        results.addAll(context.asIterable(value))
      }
      i++
    }

    context.setRoot(CONTINUE.name.toLowerCase(), continueIndex + 1)
    return results
  }

  private fun renderRange(id: String, context: LContext, offset:Int, limit:Int, vararg tokens: LNode): Any {

    val results = mutableListOf<Any?>()
    val block = tokens[4]

    val from:Int = tokens[2].executeOrNull<Int>(context) ?: return listOf<Any>()
    val to:Int = tokens[3].execute(context, 0)

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

    context[CONTINUE.name] = continueIndex + 1


    return results
  }
}
