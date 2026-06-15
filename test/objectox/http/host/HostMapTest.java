/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http.host;

import static org.testng.Assert.assertEquals;

import objectox.http.handler.HandlerNoop;
import org.testng.annotations.Test;

public class HostMapTest {

  private Host named(String name) {
    return HostY.create(opts -> {
      opts.handler = HandlerNoop.INSTANCE;

      opts.name = name;
    });
  }

  @Test
  public void testCase00() {
    final HostMap map;
    map = HostMapY.of();

    assertEquals(map.get("main.localhost"), null);
    assertEquals(map.get("local.localhost"), null);
    assertEquals(map.get("other.localhost"), null);
  }

  @Test
  public void testCase01() {
    final Host main;
    main = named("main.localhost");

    final HostMap map;
    map = HostMapY.of(main);

    assertEquals(map.get("main.localhost"), main);
    assertEquals(map.get("local.localhost"), null);
    assertEquals(map.get("other.localhost"), null);
  }

  @Test
  public void testCase02() {
    final Host main;
    main = named("main.localhost");

    final Host local;
    local = named("local.localhost");

    final HostMap map;
    map = HostMapY.of(main, local);

    assertEquals(map.get("main.localhost"), main);
    assertEquals(map.get("local.localhost"), local);
    assertEquals(map.get("other.localhost"), null);
  }

  @Test
  public void testCase03() {
    final Host main;
    main = named("main.localhost");

    final Host local;
    local = named("local.localhost");

    final Host other;
    other = named("other.localhost");

    final HostMap map;
    map = HostMapY.of(main, local, other);

    assertEquals(map.get("main.localhost"), main);
    assertEquals(map.get("local.localhost"), local);
    assertEquals(map.get("other.localhost"), other);
  }

}
