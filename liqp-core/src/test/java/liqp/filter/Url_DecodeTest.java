package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class Url_DecodeTest {

    /*
        def test_url_decode
          assert_equal 'foo bar', @filter.url_decode('foo+bar')
          assert_equal 'foo bar', @filter.url_decode('foo%20bar')
          assert_equal 'foo+1@example.com', @filter.url_decode('foo%2B1%40example.com')
          assert_equal '1', @filter.url_decode(1)
          assert_equal '2001-02-03', @filter.url_decode(Date.new(2001, 2, 3))
          assert_nil @filter.url_decode(nil)
        end
    */
    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{ 'foo+bar' | url_decode }}", "foo bar"},
                {"{{ 'foo%20bar' | url_decode }}", "foo bar"},
                {"{{ 'foo%2B1%40example.com' | url_decode }}", "foo+1@example.com"},
                {"{{ nil | url_decode }}", ""},
        };

        for (String[] test : tests) {

            Template template = LiquidParser.newInstance().parse(test[0]);
            String rendered = String.valueOf(template.render());

            assertThat(rendered, is(test[1]));
        }
    }
}
