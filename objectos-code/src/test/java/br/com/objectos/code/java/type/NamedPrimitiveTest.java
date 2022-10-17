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

import static br.com.objectos.code.java.type.NamedTypes._int;
import static org.testng.Assert.assertEquals;

import br.com.objectos.code.java.io.ImportSet;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class NamedPrimitiveTest extends AbstractCodeJavaTest {

  @Test(description = "A NamedPrimitive should never generate an import declaration.")
  public void acceptJavaFileImportSet() {
    ImportSet set;
    set = ImportSet.forPackage(TESTING_CODE);

    assertEquals(set.get(_int()), "int");
    test(
        set,
        "package testing.code;"
    );
  }

  @Test
  public void getArrayCreationExpressionName() {
    NamedType res;
    res = _int().getArrayCreationExpressionName();

    assertEquals(res, _int());
  }

  @Test
  public void toNamedArray() {
    NamedArray a1 = _int().toNamedArray();
    NamedArray a2 = a1.toNamedArray();
    NamedArray a3 = a2.toNamedArray();

    test(a1, "int[]");
    test(a2, "int[][]");
    test(a3, "int[][][]");
  }

  @Test
  public void toStringTest() {
    test(NamedPrimitive._boolean(), "boolean");
    test(NamedPrimitive._byte(), "byte");
    test(NamedPrimitive._short(), "short");
    test(NamedPrimitive._int(), "int");
    test(NamedPrimitive._long(), "long");
    test(NamedPrimitive._char(), "char");
    test(NamedPrimitive._float(), "float");
    test(NamedPrimitive._double(), "double");
  }

  @Test
  public void toWrapperClass() {
    testWrapper(NamedPrimitive._boolean(), NamedClass.of(Boolean.class));
    testWrapper(NamedPrimitive._byte(), NamedClass.of(Byte.class));
    testWrapper(NamedPrimitive._short(), NamedClass.of(Short.class));
    testWrapper(NamedPrimitive._int(), NamedClass.of(Integer.class));
    testWrapper(NamedPrimitive._long(), NamedClass.of(Long.class));
    testWrapper(NamedPrimitive._char(), NamedClass.of(Character.class));
    testWrapper(NamedPrimitive._float(), NamedClass.of(Float.class));
    testWrapper(NamedPrimitive._double(), NamedClass.of(Double.class));
  }

  private void testWrapper(NamedPrimitive name, NamedClass expected) {
    assertEquals(name.toWrapperClass(), expected);
  }

}