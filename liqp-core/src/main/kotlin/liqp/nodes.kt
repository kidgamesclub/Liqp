package liqp

import liqp.node.LNode
import liqp.nodes.TagNode
import liqp.tag.LTag

inline fun <reified T: LTag> LNode.isTag():Boolean {
  return this is TagNode && this.tag is T
}

inline fun <reified T: LTag> LNode.asTag(type:Class<T> = T::class.java):T? {
  return when {
    this.isTag<T>()-> (this as TagNode).tag as T
    else->null
  }
}
