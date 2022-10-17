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
package br.com.objectos.code.processing.type;

import static br.com.objectos.code.java.type.NamedTypes.t;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.type.NamedClass;
import objectos.util.UnmodifiableList;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PDeclaredTypeTest extends AbstractPTypeMirrorTest {

  @Test
  public void getName() {
    PTypeMirror a;
    a = getReturnType(Subject.class, "a");

    assertEquals(
        a.getName(),
        NamedClass.of(A.class)
    );

    PTypeMirror param1;
    param1 = getReturnType(Subject.class, "param1");

    assertEquals(
        param1.getName(),
        t(NamedClass.of(Param1.class), NamedClass.of(A.class))
    );
  }

  @Test
  public void getTypeArguments() {
    PTypeMirror _a;
    _a = getReturnType(Subject.class, "a");

    PDeclaredType a;
    a = _a.toDeclaredType();

    UnmodifiableList<PTypeMirror> aArgs;
    aArgs = a.getTypeArguments();

    assertEquals(aArgs.size(), 0);

    PTypeMirror _param1;
    _param1 = getReturnType(Subject.class, "param1");

    PDeclaredType param1;
    param1 = _param1.toDeclaredType();

    UnmodifiableList<PTypeMirror> param1Args;
    param1Args = param1.getTypeArguments();

    assertEquals(param1Args.size(), 1);
    assertEquals(param1Args.get(0).getName(), NamedClass.of(A.class));
  }

  @Test
  public void isDeclaredType() {
    PTypeMirror a;
    a = getReturnType(Subject.class, "a");

    assertTrue(a.isDeclaredType());

    PTypeMirror param1;
    param1 = getReturnType(Subject.class, "param1");

    assertTrue(param1.isDeclaredType());

    PTypeMirror notOfType;
    notOfType = getReturnType(Subject.class, "notOfType");

    assertFalse(notOfType.isDeclaredType());
  }

  @Test
  public void isInstanceOf() {
    PTypeMirror a;
    a = getReturnType(Subject.class, "a");

    assertTrue(a.isInstanceOf(A.class));
    assertTrue(a.isInstanceOf(Object.class));
    assertFalse(a.isInstanceOf(A[].class));
    assertFalse(a.isInstanceOf(Integer.class));

    PTypeMirror param1;
    param1 = getReturnType(Subject.class, "param1");

    assertTrue(param1.isInstanceOf(Param1.class));
    assertTrue(param1.isInstanceOf(Object.class));
    assertFalse(param1.isInstanceOf(Param1[].class));
    assertFalse(param1.isInstanceOf(A.class));
  }

  @Test
  public void toDeclaredType() {
    PTypeMirror a;
    a = getReturnType(Subject.class, "a");

    PDeclaredType resultA;
    resultA = a.toDeclaredType();

    assertNotNull(resultA);

    PTypeMirror param1;
    param1 = getReturnType(Subject.class, "param1");

    PDeclaredType resultParam1;
    resultParam1 = param1.toDeclaredType();

    assertNotNull(resultParam1);

    PTypeMirror notOfType;
    notOfType = getReturnType(Subject.class, "notOfType");

    try {
      notOfType.toDeclaredType();

      Assert.fail();
    } catch (AssertionError expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.contains("See isDeclaredType()"));
    }
  }

  @Test
  public void toProcessingType() {
    PDeclaredType a;
    a = getClassOrParameterizedType(Subject.class, "a");

    assertEquals(a.toProcessingType(), query(A.class));
  }

  private PDeclaredType getClassOrParameterizedType(
      Class<?> type, String methodName) {
    PTypeMirror returnType;
    returnType = getReturnType(type, methodName);

    return returnType.toDeclaredType();
  }

  private interface A {}

  private interface Param1<T> {}

  private abstract static class Subject<X> {

    abstract A a();

    abstract A[] notOfType();

    abstract Param1<A> param1();

  }

}
