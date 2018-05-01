package liqp.node

import liqp.LRenderer

interface LTemplate {
  val rootNode: LNode
  val renderer: LRenderer
  fun render(): String
  fun render(inputData: Any?): String
  fun render(key:String, value:Any?): String
  fun render(pair: Pair<String, Any?>): String
}
