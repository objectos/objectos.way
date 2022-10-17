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

import br.com.objectos.code.util.AbstractCodeCoreTest;
import java.util.List;
import java.util.NoSuchElementException;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Types;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;
import org.testng.annotations.Test;

public class WildcardTypeTest extends AbstractCodeCoreTest {

  @Test
  public void capture() {
    ExecutableElement m0;
    m0 = getExecutableElement(CaptureSubject.class, "m0");

    List<? extends VariableElement> parameters;
    parameters = m0.getParameters();

    UnmodifiableList<TypeMirror> capturedParameters;
    capturedParameters = capture(parameters);

    TypeMirror capturedList;
    capturedList = capturedParameters.get(0);

    DeclaredType capturedListDeclaredType;
    capturedListDeclaredType = (DeclaredType) capturedList;

    List<? extends TypeMirror> typeArguments;
    typeArguments = capturedListDeclaredType.getTypeArguments();

    TypeMirror capturedSuperT;
    capturedSuperT = typeArguments.get(0);

    assertEquals(
        capturedSuperT.getKind(),
        TypeKind.TYPEVAR
    );

    TypeVariable capturedVariable;
    capturedVariable = (TypeVariable) capturedSuperT;

    TypeMirror lowerBound;
    lowerBound = capturedVariable.getLowerBound();

    assertEquals(
        lowerBound.toString(),
        "java.lang.Number"
    );
  }

  private UnmodifiableList<TypeMirror> capture(List<? extends VariableElement> parameters) {
    GrowableList<TypeMirror> result;
    result = new GrowableList<>();

    Types types;
    types = processingEnv.getTypeUtils();

    for (int i = 0; i < parameters.size(); i++) {
      VariableElement parameter;
      parameter = parameters.get(i);

      TypeMirror parameterType;
      parameterType = parameter.asType();

      TypeMirror captured;
      captured = types.capture(parameterType);

      result.add(captured);
    }

    return result.toUnmodifiableList();
  }

  private ExecutableElement getExecutableElement(Class<?> type, String name) {
    TypeElement subject;
    subject = getTypeElement(type);

    List<? extends Element> elements;
    elements = subject.getEnclosedElements();

    for (int i = 0; i < elements.size(); i++) {
      Element element;
      element = elements.get(i);

      if (element.getKind() != ElementKind.METHOD) {
        continue;
      }

      ExecutableElement executable;
      executable = (ExecutableElement) element;

      if (executable.getSimpleName().toString().equals(name)) {
        return executable;
      }
    }

    throw new NoSuchElementException(name);
  }

  static class CaptureSubject {
    void m0(List<? super Number> list) {}
  }

}
