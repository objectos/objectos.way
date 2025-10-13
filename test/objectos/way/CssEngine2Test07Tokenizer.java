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

public class CssEngine2Test07Tokenizer {

  @DataProvider
  public Object[][] testProvider() {
    return new Object[][] {{
        "single utility",
        "margin:0",
        l(
            r("margin:0", l("0", "margin"))
        )
    }, {
        "single utility + ws",
        "margin:0\n",
        l(
            r("margin:0", l("0", "margin"))
        )
    }, {
        "single utility value w/ sp",
        "margin:0_10rx",
        l(
            r("margin:0_10rx", l("0 10rx", "margin"))
        )
    }, {
        "multiple utilities",
        "display:block\nmargin:0\n",
        l(
            r("margin:0", l("0", "margin")),
            r("display:block", l("block", "display"))
        )
    }, {
        "invalid property ignored",
        "display:\nmargin:0\nfoo\npadding:1rx\n",
        l(
            r("padding:1rx", l("1rx", "padding")),
            r("margin:0", l("0", "margin"))
        )
    }, {
        "invalid property and malformed display ignored",
        "display :\nmargin:0\nfoo : bar\npadding:1rx\n",
        l(
            r("padding:1rx", l("1rx", "padding")),
            r("margin:0", l("0", "margin"))
        )
    }, {
        "single line multiple utilities",
        "margin:0 padding:1rx",
        l(
            r("padding:1rx", l("1rx", "padding")),
            r("margin:0", l("0", "margin"))
        )
    }, {
        "single variant",
        "md/margin:0",
        l(
            r("md/margin:0", l("0", "margin", "md"))
        )
    }, {
        "two variants",
        "dark/md/margin:0",
        l(
            r("dark/md/margin:0", l("0", "margin", "md", "dark"))
        )
    }, {
        "custom variant",
        "&[data-foo]/margin:0",
        l(
            r("&[data-foo]/margin:0", l("0", "margin", "&[data-foo]"))
        )
    }, {
        "custom variant + escape '/'",
        "&[attr*='\\/']/margin:0",
        l(
            r("&[attr*='\\/']/margin:0", l("0", "margin", "&[attr*='/']"))
        )
    }, {
        "custom variant with ':'",
        ":has([data-selected=true])/margin:0",
        l(
            r(":has([data-selected=true])/margin:0", l("0", "margin", ":has([data-selected=true])"))
        )
    }, {
        "custom variant + underscore",
        "&_li:nth-child(odd)/margin:0",
        l(
            r("&_li:nth-child(odd)/margin:0", l("0", "margin", "& li:nth-child(odd)"))
        )
    }, {
        "custom variant + escape underscore",
        "&[attr*='\\_']/margin:0",
        l(
            r("&[attr*='\\_']/margin:0", l("0", "margin", "&[attr*='_']"))
        )
    }, {
        "value + escape colon",
        "content:'\\:'",
        l(
            r("content:'\\:'", l("':'", "content"))
        )
    }, {
        "value + escape colon + variant",
        "md/content:'\\:'",
        l(
            r("md/content:'\\:'", l("':'", "content", "md"))
        )
    }, {
        "value + escape underscore",
        "content:'\\_'",
        l(
            r("content:'\\_'", l("'_'", "content"))
        )
    }};
  }

  private record Result(String className, List<String> slugs) {}

  private Result r(String className, List<String> slugs) {
    return new Result(className, slugs);
  }

  @Test(dataProvider = "testProvider")
  public void test(
      String description,
      String string,
      @SuppressWarnings("exports") List<Result> expected) {
    class ThisProcessor implements CssEngine2.Slugs {
      final List<Result> results = new ArrayList<>();

      @Override
      public final void consume(String className, List<String> slugs) {
        results.add(
            r(className, List.copyOf(slugs))
        );
      }
    }

    final ThisProcessor processor;
    processor = new ThisProcessor();

    final CssEngine2.Tokenizer tokenizer;
    tokenizer = new CssEngine2.Tokenizer(processor);

    tokenizer.consume(string);

    assertEquals(processor.results, expected);
  }

  @SafeVarargs
  private <T> List<T> l(T... values) {
    return List.of(values);
  }

}