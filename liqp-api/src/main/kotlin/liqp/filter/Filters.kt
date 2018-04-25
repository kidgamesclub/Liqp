package liqp.filter

import liqp.tag.LTag
import liqp.tag.Tags
import kotlin.reflect.KProperty

data class Filters(val filters: List<LFilter> = listOf()) : List<LFilter> by filters {
  constructor(vararg filters: LFilter) : this(listOf(*filters))

  private val mapped: Map<String, LFilter> = filters.map { it.name to it }.toMap()

  /**
   * Helps with java interop.  Kotlin can just use the + operator to combine filters instances, eg:
   *
   * val filters:Filters = filters1 + filters2
   *
   */
  fun withFilter(vararg filters: LFilter): Filters {
    return this.copy(filters = this.filters + listOf(*filters))
  }

  operator fun get(name: String): LFilter? = mapped[name]

  operator fun plus(filters:List<LFilter>): Filters = this.copy(filters = this.filters + filters)
  operator fun plus(filter:LFilter): Filters = this.copy(filters = this.filters + filter)
  operator fun plus(filters: Filters): Filters = this.copy(filters = this.filters + filters.filters)

  inline operator fun <reified T: LFilter> Filters.getValue(thisRef: Filters, property: KProperty<*>): T = this[property.name] as T
}

