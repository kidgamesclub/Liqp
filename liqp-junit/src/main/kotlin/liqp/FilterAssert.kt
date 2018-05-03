package liqp

import assertk.Assert
import assertk.assert
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import liqp.context.LContext
import liqp.filter.LFilter
import liqp.node.LNode
import liqp.nodes.AtomNode
import liqp.nodes.FilterNode
import liqp.nodes.OutputNode
import liqp.nodes.RenderContext
import org.assertj.core.api.Assertions.assertThat

data class FilterAssert(val filter: LFilter,
                        val parser: LiquidParser = LiquidParser(),
                        val inputData: Any? = null,
                        val result: Any? = null,
                        val error: Exception? = null,
                        val engine: LiquidRenderer = LiquidRenderer(parser = parser),
                        val context: LContext = RenderContext(inputData, parser,
                            engine.logic,
                            engine.parser,
                            engine,
                            engine.accessors,
                            engine.settings)) {


  fun resultAsString(): Assert<String> {
    return result()
  }

  fun <T:Any> result(type: Class<T>): Assert<T> {
    return liqp.assertThat(result as T)
  }


  inline fun <reified T:Any> result(): Assert<T> {
    return (result as T?).asserting()
  }

  fun results(): Assert<List<*>> {
    return result()
  }

  inline fun <reified T:Any> nullableResult(): Assert<T?> {
    return (inputData as T?).assertNullable()
  }

  fun filtering(inputData: Any?, vararg params: Any): FilterAssert {
    val filterNode = FilterNode(filter, params = params.map {
      when (it) {
        is LNode -> it
        else -> AtomNode(it)
      }
    })
    val result: Any?
    return try {
      val outputNode = OutputNode(expr = inputData, filters = listOf(filterNode))
      result = outputNode.render(context)
      this.copy(result = result, inputData = inputData)
    } catch (e: Exception) {
      copy(error = e)
    }
  }

  fun withParams(vararg params: Any): FilterAssert {
    val filterNode = FilterNode(filter, params = params.map {
      when (it) {
        is LNode -> it
        else -> AtomNode(it)
      }
    })
    val result: Any?
    return try {
      val outputNode = OutputNode(expr = inputData, filters = listOf(filterNode))
      result = outputNode.render(context)
      this.copy(result = result,
          inputData = inputData,
          error = null,
          context = context.reset())
    } catch (e: Exception) {
      copy(error = e)
    }
  }

  @JvmOverloads
  fun hadError(ofType: Class<*> = Exception::class.java): FilterAssert {
    assertk.assert(error, "thrown error").isNotNull {
      it.isInstanceOf(ofType)
    }

    return this
  }

  fun hadNoErrors(): FilterAssert {
    assert(error, "Unexpected error").isNull()
    return this
  }

  fun isEqualTo(eq: Any?): FilterAssert {
    assertThat(error).describedAs("Should not have thrown an error but threw $error")
    assertThat(result).describedAs("There is no result.  Did you call the filtering() method?").isNotNull()
    assertThat(result).isEqualTo(eq)
    return this
  }

  fun resultContains(contains: String): FilterAssert {
    assertThat(error).describedAs("Should not have thrown an error but threw $error")
    assertThat(result).describedAs("There is no result.  Did you call the filtering() method?").isNotNull()
    assertThat(result.toString()).contains(contains)
    return this
  }
}
