package liqp.tags

import java.io.File
import liqp.LValue
import liqp.Template
import liqp.nodes.LNode
import liqp.nodes.RenderContext

class Include : Tag() {

  override fun render(context: RenderContext,
                      vararg nodes: LNode): Any {

    var entered = false
    try {
      val includeResource = LValue.asString(nodes[0].render(context))
      var extension = DEFAULT_EXTENSION
      if (includeResource.indexOf('.') > 0) {
        extension = ""
      }
      val includeResourceFile: File
      val includesDirectory = context.get<Any>(INCLUDES_DIRECTORY_KEY) as File?
      if (includesDirectory != null) {
        includeResourceFile = File(includesDirectory, includeResource + extension)
      } else {
        includeResourceFile = File(context.templateFactory.flavor.snippetsFolderName, includeResource + extension)
      }
      val template = context.templateFactory.parseFile(includeResourceFile)

      // check if there's a optional "with expression"
      if (nodes.size > 1) {
        val value = nodes[1].render(context)
        context[includeResource] = value
      }

      context.addFrame()
      entered = true

      return context.render(template)
    } catch (e: Exception) {
      return ""
    } finally {
      if (entered) {
        context.popFrame()
      }
    }
  }

  companion object {

    val INCLUDES_DIRECTORY_KEY = "liqp@includes_directory"
    var DEFAULT_EXTENSION = ".liquid"
  }
}
