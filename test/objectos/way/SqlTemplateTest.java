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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.util.List;
import objectos.way.SqlTemplate.Fragment;
import org.testng.annotations.Test;

public class SqlTemplateTest {

  @Test(description = """
  parse
  - happy path
  - frag0: no placeholders
  - frag1: 1 placeholder
  """)
  public void parse01() {
    final SqlTemplate tmpl;
    tmpl = SqlTemplate.parse("""
    select A, B, C from FOO
    --
    where X = ?
    """);

    final List<Fragment> fragments;
    fragments = tmpl.fragments;

    assertEquals(fragments.size(), 2);

    final Fragment frag0;
    frag0 = fragments.get(0);

    assertEquals(frag0.value, "select A, B, C from FOO\n");
    assertEquals(frag0.placeholders, 0);

    final Fragment frag1;
    frag1 = fragments.get(1);

    assertEquals(frag1.value, "where X = ?\n");
    assertEquals(frag1.placeholders, 1);
  }

  @Test(description = """
  parse
  - happy path
  - frag0: 1 placeholder
  - frag1: 1 placeholder
  """)
  public void parse02() {
    final SqlTemplate tmpl;
    tmpl = SqlTemplate.parse("""
    select A, B, C from FOO
    where X = ?
    --
    and Y = ?
    """
    );

    final List<Fragment> fragments;
    fragments = tmpl.fragments;

    assertEquals(fragments.size(), 2);

    final Fragment frag0;
    frag0 = fragments.get(0);

    assertEquals(frag0.value, "select A, B, C from FOO\nwhere X = ?\n");
    assertEquals(frag0.placeholders, 1);

    final Fragment frag1;
    frag1 = fragments.get(1);

    assertEquals(frag1.value, "and Y = ?\n");
    assertEquals(frag1.placeholders, 1);
  }

}