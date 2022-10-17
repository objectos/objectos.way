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
package br.com.objectos.code.model.element;

import static br.com.objectos.code.java.declaration.Modifiers._final;
import static br.com.objectos.code.java.declaration.Modifiers._static;
import static br.com.objectos.code.java.declaration.Modifiers._transient;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.declaration.FieldModifier;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.model.AbstractCodeModelTest;
import br.com.objectos.code.processing.type.PTypeMirror;
import java.util.List;
import java.util.NoSuchElementException;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import objectos.util.UnmodifiableList;
import objectos.util.UnmodifiableSet;
import org.testng.annotations.Test;

public class ProcessingFieldTest extends AbstractCodeModelTest {

  @Test
  public void getDeclaringType() {
    ProcessingField a;
    a = field(Subject.class, "a");

    ProcessingType declaringType;
    declaringType = a.getDeclaringType();

    assertEquals(declaringType.getName(), NamedClass.of(Subject.class));
  }

  @Test
  public void getModifiers() {
    ProcessingField a = field(Subject.class, "a");
    ProcessingField b = field(Subject.class, "b");
    ProcessingField c = field(Subject.class, "c");

    UnmodifiableSet<FieldModifier> amod = a.getModifiers();
    UnmodifiableSet<FieldModifier> bmod = b.getModifiers();
    UnmodifiableSet<FieldModifier> cmod = c.getModifiers();

    assertEquals(amod.size(), 0);
    assertEquals(bmod.size(), 1);
    assertEquals(cmod.size(), 2);

    assertTrue(bmod.contains(_static()));
    assertTrue(cmod.contains(_final()));
    assertTrue(cmod.contains(_transient()));
  }

  @Test
  public void getName() {
    ProcessingType subject;
    subject = query(Subject.class);

    UnmodifiableList<ProcessingField> fields;
    fields = subject.getDeclaredFields();

    ProcessingField a = fields.get(0);
    ProcessingField b = fields.get(1);
    ProcessingField c = fields.get(2);

    assertEquals(a.getName(), "a");
    assertEquals(b.getName(), "b");
    assertEquals(c.getName(), "c");
  }

  @Test(description = ""
      + "getType() should return the correct TypeMirrorQuery")
  public void getType() {
    ProcessingField a = field(Subject.class, "a");
    ProcessingField b = field(Subject.class, "b");
    ProcessingField c = field(Subject.class, "c");

    PTypeMirror aType = a.getType();
    PTypeMirror bType = b.getType();
    PTypeMirror cType = c.getType();

    assertEquals(aType.getName(), _int());
    assertEquals(bType.getName(), t(String.class));
    assertEquals(cType.getName(), NamedClass.object());
  }

  @Test
  public void hasName() {
    ProcessingType subject;
    subject = query(Subject.class);

    UnmodifiableList<ProcessingField> fields;
    fields = subject.getDeclaredFields();

    ProcessingField a = fields.get(0);
    ProcessingField b = fields.get(1);
    ProcessingField c = fields.get(2);

    assertTrue(a.hasName("a"));
    assertFalse(a.hasName("b"));
    assertFalse(a.hasName("c"));

    assertFalse(b.hasName("a"));
    assertTrue(b.hasName("b"));
    assertFalse(b.hasName("c"));

    assertFalse(c.hasName("a"));
    assertFalse(c.hasName("b"));
    assertTrue(c.hasName("c"));
  }

  @Test(description = ""
      + "Verify that the return of toIdentifier() is correct."
      + "Also that multiple calls return the exact same instance.")
  public void toIdentifier() {
    ProcessingField a = field(Subject.class, "a");
    ProcessingField b = field(Subject.class, "b");
    ProcessingField c = field(Subject.class, "c");

    Identifier aname = a.toIdentifier();
    Identifier bname = b.toIdentifier();
    Identifier cname = c.toIdentifier();

    assertEquals(aname, id("a"));
    assertEquals(bname, id("b"));
    assertEquals(cname, id("c"));

    assertSame(aname, a.toIdentifier());
    assertSame(bname, b.toIdentifier());
    assertSame(cname, c.toIdentifier());
  }

  private final ProcessingField field(Class<?> type, String name) {
    List<? extends Element> enclosed = getTypeElement(type).getEnclosedElements();
    List<VariableElement> fields = ElementFilter.fieldsIn(enclosed);
    for (VariableElement f : fields) {
      if (f.getSimpleName().toString().equals(name)) {
        return ProcessingField.adapt(processingEnv, f);
      }
    }
    throw new NoSuchElementException(name);
  }

  @SuppressWarnings("unused")
  private static class Subject {
    int a;
    static String b;
    final transient Object c = new Object();
  }

}
