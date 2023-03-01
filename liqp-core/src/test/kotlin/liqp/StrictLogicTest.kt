package liqp

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import liqp.ComparisonResult.*
import liqp.LogicResult.*
import liqp.LogicResult.NOOP
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class StrictLogicTestIsTrue(val input: Any?, val expected: Boolean) {

  @Test
  fun testIsTrue() {
    val found = StrictLogic().isTrue(input)
    assertThat(found).isEqualTo(expected)
  }

  companion object {
    @JvmStatic @Parameterized.Parameters(name = "{1}")
    fun isTrueParams(): Array<out Any> {
      return arrayOf(
          arrayOf(true, true),
          arrayOf(null, false),
          arrayOf(1, true)
      )
    }
  }
}

@RunWith(Parameterized::class)
class StrictLogicTestIsFalse(val input: Any?, val expected: Boolean) {
  @Test
  fun testIsFalse() {
    val found = StrictLogic().isFalse(input)
    assertThat(found).isEqualTo(expected)
  }

  companion object {
    @JvmStatic @Parameterized.Parameters
    fun isFalseParams(): Array<out Any> {
      return arrayOf(
          arrayOf(true, false),
          arrayOf(false, true),
          arrayOf(null, true),
          arrayOf(1, false)
      )
    }
  }
}

class StrictLogicTest {
  val strict = StrictLogic()
  @Test fun testIsEmpty_String() {
    assertThat(strict.isEmpty("foo")).isEqualTo(false)
  }

  @Test fun testIsEmpty_Array() {
    assertThat(strict.isEmpty(arrayOf("f", "o", "o"))).isEqualTo(false)
  }

  @Test fun testIsEmpty_List() {
    assertThat(strict.isEmpty(listOf("f", "o", "o"))).isEqualTo(false)
  }

  @Test fun testIsEmpty_Null() {
    assertThat(strict.isEmpty(null)).isEqualTo(true)
  }

  @Test fun testIsEmpty_EmptyString() {
    assertThat(strict.isEmpty("")).isEqualTo(true)
  }

  @Test fun testIsEmpty_EmptyArray() {
    assertThat(strict.isEmpty(arrayOf<Any>())).isEqualTo(true)
  }

  @Test fun testIsEmpty_EmptyList() {
    assertThat(strict.isEmpty(listOf<Any>())).isEqualTo(true)
  }

  @Test fun testIsEmpty_EmptyMap() {
    assertThat(strict.isEmpty(mapOf<Any, Any>())).isEqualTo(true)
  }

  @Test fun testIsEmpty_Number() {
    assertThat(strict.isEmpty(1234.12)).isEqualTo(false)
  }

  @Test fun testIsEmpty_True() {
    assertThat(strict.isEmpty(true)).isEqualTo(false)
  }

  @Test fun testIsEmpty_False() {
    assertThat(strict.isEmpty(false)).isEqualTo(true)
  }

  @Test fun testIsEmpty_Int() {
    assertThat(strict.isEmpty(123123123L)).isEqualTo(false)
  }

  @Test fun testIsEmpty_Map() {
    assertThat(strict.isEmpty(mapOf("f" to "f", "o" to "o", "O" to "O"))).isEqualTo(false)
  }

  @Test fun testCompareTo_Number() {
    assertThat(strict.compareTo(10, 20)).isEqualTo(LESS)
  }

  @Test fun testCompareTo_NumberEqualDiffType() {
    assertThat(strict.compareTo(10L, 10.0)).isEqualTo(EQUAL)
  }

  @Test fun testCompareTo_NumberEqualSameType() {
    assertThat(strict.compareTo(10L, 10L)).isEqualTo(EQUAL)
  }

  @Test fun testCompareTo_NumberToNull() {
    assertThat(strict.compareTo(10, null)).isEqualTo(NULL)
  }

  @Test fun testCompareTo_NullToNumber() {
    assertThat(strict.compareTo(null, 10)).isEqualTo(NULL)
  }

  @Test fun testCompareTo_List() {
    assertThat(strict.compareTo(listOf("a", "b"), listOf("b", "c"))).isEqualTo(GREATER)
  }

  @Test fun testCompareTo_StringEquals() {
    assertThat(strict.compareTo("abc", "abc")).isEqualTo(EQUAL)
  }

  @Test fun testCompareTo_ListReversed() {
    assertThat(strict.compareTo(listOf("b", "c"), listOf("a", "b"))).isEqualTo(GREATER)
  }

  @Test fun testCompareTo_Equal() {
    assertThat(strict.compareTo(listOf("a", "b"), listOf("a", "b"))).isEqualTo(EQUAL)
  }

  @Test fun testEquals_Noop() {
    assertThat(strict.areEqual(null, 34.2)).isEqualTo(FALSE)
  }

  @Test fun testEquals_Equal() {
    assertThat(strict.areEqual(342.0, 342L)).isEqualTo(TRUE)
  }

  @Test fun testEquals_NotEqual() {
    assertThat(strict.areEqual(342.2, 342L)).isEqualTo(FALSE)
  }
  @Test fun testEquals_NotEqual_DiffTypes() {
    assertThat(strict.areEqual(arrayOf("afds"), 342L)).isEqualTo(NOOP)
  }

  @Test fun testIsFalse_Null() {
    assertThat(strict.isFalse(null)).isEqualTo(true)
  }

  @Test fun testIsFalse_String() {
    assertThat(strict.isFalse("false")).isEqualTo(false)
  }

  @Test fun testIsFalse_Int() {
    assertThat(strict isFalse 0).isFalse()
  }

  @Test fun testIsFalse_ArrayOfFalse() {
    assertThat(strict isFalse arrayOf(false)).isFalse()
  }

  @Test fun addIterable_SingleItem() {
    assertThat(strict.add(listOf("a", "b"), "c")).isEqualTo(listOf("a", "b", "c"))
  }

  @Test fun addIterable_MultipleItem() {
    assertThat(strict.add(listOf("a"), listOf("b", "c"))).isEqualTo(listOf("a", "b", "c"))
  }

}
