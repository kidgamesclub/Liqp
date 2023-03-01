package liqp.filter

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test

class LFilterTest {
  @Test fun testFilterName() {
    assertAll {
      assertThat(BFilter().name).isEqualTo("b")
      assertThat(BlankFilter().name).isEqualTo("blank")
      assertThat(BlankTagFilter().name).isEqualTo("blank_tag")
    }
  }

  class BFilter: LFilter()
  class BlankFilter: LFilter()
  class BlankTagFilter: LFilter()
}
