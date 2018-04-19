package liqp.tags;

import liqp.nodes.LNode;

public abstract class InitializingTag extends Tag {

  /**
   * This method creates an instance tag based on the input nodes.  This allows for any optimizations or parsing to be
   * done up-front, instead of on each render iteration.
   *
   */
  public abstract void init(LNode... tokens);
}
