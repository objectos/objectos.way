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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CssEngine2Test06Tokenizer {

  @DataProvider
  public Object[][] testProvider() {
    return new Object[][] {
        {"margin:0", l(l("0", "margin")), "single utility"},
        {"margin:0\n", l(l("0", "margin")), "single utility + ws"},
        {"display:block\nmargin:0\n", l(l("0", "margin"), l("block", "display")), "multiple utilities"},
        {"display:\nmargin:0\nfoo\npadding:1rx\n", l(l("1rx", "padding"), l("0", "margin")), "invalid property ignored"},
        {"display :\nmargin:0\nfoo : bar\npadding:1rx\n", l(l("1rx", "padding"), l("0", "margin")), "invalid property and malformed display ignored"},
        {"margin:0 padding:1rx", l(l("1rx", "padding"), l("0", "margin")), "single line multiple utilities"},
        {"md/margin:0", l(l("0", "margin", "md")), "with media query"},
        {"dark/md/margin:0", l(l("0", "margin", "md", "dark")), "with media query and dark mode"},
        {"&[data-foo]/margin:0", l(l("0", "margin", "&[data-foo]")), "with data attribute selector"},
        {"&[attr*='//']/margin:0", l(l("0", "margin", "&[attr*='/']")), "with complex attribute selector"},
        {":has([data-selected=true])/margin:0", l(l("0", "margin", ":has([data-selected=true])")), "with :has selector"},
        {"&_li:nth-child(odd)/margin:0", l(l("0", "margin", "& li:nth-child(odd)")), "with nested selector"},
        {"content:'::'", l(l("content", "':'")), "content with double colon"},
        {"md/content:'::'", l(l("md", "content", "':'")), "content with media query"},
        {"content:'__'", l(l("content", "'_'")), "content with underscore"}
    };
  }

  @Test(dataProvider = "testProvider")
  public void test(String string, List<List<String>> expected, String description) {
    class ThisProcessor implements CssEngine2.Slugs {
      final List<List<String>> result = new ArrayList<>();

      @Override
      public final void consume(List<String> slugs) {
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

    assertEquals(processor.result, expected);
  }

  private List<Object> l(Object... values) {
    return List.of(values);
  }

}