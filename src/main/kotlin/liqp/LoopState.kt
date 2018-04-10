package liqp

import java.lang.Math.max

class LoopState(val length: Int, val name: String? = null) {

  var index: Int = 0 // Gets incremented right away
  val index0: Int
    get() = index - 1
  val rindex: Int
    get() = max(0, this.length - this.index + 1)
  val rindex0: Int
    get() = max(0, this.length - this.index)
  val isFirst: Boolean
    get() = index0 == 0
  val isLast: Boolean
    get() = index == length

  fun increment() {
    index++
  }
}
