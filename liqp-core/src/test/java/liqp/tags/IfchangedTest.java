package liqp.tags;

import liqp.parameterized.LiquifyWithInputTest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class IfchangedTest extends LiquifyWithInputTest {
  @Parameterized.Parameters(name = "{0}={1}")
  public static Object[] testParams() {
    return new String[][]{
          {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}",
                "1",
                "{ \"array\": [1, 1] }"},
          {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}",
                "123",
                "{ \"array\": [1, 1, 2, 2, 3, 3] }"},
          {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}",
                "1",
                "{ \"array\": [1, 1, 1, 1] }"},
          {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}",
                "",
                "{ \"array\": [] }"},
          {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}",
                "",
                "{}"}
    };
  }

  public IfchangedTest(@NotNull String templateString,
                       @Nullable String expectedResult,
                       @Nullable Object inputData) {
    super(templateString, expectedResult, inputData);
  }
}
