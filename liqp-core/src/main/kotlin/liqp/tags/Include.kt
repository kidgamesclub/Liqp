package liqp.tags

import liqp.LValue
import liqp.nodes.LNode
import liqp.nodes.RenderContext

const val DEFAULT_EXTENSION = ".liquid"

class Include : Tag() {

  override fun render(context: RenderContext, vararg nodes: LNode): Any {

    var entered = false
    try {
      val includeResource = LValue.asString(nodes[0].render(context))
      val extension = when {
        '.' in includeResource -> ""
        else -> DEFAULT_EXTENSION
      }

      val includesDir = context.includesDir
      val includeResourceFile = includesDir.resolve(includeResource + extension)

      // This will take advantage of caching for better performance, but we should probably
      // also write the template locally
      val template = context.parser.parseFile(includeResourceFile)

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
