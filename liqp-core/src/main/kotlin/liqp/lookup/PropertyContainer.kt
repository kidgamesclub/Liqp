package liqp.lookup

typealias PropertyContainer = (String)-> Any?

fun propertyContainer(data:Map<String, Any?>): PropertyContainer {
  return data::get
}

fun composite(vararg delegates: PropertyContainer): PropertyContainer {
  return delegates@{key:String->
    for (delegate in delegates) {
      val value = delegate.invoke(key)
      if (value != null) {
        return@delegates value
      }
    }
    return@delegates null
  }
}

/**
 * For java compatibility
 */
interface HasProperties {
  fun <T> get(propName:String):T?
}

fun propertyContainer(key:Any?, value:Any?): PropertyContainer {
  return { k: String ->
    if (k == key) value
    else null
  }
}


