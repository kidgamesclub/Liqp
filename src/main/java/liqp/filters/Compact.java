package liqp.filters;

import java.util.ArrayList;
import java.util.List;
import liqp.nodes.RenderContext;

public class Compact extends Filter {

    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        if (!super.isArray(value)) {
            return value;
        }

        Object[] values = super.asArray(value);
        List<Object> compacted = new ArrayList<Object>();

        for (Object obj : values) {
            if (obj != null) {
                compacted.add(obj);
            }
        }

        return compacted.toArray();
    }
}
