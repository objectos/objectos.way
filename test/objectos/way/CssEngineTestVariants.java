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

import org.testng.annotations.Test;

public class CssEngineTestVariants {

  private static abstract class AbstractSubject extends Html.Template {
    @Override
    protected final void render() {
      div(
          renderFragment(this::classes)
      );
    }

    abstract void classes();
  }

  @Test
  public void active() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("active:color:pink-300");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .active\\:color\\:pink-300:active { color: var(--color-pink-300) }
        }
        """
    );
  }

  @Test
  public void after() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("after:content:''");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .after\\:content\\:''::after { content: '' }
        }
        """
    );
  }

  @Test
  public void breakpoint() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("width:100%");
        className("sm:max-width:screen-sm");
        className("md:max-width:screen-md");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .width\\:100\\% { width: 100% }

          @media (min-width: 40rem) {
            .sm\\:max-width\\:screen-sm { max-width: var(--breakpoint-sm) }
          }

          @media (min-width: 48rem) {
            .md\\:max-width\\:screen-md { max-width: var(--breakpoint-md) }
          }
        }
        """
    );
  }

  @Test
  public void dark() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("background-color:white");
        className("color:black");
        className("dark:background-color:black");
        className("dark:color:gray-50");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .background-color\\:white { background-color: var(--color-white) }
          .color\\:black { color: var(--color-black) }

          @media (prefers-color-scheme: dark) {
            .dark\\:background-color\\:black { background-color: var(--color-black) }
            .dark\\:color\\:gray-50 { color: var(--color-gray-50) }
          }
        }
        """
    );
  }

  @Test
  public void focus() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("focus:outline:solid_blue-500");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .focus\\:outline\\:solid_blue-500:focus { outline: solid var(--color-blue-500) }
        }
        """
    );
  }

  @Test
  public void star() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("*:color:blue-500");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .\\*\\:color\\:blue-500 > * { color: var(--color-blue-500) }
        }
        """
    );
  }

  private void test(Class<?> type, String expected) {
    CssEngine engine;
    engine = new CssEngine();

    engine.scanClass(type);

    engine.skipLayer(Css.Layer.THEME);
    engine.skipLayer(Css.Layer.BASE);
    engine.skipLayer(Css.Layer.COMPONENTS);

    engine.execute();

    String result;
    result = engine.generate();

    assertEquals(result, expected);
  }

}