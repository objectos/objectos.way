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

import java.util.Set;
import java.util.function.Function;
import org.testng.annotations.Test;

public class SetOptionTest extends AbstractArgsTest {

  @Test(description = """
  Support the SetOption class
  """)
  public void testCase01() throws CommandLineException {
    SetOption<String> option;
    option = cli.newSetOption("--foo", Function.identity());

    parse(option, args("--foo", "abc", "--bar", "xyz", "--foo", "123"));

    assertEquals(option.get(), Set.of("abc", "123"));
  }

}