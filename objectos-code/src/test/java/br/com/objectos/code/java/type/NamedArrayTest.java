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
import static br.com.objectos.code.java.type.NamedTypes._long;
import static br.com.objectos.code.java.type.NamedTypes.a;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static org.testng.Assert.assertEquals;

import br.com.objectos.code.java.expression.MethodReferenceReferenceExpression;
import br.com.objectos.code.java.expression.MethodReferenceReferenceExpressionTest;
import br.com.objectos.code.java.io.ImportSet;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.concurrent.Future;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class NamedArrayTest extends AbstractCodeJavaTest
    implements
    MethodReferenceReferenceExpressionTest.Adapter {

  @Test
  public void getArrayCreationExpressionName() {
    NamedArray a1 = NamedArray.of(_int());
    NamedArray a2 = NamedArray.of(a1);
    NamedArray a3 = NamedArray.of(a2);
    assertEquals(a1.getArrayCreationExpressionName(), _int());
    assertEquals(a2.getArrayCreationExpressionName(), _int());
    assertEquals(a3.getArrayCreationExpressionName(), _int());
  }

  @Test
  public void getComponentName() {
    NamedClass object;
    object = NamedClass.object();

    NamedArray o1;
    o1 = NamedArray.of(object);

    assertEquals(o1.getComponent(), object);

    NamedArray o2;
    o2 = NamedArray.of(o1);

    assertEquals(o2.getComponent(), o1);

    NamedArray o3;
    o3 = NamedArray.of(o2);

    assertEquals(o3.getComponent(), o2);
  }

  @Test
  public void getDeepComponentName() {
    NamedClass object;
    object = NamedClass.object();

    NamedArray o1;
    o1 = NamedArray.of(object);

    assertEquals(o1.getDeepComponent(), object);

    NamedArray o2;
    o2 = NamedArray.of(o1);

    assertEquals(o2.getDeepComponent(), object);

    NamedArray o3;
    o3 = NamedArray.of(o2);

    assertEquals(o3.getDeepComponent(), object);
  }

  @Override
  public final MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return NamedArray.of(_long());
  }

  @Test
  public void of() {
    NamedArray a1 = NamedArray.of(_int());
    NamedArray a2 = NamedArray.of(a1);
    NamedArray a3 = NamedArray.of(a2);
    test(a1, "int[]");
    test(a2, "int[][]");
    test(a3, "int[][][]");
  }

  @Test
  public void primitiveImportSetTest() {
    ImportSet set = ImportSet.forPackage(TESTING_CODE);
    assertEquals(set.get(a(_int())), "int[]");
    assertEquals(set.get(a(a(_int()))), "int[][]");
    assertEquals(set.get(a(a(a(_int())))), "int[][][]");
    testToString(
        set,
        "package testing.code;");
  }

  @Test
  public void printVarArgsSymbol() {
    NamedArray a1 = NamedArray.of(_int());
    NamedArray a2 = NamedArray.of(a1);
    NamedArray a3 = NamedArray.of(a2);
    assertEquals(a1.printVarArgsSymbol().toString(), "...");
    assertEquals(a2.printVarArgsSymbol().toString(), "[]...");
    assertEquals(a3.printVarArgsSymbol().toString(), "[][]...");
  }

  @Test
  public void referenceImportSetTest() {
    NamedClass fut = t(Future.class);
    ImportSet set = ImportSet.forPackage(TESTING_CODE);
    assertEquals(set.get(a(fut)), "Future[]");
    assertEquals(set.get(a(a(fut))), "Future[][]");
    assertEquals(set.get(a(a(a(fut)))), "Future[][][]");
    testToString(
        set,
        "package testing.code;",
        "",
        "import java.util.concurrent.Future;");
  }

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        MethodReferenceReferenceExpressionTest.with(this)
    };
  }

  @Test
  public void toNamedArray() {
    NamedArray a1 = NamedArray.of(_int());
    NamedArray a2 = NamedArray.of(a1);
    NamedArray a3 = NamedArray.of(a2);

    assertEquals(a1.toNamedArray(), a2);
    assertEquals(a2.toNamedArray(), a3);
  }

}