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
package br.com.objectos.code.java.type;

import static br.com.objectos.code.java.type.NamedTypes.t;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.io.ImportSet;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.Collection;
import org.testng.annotations.Test;

public class NamedWildcardTest extends AbstractCodeJavaTest {

  @Test
  public void wildcardExtendsImportTest() {
    NamedWildcard res = NamedTypes.wildcardExtends(t(Collection.class));
    ImportSet set = ImportSet.forPackage(TESTING_CODE);
    assertEquals(set.get(res), "? extends Collection");
    testToString(
        set,
        "package testing.code;",
        "",
        "import java.util.Collection;");
  }

  @Test
  public void wildcardExtendsTest() {
    NamedClass number = NamedTypes.t(Number.class);
    NamedWildcard res = NamedTypes.wildcardExtends(number);
    assertTrue(res instanceof NamedWildcard.Extends);
    assertEquals(res, NamedTypes.wildcardExtends(number));
    assertNotEquals(res, NamedTypes.wildcard());
    assertNotEquals(res, NamedTypes.wildcardExtends(t(Integer.class)));
    assertEquals(res.toString(), "? extends java.lang.Number");
  }

  @Test
  public void wildcardSuperImportTest() {
    NamedWildcard res = NamedTypes.wildcardSuper(t(Collection.class));
    ImportSet set = ImportSet.forPackage(TESTING_CODE);
    assertEquals(set.get(res), "? super Collection");
    testToString(
        set,
        "package testing.code;",
        "",
        "import java.util.Collection;");
  }

  @Test
  public void wildcardSuperTest() {
    NamedClass number = NamedTypes.t(Number.class);
    NamedWildcard res = NamedTypes.wildcardSuper(number);
    assertTrue(res instanceof NamedWildcard.Super);
    assertEquals(res, NamedTypes.wildcardSuper(number));
    assertNotEquals(res, NamedTypes.wildcard());
    assertNotEquals(res, NamedTypes.wildcardSuper(t(Integer.class)));
    assertEquals(res.toString(), "? super java.lang.Number");
  }

  @Test
  public void wildcardUnboundedImportTest() {
    NamedWildcard res = NamedTypes.wildcard();
    ImportSet set = ImportSet.forPackage(TESTING_CODE);
    assertEquals(set.get(res), "?");
    testToString(
        set,
        "package testing.code;");
  }

  @Test
  public void wildcardUnboundedTest() {
    NamedWildcard res = NamedTypes.wildcard();
    assertTrue(res instanceof NamedWildcard.Unbound);
    assertTrue(res == NamedTypes.wildcard());
    assertEquals(res.toString(), "?");
  }

}
