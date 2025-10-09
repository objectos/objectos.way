/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;

public class CssEngine2Test06Tokenizer {

  @Test
  public void testCase01() {
    test(
        "margin:0",

        List.of("margin", "0")
    );
  }

  @Test
  public void testCase02() {
    test(
        """
        margin:0
        """,

        List.of("margin", "0")
    );
  }

  @Test
  public void testCase03() {
    test(
        """
        display:block
        margin:0
        """,

        List.of("display", "block"),
        List.of("margin", "0")
    );
  }

  @Test
  public void testCase04() {
    test(
        """
        display:
        margin:0
        foo
        padding:1rx
        """,

        List.of("margin", "0"),
        List.of("padding", "1rx")
    );
  }

  @Test
  public void testCase05() {
    test(
        """
        display :
        margin:0
        foo : bar
        padding:1rx
        """,

        List.of("margin", "0"),
        List.of("padding", "1rx")
    );
  }

  @Test
  public void testCase06() {
    test(
        "margin:0 padding:1rx",

        List.of("margin", "0"),
        List.of("padding", "1rx")
    );
  }

  @SafeVarargs
  private void test(String string, List<String>... lists) {
    class ThisProcessor implements CssEngine2.Processor {
      final List<List<String>> result = new ArrayList<>();

      @Override
      public final void process(List<String> slugs) {
        result.add(
            List.copyOf(slugs)
        );
      }
    }

    final ThisProcessor processor;
    processor = new ThisProcessor();

    final CssEngine2.Tokenizer tokenizer;
    tokenizer = new CssEngine2.Tokenizer(processor);

    tokenizer.consume(string);

    final List<List<String>> expected;
    expected = List.of(lists);

    assertEquals(processor.result, expected);
  }

}