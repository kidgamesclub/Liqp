package liqp.params

import liqp.context.LContext
import liqp.node.LNode
import liqp.nodes.AttributeNode

/**
 * Contains filter parameters that can be resolved when needed.  The instance ensures that the
 * params are resolved exactly once, and cached thereafter.
 */
data class ResolvableNamedParams(private val context: LContext,
                                 private val paramNodes: List<LNode>,
                                 private val defaults: Map<String, Any> = mapOf()) : NamedParams() {
  override val params: Map<String, Any?> by lazy {
    paramNodes
        .map { it as AttributeNode }
        .map {
          val key:String = it.key.execute(context)
          val value:Any? = it.value.executeOrNull(context)
          key to value
        }
        .toMap()
  }
}

class ResolvedNamedParams(override val params:Map<String, Any?>) : NamedParams() {
}

abstract class NamedParams : Map<String, Any?> {
  protected abstract val params: Map<String, Any?>

  companion object {
    @JvmStatic
    fun empty() = emptyNamedParams

    @JvmStatic
    fun of(vararg pairs: Pair<String, Any?>) = ResolvedNamedParams(mapOf(*pairs))
  }

  override val size get() = params.size
  override val entries: Set<Map.Entry<String, Any?>> get() = this.params.entries
  override val keys: Set<String> get() = this.params.keys
  override val values: Collection<Any?> get() = this.params.values
  override fun containsKey(key: String): Boolean = params.containsKey(key)
  override fun containsValue(value: Any?): Boolean = params.containsKey(value)
  override fun get(key: String): Any? = params[key]
  override fun isEmpty(): Boolean = params.isEmpty()

  operator fun <T : Any> get(key: String, default: T): T {
    return params.getOrDefault(key, defaultValue = default) as T? ?: default
  }
}

val emptyNamedParams = ResolvedNamedParams(mapOf())
