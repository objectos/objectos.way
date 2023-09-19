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
package objectos.lang;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

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
  public void objects() {
    assertTrue(
      Equals.of(
        null, null
      )
    );

    assertTrue(
      Equals.of(
        a, a
      )
    );
    assertTrue(
      Equals.of(
        a, b
      )
    );
    assertTrue(
      Equals.of(
        b, a
      )
    );

    assertTrue(
      Equals.of(
        null, null,
        null, null
      )
    );

    assertTrue(
      Equals.of(
        a, a,
        a, a
      )
    );
    assertTrue(
      Equals.of(
        a, a,
        b, b
      )
    );
    assertTrue(
      Equals.of(
        b, b,
        c, c
      )
    );
    assertTrue(
      Equals.of(
        a, b,
        c, d
      )
    );
    assertTrue(
      Equals.of(
        b, a,
        d, c
      )
    );

    assertFalse(
      Equals.of(
        a, null
      )
    );
    assertFalse(
      Equals.of(
        null, a
      )
    );
    assertFalse(
      Equals.of(
        a, c
      )
    );
    assertFalse(
      Equals.of(
        a, c,
        b, d
      )
    );
    assertFalse(
      Equals.of(
        a, b,
        c, null
      )
    );
  }

}