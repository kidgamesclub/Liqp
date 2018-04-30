package liqp.nodes

import liqp.context.LContext
import liqp.node.LNode
import liqp.tag.LTag
import liqp.tags.CustomTag

class TagNode(tag: LTag, val tokens: List<LNode>, val tagName: String = tag.name) : LNode() {

  constructor(tag: LTag, vararg tokens: LNode, tagName: String = tag.name):
      this(tag, listOf(*tokens), tagName)

  val tag: LTag

  override val children = this.tokens

  init {
    if (tag is CustomTag) {
      this.tag = tag.createTagForNode(*tokens.toTypedArray())
    } else {
      this.tag = tag
    }
  }

  override fun render(context: LContext): Any? {
    return tag.render(context, tokens)
  }
}
