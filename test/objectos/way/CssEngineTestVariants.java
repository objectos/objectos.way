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