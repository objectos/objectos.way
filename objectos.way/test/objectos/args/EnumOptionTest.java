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

public class EnumOptionTest extends AbstractArgsTest {

  @Test(description = """
  Support the EnumOption class
  """)
  public void testCase01() throws CommandLineException {
    EnumOption<Mode> option;
    option = new EnumOption<>(Mode.class, "--mode");

    parse(option, args("--mode", "DEV"));

    assertEquals(option.get(), Mode.DEV);
  }

  @Test(description = """
  Support required
  """)
  public void testCase02() throws CommandLineException {
    EnumOption<Mode> option;
    option = new EnumOption<>(Mode.class, "--mode");
    option.required();

    parse(option, args("--mode", "PROD"));

    assertEquals(option.get(), Mode.PROD);

    try {
      parse(option, args("--mode"));

      Assert.fail();
    } catch (CommandLineException e) {
      assertMessage(e, """
      Missing required option: --mode
      """);
    }
  }

}