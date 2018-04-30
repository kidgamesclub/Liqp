package liqp.filter

import liqp.context.LContext
import liqp.node.LNode

/**
 * Contains filter parameters that can be resolved when needed.  The instance ensures that the
 * params are resolved exactly once, and cached thereafter.
 */
data class ResolvableFilterParams(private val context: LContext,
                                  private val paramNodes: List<LNode?>) : FilterParams() {
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

class ResolvedFilterParams(vararg params: Any?) : FilterParams() {
  override val params = listOf(*params)
}

abstract class FilterParams : Iterable<Any?> {
  abstract val params: List<Any?>
  val size get() = params.size
  inline operator fun <reified T : Any> get(index: Int): T? = params.getOrNull(index) as T?
  inline operator fun <reified T : Any> get(index: Int, default: T): T = params.getOrNull(index) as T?
      ?: default

  override fun iterator(): Iterator<Any?> = params.iterator()

  companion object {
    @JvmStatic
    fun empty() = emptyParams

    @JvmStatic
    fun of(vararg param:Any?) = ResolvedFilterParams(*param)
  }
}

val emptyParams = ResolvedFilterParams()
