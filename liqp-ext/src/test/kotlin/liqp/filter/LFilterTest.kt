package liqp.filter

import assertk.assert
import assertk.assertAll
import assertk.assertions.isEqualTo
import org.junit.Test

class LFilterTest {
  @Test fun testFilterName() {
    assertAll {
      assert(BFilter().name).isEqualTo("b")
      assert(BlankFilter().name).isEqualTo("blank")
      assert(BlankTagFilter().name).isEqualTo("blank_tag")
    }
  }

  class BFilter: LFilter()
  class BlankFilter: LFilter()
  class BlankTagFilter: LFilter()
}
