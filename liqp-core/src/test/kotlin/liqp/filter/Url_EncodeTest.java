package liqp.filter;

import static liqp.AssertsKt.createTestParser;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidTemplate;
import liqp.LiquidParser;
import liqp.node.LTemplate;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class Url_EncodeTest {

    /*
        def test_url_encode
          assert_equal 'foo%2B1%40example.com', @filter.url_encode('foo+1@example.com')
          assert_equal '1', @filter.url_encode(1)
          assert_equal '2001-02-03', @filter.url_encode(Date.new(2001, 2, 3))
          assert_nil @filter.url_encode(nil)
        end
    */
    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{ 'foo+1@example.com' | url_encode }}", "foo%2B1%40example.com"},
                {"{{ '1' | url_encode }}", "1"},
                {"{{ nil | url_encode }}", ""},
        };

        for (String[] test : tests) {

            LTemplate template = createTestParser().parse(test[0]);
            String rendered = template.render();

            assertThat(rendered, is(test[1]));
        }
    }
}
