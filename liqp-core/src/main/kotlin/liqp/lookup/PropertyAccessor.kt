package liqp.lookup

import liqp.lookup.PropertyAccessors.Companion.nullAccessor

/**
 * Simple functional interface that allows access to child properties.
 */
interface PropertyAccessor {
  fun get(instance: Any): Any?
}

typealias Getter<T> = (T)-> Any?

fun PropertyAccessor.isNullAccessor():Boolean {
  return this === nullAccessor
}



