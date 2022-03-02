@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package liqp

import java.lang.Double.*
import java.lang.Math.getExponent
import javax.json.*
import kotlin.reflect.KClass


interface LiquidCoercion {
    fun coerceToNumber(input: Any?): Number?
    fun coerceToIterable(input: Any?): Iterable<Any?>
}


object DefaultLiquidCoercion: LiquidCoercion {
    override fun coerceToNumber(input: Any?): Number? {
        return when (input) {
            null -> null
            is JsonNumber -> input.numberValue()
            is Number -> input
            is Char -> input.code
            is CharSequence -> input.toString().toNumberOrNull()
            is Boolean -> null
            else -> null
        }
    }

    override fun coerceToIterable(input: Any?): Iterable<Any?> {
        return when (input) {
            null -> emptyList<Any>()
            is JsonArray -> input.unboxOrNull() ?: listOf()
            is JsonObject -> input.unbox().map { (k, v) -> k to v }
            is Array<*> -> listOf(*input)
            is Iterable<*> -> input
            is Map<*, *> -> input.asIterable()
            else -> listOf(input)
        }
    }
}

fun JsonValue.unboxAsAny(): Any? = unboxOrNull()

/**
 * Inline function that requires a reified type parameter, but accepts a null return value.  This
 * function will do a recursive unboxing, so a JsonObject or JsonArray will be completely unboxed
 * before being returned.
 */
inline fun <reified R : Any> JsonValue.unboxOrNull(): R? {
    val jsrValue = this

    return when {
        R::class in JsonValue::class -> this as R
        else -> {
            val unboxed: Any? = when (jsrValue) {
                JsonValue.TRUE -> true
                JsonValue.FALSE -> false
                JsonValue.NULL -> null
                is JsonObjectBuilder -> jsrValue.build().unbox()
                is JsonArrayBuilder -> jsrValue.build().unbox()
                is JsonNumber -> if (jsrValue.integral) jsrValue.longValue() else jsrValue.doubleValue()
                is JsonString -> jsrValue.string
                is JsonArray -> jsrValue.unbox()
                is JsonObject -> jsrValue.unbox()
                else -> throw error("Don't know how to unbox ${this::class.qualifiedName} $jsrValue")
            }
            when (unboxed) {
                null -> null
                is R -> unboxed
                else -> error("Unboxed json value was not the expected type. Actual ${unboxed::class.qualifiedName}, " +
                        "Expected: ${R::class.qualifiedName}")
            }
        }
    }
}

fun isFinite(d: Number): Boolean {
    return Math.getExponent(d.toDouble()) <= MAX_EXPONENT
}

const val SIGNIFICAND_BITS = 52

fun Number.isIntegral(): Boolean {

        return isFinite(this)
                && (this == 0.0
                || SIGNIFICAND_BITS - java.lang.Long.numberOfTrailingZeros(getSignificand(this)) <= getExponent(this.toDouble()));
}

// The mask for the sign, according to the {@link
// Double#doubleToRawLongBits(double)} spec.
const val SIGNIFICAND_MASK: Long = 0x000fffffffffffffL

/** The implicit 1 bit that is omitted in significands of normal doubles.  */
const val IMPLICIT_BIT = SIGNIFICAND_MASK + 1

fun getSignificand(d: Number): Long {
    assert(isFinite(d)) { -> "not a normal value" }
    val d = d.toDouble()
    val exponent = getExponent(d)
    var bits = doubleToRawLongBits(d)
    bits = bits and SIGNIFICAND_MASK
    return if (exponent == MIN_EXPONENT - 1) bits shl 1 else bits or IMPLICIT_BIT
}

val JsonNumber.integral: Boolean get() = numberValue()!!.isIntegral()
infix operator fun KClass<*>.contains(other: KClass<*>): Boolean =
    java.isAssignableFrom(other.java)

fun JsonObject.unbox(): Map<String, Any?> = this.properties.map { it.key to it.value.unboxOrNull<Any>() }.toMap()
fun JsonArray.unbox(): List<Any?> = this.map { it.unboxOrNull() }

val JsonObject.properties: Set<Map.Entry<String, JsonValue>> get() = this.entries
