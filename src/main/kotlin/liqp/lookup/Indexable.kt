package liqp.lookup

import liqp.nodes.RenderContext

/**
 * Represents a navigatable property or index on an object.
 */
@FunctionalInterface
interface Indexable {

  /**
   * Retrieve the appropriate child object based on the parsed template.
   */
  operator fun get(value: Any?, context: RenderContext): Any?
}


