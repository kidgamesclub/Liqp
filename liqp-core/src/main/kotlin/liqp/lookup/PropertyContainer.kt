package liqp.lookup


/**
 * Simple functional interface for retrieving values from known object.
 */
typealias PropertyContainer = (propertyName: String)-> Any?

fun propertyContainer(data:Map<String, Any>): PropertyContainer {
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

@FunctionalInterface
interface HasProperties {
  fun getProperty(propName:String):Any?
}

fun propertyContainer(key:Any?, value:Any?): PropertyContainer {
  return { k: String ->
    if (k == key) value
    else null
  }
}
