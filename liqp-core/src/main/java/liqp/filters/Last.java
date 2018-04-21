package liqp.filters;

import liqp.nodes.RenderContext;

class Last extends Filter {

    /*
     * last(array)
     *
     * Get the last element of the passed in array
     */
    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        Object[] array = super.asArray(value);

        return array.length == 0 ? null : super.asString(array[array.length - 1]);
    }
}
