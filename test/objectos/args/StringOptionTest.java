/*
 * Copyright (C) 2024 Objectos Software LTDA.
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
package objectos.args;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StringOptionTest extends AbstractArgsTest {

  @Test(description = """
  Support the StringOption class
  """)
  public void testCase01() throws CommandLineException {
    StringOption option;
    option = new StringOption("--name");

    parse(option, args("--name", "foo"));

    assertEquals(option.get(), "foo");
  }

  @Test(description = """
  Support required
  """)
  public void testCase02() throws CommandLineException {
    StringOption option;
    option = new StringOption("--name");
    option.required();

    parse(option, args("--name", "foo"));

    assertEquals(option.get(), "foo");

    try {
      parse(option, args("--name"));

      Assert.fail();
    } catch (CommandLineException e) {
      assertMessage(e, """
      Missing required option: --name
      """);
    }
  }

  @Test(description = """
  Support default value
  """)
  public void testCase03() throws CommandLineException {
    StringOption option;
    option = new StringOption("--name");
    option.required();
    option.set("foo");

    parse(option, args());

    assertEquals(option.get(), "foo");
  }

  @Test(description = """
  Support custom validator
  """)
  public void testCase04() throws CommandLineException {
    StringOption option;
    option = new StringOption("--name");
    option.required();
    option.set("foo");
    option.validator(this::validateName, "name must be at least 10 characters long");

    try {
      parse(option, args("--name", "foo"));

      Assert.fail();
    } catch (CommandLineException e) {
      assertMessage(e, """
      Invalid --name value: name must be at least 10 characters long
      """);
    }
  }

  private boolean validateName(String name) {
    return name.length() > 10;
  }

}