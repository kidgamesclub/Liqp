package liqp.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import liqp.node.LNode;
import liqp.exceptions.MissingVariableException;
import liqp.lookup.Indexable;
import lombok.Getter;

@Getter
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
  public Object render(LContext context) {

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
    } catch (MissingVariableException e) {
      throw new MissingVariableException(this.getVariableName(), e.getVariableName());
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

  @Override
  public List<LNode> children() {
    return Collections.emptyList();
  }
}
