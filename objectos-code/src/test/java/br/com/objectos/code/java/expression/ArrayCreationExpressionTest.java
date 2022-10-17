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
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.expression.Expressions._new;
import static br.com.objectos.code.java.expression.Expressions.a;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static br.com.objectos.code.java.type.NamedTypes.tvar;

import br.com.objectos.code.java.type.NamedArray;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedParameterized;
import br.com.objectos.code.java.type.NamedTypeVariable;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.Arrays;
import java.util.List;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class ArrayCreationExpressionTest extends AbstractCodeJavaTest
    implements
    FieldAccessReferenceExpressionTest.Adapter,
    RelationalTest.Adapter {

  @Test
  public void arrayCreationExpression() {
    NamedArray a1 = NamedArray.of(_int());
    NamedArray a2 = a1.toNamedArray();
    NamedArray a3 = a2.toNamedArray();
    NamedArray a4 = a3.toNamedArray();
    NamedArray a5 = a4.toNamedArray();

    test(_new(a1, id("i0")), "new int[i0]");
    test(_new(a2, id("i0"), id("i1")), "new int[i0][i1]");
    test(_new(a3, id("i0"), id("i1"), id("i2")), "new int[i0][i1][i2]");
    test(_new(a4, id("i0"), id("i1"), id("i2"), id("i3")), "new int[i0][i1][i2][i3]");
    test(_new(a5, Arrays.asList(
        l(1), l(2), l(3), l(4), l(5)
    )), "new int[1][2][3][4][5]");
  }

  @Test
  public void className() {
    NamedArray arr1d = NamedArray.of(NamedClass.of(String.class));
    test(_new(arr1d, l(10)), "new java.lang.String[10]");
    test(_new(arr1d, a()), "new java.lang.String[] {}");
    test(_new(arr1d, a(l("A"), l("B"))), "new java.lang.String[] {\"A\", \"B\"}");

    NamedArray arr2d = arr1d.toNamedArray();
    test(_new(arr2d, id("x")), "new java.lang.String[x][]");
    test(_new(arr2d, id("x"), invoke("y")), "new java.lang.String[x][y()]");

    NamedArray arr3d = arr2d.toNamedArray();
    test(_new(arr3d, l(1)), "new java.lang.String[1][][]");
    test(_new(arr3d, l(1), l(2)), "new java.lang.String[1][2][]");
    test(_new(arr3d, l(1), l(2), l(3)), "new java.lang.String[1][2][3]");
  }

  @Override
  public final FieldAccessReferenceExpression fieldAccessReferenceExpression() {
    NamedArray typeName = t(Object.class).toNamedArray();
    return _new(typeName, l(10));
  }

  @Test
  public void parameterizedTypeName() {
    NamedParameterized list = t(t(List.class), tvar("E"));
    NamedArray arr1d = NamedArray.of(list);
    test(_new(arr1d, l(10)), "new java.util.List[10]");

    NamedArray arr2d = arr1d.toNamedArray();
    test(_new(arr2d, id("x")), "new java.util.List[x][]");
    test(_new(arr2d, id("x"), invoke("y")), "new java.util.List[x][y()]");

    NamedArray arr3d = arr2d.toNamedArray();
    test(_new(arr3d, l(1)), "new java.util.List[1][][]");
    test(_new(arr3d, l(1), l(2)), "new java.util.List[1][2][]");
    test(_new(arr3d, l(1), l(2), l(3)), "new java.util.List[1][2][3]");
  }

  @Test
  public void primitive() {
    NamedArray arr1d = NamedArray.of(_int());
    test(_new(arr1d, l(10)), "new int[10]");
    test(_new(arr1d, a(l(10))), "new int[] {10}");

    NamedArray arr2d = arr1d.toNamedArray();
    test(_new(arr2d, id("x")), "new int[x][]");
    test(_new(arr2d, id("x"), invoke("y")), "new int[x][y()]");
    test(_new(arr2d, a(a(l(10)), a(l(20)))), "new int[][] {{10}, {20}}");

    NamedArray arr3d = arr2d.toNamedArray();
    test(_new(arr3d, l(1)), "new int[1][][]");
    test(_new(arr3d, l(1), l(2)), "new int[1][2][]");
    test(_new(arr3d, l(1), l(2), l(3)), "new int[1][2][3]");
  }

  @Override
  public final RelationalExpression relationalExpression() {
    NamedArray typeName = t(Object.class).toNamedArray();
    return _new(typeName, l(10));
  }

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        FieldAccessReferenceExpressionTest.with(this),
        RelationalTest.with(this)
    };
  }

  @Test
  public void typeVariableName() {
    NamedTypeVariable e = NamedTypeVariable.of("E");
    NamedArray arr1d = e.toNamedArray();
    test(_new(arr1d, l(10)), "new java.lang.Object[10]");
  }

}
