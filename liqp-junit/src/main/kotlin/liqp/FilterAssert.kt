package liqp

import liqp.filters.LFilter
import liqp.nodes.AtomNode
import liqp.nodes.FilterNode
import liqp.nodes.LNode
import liqp.nodes.OutputNode
import liqp.nodes.RenderContext
import org.assertj.core.api.Assertions.assertThat

data class FilterAssert(val filter: LFilter,
                        val parser: LiquidParser = LiquidParser(),
                        val inputData: Any? = null,
                        val result: Any? = null,
                        val error: Exception? = null,
                        val engine: LiquidRenderer = LiquidRenderer(parser = parser),
                        val context: RenderContext = RenderContext(inputData, parser,
                            engine,
                            engine.accessors,
                            engine.settings)) {

  fun withInputData(inputData: Any?): FilterAssert {
    return this.copy(inputData=inputData)
  }

  fun filtering(value:Any?, vararg params:Any): FilterAssert {
    val filterNode = FilterNode.builder()
        .filter(filter)
        .params(params.map {
          when (it) {
            is LNode -> it
            else -> AtomNode(it)
          }
        }).build()
    val result: Any?
    try {
      val outputNode = OutputNode.builder()
             .value(value)
             .filter(filterNode)
             .build()
      result = outputNode.render(context)
      return this.copy(result=result)
    } catch (e: Exception) {
      return copy(error=e)
    }
  }

  @JvmOverloads
  fun hadError(ofType:Class<*> = Exception::class.java): FilterAssert {
    assertThat(error).describedAs("Should have had an error but didn't")
        .isNotNull()
        .isInstanceOf(ofType)

    return this
  }

  @JvmOverloads
  fun hadNoErrors(): FilterAssert {
    assertThat(error).describedAs("Should not have had an error but found $error")
        .isNull()

    assertThat(result).describedAs("Result was null.  Did you call the filtering() method?")
        .isNotNull()
    return this
  }

  fun isEqualTo(eq:Any?): FilterAssert {
    assertThat(error).describedAs("Should not have thrown an error but threw $error")
    assertThat(result).describedAs("There is no result.  Did you call the filtering() method?").isNotNull()
    assertThat(result).isEqualTo(eq)
    return this
  }

  fun resultContains(contains:String): FilterAssert {
    assertThat(error).describedAs("Should not have thrown an error but threw $error")
    assertThat(result).describedAs("There is no result.  Did you call the filtering() method?").isNotNull()
    assertThat(result.toString()).contains(contains)
    return this
  }


}
