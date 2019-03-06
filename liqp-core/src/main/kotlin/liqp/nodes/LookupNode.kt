package liqp.nodes

import liqp.context.LContext
import liqp.exceptions.MissingVariableException
import liqp.lookup.Indexable
import liqp.node.LNode
import liqp.resolve

class LookupNode(val id: String,
                 private val indexes: List<Indexable> = emptyList()) : LNode() {

  private val variableName: String by lazy {
    val variableFullName = StringBuilder(id)
    for (index in indexes) {
      variableFullName.append(index.toString())
    }
    variableFullName.toString()
  }

  override val children: List<LNode> = emptyList()
  override fun render(context: LContext): Any? {
    // Check if there's a [var] lookup, AST: ^(LOOKUP Id["@var"])
    var value: Any? = when {
      id == "_" -> context.inputData
      id.startsWith("@") -> {
        val existing: Any? = context[id.substring(1)]
        val varName = existing.toString()
        context[varName]
      }
      else -> context[id]
    }

    if (value == null && context.parseSettings.isStrictVariables) {
      throw MissingVariableException(variableName)
    }

    try {
      value = indexes.resolve(value, context)
    } catch (e: MissingVariableException) {
      throw MissingVariableException(this.variableName, e.variableName)
    }

    return value
  }

  override fun toString(): String {
    return variableName
  }
}
