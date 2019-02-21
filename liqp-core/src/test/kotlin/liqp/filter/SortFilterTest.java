package liqp.filter;

import static liqp.Mocks.mockRenderContext;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.stream.Collectors;
import liqp.LiquidDefaults;
import liqp.exceptions.LiquidRenderingException;
import liqp.parameterized.LiquifyNoInputTest;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runners.Parameterized;

public class SortFilterTest extends LiquifyNoInputTest {

  @Parameterized.Parameters
  public static Object[] testParams() {
    String[][] tests = {
          {"{{ x | sort }}", ""},
          {"{{ words | sort }}", "1132"},
          {"{{ numbers | sort }}", "1213"},
          {"{{ numbers | sort | last }}", "13"},
          {"{{ numbers | sort | first }}", "1"},
    };
    return tests;
  }

  public SortFilterTest(@NotNull String templateString,
                        @NotNull String expectedResult) {
    super(templateString, expectedResult, "{ \"words\"   : [\"2\", \"13\", \"1\"],  \"numbers\" : [2, 13, 1] }");
  }

  /*
   * def test_sort
   *   assert_equal [1,2,3,4], @filter.sort([4,3,2,1])
   *   assert_equal [{"a" => 1}, {"a" => 2}, {"a" => 3}, {"a" => 4}], @filter.sort([{"a" => 4}, {"a" => 3}, {"a" =>
   *   1}, {"a" => 2}], "a")
   * end
   */
  @Test
  public void applyOriginalTest() {

    LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("sort");
    final Object rendered = filter.onFilterAction(mockRenderContext(),
          new Integer[]{4, 3, 2, 1});
    Assertions.assertThat(rendered).asList().containsExactly(1, 2, 3, 4);
  }

  @Test
  public void applySortWithProperty() {

    LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("sort");
    final List<Sortage> rendered = (List<Sortage>) filter.onFilterAction(mockRenderContext(),
          ImmutableList.of(
                new Sortage(4, "Four"),
                new Sortage(2, "Two"),
                new Sortage(1, "One"),
                new Sortage(11, "Eleven"),
                new Sortage(6, "Six")),
          "id");

    final List<Integer> sortedIds = rendered.stream()
          .map(Sortage::getId)
          .collect(Collectors.toList());

    Assertions.assertThat(sortedIds).asList().containsExactly(1, 2, 4, 6, 11);
  }

  @Test
  public void applySortWithMissingProperty() {
    Assertions.assertThatCode(() -> {
      LFilter filter = new SortFilter();
      final List<Sortage> rendered = (List<Sortage>) filter.onFilterAction(mockRenderContext(),
            ImmutableList.of(
                  new Sortage(4, "Four"),
                  new Sortage(2, "Two"),
                  new Sortage(1, "One"),
                  new Sortage(11, "Eleven"),
                  new Sortage(6, "Six")),
            "idx");
    }).isInstanceOf(LiquidRenderingException.class);
  }

  static class Sortage {
    private int id;
    private String word;

    public Sortage(int id, String word) {
      this.id = id;
      this.word = word;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getWord() {
      return word;
    }

    public Sortage word(String word) {
      this.word = word;
      return this;
    }
  }
}
