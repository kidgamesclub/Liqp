package liqp.context

import java.lang.Math.max

/**
 * As part of the liquid "spec", these properties are available inside a for-loop, and so
 * should be accessible for merging into templates.
 */
data class LoopState(val length: Int, val name: String? = null) {
  var index = 0 // Gets incremented right away
  val index0 get() = index - 1
  val rindex get() = max(0, this.length - this.index + 1)
  val rindex0 get() = max(0, this.length - this.index)
  val isFirst get() = index0 == 0
  val isLast get() = index == length

  fun increment() {
    index++
  }
}
