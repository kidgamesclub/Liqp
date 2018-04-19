package liqp.lookup

import liqp.lookup.PropertyAccessors.Companion.propertyAccessor

/**
 * Locates a {@link PropertyAccessor} given a model instance and a property name.  For nested
 * properties, this method would be called for each nested property that was accessed.
 *
 * The {@link PropertyAccessor}
 */
interface AccessorResolutionStrategy {
  fun getAccessor(model: Any, property: String): PropertyAccessor?
}

private const val SIZE = "size"
private const val FIRST = "first"
private const val LAST = "last"

/**
 * Handles the synthetic "size" property
 */
class StringSizeAccessor : AccessorResolutionStrategy {
  override fun getAccessor(model: Any, property: String): PropertyAccessor? {
    return when (property) {
      SIZE -> {
        when (model) {
          is Collection<*> -> propertyAccessor { i: Collection<Any> -> i.size }
          is Map<*, *> -> propertyAccessor { i: Map<*, *> -> i[SIZE] ?: i.size }
          is Array<*> -> propertyAccessor { i: Array<*> -> i.size }
          is CharSequence -> propertyAccessor { i: CharSequence -> i.length }
          else -> null
        }
      }
      else -> null
    }
  }
}

class FirstElementAccessor : AccessorResolutionStrategy {

  override fun getAccessor(model: Any, property: String): PropertyAccessor? {

    if (property != FIRST) {
      return null
    }

    return when (model) {
      is Collection<*> -> propertyAccessor { i: Collection<Any> -> i.firstOrNull() }
      is Array<*> -> propertyAccessor { i: Array<*> -> i.firstOrNull() }
      else -> null
    }
  }
}

class LastElementAccessor : AccessorResolutionStrategy {
  override fun getAccessor(model: Any, property: String): PropertyAccessor? {

    if (property != LAST) {
      return null
    }

    return when (model) {
      is Collection<*> -> propertyAccessor { i: Collection<Any> -> i.lastOrNull() }
      is Array<*> -> propertyAccessor { i: Array<*> -> i.lastOrNull() }
      else -> null
    }
  }
}

