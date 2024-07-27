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

public class CssGeneratorAdapterTest {

  @Test
  public void testCase01() {
    CssConfig config;
    config = config();

    CssGenerator round;
    round = new CssGenerator(config);

    round.putRule("bg-black", new CssUtility(Css.Key.BACKGROUND_COLOR, "bg-black", List.of(), Css.parseProperties("background-color: black")));

    assertEquals(
        round.generate(),

        """
        .bg-black { background-color: black }
        """
    );
  }

  @Test(description = "cache hit")
  public void cache01() {
    Css.Rule foo;
    foo = Css.Rule.NOOP;

    class Impl extends CssGeneratorAdapter {
      private Css.Rule hit;

      public Impl() {}

      @Override
      final Css.Rule createFragment(String className) {
        Assert.fail("It should have returned from the cache");

        return null;
      }

      @Override
      final void consumeExisting(String token, Css.Rule existing) {
        hit = existing;
      }

      @Override
      Css.Rule getFragment(String token) {
        return "foo".equals(token) ? foo : null;
      }
    }

    Impl impl;
    impl = new Impl();

    impl.processToken("foo");

    assertSame(impl.hit, foo);
  }

  @Test(description = "cache miss")
  public void cache02() {
    Css.Rule foo;
    foo = Css.Rule.NOOP;

    class Impl extends CssGeneratorAdapter {
      String token;
      Css.Rule rule;

      @Override
      final Css.Rule createFragment(String className) {
        return foo;
      }

      @Override
      Css.Rule getFragment(String token) {
        return null;
      }

      @Override
      void store(String token, Css.Rule rule) {
        this.token = token;
        this.rule = rule;
      }
    }

    Impl impl;
    impl = new Impl();

    impl.processToken("foo");

    assertEquals(impl.token, "foo");
    assertSame(impl.rule, foo);
  }

  @Test(description = "single class name")
  public void processRawString01() {
    processRawString(
        "m-0",

        "m-0"
    );
  }

  @Test(description = "many class names")
  public void processRawString02() {
    processRawString(
        "m-0 block leading-3",

        "m-0", "block", "leading-3"
    );
  }

  @Test(description = "many class names w/ additional whitespace")
  public void processRawString03() {
    processRawString(
        "m-0   block  leading-3",

        "m-0", "block", "leading-3"
    );
  }

  @Test(description = "leading whitespace")
  public void processRawString04() {
    processRawString(
        " block",

        "block"
    );
  }

  @Test(description = "trailing whitespace")
  public void processRawString05() {
    processRawString(
        "block ",

        "block"
    );
  }

  @Test
  public void processRawString06() {
    processRawString(
        "sr-only underline focus:not-sr-only focus:flex focus:h-full focus:items-center focus:border-4 focus:border-focus focus:py-16px focus:outline-none",

        "sr-only", "underline", "focus:not-sr-only", "focus:flex", "focus:h-full",
        "focus:items-center", "focus:border-4", "focus:border-focus", "focus:py-16px",
        "focus:outline-none"
    );
  }

  @Test(description = "multine string")
  public void processRawString07() {
    processRawString(
        """
        mx-auto grid w-full max-w-screen-max grid-cols-4
        px-0px
        md:grid-cols-8 md:px-16px
        lg:grid-cols-16
        max:px-24px
        *:mx-16px
        """,

        "mx-auto", "grid", "w-full", "max-w-screen-max", "grid-cols-4",
        "px-0px",
        "md:grid-cols-8", "md:px-16px",
        "lg:grid-cols-16",
        "max:px-24px",
        "*:mx-16px"
    );
  }

  private void processRawString(String raw, String... expected) {
    List<String> result;
    result = new ArrayList<>();

    CssGeneratorAdapter adapter;
    adapter = new CssGeneratorAdapter() {
      @Override
      final void processToken(String s) {
        result.add(s);
      }
    };

    adapter.processRawString(raw);

    assertEquals(result, List.of(expected));
  }

  private final Css.Variant hover = new Css.ClassNameFormat("", ":hover");

  @Test(description = "single variant")
  public void variants01() {
    VariantsTester impl;
    impl = new VariantsTester();

    Css.Rule rule;
    rule = impl.createUtility("hover:block");

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

    Css.Rule rule;
    rule = impl.createUtility("xyz:block");

    assertSame(rule, Css.Rule.NOOP);
  }

  private class VariantsTester extends CssGeneratorAdapter {

    @Override
    final Css.Variant getVariant(String variantName) {
      return switch (variantName) {
        case "hover" -> hover;

        default -> null;
      };
    }

    @Override
    final Css.Rule createUtility(String className, List<Css.Variant> variants, String value) {
      return new CssUtility(
          Css.Key.DISPLAY,
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
        Css.Key._COLORS,

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

    gen.skipReset = true;

    return gen;
  }

}