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

import java.util.Set;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CssEngineTestScan {

  private final CssEngine engine = new CssEngine();

  @BeforeClass
  public void beforeClass() {
    engine.noteSink(TestingNoteSink.INSTANCE);

    engine.execute();

    engine.sourceName(getClass().getCanonicalName());
  }

  @Test(description = "single valid utility")
  public void processStringConstant01() {
    engine.processStringConstant("display:block");

    assertEquals(engine.testProcess(), Set.of("display:block"));
  }

  @Test(description = "space separated utilities")
  public void processStringConstant02() {
    engine.processStringConstant("flex:1 cursor:pointer font-weight:400");

    assertEquals(engine.testProcess(), Set.of("flex:1", "cursor:pointer", "font-weight:400"));
  }

  @Test(description = "trailing whitespace")
  public void processStringConstant03() {
    engine.processStringConstant("flex:2 ");

    assertEquals(engine.testProcess(), Set.of("flex:2"));
  }

  @Test(description = "text block")
  public void processStringConstant04() {
    engine.processStringConstant("""
    display:none gap:0
    cursor:not-allowed flex:1
    ignore-me
    z-index:100
    """);

    assertEquals(engine.testProcess(), Set.of("display:none", "gap:0", "cursor:not-allowed", "flex:1", "z-index:100"));
  }

  @Test(description = "edge cases")
  public void processStringConstant05() {
    engine.processStringConstant("md::2px");

    assertEquals(engine.testProcess(), Set.of("md::2px"));
  }

}