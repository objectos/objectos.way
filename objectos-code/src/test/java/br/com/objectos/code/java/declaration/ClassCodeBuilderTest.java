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

import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.io.InputStream;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Test;

public class ClassCodeBuilderTest extends AbstractCodeJavaTest {

  @Test
  public void modifiers() {
    test(
        ClassCode.builder()
            .addModifier()
            .build(),
        "class Unnamed {}"
    );
    test(
        ClassCode.builder()
            .addModifier(PUBLIC)
            .build(),
        "public class Unnamed {}"
    );
    test(
        ClassCode.builder()
            .addModifier(PUBLIC, FINAL)
            .build(),
        "public final class Unnamed {}"
    );
    test(
        ClassCode.builder()
            .addModifiers(UnmodifiableList.of(PUBLIC, FINAL))
            .build(),
        "public final class Unnamed {}"
    );
  }

  @Test
  public void simpleName() {
    test(
        ClassCode.builder()
            .simpleName("FromString")
            .build(),
        "class FromString {}"
    );
    test(
        ClassCode.builder()
            .simpleName(TESTING_CODE.nestedClass("FromClassName"))
            .build(),
        "class FromClassName {}"
    );
  }

  @Test
  public void superclass() {
    test(
        ClassCode.builder()
            .simpleName("Something")
            .superclass(TESTING_CODE.nestedClass("Else"))
            .build(),
        "class Something extends testing.code.Else {}"
    );
    test(
        ClassCode.builder()
            .simpleName("OfClass")
            .superclass(InputStream.class)
            .build(),
        "class OfClass extends java.io.InputStream {}"
    );
  }

}
