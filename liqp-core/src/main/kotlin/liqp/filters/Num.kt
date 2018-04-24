package liqp.filters

import liqp.nodes.RenderContext

class Num : Filter() {

  /*
     * Converts input to a number if possible.
     */
  public override fun apply(context: RenderContext, value: Any?, vararg params: Any): Number {
    return when(value) {
      null-> 0
      is Number-> value
      else-> value.toString().toDoubleOrNull() ?: 0
    }
  }
}
