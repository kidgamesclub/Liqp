package liqp.lookup

import liqp.context.LContext

/**
 * Locates a {@link PropertyAccessor} given a node instance and a property name.  For nested
 * properties, this method would be called for each nested property that was accessed.
 *
 * The {@link PropertyAccessor}
 */
interface AccessorResolutionStrategy {
  fun getAccessor(lcontext: LContext, sample: Any?, property: String): Getter<Any>?
}

private const val SIZE = "size"
private const val FIRST = "first"
private const val LAST = "last"

/**
 * Handles the synthetic "size" property
 */
class StringSizeAccessor : AccessorResolutionStrategy {
  override fun getAccessor(lcontext: LContext, sample: Any?, property: String): Getter<Any>? {
    return when (property) {
      SIZE -> {
        when (sample) {
          is Collection<*> -> { i -> (i as Collection<*>).size }
          is Map<*, *> -> { i -> (i as Map<*, *>)[SIZE] ?: i.size }
          is Array<*> -> { i -> (i as Array<*>).size }
          is CharSequence -> { i -> (i as CharSequence).length }
          else -> null as Getter<Any>?
        }
      }
      else -> null
    }
  }
}

/**
 * Handles the synthetic "first" property
 */
class FirstElementAccessor : AccessorResolutionStrategy {
  override fun getAccessor(lcontext: LContext, sample: Any?, property: String): Getter<Any>? {
    if (property != FIRST) {
      return null
    }

    return when (sample) {
      is Collection<*> -> { i -> (i as Array<*>).firstOrNull() }
      is Iterable<*> -> { i -> (i as Iterable<*>).firstOrNull() }
      is Array<*> -> { i -> (i as Array<*>).firstOrNull() }
      else -> null as Getter<Any>?
    }
  }
}

/**
 * Handles the synthetic "last" property
 */
class LastElementAccessor : AccessorResolutionStrategy {
  override fun getAccessor(lcontext: LContext, sample: Any?, property: String): Getter<Any>? {
    if (property != LAST) {
      return null
    }

    return when (sample) {
      is Collection<*> -> { i -> (i as Array<*>).lastOrNull() }
      is Iterable<*> -> { i -> (i as Iterable<*>).lastOrNull() }
      is Array<*> -> { i -> (i as Array<*>).lastOrNull() }
      else -> null as Getter<Any>?
    }
  }
}

