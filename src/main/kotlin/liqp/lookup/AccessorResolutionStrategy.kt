package liqp.lookup

import liqp.lookup.PropertyAccessors.Companion.propertyAccessor

/**
 * Represents
 */
interface AccessorResolutionStrategy {
  fun getAccessor(instance: Any, lookup: String): PropertyAccessor?
}

private const val SIZE = "size"
private const val FIRST = "first"
private const val LAST = "last"

/**
 * Handles the synthetic "size" property
 */
class StringSizeAccessor : AccessorResolutionStrategy {
  override fun getAccessor(instance: Any, lookup: String): PropertyAccessor? {
    return when (lookup) {
      SIZE -> {
        when (instance) {
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

  override fun getAccessor(instance: Any, lookup: String): PropertyAccessor? {

    if (lookup != FIRST) {
      return null
    }

    return when (instance) {
      is Collection<*> -> propertyAccessor { i: Collection<Any> -> i.firstOrNull() }
      is Array<*> -> propertyAccessor { i: Array<*> -> i.firstOrNull() }
      else -> null
    }
  }
}

class LastElementAccessor : AccessorResolutionStrategy {
  override fun getAccessor(instance: Any, lookup: String): PropertyAccessor? {

    if (lookup != LAST) {
      return null
    }

    return when (instance) {
      is Collection<*> -> propertyAccessor { i: Collection<Any> -> i.lastOrNull() }
      is Array<*> -> propertyAccessor { i: Array<*> -> i.lastOrNull() }
      else -> null
    }
  }
}

