package liqp.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import liqp.nodes.RenderContext;

public class Reverse extends Filter {

    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        if (!super.isArray(value)) {
            return value;
        }

        Object[] values = super.asArray(value);
        List<Object> list = new ArrayList<Object>(Arrays.asList(values));

        Collections.reverse(list);

        return list.toArray();
    }
}
