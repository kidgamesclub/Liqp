package liqp.traverse

import liqp.LiquidParser
import liqp.isTag
import liqp.nodes.LookupNode
import liqp.tags.If
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class FilteredLNodeVisitorTest {
  @Test
  fun testNodeVisitor_Iterator() {
    val parsedTemplate = LiquidParser.newBuilder()
        .toParser()
        .parse("Hey, welcome to {{ root.branch.leaf }} of our " +
            "{%if child.parent.grandparent.name == 'Bob'%}" +
            "  {% for person in parents.children %}" +
            "    {{ person.name | strip:'e' }} " +
            "  {% endfor %} " +
            "{% endif %}")

    val lookupNames = parsedTemplate
        .iterator<LookupNode>()
        .map { it.toString() }
        .toSet()

    assertThat(lookupNames).containsExactlyInAnyOrder("root.branch.leaf",
        "child.parent.grandparent.name",
        "parents.children",
        "person.name")
  }

  @Test
  fun testNodeVisitor_Parents() {
    val parsedTemplate = LiquidParser.newBuilder()
        .toParser()
        .parse("Hey, welcome to {{ root.branch.leaf }} of our " +
            "{%if child.parent.grandparent.name == 'Bob'%}" +
            "  {% for person in parents.children %}" +
            "    {{ person.name | strip:'e' }} " +
            "  {% endfor %} " +
            "{% endif %}" +
            "{{ first.second.third }}")

    val namesInsideIfs = mutableSetOf<String>()
    parsedTemplate.walkNodeTree { lookupNode: LookupNode, parents ->
      if(parents.any { it.isTag<If>() }) {
        namesInsideIfs += lookupNode.toString()
      }
    }

    assertThat(namesInsideIfs).containsExactlyInAnyOrder("child.parent.grandparent.name",
        "parents.children",
        "person.name")
  }

}
