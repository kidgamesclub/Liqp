package liqp

import assertk.assert
import assertk.assertions.isEqualTo
import org.junit.Test

class TypeCoersionTest {
  val coersion = TypeCoersion(strictLogic, strictLogic)

  @Test fun testCoerceOrNull() {
    val long:Long? = coersion.coerceOrNull("2654532")
    assert(long).isEqualTo(2654532L)
  }

  @Test fun testCoerceOrNullDouble() {
    val double:Double? = coersion.coerceOrNull("2654532")
    assert(double).isEqualTo(2654532.0)
  }

  @Test fun testCoerceOrNullInt() {
    val value:Int? = coersion.coerceOrNull("2654532")
    assert(value).isEqualTo(2654532)
  }
}
