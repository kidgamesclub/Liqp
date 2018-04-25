package liqp.config

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KFunction1
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

inline fun <reified C, reified T> delegate(getter: KProperty0<T>,
                                           setter: KFunction1<T, MutableRenderSettings>): ReadWriteProperty<C, T> {
  return object : ReadWriteProperty<C, T> {
    override fun getValue(thisRef: C, property: KProperty<*>): T {
      return getter.get()
    }

    override fun setValue(thisRef: C, property: KProperty<*>, value: T) {
      setter(value)
    }
  }
}
