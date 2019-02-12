package liqp.nodes

import lang.suppress.Suppressions.UNCHECKED_CAST
import liqp.HasProperties

class RecursivePropertyContainer : HasProperties {
  @Suppress(UNCHECKED_CAST)
  override fun <T : Any> getValue(propName: String): T? {
    return if (propName.equals("title", ignoreCase = true)) {
      "Lord of the Grapes" as T?
    } else {
      RecursivePropertyContainer() as T?
    }
  }
}
