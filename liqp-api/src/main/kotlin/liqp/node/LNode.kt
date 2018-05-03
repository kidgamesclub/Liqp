package liqp.node

import liqp.context.LContext
import liqp.exceptions.LiquidRenderingException
import kotlin.reflect.full.isSubclassOf

/**
 * Denotes a node in the AST the parse creates from the
 * input source.
 */
abstract class LNode {

  abstract val children: Iterable<LNode>

  /**
   * Renders this AST.
   *
   * @param context
   * the context (variables) with which this
   * node should be rendered.
   *
   * @return an Object denoting the rendered AST.
   */
  abstract fun render(context: LContext): Any?

  /**
   * Usable in java.
   */
  operator fun <T:Any> invoke(context: LContext, type: Class<T>): T? {
    val rendered = render(context)
    return when (type) {
      String::class.java -> context.asString(rendered) as T?
      Boolean::class.java-> context.isTrue(rendered) as T?
      Int::class.java -> context.asInteger(rendered) as T?
      Long::class.java -> context.asLong(rendered) as T?
      Double::class.java -> context.asDouble(rendered) as T?

      java.lang.String::class.java-> context.asString(rendered) as T?
      java.lang.Boolean::class.java-> context.isTrue(rendered) as T?
      java.lang.Integer::class.java -> context.asInteger(rendered) as T?
      java.lang.Long::class.java -> context.asLong(rendered) as T?
      java.lang.Double::class.java -> context.asDouble(rendered) as T?

      else-> {
        when {
          Number::class.java.isAssignableFrom(type)->context.asNumber(rendered) as T?
          java.lang.Number::class.java.isAssignableFrom(type)->context.asNumber(rendered) as T?
          Iterable::class.java.isAssignableFrom(type)->context.asIterable(rendered) as T?
          java.lang.Iterable::class.java.isAssignableFrom(type)->context.asIterable(rendered) as T?
          else-> rendered as T?
        }
      }
    }
  }

  inline fun <reified T:Any> executeOrNull(context: LContext): T? {
    val rendered = render(context) ?: return null
    return when (T::class) {
      List::class-> context.asIterable(rendered).toList() as T?
      String::class -> context.asString(rendered) as T?
      Boolean::class-> context.isTrue(rendered) as T?
      Int::class -> context.asInteger(rendered) as T?
      Long::class -> context.asLong(rendered) as T?
      Double::class -> context.asDouble(rendered) as T?
      else-> {
        when {
          T::class.isSubclassOf(Number::class)->context.asNumber(rendered) as T?
          T::class.isSubclassOf(Iterable::class)->context.asIterable(rendered) as T?
          else-> rendered as T?
        }
      }
    }
  }

  inline fun <reified T:Any> execute(context: LContext, default:T? = null): T {
    return executeOrNull(context)
        ?: default
        ?: throw LiquidRenderingException("Expected non-null render result")
  }
}
