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

public class CssEngineTestUtilities {

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
  public void alignContent() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("align-content-normal align-content-flex-start");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .align-content-normal { align-content: normal }
          .align-content-flex-start { align-content: flex-start }
        }
        """
    );
  }

  @Test
  public void alignItems() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("align-items-flex-start align-items-center");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .align-items-flex-start { align-items: flex-start }
          .align-items-center { align-items: center }
        }
        """
    );
  }

  @Test
  public void alignSelf() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("align-self-auto align-self-flex-start");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .align-self-auto { align-self: auto }
          .align-self-flex-start { align-self: flex-start }
        }
        """
    );
  }

  @Test
  public void appearance() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("appearance-auto appearance-none");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .appearance-auto { appearance: auto }
          .appearance-none { appearance: none }
        }
        """
    );
  }

  @Test
  public void aspectRatio() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("aspect-ratio-auto aspect-ratio-2 aspect-ratio-16/9");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .aspect-ratio-auto { aspect-ratio: auto }
          .aspect-ratio-2 { aspect-ratio: 2 }
          .aspect-ratio-16\\/9 { aspect-ratio: 16/9 }
        }
        """
    );
  }

  @Test
  public void backgroundColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("background-color-currentColor");
        className("background-color-transparent");
        className("background-color-red-50");
        className("background-color-white");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .background-color-currentColor { background-color: currentColor }
          .background-color-transparent { background-color: transparent }
          .background-color-red-50 { background-color: var(--color-red-50) }
          .background-color-white { background-color: var(--color-white) }
        }
        """
    );
  }

  @Test
  public void border() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border-1px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .border-1px { border: 0.0625rem }
        }
        """
    );
  }

  @Test
  public void borderCollapse() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border-collapse-collapse border-collapse-separate border-collapse-inherit");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .border-collapse-collapse { border-collapse: collapse }
          .border-collapse-separate { border-collapse: separate }
          .border-collapse-inherit { border-collapse: inherit }
        }
        """
    );
  }

  private void test(Class<?> type, String expected) {
    CssEngine config;
    config = new CssEngine();

    config.scanClass(type);

    config.execute();

    assertEquals(
        config.testUtilities(),

        expected
    );
  }

}