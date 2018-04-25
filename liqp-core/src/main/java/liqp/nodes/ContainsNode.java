package liqp.nodes;

import java.util.Arrays;
import liqp.node.LNode;
import lombok.Getter;

@Getter
public class ContainsNode extends ExpressionNode {

    public ContainsNode(LNode lhs, LNode rhs) {
      super(lhs, rhs);
    }

    @Override
    public Object render(LContext context) {

        Object collection = lhs.render(context);
        Object needle = rhs.render(context);

        if(Companion.isArray(collection)) {
            Object[] array = Companion.asArray(collection);
            return Arrays.asList(array).contains(needle);
        }

        if(Companion.isString(collection)) {
            return Companion.asString(collection).contains(Companion.asString(needle));
        }

        return false;
    }
}
