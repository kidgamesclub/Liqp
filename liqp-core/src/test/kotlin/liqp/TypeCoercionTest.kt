package liqp

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test

class TypeCoercionTest {
  val coercion = TypeCoercion(strictLogic, strictLogic)

  @Test fun testCoerceOrNull() {
    val long:Long? = coercion.coerceOrNull("2654532")
    assertThat(long).isEqualTo(2654532L)
  }

  @Test fun testCoerceOrNullDouble() {
    val double:Double? = coercion.coerceOrNull("2654532")
    assertThat(double).isEqualTo(2654532.0)
  }

  @Test fun testCoerceOrNullInt() {
    val value:Int? = coercion.coerceOrNull("2654532")
    assertThat(value).isEqualTo(2654532)
  }
}
