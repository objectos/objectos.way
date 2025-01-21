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

import org.testng.Assert;
import org.testng.annotations.Test;

public class AppInjectorTest {

  @Test(description = "happy path: register and get")
  public void testCase01() {
    final App.Injector.Builder ctx;
    ctx = App.Injector.Builder.create();

    ctx.putInstance(String.class, "ABC");

    assertEquals(ctx.getInstance(String.class), "ABC");

    final App.Injector injector;
    injector = ctx.build();

    assertEquals(injector.getInstance(String.class), "ABC");
  }

  @Test(description = "disallow mapping overwrite")
  public void testCase02() {
    final App.Injector.Builder ctx;
    ctx = App.Injector.Builder.create();

    ctx.putInstance(String.class, "ABC");

    try {
      ctx.putInstance(String.class, "Must throw");

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "class java.lang.String is already mapped to ABC");
    }
  }

  @Test(description = "Query for unknown key should throw")
  public void testCase03() {
    final App.Injector.Builder ctx;
    ctx = App.Injector.Builder.create();

    try {
      ctx.getInstance(String.class);

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "No mappings were found for class java.lang.String");
    }
  }

  @Test(description = "Query for unknown key should throw")
  public void testCase04() {
    final App.Injector.Builder ctx;
    ctx = App.Injector.Builder.create();

    final App.Injector injector;
    injector = ctx.build();

    try {
      injector.getInstance(String.class);

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "No mappings were found for class java.lang.String");
    }
  }

}