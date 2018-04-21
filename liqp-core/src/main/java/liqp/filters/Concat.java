package liqp.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import liqp.nodes.RenderContext;

public class Concat extends Filter {

    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        super.checkParams(params, 1);

        if (!super.isArray(params[0])) {
            throw new RuntimeException("Liquid error: concat filter requires an array argument");
        }

        List<Object> allValues = new ArrayList<Object>();

        if (super.isArray(value)) {
            allValues.addAll(Arrays.asList(super.asArray(value)));
        }

        allValues.addAll(Arrays.asList(super.asArray(params[0])));

        return allValues.toArray();
    }
}
