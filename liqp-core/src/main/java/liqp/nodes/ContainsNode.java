package liqp.nodes;

import java.util.Arrays;
import liqp.LValue;
import lombok.Getter;

@Getter
public class ContainsNode extends ExpressionNode {

    public ContainsNode(LNode lhs, LNode rhs) {
      super(lhs, rhs);
    }

    @Override
    public Object render(RenderContext context) {

        Object collection = lhs.render(context);
        Object needle = rhs.render(context);

        if(isArray(collection)) {
            Object[] array = asArray(collection);
            return Arrays.asList(array).contains(needle);
        }

        if(isString(collection)) {
            return asString(collection).contains(asString(needle));
        }

        return false;
    }
}
