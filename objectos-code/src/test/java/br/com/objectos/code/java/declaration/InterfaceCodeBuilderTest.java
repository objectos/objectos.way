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

import static br.com.objectos.code.java.declaration.Modifiers.PUBLIC;
import static br.com.objectos.code.java.declaration.Modifiers.STATIC;
import static br.com.objectos.code.java.declaration.Modifiers.STRICTFP;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Test;

public class InterfaceCodeBuilderTest extends AbstractCodeJavaTest {

  @Test
  public void annotation() {
    AnnotationCode ann1 = AnnotationCode.annotation(TESTING_CODE.nestedClass("Ann1"));
    AnnotationCode ann2 = AnnotationCode.annotation(TESTING_CODE.nestedClass("Ann2"));
    test(
        InterfaceCode.builder()
            .addAnnotation(ann1)
            .build(),
        "@testing.code.Ann1",
        "interface Unnamed {}"
    );
    test(
        InterfaceCode.builder()
            .addAnnotation(Deprecated.class)
            .build(),
        "@java.lang.Deprecated",
        "interface Unnamed {}"
    );
    test(
        InterfaceCode.builder()
            .addAnnotations(UnmodifiableList.of(ann1, ann2))
            .build(),
        "@testing.code.Ann1",
        "@testing.code.Ann2",
        "interface Unnamed {}"
    );
  }
  
  @Test
  public void modifiers() {
    test(
        InterfaceCode.builder()
            .addModifier()
            .build(),
        "interface Unnamed {}"
    );
    test(
        InterfaceCode.builder()
            .addModifier(PUBLIC)
            .build(),
        "public interface Unnamed {}"
    );
    test(
        InterfaceCode.builder()
            .addModifier(PUBLIC, Modifiers.STATIC)
            .build(),
        "public static interface Unnamed {}"
    );
    test(
        InterfaceCode.builder()
            .addModifier(PUBLIC, STATIC, STRICTFP)
            .build(),
        "public static strictfp interface Unnamed {}"
    );
    test(
        InterfaceCode.builder()
            .addModifiers(UnmodifiableList.of(PUBLIC, Modifiers.STATIC))
            .build(),
        "public static interface Unnamed {}"
    );
  }

  @Test
  public void simpleName() {
    test(
        InterfaceCode.builder()
            .simpleName("FromString")
            .build(),
        "interface FromString {}"
    );
    test(
        InterfaceCode.builder()
            .simpleName(TESTING_CODE.nestedClass("FromClassName"))
            .build(),
        "interface FromClassName {}"
    );
  }

}
