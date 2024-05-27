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

public class IntegerOptionTest extends AbstractArgsTest {

  @Test(description = """
  Support the IntegerOption class
  """)
  public void testCase01() throws CommandLineException {
    IntegerOption option;
    option = cli.newIntegerOption("--port");

    parse(option, args("--port", "1234"));

    assertEquals(option.get(), 1234);
  }

  @Test(description = """
  Support required
  """)
  public void testCase02() throws CommandLineException {
    IntegerOption option;
    option = cli.newIntegerOption("--port");
    option.required();

    parse(option, args("--port", "1234"));

    assertEquals(option.get(), 1234);

    try {
      parse(option, args("--port"));

      Assert.fail();
    } catch (CommandLineException e) {
      assertMessage(e, """
      Missing required option: --port
      """);
    }
  }

  @Test(description = """
  Support default value
  """)
  public void testCase03() throws CommandLineException {
    IntegerOption option;
    option = cli.newIntegerOption("--port");
    option.required();
    option.set(1234);

    parse(option, args());

    assertEquals(option.get(), 1234);
  }

  @Test(description = """
  Support custom validator
  """)
  public void testCase04() throws CommandLineException {
    IntegerOption option;
    option = cli.newIntegerOption("--port");
    option.required();
    option.set(1234);
    option.validator(this::validatePort, "valid range is 1024 < port < 65555");

    try {
      parse(option, args("--port", "123"));

      Assert.fail();
    } catch (CommandLineException e) {
      assertMessage(e, """
      Invalid --port value: valid range is 1024 < port < 65555
      """);
    }
  }

  private boolean validatePort(Integer port) {
    int value;
    value = port.intValue();

    return 1024 < value && value < 65555;
  }

}