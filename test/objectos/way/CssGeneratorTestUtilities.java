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

public class CssGeneratorTestUtilities {

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
        .align-content-normal { align-content: normal }
        .align-content-flex-start { align-content: flex-start }
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
        .align-items-flex-start { align-items: flex-start }
        .align-items-center { align-items: center }
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
        .align-self-auto { align-self: auto }
        .align-self-flex-start { align-self: flex-start }
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
        .appearance-auto { appearance: auto }
        .appearance-none { appearance: none }
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
        .aspect-ratio-auto { aspect-ratio: auto }
        .aspect-ratio-2 { aspect-ratio: 2 }
        .aspect-ratio-16\\/9 { aspect-ratio: 16/9 }
        """
    );
  }

  private void test(Class<?> type, String expected) {
    CssStyleSheetConfig config;
    config = new CssStyleSheetConfig();

    config.scanClass(type);

    config.execute();

    assertEquals(
        config.testUtilities(),

        expected
    );
  }

}