package liqp.lookup

import liqp.context.LContext

/**
 * Represents a navigatable property or index on an object.
 */
@FunctionalInterface
interface Indexable {

  /**
   * Retrieve the appropriate child object based on the parsed template.
   */
  operator fun get(value: Any?, context: LContext): Any?
}


