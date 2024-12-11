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

import java.util.Set;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CssEngineTestScan {

  private final CssEngine engine = new CssEngine();

  @BeforeClass
  public void beforeClass() {
    engine.execute();

    engine.sourceName(getClass().getCanonicalName());
  }

  @Test(description = "single valid utility")
  public void processStringConstant01() {
    engine.processStringConstant("foo");

    assertEquals(engine.testProcess(), Set.of("foo"));
  }

  @Test(description = "space separated utilities")
  public void processStringConstant02() {
    engine.processStringConstant("foo bar foo");

    assertEquals(engine.testProcess(), Set.of("foo", "bar"));
  }

  @Test(description = "trailing whitespace")
  public void processStringConstant03() {
    engine.processStringConstant("foo ");

    assertEquals(engine.testProcess(), Set.of("foo"));
  }

  @Test(description = "text block")
  public void processStringConstant04() {
    engine.processStringConstant("""
    hey ya
    foo bar
    baz
    """);

    assertEquals(engine.testProcess(), Set.of("hey", "ya", "foo", "bar", "baz"));
  }

}