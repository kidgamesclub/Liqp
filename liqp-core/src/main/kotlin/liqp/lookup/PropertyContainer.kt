package liqp.lookup

import liqp.PropertyGetter

fun propertyContainer(data:Map<String, Any?>): PropertyGetter {
  return data::get
}

fun composite(vararg delegates: PropertyGetter): PropertyGetter {
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

fun propertyContainer(key:Any?, value:Any?): PropertyGetter {
  return { k: String ->
    if (k == key) value
    else null
  }
}


