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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.type.NamedTypeVariable;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PTypeVariableTest extends AbstractPTypeMirrorTest {

  @Test
  public void getName() {
    PTypeMirror a;
    a = getReturnType(Subject.class, "a");

    assertEquals(
        a.getName(),
        NamedTypeVariable.of("A")
    );
  }

  @Test
  public void isInstanceOf() {
    PTypeMirror a;
    a = getReturnType(Subject.class, "a");

    assertTrue(a.isInstanceOf(Number.class));
    assertTrue(a.isInstanceOf(Object.class));
    assertFalse(a.isInstanceOf(Number[].class));
    assertFalse(a.isInstanceOf(Integer.class));

    PTypeMirror e = getReturnType(Subject.class, "e");

    assertTrue(e.isInstanceOf(Object.class));
    assertFalse(e.isInstanceOf(Object[].class));
    assertFalse(e.isInstanceOf(Integer.class));
  }

  @Test
  public void isTypeVariable() {
    PTypeMirror a;
    a = getReturnType(Subject.class, "a");

    assertTrue(a.isTypeVariable());

    PTypeMirror notOfType;
    notOfType = getReturnType(Subject.class, "notOfType");

    assertFalse(notOfType.isTypeVariable());
  }

  @Test
  public void toTypeVariable() {
    PTypeMirror a;
    a = getReturnType(Subject.class, "a");

    PTypeVariable resultA;
    resultA = a.toTypeVariable();

    assertNotNull(resultA);

    PTypeMirror notOfType;
    notOfType = getReturnType(Subject.class, "notOfType");

    try {
      notOfType.toTypeVariable();
      Assert.fail();
    } catch (AssertionError expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.contains("See isTypeVariable()"));
    }
  }

  private abstract static class Subject<A extends Number> {

    abstract A a();
    abstract <E> E e();
    abstract void notOfType();

  }

}
