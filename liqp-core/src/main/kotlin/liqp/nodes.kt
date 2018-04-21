package liqp

import liqp.nodes.LNode
import liqp.nodes.TagNode
import liqp.tags.Tag

inline fun <reified T: Tag> LNode.isTag():Boolean {
  return this is TagNode && this.tag is T
}

inline fun <reified T: Tag> LNode.asTag(type:Class<T> = T::class.java):T? {
  return when {
    this.isTag<T>()-> (this as TagNode).tag as T
    else->null
  }
}
