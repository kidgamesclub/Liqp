package liqp.filter;

public class CeilTest {

  /*
      def test_ceil
        assert_template_result "5", "{{ input | ceil }}", 'input' => 4.6
        assert_template_result "5", "{{ '4.3' | ceil }}"
        assert_template_result "5", "{{ price | ceil }}", 'price' => NumberLikeThing.new(4.6)
      end
  */
  public static Object[] testParams() {
    return new String[][]{
          {"{{ input | ceil }}", "5", "{ \"input\": 4.6 }"},
          {"{{ '4.3' | ceil }}", "5", "{}"},
          {"{{ price | ceil }}", "5", "{ \"price\": 4.6 }"}
    };
  }
}
