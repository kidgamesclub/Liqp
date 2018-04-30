package liqp.tag

import liqp.context.LContext
import liqp.node.LNode

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

      val includesDir = context.includesDir
      val includeResourceFile = includesDir.resolve(includeResource + extension)

      // This will take advantage of caching for better performance, but we should probably
      // also cache the template locally
      val template = context.parseFile(includeResourceFile)

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
