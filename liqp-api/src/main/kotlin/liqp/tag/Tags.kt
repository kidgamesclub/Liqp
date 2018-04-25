package liqp.tag

import kotlin.reflect.KProperty

data class Tags(val tags: List<LTag> = listOf()) : List<LTag> by tags {
  constructor(vararg tags: LTag) : this(listOf(*tags))

  private val mapped: Map<String, LTag> = tags.map { it.name to it }.toMap()

  /**
   * Helps with java interop.  Kotlin can just use the + operator to combine tags instances, eg:
   *
   * val tags:Tags = tags1 + tags2
   *
   */
  fun withTag(vararg tags: LTag): Tags {
    return this + listOf(*tags)
  }

  operator fun get(name: String): LTag = mapped[name]!!

  operator fun plus(tags:List<LTag>):Tags = this.copy(tags = this.tags + tags)
  operator fun plus(tag:LTag):Tags = this.copy(tags = this.tags + tag)
  operator fun plus(tags:Tags):Tags = this.copy(tags = this.tags + tags.tags)

  inline operator fun <reified T: LTag> Tags.getValue(thisRef: Tags, property: KProperty<*>): T = this[property.name] as T
}

