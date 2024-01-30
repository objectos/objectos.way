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
package objectos.args;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.way.Rmdir;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PathOptionTest extends AbstractArgsTest {

  private Path directory;

  @BeforeClass
  public void beforeClass() throws IOException {
    directory = Files.createTempDirectory("objectos-args-test-");
  }

  @AfterClass(alwaysRun = true)
  public void afterClass() throws IOException {
    Rmdir.rmdir(directory);
  }

  @Test(description = """
  Support the PathOption class
  """)
  public void testCase01() throws CommandLineException {
    PathOption option;
    option = new PathOption("--class-output");

    parse(option, args("--class-output", directory.toString()));

    assertEquals(option.get(), directory);
  }

  @Test(description = """
  Support required
  """)
  public void testCase02() throws CommandLineException {
    PathOption option;
    option = new PathOption("--class-output");
    option.required();

    parse(option, args("--class-output", directory.toString()));

    assertEquals(option.get(), directory);

    try {
      parse(option, args("--class-output"));

      Assert.fail();
    } catch (CommandLineException e) {
      assertMessage(e, """
      Missing required option: --class-output
      """);
    }
  }

  @Test(description = """
  Support custom validator
  """)
  public void testCase04() throws CommandLineException {
    PathOption option;
    option = new PathOption("--class-output");
    option.required();
    option.validator(Files::isDirectory, "must be an existing directory");

    try {
      parse(option, args("--class-output", "i-do-not-exist"));

      Assert.fail();
    } catch (CommandLineException e) {
      assertMessage(e, """
      Invalid --class-output value: must be an existing directory
      """);
    }
  }

  @Test(description = """
  Support requiredIf
  """)
  public void testCase05() throws CommandLineException {
    EnumOption<Mode> modeOption;
    modeOption = new EnumOption<>(Mode.class, "--mode");

    PathOption option;
    option = new PathOption("--class-output");
    option.activator(() -> modeOption.is(Mode.DEV));
    option.required();
    option.validator(Files::isDirectory, "must be an existing directory");

    parse(option, args());

    modeOption.set(Mode.DEV);

    try {
      parse(option, args());

      Assert.fail();
    } catch (CommandLineException e) {
      assertMessage(e, """
      Missing required option: --class-output
      """);
    }
  }

}
