package liqp.node

interface LTemplate {
  val rootNode: LNode
  fun render(): String
  fun render(inputData: Any?): String
  fun render(key:String, value:Any?): String
  fun render(pair: Pair<String, Any?>): String
}
