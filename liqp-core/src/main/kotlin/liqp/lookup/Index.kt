package liqp.lookup

import liqp.context.LContext
import liqp.exceptions.MissingVariableException
import liqp.node.LNode
import liqp.nodes.RenderContext
import liqp.onMissingVariable

/**
 * Resolves the expression in brackets first, then resolves the path by using an integer index, or
 * by using a property name
 */
class Index(private val expression: LNode) : Indexable {

  /**
   * todo: remove this field
   */
  private var key: Any? = null

  override operator fun get(value: Any?, context: LContext): Any? {

    if (value == null) {
      return null
    }

    val propertyName = expression.render(context)
    this.key = propertyName

    if (propertyName is Number) {
      val index = propertyName.toInt()

      return when (value) {
        is Array<*> -> value[index]
        is List<*> -> value[index]
        else -> null
      }
    } else {
      // hashes only work on maps, not on arrays/lists
      if (value is Array<*> || value is Collection<*>) {
        return null
      }

      // This will cache the accessor for the path, so if the same expression resolves to the
      // same value, it will use the same accessor
      val accessor = context.getAccessor(value, propertyName.toString())
      if (accessor.isNullAccessor()) {
        context.onMissingVariable(propertyName.toString())
        return null
      }
      return accessor.invoke(value)
    }
  }

  override fun toString(): String {
    return String.format("[%s]", key)
  }
}
