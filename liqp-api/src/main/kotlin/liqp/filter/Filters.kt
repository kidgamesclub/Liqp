package liqp.filter

import lang.exception.illegalArgument
import lang.suppress.Suppressions.UNCHECKED_CAST
import kotlin.reflect.KProperty

data class Filters(val filters: List<LFilter> = listOf()) : List<LFilter> by filters {
  constructor(vararg filters: LFilter) : this(listOf(*filters))

  private val mapped: Map<String, LFilter> = filters
      .flatMap { filter -> filter.names.map { it to filter } }
      .toMap()

  /**
   * Helps with java interop.  Kotlin can just use the + operator to combine filters instances, eg:
   *
   * val filters:Filters = filters1 + filters2
   *
   */
  fun withFilter(vararg filters: LFilter): Filters {
    return this.copy(filters = this.filters + listOf(*filters))
  }

  operator fun get(name: String): LFilter = mapped[name]
      ?: illegalArgument("No filter found with name $name")

  fun getOrNull(name: String): LFilter? = mapped[name]

  @Suppress(UNCHECKED_CAST)
  fun <F : LFilter> cast(name: String): F? = mapped[name] as? F

  fun <F : LFilter> getFilter(name: String): F = cast(name)
      ?: illegalArgument("Missing filter named $name")

  operator fun plus(filters: List<LFilter>): Filters = this.copy(filters = this.filters + filters)
  operator fun plus(filter: LFilter): Filters = this.copy(filters = this.filters + filter)
  operator fun plus(filters: Filters): Filters = this.copy(filters = this.filters + filters.filters)

  @Suppress("UNCHECKED_CAST")
  operator fun <F : LFilter> getValue(nothing: Nothing?, property: KProperty<*>): F {
    return this[property.name] as F
  }
}


