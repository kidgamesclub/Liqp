package liqp.tag

import liqp.context.LContext
import liqp.exceptions.InvalidIncludeException
import liqp.node.EmptyTemplate
import liqp.node.LNode
import liqp.node.LTemplate

const val DEFAULT_EXTENSION = ".liquid"

class Include : LTag() {

  override fun render(context: LContext, vararg nodes: LNode): Any? {
    var entered = false
    try {
      val includeResource = context.asString(nodes[0].render(context)) ?: ""
      val extension = when {
        '.' in includeResource -> ""
        else -> DEFAULT_EXTENSION
      }

      val includeResourceFile = context.includeFile.resolve(includeResource + extension)

      // This will take advantage of caching for better performance, but we should probably
      // also cache the template locally
      val template: LTemplate = try {
        context.parseFile(includeResourceFile)
      } catch (e: Exception) {
        if (context.parseSettings.isStrictIncludes) {
          throw InvalidIncludeException(includeResourceFile, e)
        }
        EmptyTemplate
      }

      // Push the frame prior to setting the variable.  This ensures that the variable doesnt
      // leak to any siblings
      context.pushFrame()
      entered = true

      // check if there's a optional "with expression"  If so, push
      if (nodes.size > 1) {
        val value = nodes[1].render(context)
        context[includeResource] = value
      }

      return context.render(template)
    } catch (e: InvalidIncludeException) {
      throw e
    } catch (e: Exception) {
      context.logs += e.toString()
      return ""
    } finally {
      if (entered) {
        context.popFrame()
      }
    }
  }
}
