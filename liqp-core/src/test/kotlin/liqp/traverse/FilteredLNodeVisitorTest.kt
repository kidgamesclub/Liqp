package liqp.traverse

import assertk.assertions.containsAll
import liqp.assertThat
import liqp.isTag
import liqp.createParseSettings
import liqp.nodes.LookupNode
import liqp.tags.If
import liqp.toParser
import org.junit.Test

class FilteredLNodeVisitorTest {
  @Test
  fun testNodeVisitor_Iterator() {
    val parsedTemplate = createParseSettings()
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

    assertThat(lookupNames).containsAll("root.branch.leaf",
        "child.parent.grandparent.name",
        "parents.children",
        "person.name")
  }

  @Test
  fun testNodeVisitor_Parents() {
    val parsedTemplate = createParseSettings()
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

    assertThat(namesInsideIfs).containsAll("child.parent.grandparent.name",
        "parents.children",
        "person.name")
  }

}
