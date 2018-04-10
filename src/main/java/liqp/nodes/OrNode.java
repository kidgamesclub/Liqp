package liqp.nodes;

import liqp.LValue;

public class OrNode extends LValue implements LNode {

    private LNode lhs;
    private LNode rhs;

    public OrNode(LNode lhs, LNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Object render(RenderContext context) {

        return super.asBoolean(lhs.render(context)) || super.asBoolean(rhs.render(context));

    }
}
