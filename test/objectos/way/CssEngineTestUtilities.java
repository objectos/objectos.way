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
        className("border-2px_dashed");
        className("border-4px_solid_red-50");
        className("border-top-solid");
        className("border-top-dashed_red-500");
        className("border-top-1rem_solid");
        className("border-top-thick_double_#32a1ce");
        className("border-right-2px");
        className("border-bottom-2px_dotted");
        className("border-left-medium_dashed_green");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .border-1px { border: 1px }
          .border-2px_dashed { border: 2px dashed }
          .border-4px_solid_red-50 { border: 4px solid var(--color-red-50) }
          .border-top-solid { border-top: solid }
          .border-top-dashed_red-500 { border-top: dashed var(--color-red-500) }
          .border-top-1rem_solid { border-top: 1rem solid }
          .border-top-thick_double_#32a1ce { border-top: thick double #32a1ce }
          .border-right-2px { border-right: 2px }
          .border-bottom-2px_dotted { border-bottom: 2px dotted }
          .border-left-medium_dashed_green { border-left: medium dashed green }
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

  @Test
  public void borderRadius() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border-radius-10px");
        className("border-radius-10px_5%");
        className("border-radius-2px_4px_2px");
        className("border-radius-1px_0_3px_4px");
        className("border-radius-10px/20px");
        className("border-radius-10px_5%_/_20px_30px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .border-radius-10px { border-radius: 10px }
          .border-radius-10px_5\\% { border-radius: 10px 5% }
          .border-radius-2px_4px_2px { border-radius: 2px 4px 2px }
          .border-radius-1px_0_3px_4px { border-radius: 1px 0 3px 4px }
          .border-radius-10px\\/20px { border-radius: 10px/20px }
          .border-radius-10px_5\\%_\\/_20px_30px { border-radius: 10px 5% / 20px 30px }
        }
        """
    );
  }

  @Test
  public void borderSpacing() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border-spacing-1px");
        className("border-spacing-1cm_2em");
        className("border-spacing-unset");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .border-spacing-1px { border-spacing: 1px }
          .border-spacing-1cm_2em { border-spacing: 1cm 2em }
          .border-spacing-unset { border-spacing: unset }
        }
        """
    );
  }

  @Test
  public void boxShadow() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("box-shadow-inset_0_2px_4px_0_rgb(0_0_0_/_0.05)");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .box-shadow-inset_0_2px_4px_0_rgb\\(0_0_0_\\/_0\\.05\\) { box-shadow: inset 0 2px 4px 0 rgb(0 0 0 / 0.05) }
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