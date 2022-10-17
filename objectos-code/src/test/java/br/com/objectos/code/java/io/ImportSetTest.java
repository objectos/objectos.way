/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.io;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.declaration.PackageName;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class ImportSetTest extends AbstractCodeJavaTest {

  @Test
  public void javaLang() {
    NamedClass override = NamedClass.of(Override.class);
    ImportSet set = ImportSet.forPackage(TESTING_CODE);
    assertEquals(set.get(override), "Override");
    testToString(
        set,
        "package testing.code;");
  }

  @Test
  public void noop() {
    ImportSet noop = ImportSet.forToString();
    String res = noop.get(TESTING_CODE.nestedClass("Subject"));
    assertEquals(res, "testing.code.Subject");
  }

  @Test
  public void otherPackage() {
    NamedClass a = TESTING_OTHER.nestedClass("A");
    NamedClass b = TESTING_OTHER.nestedClass("B");
    ImportSet set = ImportSet.forPackage(TESTING_CODE);
    assertEquals(set.get(a), "A");
    assertEquals(set.get(b), "B");
    assertEquals(set.size(), 2);
    assertTrue(set.contains(a));
    assertTrue(set.contains(b));
    testToString(
        set,
        "package testing.code;",
        "",
        "import testing.other.A;",
        "import testing.other.B;");
  }

  @Test
  public void samePackage() {
    NamedClass a = TESTING_CODE.nestedClass("A");
    NamedClass b = TESTING_CODE.nestedClass("B");
    ImportSet set = ImportSet.forPackage(TESTING_CODE);
    assertEquals(set.get(a), "A");
    assertEquals(set.get(b), "B");
    assertEquals(set.size(), 0);
    testToString(
        set,
        "package testing.code;");
  }

  @Test
  public void unnamedPackage() {
    NamedClass a = TESTING_OTHER.nestedClass("A");
    NamedClass b = TESTING_OTHER.nestedClass("B");
    ImportSet set = ImportSet.forPackage(PackageName.unnamed());
    assertEquals(set.get(a), "A");
    assertEquals(set.get(b), "B");
    testToString(
        set,
        "import testing.other.A;",
        "import testing.other.B;");
  }

}