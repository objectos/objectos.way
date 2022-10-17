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
package br.com.objectos.code.java.declaration;

import static br.com.objectos.code.java.declaration.Modifiers.FINAL;
import static br.com.objectos.code.java.declaration.Modifiers.PUBLIC;
import static br.com.objectos.code.java.type.NamedTypes._int;

import br.com.objectos.code.java.statement.ReturnStatement;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedTypeVariable;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Test;

public class MethodCodeBuilderTest extends AbstractCodeJavaTest {

  @Test
  public void accessLevel() {
    test(
        MethodCode.builder().accessLevel(AccessLevel.DEFAULT).build(),
        "void unnamed();"
    );
    test(
        MethodCode.builder().accessLevel(AccessLevel.PUBLIC).build(),
        "public void unnamed();"
    );
    test(
        MethodCode.builder().accessLevel(AccessLevel.PROTECTED).build(),
        "protected void unnamed();"
    );
    test(
        MethodCode.builder().accessLevel(AccessLevel.PRIVATE).build(),
        "private void unnamed();"
    );
  }

  @Test
  public void addModifiers() {
    test(
        MethodCode.builder().build(),
        "void unnamed();"
    );
    test(
        MethodCode.builder()
            .addModifier(PUBLIC)
            .build(),
        "public void unnamed();"
    );
    test(
        MethodCode.builder()
            .addModifiers(UnmodifiableList.of(PUBLIC, FINAL))
            .build(),
        "public final void unnamed();"
    );
  }

  @Test
  public void addThrownType() {
    test(
        MethodCode.builder()
            .addThrownType(IOException.class)
            .build(),
        "void unnamed() throws java.io.IOException;"
    );
    test(
        MethodCode.builder()
            .addThrownType(NamedClass.of(ExecutionException.class))
            .build(),
        "void unnamed() throws java.util.concurrent.ExecutionException;"
    );
    test(
        MethodCode.builder()
            .addThrownType(IOException.class)
            .addThrownType(InterruptedException.class)
            .build(),
        "void unnamed() throws java.io.IOException, java.lang.InterruptedException;"
    );
    test(
        MethodCode.builder()
            .addThrownType("E")
            .addThrownType(NamedTypeVariable.of("X"))
            .build(),
        "void unnamed() throws E, X;"
    );
    test(
        MethodCode.builder()
            .addThrownType(IOException.class)
            .addStatement(ReturnStatement._return("foo"))
            .build(),
        "void unnamed() throws java.io.IOException {",
        "  return foo;",
        "}"
    );
  }

  @Test
  public void emptyBody() {
    test(
        MethodCode.builder().emptyBody().build(),
        "void unnamed() {}"
    );
  }

  @Test
  public void name() {
    test(
        MethodCode.builder().name("m").build(),
        "void m();"
    );
  }

  @Test
  public void returnType() {
    test(
        MethodCode.builder().returnType(_int()).build(),
        "int unnamed();"
    );
    test(
        MethodCode.builder().returnType(String.class).build(),
        "java.lang.String unnamed();"
    );
  }

}
