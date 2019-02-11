package liqp.node

import liqp.EmptyNode

interface LTemplate {
  val rootNode: LNode
  fun render(): String
  fun render(inputData: Any?): String
  fun render(key: String, value: Any?): String
  fun render(pair: Pair<String, Any?>): String
}

object EmptyTemplate : LTemplate {
  override val rootNode: LNode = EmptyNode
  override fun render() = ""
  override fun render(inputData: Any?) = ""
  override fun render(key: String, value: Any?) = ""
  override fun render(pair: Pair<String, Any?>) = ""
}
