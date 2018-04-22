package liqp.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import liqp.nodes.RenderContext;

public class SortNatural extends Filter {

  @Override
  public Object apply(RenderContext context, Object value, Object... params) {

    if (!super.isArray(value)) {
      return value;
    }

    Object[] array = super.asArray(value);
    List<Object> list = new ArrayList<Object>(Arrays.asList(array));

    Collections.sort(list, (Comparator) (o1, o2)
          -> String.valueOf(o1).compareToIgnoreCase(String.valueOf(o2)));

    return list.toArray();
  }
}
