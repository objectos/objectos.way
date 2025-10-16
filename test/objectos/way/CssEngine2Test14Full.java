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
import org.testng.annotations.Test;

@SuppressWarnings("unused")
public class CssEngine2Test14Full {

  @Test
  public void testCase01() throws IOException {
    class Subject {}

    final CssEngine2.System system;
    system = new CssEngine2.System();

    system.base = "";

    system.theme = "";

    final CssEngine2 engine;
    engine = new CssEngine2(system);

    engine.noteSink(Y.noteSink());

    engine.scanClass(Subject.class);

    final StringBuilder out;
    out = new StringBuilder();

    engine.generate(out);

    assertEquals(
        out.toString(),

        ""
    );
  }

  @Test
  public void testCase02() throws IOException {
    class Subject {}

    final CssEngine2.System system;
    system = new CssEngine2.System();

    system.base = """
    html {
      line-height: 1.5;
    }
    """;

    system.theme = "";

    final CssEngine2 engine;
    engine = new CssEngine2(system);

    engine.noteSink(Y.noteSink());

    engine.scanClass(Subject.class);

    final StringBuilder out;
    out = new StringBuilder();

    engine.generate(out);

    assertEquals(
        out.toString(),

        """
        @layer base {
          html {
            line-height: 1.5;
          }
        }
        """
    );
  }

  @Test
  public void testCase03() throws IOException {
    class Subject {
      String s = """
      margin:0
      """;
    }

    final CssEngine2.System system;
    system = new CssEngine2.System();

    system.base = "";

    system.theme = "";

    final CssEngine2 engine;
    engine = new CssEngine2(system);

    engine.noteSink(Y.noteSink());

    engine.scanClass(Subject.class);

    final StringBuilder out;
    out = new StringBuilder();

    engine.generate(out);

    assertEquals(
        out.toString(),

        """
        @layer utilities {
          .margin\\:0 { margin: 0 }
        }
        """
    );
  }

  @Test
  public void testCase04() throws IOException {
    class Subject {
      String s = """
      margin:0
      """;
    }

    final CssEngine2.System system;
    system = new CssEngine2.System();

    system.base = "";

    system.theme = """
    --color-red-50: oklch(97.1% 0.013 17.38);
    """;

    final CssEngine2 engine;
    engine = new CssEngine2(system);

    engine.noteSink(Y.noteSink());

    engine.scanClass(Subject.class);

    final StringBuilder out;
    out = new StringBuilder();

    engine.generate(out);

    assertEquals(
        out.toString(),

        """
        @layer utilities {
          .margin\\:0 { margin: 0 }
        }
        """
    );
  }

  @Test
  public void testCase05() throws IOException {
    class Subject {
      String s = """
      color:red-50
      margin:0
      """;
    }

    final CssEngine2.System system;
    system = new CssEngine2.System();

    system.base = "";

    system.theme = """
    --color-red-50: oklch(97.1% 0.013 17.38);
    """;

    final CssEngine2 engine;
    engine = new CssEngine2(system);

    engine.noteSink(Y.noteSink());

    engine.scanClass(Subject.class);

    final StringBuilder out;
    out = new StringBuilder();

    engine.generate(out);

    assertEquals(
        out.toString(),

        """
        @layer theme {
          :root {
            --color-red-50: oklch(97.1% 0.013 17.38);
          }
        }
        @layer utilities {
          .color\\:red-50 { color: oklch(97.1% 0.013 17.38) }
          .margin\\:0 { margin: 0 }
        }
        """
    );
  }

  @Test
  public void testCase06() throws IOException {
    class Subject {
      String s = """
      color:red-50
      margin:0
      """;
    }

    final CssEngine2.System system;
    system = new CssEngine2.System();

    system.base = """
    html {
      line-height: 1.5;
    }
    """;

    system.theme = """
    --color-red-50: oklch(97.1% 0.013 17.38);
    """;

    final CssEngine2 engine;
    engine = new CssEngine2(system);

    engine.noteSink(Y.noteSink());

    engine.scanClass(Subject.class);

    final StringBuilder out;
    out = new StringBuilder();

    engine.generate(out);

    assertEquals(
        out.toString(),

        """
        @layer theme {
          :root {
            --color-red-50: oklch(97.1% 0.013 17.38);
          }
        }
        @layer base {
          html {
            line-height: 1.5;
          }
        }
        @layer utilities {
          .color\\:red-50 { color: oklch(97.1% 0.013 17.38) }
          .margin\\:0 { margin: 0 }
        }
        """
    );
  }

}