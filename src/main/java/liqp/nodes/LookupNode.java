package liqp.nodes;

import java.util.ArrayList;
import java.util.List;
import liqp.exceptions.VariableNotExistException;
import liqp.lookup.Indexable;

public class LookupNode implements LNode {

  private final String id;
  private final List<Indexable> indexes;

  public LookupNode(String id) {
    this.id = id;
    indexes = new ArrayList<>();
  }

  public void add(Indexable indexable) {
    indexes.add(indexable);
  }

  @Override
  public Object render(RenderContext context) {

    Object value;

    // Check if there's a [var] lookup, AST: ^(LOOKUP Id["@var"])
    if (id.startsWith("@")) {
      final Object existing = context.get(id.substring(1));
      final String varName = String.valueOf(existing);
      value = context.get(varName);
    } else {
      value = context.get(id);
    }

    try {
      for (Indexable index : indexes) {
        value = index.get(value, context);
      }
    } catch (VariableNotExistException e) {
      throw new VariableNotExistException(this.getVariableName());
    }

    return value;
  }

  @Override
  public String toString() {

    StringBuilder builder = new StringBuilder();

    builder.append(id);

    for (Indexable index : this.indexes) {
      builder.append(index.toString());
    }

    return builder.toString();
  }

  private String getVariableName() {
    StringBuilder variableFullName = new StringBuilder(id);
    for (Indexable index : indexes) {
      variableFullName.append(index.toString());
    }
    return variableFullName.toString();
  }
}
