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

import br.com.objectos.code.java.type.NamedPrimitive;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PPrimitiveTypeTest extends AbstractPTypeMirrorTest {

  @Test
  public void getName() {
    PTypeMirror booleanValue;
    booleanValue = getReturnType(Subject.class, "booleanValue");

    assertEquals(
        booleanValue.getName(),
        NamedPrimitive.BOOLEAN
    );

    PTypeMirror intValue;
    intValue = getReturnType(Subject.class, "intValue");

    assertEquals(
        intValue.getName(),
        NamedPrimitive.INT
    );
  }

  @Test
  public void isInstanceOf() {
    PTypeMirror intValue;
    intValue = getReturnType(Subject.class, "intValue");

    assertTrue(intValue.isInstanceOf(int.class));
    assertFalse(intValue.isInstanceOf(Object.class));
    assertFalse(intValue.isInstanceOf(int[].class));
    assertFalse(intValue.isInstanceOf(Integer.class));
  }

  @Test
  public void isPrimitiveType() {
    PTypeMirror booleanValue;
    booleanValue = getReturnType(Subject.class, "booleanValue");

    assertTrue(booleanValue.isPrimitiveType());

    PTypeMirror notOfType;
    notOfType = getReturnType(Subject.class, "notOfType");

    assertFalse(notOfType.isPrimitiveType());
  }

  @Test
  public void toPrimitiveType() {
    PTypeMirror intValue;
    intValue = getReturnType(Subject.class, "intValue");

    assertNotNull(intValue.toPrimitiveType());

    PTypeMirror notOfType;
    notOfType = getReturnType(Subject.class, "notOfType");

    try {
      notOfType.toPrimitiveType();
      Assert.fail();
    } catch (AssertionError expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.contains("See isPrimitiveType()"));
    }
  }

  private abstract static class Subject {

    abstract boolean booleanValue();
    abstract int intValue();
    abstract Object notOfType();

  }

}
