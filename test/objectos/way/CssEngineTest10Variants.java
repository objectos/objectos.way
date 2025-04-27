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

import org.testng.annotations.Test;

public class CssEngineTest10Variants {

  @Test
  public void active() {
    class Subject extends CssSubject {
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
    class Subject extends CssSubject {
      @Override
      final void classes() {
        className("after:content:''");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .after\\:content\\:\\'\\'::after { content: '' }
        }
        """
    );
  }

  @Test
  public void attribute() {
    class Subject extends CssSubject {
      @Override
      final void classes() {
        className("[data-marker]:content:''");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .\\[data-marker\\]\\:content\\:\\'\\'[data-marker] { content: '' }
        }
        """
    );
  }

  @Test
  public void breakpoint() {
    class Subject extends CssSubject {
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
  public void checked() {
    class Subject extends CssSubject {
      @Override
      final void classes() {
        className("checked:background-color:gray-500");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .checked\\:background-color\\:gray-500:checked { background-color: var(--color-gray-500) }
        }
        """
    );
  }

  @Test
  public void dark() {
    class Subject extends CssSubject {
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
  public void disabled() {
    class Subject extends CssSubject {
      @Override
      final void classes() {
        className("disabled:background-color:gray-500");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .disabled\\:background-color\\:gray-500:disabled { background-color: var(--color-gray-500) }
        }
        """
    );
  }

  @Test
  public void element01() {
    class Subject extends CssSubject {
      @Override
      final void classes() {
        className("tr:background-color:neutral-100");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .tr\\:background-color\\:neutral-100 tr { background-color: var(--color-neutral-100) }
        }
        """
    );
  }

  @Test
  public void element02() {
    class Subject extends CssSubject {
      @Override
      final void classes() {
        className("span:[data-line]:display:block");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .span\\:\\[data-line\\]\\:display\\:block span[data-line] { display: block }
        }
        """
    );
  }

  @Test
  public void firstChild() {
    class Subject extends CssSubject {
      @Override
      final void classes() {
        className("hover:first-child:color:white");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .hover\\:first-child\\:color\\:white:hover:first-child { color: var(--color-white) }
        }
        """
    );
  }

  @Test
  public void firstLetter() {
    class Subject extends CssSubject {
      @Override
      final void classes() {
        className("first-letter:font-weight:bold");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .first-letter\\:font-weight\\:bold::first-letter { font-weight: bold }
        }
        """
    );
  }

  @Test
  public void firstLine() {
    class Subject extends CssSubject {
      @Override
      final void classes() {
        className("first-line:text-decoration:underline");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .first-line\\:text-decoration\\:underline::first-line { text-decoration: underline }
        }
        """
    );
  }

  @Test
  public void focus() {
    class Subject extends CssSubject {
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
  public void focusVisible() {
    class Subject extends CssSubject {
      @Override
      final void classes() {
        className("focus-visible:outline:solid_blue-500");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .focus-visible\\:outline\\:solid_blue-500:focus-visible { outline: solid var(--color-blue-500) }
        }
        """
    );
  }

  @Test
  public void group() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        a(
            css("group"),

            div(css("group-hover:outline:1px_solid_blue-500")),

            div(css("group-[data-marker]:background-color:blue-500"))
        );
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .group[data-marker] .group-\\[data-marker\\]\\:background-color\\:blue-500 { background-color: var(--color-blue-500) }
          .group:hover .group-hover\\:outline\\:1px_solid_blue-500 { outline: 1px solid var(--color-blue-500) }
        }
        """
    );
  }

  @Test
  public void lastChild() {
    class Subject extends CssSubject {
      @Override
      final void classes() {
        className("hover:last-child:color:white");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .hover\\:last-child\\:color\\:white:hover:last-child { color: var(--color-white) }
        }
        """
    );
  }

  @Test
  public void nthChild() {
    class Subject extends CssSubject {
      @Override
      final void classes() {
        className("nth-child(-n+15):display:none");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .nth-child\\(-n\\+15\\)\\:display\\:none:nth-child(-n+15) { display: none }
        }
        """
    );
  }

  @Test
  public void star() {
    class Subject extends CssSubject {
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

  @Test(description = "edge cases")
  public void testCase01() {
    class Subject extends CssSubject {
      @Override
      final void classes() {
        text("::");
        text("foo:");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
        }
        """
    );
  }

  private void test(Class<?> type, String expected) {
    assertEquals(
        CssEngine.generate(config -> {
          config.noteSink(Y.noteSink());

          config.scanClass(type);

          config.skipLayer(Css.Layer.THEME);
          config.skipLayer(Css.Layer.BASE);
          config.skipLayer(Css.Layer.COMPONENTS);
        }),

        expected
    );
  }

}