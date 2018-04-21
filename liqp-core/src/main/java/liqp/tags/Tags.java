package liqp.tags;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import one.util.streamex.StreamEx;

@Value
public class Tags {
  private static final Tags DEFAULT = createDefaultTags();

  public static Tags getDefaultTags() {
    return DEFAULT;
  }

  private static Tags createDefaultTags() {
    // Register all standard tags.
    return new Tags(ImmutableList.of(
          new Assign(),
          new Break(),
          new Capture(),
          new Case(),
          new Comment(),
          new Continue(),
          new Cycle(),
          new Decrement(),
          new For(),
          new If(),
          new Ifchanged(),
          new Include(),
          new Increment(),
          new Raw(),
          new Tablerow(),
          new Unless()));
  }

  public TagsBuilder toBuilder () {
    return new TagsBuilder().tags(this.tags.values());
  }

  public Tag getTag(String tagName) {
    final Tag b = tags.get(tagName);
    if (b==null) {
      throw new IllegalArgumentException("No tag exists with name: " + tagName);
    }
    return b;
  }

  public Tags withTags(Tag... tags) {
    return new Tags(ImmutableList.<Tag>builder()
          .addAll(this.tags.values())
          .add(tags)
          .build());
  }

  private final Map<String, Tag> tags;

  @Builder
  private Tags(@NonNull @Singular List<Tag> tags) {
    this.tags = StreamEx.of(tags)
          .mapToEntry(t -> t.name, t -> t)
          .toImmutableMap();
  }
}
