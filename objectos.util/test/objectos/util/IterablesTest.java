/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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
package objectos.util;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.testng.annotations.Test;

public class IterablesTest {

  @Test
  public void equals() {
    Set<String> a = linkedSet("a", "b", "c");

    List<String> b = Arrays.asList("a", "b", "c");

    Set<String> c = linkedSet("c", "b", "a");

    List<String> d = Arrays.asList("c", "b", "a");

    assertTrue(Iterables.equals(a, b));
    assertTrue(Iterables.equals(b, a));

    assertTrue(Iterables.equals(c, d));
    assertTrue(Iterables.equals(d, c));

    assertFalse(Iterables.equals(a, c));
    assertFalse(Iterables.equals(c, a));

    assertFalse(Iterables.equals(b, d));
    assertFalse(Iterables.equals(d, b));

    assertFalse(Iterables.equals(a, null));
    assertFalse(Iterables.equals(null, a));

    assertTrue(Iterables.equals(null, null));
  }

  private Set<String> linkedSet(String... s) {
    List<String> list;
    list = Arrays.asList(s);

    return new LinkedHashSet<String>(list);
  }

}