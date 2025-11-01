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
import java.util.Map;
import org.testng.annotations.Test;

public class CssEngineTest00Marker {

  @Test(description = "value w/ 1 prop")
  public void testCase01() {
    var target = CssEngine.decl("--target", "x");
    var decl = CssEngine.decl("--src", "var(--target)");

    assertEquals(target.marked(), false);
    assertEquals(decl.marked(), false);

    final Map<String, CssEngine.Decl> properties;
    properties = Map.of("--target", target);

    final CssEngine.Marker marker;
    marker = new CssEngine.Marker(properties);

    marker.accept(decl);

    assertEquals(target.marked(), true);
    assertEquals(decl.marked(), true);
  }

  @Test(description = "value w/ 2 props")
  public void testCase02() {
    var target = CssEngine.decl("--target", "x");
    var decl = CssEngine.decl("--src", "var(--ignore, var(--target)");

    assertEquals(target.marked(), false);
    assertEquals(decl.marked(), false);

    final Map<String, CssEngine.Decl> properties;
    properties = Map.of("--target", target);

    final CssEngine.Marker marker;
    marker = new CssEngine.Marker(properties);

    marker.accept(decl);

    assertEquals(target.marked(), true);
    assertEquals(decl.marked(), true);
  }

  @Test(description = "value w/ no props")
  public void testCase03() {
    var target = CssEngine.decl("--target", "x");
    var decl = CssEngine.decl("--src", "10px");

    assertEquals(target.marked(), false);
    assertEquals(decl.marked(), false);

    final Map<String, CssEngine.Decl> properties;
    properties = Map.of("--target", target);

    final CssEngine.Marker marker;
    marker = new CssEngine.Marker(properties);

    marker.accept(decl);

    assertEquals(target.marked(), false);
    assertEquals(decl.marked(), true);
  }

}