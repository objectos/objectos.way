/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
package objectos.lang;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.testng.annotations.Test;

public class EqualsTest {

  private final Object a = UUID.randomUUID();

  private final Object b = UUID.fromString(a.toString());

  private final Object c = UUID.randomUUID();

  private final Object d = UUID.fromString(c.toString());

  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void componentInt() {
    ComponentInt a;
    a = new ComponentInt(0);

    ComponentInt b;
    b = new ComponentInt(0);

    ComponentInt c;
    c = new ComponentInt(1);

    ComponentInt d;
    d = new ComponentInt(1);

    assertTrue(a.equals(b));
    assertTrue(b.equals(a));

    assertTrue(c.equals(d));
    assertTrue(d.equals(c));

    assertFalse(a.equals(c));
    assertFalse(c.equals(a));

    assertFalse(b.equals(d));
    assertFalse(d.equals(b));

    assertFalse(a.equals(null));

    assertFalse(a.equals("0"));
  }

  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void componentString() {
    ComponentString a;
    a = new ComponentString("ABC");

    ComponentString b;
    b = new ComponentString(new String("ABC"));

    ComponentString c;
    c = new ComponentString("123");

    ComponentString d;
    d = new ComponentString(new String("123"));

    assertTrue(a.equals(b));
    assertTrue(b.equals(a));

    assertTrue(c.equals(d));
    assertTrue(d.equals(c));

    assertFalse(a.equals(c));
    assertFalse(c.equals(a));

    assertFalse(b.equals(d));
    assertFalse(d.equals(b));

    assertFalse(a.equals(null));

    assertFalse(a.equals("ABC"));
  }

  @Test
  public void iterables() {
    Set<String> a = linkedSet("a", "b", "c");

    List<String> b = Arrays.asList("a", "b", "c");

    Set<String> c = linkedSet("c", "b", "a");

    List<String> d = Arrays.asList("c", "b", "a");

    assertTrue(Equals.iterables(a, b));
    assertTrue(Equals.iterables(b, a));

    assertTrue(Equals.iterables(c, d));
    assertTrue(Equals.iterables(d, c));

    assertFalse(Equals.iterables(a, c));
    assertFalse(Equals.iterables(c, a));

    assertFalse(Equals.iterables(b, d));
    assertFalse(Equals.iterables(d, b));

    assertFalse(Equals.iterables(a, null));
    assertFalse(Equals.iterables(null, a));

    assertTrue(Equals.iterables(null, null));
  }

  @Test
  public void iterators() {
    Set<String> a = linkedSet("a", "b", "c");

    List<String> b = Arrays.asList("a", "b", "c");

    Set<String> c = linkedSet("c", "b", "a");

    List<String> d = Arrays.asList("c", "b", "a");

    assertTrue(Equals.iterators(a.iterator(), b.iterator()));
    assertTrue(Equals.iterators(b.iterator(), a.iterator()));

    assertTrue(Equals.iterators(c.iterator(), d.iterator()));
    assertTrue(Equals.iterators(d.iterator(), c.iterator()));

    assertFalse(Equals.iterators(a.iterator(), c.iterator()));
    assertFalse(Equals.iterators(c.iterator(), a.iterator()));

    assertFalse(Equals.iterators(b.iterator(), d.iterator()));
    assertFalse(Equals.iterators(d.iterator(), b.iterator()));

    assertFalse(Equals.iterators(a.iterator(), null));
    assertFalse(Equals.iterators(null, a.iterator()));

    assertTrue(Equals.iterators(null, null));
  }

  @Test
  public void objects() {
    assertTrue(
        Equals.objects(
            null, null
        )
    );

    assertTrue(
        Equals.objects(
            a, a
        )
    );
    assertTrue(
        Equals.objects(
            a, b
        )
    );
    assertTrue(
        Equals.objects(
            b, a
        )
    );

    assertTrue(
        Equals.objects(
            null, null,
            null, null
        )
    );

    assertTrue(
        Equals.objects(
            a, a,
            a, a
        )
    );
    assertTrue(
        Equals.objects(
            a, a,
            b, b
        )
    );
    assertTrue(
        Equals.objects(
            b, b,
            c, c
        )
    );
    assertTrue(
        Equals.objects(
            a, b,
            c, d
        )
    );
    assertTrue(
        Equals.objects(
            b, a,
            d, c
        )
    );

    assertFalse(
        Equals.objects(
            a, null
        )
    );
    assertFalse(
        Equals.objects(
            null, a
        )
    );
    assertFalse(
        Equals.objects(
            a, c
        )
    );
    assertFalse(
        Equals.objects(
            a, c,
            b, d
        )
    );
    assertFalse(
        Equals.objects(
            a, b,
            c, null
        )
    );
  }

  private Set<String> linkedSet(String... s) {
    List<String> list;
    list = Arrays.asList(s);

    return new LinkedHashSet<String>(list);
  }

}