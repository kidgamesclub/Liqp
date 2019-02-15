package liqp

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import liqp.ComparisonResult.EQUAL
import liqp.ComparisonResult.GREATER
import liqp.ComparisonResult.LESS
import liqp.ComparisonResult.NULL
import liqp.LogicResult.*
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class StrictLogicTestIsTrue(val input: Any?, val expected: Boolean) {

  @Test
  fun testIsTrue() {
    val found = strictLogic.isTrue(input)
    Assertions.assertThat(found).isEqualTo(expected)
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
    val found = strictLogic.isFalse(input)
    Assertions.assertThat(found).isEqualTo(expected)
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
    assert(strict.isEmpty("foo")).isEqualTo(false)
  }

  @Test fun testIsEmpty_Array() {
    assert(strict.isEmpty(arrayOf("f", "o", "o"))).isEqualTo(false)
  }

  @Test fun testIsEmpty_List() {
    assert(strict.isEmpty(listOf("f", "o", "o"))).isEqualTo(false)
  }

  @Test fun testIsEmpty_Null() {
    assert(strict.isEmpty(null)).isEqualTo(true)
  }

  @Test fun testIsEmpty_EmptyString() {
    assert(strict.isEmpty("")).isEqualTo(true)
  }

  @Test fun testIsEmpty_EmptyArray() {
    assert(strict.isEmpty(arrayOf<Any>())).isEqualTo(true)
  }

  @Test fun testIsEmpty_EmptyList() {
    assert(strict.isEmpty(listOf<Any>())).isEqualTo(true)
  }

  @Test fun testIsEmpty_EmptyMap() {
    assert(strict.isEmpty(mapOf<Any, Any>())).isEqualTo(true)
  }

  @Test fun testIsEmpty_Number() {
    assert(strict.isEmpty(1234.12)).isEqualTo(false)
  }

  @Test fun testIsEmpty_True() {
    assert(strict.isEmpty(true)).isEqualTo(false)
  }

  @Test fun testIsEmpty_False() {
    assert(strict.isEmpty(false)).isEqualTo(true)
  }

  @Test fun testIsEmpty_Int() {
    assert(strict.isEmpty(123123123L)).isEqualTo(false)
  }

  @Test fun testIsEmpty_Map() {
    assert(strict.isEmpty(mapOf("f" to "f", "o" to "o", "O" to "O"))).isEqualTo(false)
  }

  @Test fun testCompareTo_Number() {
    assert(strict.compareTo(10, 20)).isEqualTo(LESS)
  }

  @Test fun testCompareTo_NumberEqualDiffType() {
    assert(strict.compareTo(10L, 10.0)).isEqualTo(EQUAL)
  }

  @Test fun testCompareTo_NumberEqualSameType() {
    assert(strict.compareTo(10L, 10L)).isEqualTo(EQUAL)
  }

  @Test fun testCompareTo_NumberToNull() {
    assert(strict.compareTo(10, null)).isEqualTo(NULL)
  }

  @Test fun testCompareTo_NullToNumber() {
    assert(strict.compareTo(null, 10)).isEqualTo(NULL)
  }

  @Test fun testCompareTo_List() {
    assert(strict.compareTo(listOf("a", "b"), listOf("b", "c"))).isEqualTo(GREATER)
  }

  @Test fun testCompareTo_StringEquals() {
    assert(strict.compareTo("abc", "abc")).isEqualTo(EQUAL)
  }

  @Test fun testCompareTo_ListReversed() {
    assert(strict.compareTo(listOf("b", "c"), listOf("a", "b"))).isEqualTo(GREATER)
  }

  @Test fun testCompareTo_Equal() {
    assert(strict.compareTo(listOf("a", "b"), listOf("a", "b"))).isEqualTo(EQUAL)
  }

  @Test fun testEquals_Noop() {
    assert(strict.areEqual(null, 34.2)).isEqualTo(FALSE)
  }

  @Test fun testEquals_Equal() {
    assert(strict.areEqual(342.0, 342L)).isEqualTo(TRUE)
  }

  @Test fun testEquals_NotEqual() {
    assert(strict.areEqual(342.2, 342L)).isEqualTo(FALSE)
  }
  @Test fun testEquals_NotEqual_DiffTypes() {
    assert(strict.areEqual(arrayOf("afds"), 342L)).isEqualTo(NOOP)
  }

  @Test fun testIsFalse_Null() {
    assert(strict.isFalse(null)).isEqualTo(true)
  }

  @Test fun testIsFalse_String() {
    assert(strict.isFalse("false")).isEqualTo(false)
  }

  @Test fun testIsFalse_Int() {
    assert(strict isFalse 0).isFalse()
  }

  @Test fun testIsFalse_ArrayOfFalse() {
    assert(strict isFalse arrayOf(false)).isFalse()
  }

  @Test fun addIterable_SingleItem() {
    assert(strict.add(listOf("a", "b"), "c")).isEqualTo(listOf("a", "b", "c"))
  }

  @Test fun addIterable_MultipleItem() {
    assert(strict.add(listOf("a"), listOf("b", "c"))).isEqualTo(listOf("a", "b", "c"))
  }

}
