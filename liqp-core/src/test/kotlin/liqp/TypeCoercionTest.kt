package liqp

import assertk.assert
import assertk.assertions.isEqualTo
import org.junit.Test

class TypeCoercionTest {
  val coercion = TypeCoercion(strictLogic, strictLogic)

  @Test fun testCoerceOrNull() {
    val long:Long? = coercion.coerceOrNull("2654532")
    assert(long).isEqualTo(2654532L)
  }

  @Test fun testCoerceOrNullDouble() {
    val double:Double? = coercion.coerceOrNull("2654532")
    assert(double).isEqualTo(2654532.0)
  }

  @Test fun testCoerceOrNullInt() {
    val value:Int? = coercion.coerceOrNull("2654532")
    assert(value).isEqualTo(2654532)
  }
}
