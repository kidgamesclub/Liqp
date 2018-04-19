package liqp.nodes;

import liqp.LValue;
import lombok.Getter;

@Getter
public class EqNode implements LNode {

    private LNode lhs;
    private LNode rhs;

    public EqNode(LNode lhs, LNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Object render(RenderContext context) {

        Object a = lhs.render(context);
        Object b = rhs.render(context);

        return LValue.areEqual(a, b);

    }
}
