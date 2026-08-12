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
package objectox.html.elem;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;

import org.testng.annotations.Test;

public class ElementNameUniverseTest {

  @Test(description = "getOrCreate stores a new name with index 0")
  public void getOrCreate01() {
    final ElementNameUniverse subject;
    subject = new ElementNameUniverse();

    final ElementName div;
    div = subject.getOrCreate("div");

    assertEquals(div.index(), 0);
    assertEquals(div.name(), "div");
  }

  @Test(description = "indexes are assigned sequentially")
  public void getOrCreate02() {
    final ElementNameUniverse subject;
    subject = new ElementNameUniverse();

    final ElementName div;
    div = subject.getOrCreate("div");

    final ElementName span;
    span = subject.getOrCreate("span");

    assertEquals(div.index(), 0);
    assertEquals(span.index(), 1);
  }

  @Test(description = "a name already stored is returned as the same instance")
  public void getOrCreate_returnsSameInstance() {
    final ElementNameUniverse subject;
    subject = new ElementNameUniverse();

    final ElementName div;
    div = subject.getOrCreate("div");

    final ElementName again;
    again = subject.getOrCreate("div");

    assertSame(again, div);
  }

  @Test(description = "get returns the instance stored at the given array index")
  public void get_returnsInstanceByIndex() {
    final ElementNameUniverse subject;
    subject = new ElementNameUniverse();

    final ElementName div;
    div = subject.getOrCreate("div");

    final ElementName span;
    span = subject.getOrCreate("span");

    final ElementName a;
    a = subject.getOrCreate("a");

    assertSame(subject.get(0), div);
    assertSame(subject.get(1), span);
    assertSame(subject.get(2), a);
  }

  @Test(description = "create always creates a new instance, even for a duplicate name")
  public void create_returnsNewInstanceEachTime() {
    final ElementNameUniverse subject;
    subject = new ElementNameUniverse();

    final ElementName first;
    first = subject.create("div");

    final ElementName second;
    second = subject.create("div");

    assertNotSame(second, first);

    assertEquals(first.index(), 0);
    assertEquals(second.index(), 1);
  }

  @Test(description = "create stores the instance so it is available via get and getOrCreate")
  public void create_storesInstance() {
    final ElementNameUniverse subject;
    subject = new ElementNameUniverse();

    final ElementName created;
    created = subject.create("a");

    assertEquals(created.name(), "a");
    assertEquals(created.index(), 0);

    assertSame(subject.get(0), created);
    assertSame(subject.getOrCreate("a"), created);
  }

  @Test(
      description = "an empty name is rejected",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid element name: it must not be empty")
  public void create_invalidName() {
    final ElementNameUniverse subject;
    subject = new ElementNameUniverse();

    subject.create("");
  }

  @Test(
      description = "a null name is rejected",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "name == null")
  public void create_nullName() {
    final ElementNameUniverse subject;
    subject = new ElementNameUniverse();

    subject.create(null);
  }

  @Test(
      description = "a name not starting with a letter is rejected",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid element name: it must start with a letter")
  public void getOrCreate_invalidName() {
    final ElementNameUniverse subject;
    subject = new ElementNameUniverse();

    subject.getOrCreate("9div");
  }

  @Test(
      description = "a null name is rejected",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "name == null")
  public void getOrCreate_nullName() {
    final ElementNameUniverse subject;
    subject = new ElementNameUniverse();

    subject.getOrCreate(null);
  }

}
