package liqp.lookup

import lang.json.JsrObject
import lang.json.unboxAsAny
import lang.suppress.Suppressions.UNCHECKED_CAST
import liqp.Getter
import liqp.PropertyGetter

object MapPropertyGetter : PropertyGetter {
  val properties = mutableMapOf<String, MapGetter>()
  override fun getterOrNull(property: String): Getter<Any> = properties.getOrPut(property) {
    MapGetter(property)
  }
}

object JsonPropertyGetter : PropertyGetter {
  val properties = mutableMapOf<String, JsonGetter>()
  override fun getterOrNull(property: String): Getter<Any> = properties.getOrPut(property) {
    JsonGetter(property)
  }
}

data class JsonGetter(val property: String) : Getter<Any> {
  override fun invoke(p1: Any): Any? {
    return (p1 as JsrObject)[property]?.unboxAsAny()
  }
}

data class MapInstanceProperties(val data: Map<String, Any?>) : PropertyGetter {
  val properties = mutableMapOf<String, Getter<Any>>()
  override fun getterOrNull(property: String): Getter<Any> = properties.getOrPut(property) {
    MapInstanceGetter(data, property)
  }
}

data class MapGetter(val property: String) : Getter<Any> {
  override fun invoke(data: Any): Any? = (data as Map<*, *>)[property]
}

data class MapInstanceGetter(val map: Map<String, Any?>, val property: String) : Getter<Any> {
  override fun invoke(data: Any): Any? = map[property]
}

data class CompositeGetter(val delegates: List<PropertyGetter>) : PropertyGetter {
  override fun getterOrNull(property: String): Getter<Any>? = delegates.asSequence()
      .mapNotNull { it.getterOrNull(property) }
      .firstOrNull()
}

fun propertyContainer(key: String, value: Any?): PropertyGetter {
  return object : PropertyGetter {
    override fun getterOrNull(property: String): Getter<Any>? {
      return when (property) {
        key -> StaticGetter(value)
        else -> null
      }
    }
  }
}

data class StaticGetter<T>(val data: T) : Getter<T> {
  override fun invoke(instance: T): Any? = data
}

class NullGetter<T> : Getter<T> {
  override fun invoke(instance: T): Any? = null
}


