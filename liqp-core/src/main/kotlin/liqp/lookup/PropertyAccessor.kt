package liqp.lookup

/**
 * Simple functional interface that retrieves a property from a known type
 */
interface PropertyAccessor<I, R> : Function1<I, R>

typealias Getter<T> = (T) -> Any?

fun Getter<*>.isNullAccessor(): Boolean {
  return this === PropertyAccessors.NullGetter
}



