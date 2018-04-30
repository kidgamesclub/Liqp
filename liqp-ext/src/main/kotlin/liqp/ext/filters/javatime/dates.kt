package liqp.ext.filters.javatime

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

fun Date.toZonedDateTime(): OffsetDateTime {
  //I don't think this QUITE works... maybe need some help on it.
  val zone = ZoneId.ofOffset("GMT", ZoneOffset.ofHours(this.timezoneOffset / 60))
  return OffsetDateTime.ofInstant(this.toInstant(), zone)
}

fun LocalDate.toLocalDateTime(): LocalDateTime {
  return LocalDateTime.from(this)
}
