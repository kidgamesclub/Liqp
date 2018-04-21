package liqp.filters;

import liqp.nodes.RenderContext;

class Minus extends Filter {

    /*
     * plus(input, operand)
     *
     * subtraction
     */
    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        if(value == null) {
            value = 0L;
        }

        super.checkParams(params, 1);

        Object rhsObj = params[0];

        if (super.isInteger(value) && super.isInteger(rhsObj)) {
            return super.asNumber(value).longValue() - super.asNumber(rhsObj).longValue();
        }

        return super.asNumber(value).doubleValue() - super.asNumber(rhsObj).doubleValue();
    }
}
