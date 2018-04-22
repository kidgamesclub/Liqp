package liqp.lookup

import liqp.exceptions.MissingVariableException
import liqp.nodes.LNode
import liqp.nodes.RenderContext

/**
 * Resolves the expression in brackets first, then resolves the path by using an integer index, or
 * by using a property name
 */
class Index(private val isStrictVariables:Boolean,
            private val expression: LNode) : Indexable {

  /**
   * todo: remove this field
   */
  private var key: Any? = null

  override fun get(value: Any?, context: RenderContext): Any? {

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
      val accessor = context.accessors.getAccessor(value, propertyName.toString())
      if (isStrictVariables && accessor.isNullAccessor()) {
        throw MissingVariableException(propertyName.toString())
      }
      return accessor.invoke(value)
    }
  }

  override fun toString(): String {
    return String.format("[%s]", key)
  }
}
