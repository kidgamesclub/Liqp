package liqp.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import liqp.LValue;
import liqp.exceptions.ExceededMaxIterationsException;
import liqp.nodes.BlockNode;
import liqp.nodes.LNode;
import liqp.nodes.RenderContext;

class For extends Tag {

    private static final String OFFSET = "offset";
    private static final String LIMIT = "limit";
    private static final String CONTINUE = "continue";

    /*
     * forloop.length      # => length of the entire for loop
     * forloop.index       # => index of the current iteration
     * forloop.index0      # => index of the current iteration (zero based)
     * forloop.rindex      # => how many items are still left?
     * forloop.rindex0     # => how many items are still left? (zero based)
     * forloop.first       # => is this the first iteration?
     * forloop.last        # => is this the last iteration?
     */

  /*
   * For loop
   */
  @Override
  public Object render(RenderContext context, LNode... nodes) {

    // The first node in the array denotes whether this is a for-tag
    // over an array, `for item in array ...`, or a for-tag over a
    // range, `for i in (4..item.length)`.
    boolean array = super.asBoolean(nodes[0].render(context));

    String id = super.asString(nodes[1].render(context));

    // Each for tag has its own context that keeps track of its own variables (scope)
    context.addFrame();

    Object rendered = array ? renderArray(id, context, nodes) : renderRange(id, context, nodes);

    context.popFrame();
    return rendered;
  }

  private Object renderArray(String id, RenderContext context, LNode... tokens) {

    StringBuilder builder = new StringBuilder();

    // attributes start from index 5
    Map<String, Integer> attributes = getAttributes(5, context, tokens);

    int offset = attributes.get(OFFSET);
    int limit = attributes.get(LIMIT);

    Object[] array = super.asArray(tokens[2].render(context));

    LNode block = tokens[3];
    LNode blockIfEmptyOrNull = tokens[4];

    if (array == null || array.length == 0) {
      return blockIfEmptyOrNull == null ? null : blockIfEmptyOrNull.render(context);
    }

    int length = Math.min(limit, array.length);

    final String loopName = id + "-" + tokens[2].toString();
    context.startLoop(length, loopName);

    int continueIndex = offset;

    for (int i = offset, n = 0; n < limit && i < array.length; i++, n++) {

      context.incrementIterations();

      continueIndex = i;
      context.set(id, array[i]);

      List<LNode> children = ((BlockNode) block).getChildren();
      boolean isBreak = false;

      for (LNode node : children) {

        Object value = node.render(context);

        if (value == LValue.CONTINUE) {
          // break from this inner loop: equals continue outer loop!
          break;
        }

        if (value == LValue.BREAK) {
          // break from inner loop
          isBreak = true;
          break;
        }

        if (value != null && value.getClass().isArray()) {

          Object[] arr = (Object[]) value;

          for (Object obj : arr) {
            builder.append(String.valueOf(obj));
          }
        } else {
          builder.append(super.asString(value));
        }
      }

      if (isBreak) {
        // break from outer loop
        break;
      }
    }

    context.setRoot(CONTINUE, continueIndex + 1);

    return builder.toString();
  }

  private Object renderRange(String id, RenderContext context, LNode... tokens) {

    StringBuilder builder = new StringBuilder();

    // attributes start from index 5
    Map<String, Integer> attributes = getAttributes(5, context, tokens);

    int offset = attributes.get(OFFSET);
    int limit = attributes.get(LIMIT);

    LNode block = tokens[4];

    try {
      int from = super.asNumber(tokens[2].render(context)).intValue();
      int to = super.asNumber(tokens[3].render(context)).intValue();

      int length = (to - from);

      context.startLoop(length, null);
      int continueIndex = from + offset;

      for (int i = from + offset, n = 0; i <= to && n < limit; i++, n++) {

        context.incrementIterations();
        continueIndex = i;
        context.set(id, i);

        List<LNode> children = ((BlockNode) block).getChildren();
        boolean isBreak = false;

        for (LNode node : children) {

          Object value = node.render(context);

          if (value == null) {
            continue;
          }

          if (value == LValue.CONTINUE) {
            // break from this inner loop: equals continue outer loop!
            break;
          }

          if (value == LValue.BREAK) {
            // break from inner loop
            isBreak = true;
            break;
          }

          if (super.isArray(value)) {

            Object[] arr = super.asArray(value);

            for (Object obj : arr) {
              builder.append(String.valueOf(obj));
            }
          } else {
            builder.append(super.asString(value));
          }
        }

        if (isBreak) {
          // break from outer loop
          break;
        }
      }

      context.set(CONTINUE, continueIndex + 1);
    } catch (ExceededMaxIterationsException e) {
      throw e;
    } catch (Exception e) {
      /* just ignore incorrect expressions */
    }

    return builder.toString();
  }

  private Map<String, Integer> getAttributes(int fromIndex, RenderContext context, LNode... tokens) {

    Map<String, Integer> attributes = new HashMap<String, Integer>();

    attributes.put(OFFSET, 0);
    attributes.put(LIMIT, Integer.MAX_VALUE);

    for (int i = fromIndex; i < tokens.length; i++) {

      Object[] attribute = super.asArray(tokens[i].render(context));

      try {
        attributes.put(super.asString(attribute[0]), super.asNumber(attribute[1]).intValue());
      } catch (Exception e) {
        /* just ignore incorrect attributes */
      }
    }

    return attributes;
  }
}
