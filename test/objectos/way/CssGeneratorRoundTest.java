/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
import static org.testng.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CssGeneratorRoundTest {

  @Test
  public void testCase01() {
    CssConfig config;
    config = config();

    CssGeneratorRound round;
    round = new CssGeneratorRound(config);

    round.putRule("bg-black", new CssUtility(CssKey.BACKGROUND_COLOR, "bg-black", List.of(), Css.parseProperties("background-color: black")));

    assertEquals(
        round.generate(),

        """
        .bg-black { background-color: black }
        """
    );
  }

  @Test(description = "cache hit")
  public void cache01() {
    CssRule foo;
    foo = CssRule.NOOP;

    class Impl extends CssGeneratorRound {
      private CssRule hit;

      public Impl() {
        putRule("foo", foo);
      }

      @Override
      final void onCacheHit(CssRule existing) {
        hit = existing;
      }

      @Override
      final CssRule onCacheMiss(String className) {
        Assert.fail("It should have returned from the cache");

        return null;
      }
    }

    Impl impl;
    impl = new Impl();

    impl.onSplit("foo");

    assertSame(impl.hit, foo);
  }

  @Test(description = "cache miss")
  public void cache02() {
    CssRule foo;
    foo = CssRule.NOOP;

    class Impl extends CssGeneratorRound {
      @Override
      final CssRule onCacheMiss(String className) {
        return foo;
      }
    }

    Impl impl;
    impl = new Impl();

    impl.onSplit("foo");

    assertSame(impl.getRule("foo"), foo);
  }

  @Test(description = "single class name")
  public void split01() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(className("m-0"));
      }
    }

    split(Subject.class, "m-0");
  }

  @Test(description = "many class names")
  public void split02() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(
            className("m-0 block leading-3")
        );
      }
    }

    split(Subject.class, "m-0", "block", "leading-3");
  }

  @Test(description = "many class names w/ additional whitespace")
  public void split03() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(
            className("m-0   block  leading-3")
        );
      }
    }

    split(Subject.class, "m-0", "block", "leading-3");
  }

  @Test(description = "leading whitespace")
  public void split04() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(
            className(" block")
        );
      }
    }

    split(Subject.class, "block");
  }

  @Test(description = "trailing whitespace")
  public void split05() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(
            className("block ")
        );
      }
    }

    split(Subject.class, "block");
  }

  @Test
  public void split06() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(
            className("sr-only underline focus:not-sr-only focus:flex focus:h-full focus:items-center focus:border-4 focus:border-focus focus:py-16px focus:outline-none")
        );
      }
    }

    split(Subject.class, "sr-only", "underline", "focus:not-sr-only", "focus:flex", "focus:h-full", "focus:items-center", "focus:border-4", "focus:border-focus", "focus:py-16px",
        "focus:outline-none");
  }

  private void split(Class<?> type, String... expected) {
    List<String> result;
    result = new ArrayList<>();

    CssGeneratorRound round;
    round = new CssGeneratorRound() {
      @Override
      final void onSplit(String s) {
        result.add(s);
      }
    };

    CssGeneratorScanner scanner;
    scanner = new CssGeneratorScanner();

    scanner.scan(type, round::split);

    assertEquals(result, List.of(expected));
  }

  private final CssVariant hover = new CssVariant.ClassNameFormat("", ":hover");

  @Test(description = "single variant")
  public void variants01() {
    VariantsTester impl;
    impl = new VariantsTester();

    CssRule rule;
    rule = impl.onCacheMiss("hover:block");

    assertEquals(
        rule.toString(),

        """
        .hover\\:block:hover { display: block }
        """
    );
  }

  @Test(description = "it should not process class name with unknown prefix")
  public void variants02() {
    VariantsTester impl;
    impl = new VariantsTester();

    CssRule rule;
    rule = impl.onCacheMiss("xyz:block");

    assertSame(rule, CssRule.NOOP);
  }

  private class VariantsTester extends CssGeneratorRound {

    @Override
    final CssVariant getVariant(String variantName) {
      return switch (variantName) {
        case "hover" -> hover;

        default -> null;
      };
    }

    @Override
    final CssRule onVariants(String className, List<CssVariant> variants, String value) {
      return new CssUtility(
          CssKey.DISPLAY,
          className, variants,
          CssProperties.of("display", value)
      );
    }

  }

  private CssConfig config() {
    CssConfig gen;
    gen = new CssConfig();

    gen.classes(Set.of());

    gen.override(
        CssKey._COLORS,

        Css.parseProperties(
            """
            inherit: inherit
            current: currentColor
            transparent: transparent

            black: #000000
            white: #ffffff"
            """
        )
    );

    gen.skipReset(true);

    return gen;
  }

}