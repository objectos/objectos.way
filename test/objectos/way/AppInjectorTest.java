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

import java.util.function.Consumer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AppInjectorTest {

  @Test(description = "happy path: register and get")
  public void class01() {
    final App.Injector injector;
    injector = App.Injector.create(opts -> {
      opts.putInstance(String.class, "ABC");

      assertEquals(opts.getInstance(String.class), "ABC");
    });

    assertEquals(injector.getInstance(String.class), "ABC");
  }

  @Test(description = "disallow mapping overwrite")
  public void class02() {
    try {
      App.Injector.create(opts -> {
        opts.putInstance(String.class, "ABC");

        opts.putInstance(String.class, "Must throw");
      });

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "class java.lang.String is already mapped to ABC");
    }
  }

  @Test(description = "Query for unknown key should throw")
  public void class03() {
    try {
      App.Injector.create(opts -> {
        opts.getInstance(String.class);
      });

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "No mappings were found for class java.lang.String");
    }
  }

  private final Consumer<Object> noop = o -> {};

  @Test(description = "Query for unknown key should throw")
  public void class04() {
    try {
      final App.Injector injector;
      injector = App.Injector.create(noop);

      injector.getInstance(String.class);

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "No mappings were found for class java.lang.String");
    }
  }

  private static final Lang.Key<String> STRING_A = Lang.Key.of("A");

  private static final Lang.Key<String> STRING_B = Lang.Key.of("B");

  @Test
  public void key01() {
    final App.Injector injector;
    injector = App.Injector.create(opts -> {
      opts.putInstance(STRING_A, "ABC");

      assertEquals(opts.getInstance(STRING_A), "ABC");
    });

    assertEquals(injector.getInstance(STRING_A), "ABC");
  }

  @Test
  public void key02() {
    try {
      App.Injector.create(opts -> {
        opts.putInstance(STRING_A, "ABC");

        opts.putInstance(STRING_A, "Must throw");
      });

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "LangKey[unique=A] is already mapped to ABC");
    }
  }

  @Test
  public void key03() {
    try {
      App.Injector.create(opts -> {
        opts.putInstance(STRING_A, "ABC");

        opts.getInstance(STRING_B);
      });

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "No mappings were found for LangKey[unique=B]");
    }
  }

  @Test
  public void key04() {
    try {
      final App.Injector injector;
      injector = App.Injector.create(opts -> {
        opts.putInstance(STRING_A, "ABC");
      });

      injector.getInstance(STRING_B);

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "No mappings were found for LangKey[unique=B]");
    }
  }

}