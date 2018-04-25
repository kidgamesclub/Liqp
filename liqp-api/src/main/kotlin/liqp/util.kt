package liqp

import com.google.common.base.CaseFormat
import liqp.filter.LFilter
import liqp.tag.LTag

val SNAKE_CONVERTER = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE)

fun LTag.toSnakeCase():String {
  return SNAKE_CONVERTER.convert(this::class.java.simpleName.replace("Tag$", ""))!!
}

fun LFilter.toSnakeCase():String {
  return SNAKE_CONVERTER.convert(this::class.java.simpleName.replace("Tag$", ""))!!
}
