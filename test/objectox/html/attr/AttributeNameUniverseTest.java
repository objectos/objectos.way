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
package objectox.html.attr;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;

import org.testng.annotations.Test;

public class AttributeNameUniverseTest {

  @Test(description = "getOrCreate stores a new name with index 0")
  public void getOrCreate01() {
    final AttributeNameUniverse subject;
    subject = new AttributeNameUniverse();

    final AttributeNamePojo id;
    id = subject.getOrCreate("id");

    assertEquals(id.index(), 0);
    assertEquals(id.name(), "id");
  }

  @Test(description = "indexes are assigned sequentially")
  public void getOrCreate02() {
    final AttributeNameUniverse subject;
    subject = new AttributeNameUniverse();

    final AttributeNamePojo id;
    id = subject.getOrCreate("id");

    final AttributeNamePojo class_;
    class_ = subject.getOrCreate("class");

    assertEquals(id.index(), 0);
    assertEquals(class_.index(), 1);
  }

  @Test(description = "a name already stored is returned as the same instance")
  public void getOrCreate_returnsSameInstance() {
    final AttributeNameUniverse subject;
    subject = new AttributeNameUniverse();

    final AttributeNamePojo id;
    id = subject.getOrCreate("id");

    final AttributeNamePojo again;
    again = subject.getOrCreate("id");

    assertSame(again, id);
  }

  @Test(description = "get returns the instance stored at the given array index")
  public void get_returnsInstanceByIndex() {
    final AttributeNameUniverse subject;
    subject = new AttributeNameUniverse();

    final AttributeNamePojo id;
    id = subject.getOrCreate("id");

    final AttributeNamePojo class_;
    class_ = subject.getOrCreate("class");

    final AttributeNamePojo onclick;
    onclick = subject.getOrCreate("onclick");

    assertSame(subject.get(0), id);
    assertSame(subject.get(1), class_);
    assertSame(subject.get(2), onclick);
  }

  @Test(description = "create always creates a new instance, even for a duplicate name")
  public void create_returnsNewInstanceEachTime() {
    final AttributeNameUniverse subject;
    subject = new AttributeNameUniverse();

    final AttributeName first;
    first = subject.create("id");

    final AttributeName second;
    second = subject.create("id");

    assertNotSame(second, first);

    assertEquals(first.index(), 0);
    assertEquals(second.index(), 1);
  }

  @Test(description = "create stores the instance so it is available via get and getOrCreate")
  public void create_storesInstance() {
    final AttributeNameUniverse subject;
    subject = new AttributeNameUniverse();

    final AttributeName created;
    created = subject.create("onclick");

    assertEquals(created.name(), "onclick");
    assertEquals(created.index(), 0);

    assertSame(subject.get(0), created);
    assertSame(subject.getOrCreate("onclick"), created);
  }

  @Test(
      description = "an empty name is rejected",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid attribute name: it must not be empty")
  public void create_invalidName() {
    final AttributeNameUniverse subject;
    subject = new AttributeNameUniverse();

    subject.create("");
  }

  @Test(
      description = "a null name is rejected",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "name == null")
  public void create_nullName() {
    final AttributeNameUniverse subject;
    subject = new AttributeNameUniverse();

    subject.create(null);
  }

  @Test(
      description = "a name not starting with a letter is rejected",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid attribute name: it must start with a letter")
  public void getOrCreate_invalidName() {
    final AttributeNameUniverse subject;
    subject = new AttributeNameUniverse();

    subject.getOrCreate("9onclick");
  }

  @Test(
      description = "a null name is rejected",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "name == null")
  public void getOrCreate_nullName() {
    final AttributeNameUniverse subject;
    subject = new AttributeNameUniverse();

    subject.getOrCreate(null);
  }

}
