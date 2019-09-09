package liqp

import lang.string.after
import liqp.context.LContext
import liqp.exceptions.MissingVariable
import liqp.exceptions.MissingVariableException

fun LContext.onMissingVariable(variableName: String, rootName: String? = null): Any? {
  if (renderSettings.isStrictVariables) {
    when (rootName) {
      null -> throw MissingVariableException(variableName = variableName)
      else -> throw MissingVariableException(rootName = rootName, variableName = variableName)
    }
  } else {
    logs += MissingVariable("Missing variable $variableName ${rootName after " at root path: "}")
  }
  return null
}