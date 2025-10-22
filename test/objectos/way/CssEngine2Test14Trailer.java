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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CssEngine2Test14Trailer {

  @DataProvider
  public Object[][] writeProvider() {
    return new Object[][] {{
        "@keyframes",

        tester(t -> {
          t.keyframes(
              "fade-in",
              CssEngine2.parsedRule("from", List.of(
                  CssEngine2.decl("opacity", "0")
              )),
              CssEngine2.parsedRule("100%", List.of(
                  CssEngine2.decl("opacity", "1")
              ))
          );
        }),

        """
        @keyframes fade-in {
          from {
            opacity: 0;
          }
          100% {
            opacity: 1;
          }
        }
        """
    }, {
        "@font-face",

        tester(t -> {
          t.fontFace(
              CssEngine2.decl("font-family", "\"IBM Plex Sans\""),
              CssEngine2.decl("font-style", "normal"),
              CssEngine2.decl("font-weight", "700"),
              CssEngine2.decl("src", "local(\"IBM Plex Sans Bold\")")
          );
        }),

        """
        @font-face {
          font-family: "IBM Plex Sans";
          font-style: normal;
          font-weight: 700;
          src: local("IBM Plex Sans Bold");
        }
        """
    }};
  }

  @Test(dataProvider = "writeProvider")
  public void write(
      String description,
      @SuppressWarnings("exports") Consumer<? super Tester> test,
      String expected) {
    final Tester tester;
    tester = new Tester();

    test.accept(tester);

    assertEquals(tester.toString(), expected);
  }

  private Consumer<Tester> tester(Consumer<Tester> tester) {
    return tester;
  }

  static final class Tester {

    final List<List<CssEngine2.Decl>> fontFaces = new ArrayList<>();

    final List<CssEngine2.Keyframes> keyframes = new ArrayList<>();

    final void fontFace(CssEngine2.Decl... decls) {
      fontFaces.add(List.of(decls));
    }

    final void keyframes(String name, CssEngine2.ParsedRule... rules) {
      keyframes.add(
          CssEngine2.keyframes(
              name,

              List.of(rules)
          )
      );
    }

    @Override
    public final String toString() {
      try {
        final StringBuilder out;
        out = new StringBuilder();

        final CssEngine2.Trailer w;
        w = new CssEngine2.Trailer(fontFaces, keyframes);

        w.write(out);

        return out.toString();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

  }

}