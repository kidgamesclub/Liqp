package liqp.ext.filters.javatime
import liqp.nodes.RenderContext


import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date

/**
 * Created by ericm on 8/21/16.
 */
object DateUtils {

  fun toZonedDateTime(value: Date?): OffsetDateTime? {
    if (value == null) {
      return null
    } else {
      //I don't think this QUITE works... maybe need some help on it.
      val zone = ZoneId.ofOffset("GMT", ZoneOffset.ofHours(value.timezoneOffset / 60))
      return OffsetDateTime.ofInstant(value.toInstant(), zone)
    }
  }
}
