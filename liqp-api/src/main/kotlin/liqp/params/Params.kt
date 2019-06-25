package liqp.params

import liqp.TypeCoercion
import liqp.context.LContext
import liqp.node.LNode

/**
 * Contains filter parameters that can be resolved when needed.  The instance ensures that the
 * params are resolved exactly once, and cached thereafter.
 */
data class ResolvableFilterParams(private val context: LContext,
                                  private val paramNodes: List<LNode?>,
                                  override val coercion: TypeCoercion = context.coercion) : FilterParams() {
  override val params: List<Any?> by lazy {
    paramNodes
        .map { pnode ->
          when (pnode) {
            null -> null
            else -> pnode.render(context)
          }
        }
  }
}

data class ResolvedFilterParams(override val coercion: TypeCoercion?,
                                override val params: List<Any?>) : FilterParams() {
  constructor(vararg params: Any?) : this(null, listOf(*params))
}

abstract class FilterParams : Iterable<Any?> {
  abstract val coercion: TypeCoercion?
  abstract val params: List<Any?>

  val size get() = params.size
  val isEmpty: Boolean by lazy { params.isEmpty() }

  inline operator fun <reified T : Any> get(index: Int): T? {
    val value = params.getOrNull(index)
    return when {
      value is T? -> value
      coercion != null -> coercion!!.coerceOrNull(value, T::class.java)
      else -> throw ClassCastException("Unable to convert $value to ${T::class.java}")
    }
  }

  inline operator fun <reified T : Any> get(index: Int, default: T): T {
    val param = params.getOrNull(index)
    return when {
      param == null-> default
      param is T -> param
      coercion != null -> coercion!!.coerceOrNull(param) ?: throw java.lang.ClassCastException("Unable to coerce $param to ${T::class}")
      else -> throw ClassCastException("Unable to convert $param to ${T::class.java}")
    }
  }

  override fun iterator(): Iterator<Any?> =
      params.iterator()

  companion object {
    @JvmStatic
    fun empty() = emptyParams

    @JvmStatic
    fun of(vararg param: Any?) = ResolvedFilterParams(*param)
  }
}

val emptyParams = ResolvedFilterParams()
