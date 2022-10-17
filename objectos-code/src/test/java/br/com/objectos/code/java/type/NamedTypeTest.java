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
import static br.com.objectos.code.java.type.NamedTypes.t;
import static br.com.objectos.code.java.type.NamedTypes.tvar;
import static br.com.objectos.code.java.type.NamedTypes.wildcard;
import static br.com.objectos.code.java.type.NamedTypes.wildcardExtends;
import static br.com.objectos.code.java.type.NamedTypes.wildcardSuper;
import static org.testng.Assert.assertEquals;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.testng.annotations.Test;

public class NamedTypeTest extends AbstractCodeJavaTest {

  @Test
  public void of() {
    // NamedArray
    assertEquals(
        fromMethods("intArray"),
        _int().toNamedArray()
    );
    assertEquals(
        fromMethods("intArrayArray"),
        _int().toNamedArray().toNamedArray()
    );
    assertEquals(
        fromMethods("stringArray"),
        t(String.class).toNamedArray()
    );
    assertEquals(
        fromMethods("stringArrayArray"),
        t(String.class).toNamedArray().toNamedArray()
    );

    NamedClass string = NamedClass.of(String.class);

    // NamedClass
    assertEquals(fromMethods("string"), string);

    // NamedParameterized
    assertEquals(
        fromMethods("setOfString"),
        NamedParameterized.of(NamedClass.of(Set.class), string)
    );
  }

  @Test
  public void ofFromTypeMirror_primitive() {
    PrimitiveType type = getTypeUtils().getPrimitiveType(TypeKind.INT);
    NamedType res = NamedTypeFactory.of(type);
    assertEquals(res, NamedPrimitive._int());
  }

  @Test
  public void ofFromTypeMirror_typevar() {
    assertEquals(
        fromMethods("listOfE"),
        t(NamedClass.of(List.class), tvar("E"))
    );
    assertEquals(
        fromMethods("listOfE_Number"),
        t(NamedClass.of(List.class), tvar("E"))
    );
  }

  @Test
  public void ofFromTypeMirror_wildcard() {
    assertEquals(
        fromMethods("classWildcard"),
        t(t(Class.class), wildcard())
    );
    assertEquals(
        fromMethods("classWildcardExtends"),
        t(t(Class.class), wildcardExtends(t(Number.class)))
    );
    assertEquals(
        fromMethods("classWildcardSuper"),
        t(t(Class.class), wildcardSuper(t(t(Comparable.class), wildcard())))
    );
  }

  @Test
  public void toClassNameUnchecked() {
    NamedClass className = NamedClass.of(String.class);
    NamedType typeName = className;
    NamedClass res = typeName.toClassNameUnchecked();
    assertEquals(res, className);
  }

  private NamedType fromMethods(String name) {
    ExecutableElement method;
    method = getMethodElement(FromMethods.class, name);

    TypeMirror returnType;
    returnType = method.getReturnType();

    return NamedType.of(returnType);
  }

  abstract class FromMethods {
    abstract Class<?> classWildcard();
    abstract Class<? extends Number> classWildcardExtends();
    abstract Class<? super Comparable<?>> classWildcardSuper();
    abstract int[] intArray();
    abstract int[][] intArrayArray();
    abstract Object isJavaLangObject();
    abstract void isVoid();
    abstract <E> List<E> listOfE();
    abstract <E extends Number> List<E> listOfE_Number();
    abstract Set<String> setOfString();
    abstract String string();
    abstract String[] stringArray();
    abstract String[][] stringArrayArray();
  }

}