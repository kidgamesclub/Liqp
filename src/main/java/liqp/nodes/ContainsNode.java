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

        if(super.isArray(collection)) {
            Object[] array = super.asArray(collection);
            return Arrays.asList(array).contains(needle);
        }

        if(super.isString(collection)) {
            return super.asString(collection).contains(super.asString(needle));
        }

        return false;
    }
}
