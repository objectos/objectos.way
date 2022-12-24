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
package objectos.code2;

import objectos.code2.JavaModel.AutoImports;
import objectos.code2.JavaModel.Body;
import objectos.code2.JavaModel.ClassKeyword;
import objectos.code2.JavaModel.ClassType;
import objectos.code2.JavaModel.ExtendsKeyword;
import objectos.code2.JavaModel.FinalModifier;
import objectos.code2.JavaModel.PackageKeyword;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

class InternalApi {

  AutoImportsHey autoImports = new AutoImportsHey();

  int aux;

  int[] elemArray = new int[128];

  int elemIndex;

  Object[] objectArray = new Object[64];

  int objectIndex;

  int[] protoArray = new int[256];

  int protoIndex;

  public final ClassKeyword _class(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check

    $protoadd(ByteProto.CLASS0, $objectadd(name));

    return $protoret();
  }

  public final ExtendsKeyword _extends(ClassType type) {
    return $elemret(ByteProto.EXTENDS, 1);
  }

  public final FinalModifier _final() {
    modifier(Keyword.FINAL);

    return $protoret();
  }

  public final PackageKeyword _package(String name) {
    JavaModel.checkPackageName(name.toString()); // implicit null check

    autoImports.packageName(name);

    $protoadd(ByteProto.PACKAGE, $objectadd(name));

    return $protoret();
  }

  public final AutoImports autoImports() {
    autoImports.enable();

    $protoadd(ByteProto.AUTO_IMPORTS);

    return $protoret();
  }

  public final Body body() {
    return $elemret(ByteProto.BODY, 0);
  }

  public final ClassType t(Class<?> type) {
    var last = objectIndex;

    while (true) {
      var simpleName = type.getSimpleName(); // implicit null-check

      $objectadd(simpleName);

      var outer = type.getEnclosingClass();

      if (outer == null) {
        break;
      } else {
        type = outer;
      }
    }

    var first = objectIndex - 1;

    var names = objectIndex - last;

    var packageName = type.getPackageName();

    $protoadd(ByteProto.CLASS_TYPE, $objectadd(packageName), names);

    for (var index = first; index >= last; index--) {
      var simpleName = objectArray[index];

      $protoadd($objectadd(simpleName));
    }

    return $protoret();
  }

  public final ClassType t(String packageName, String simpleName) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check
    JavaModel.checkSimpleName(simpleName.toString()); // implicit null check

    $protoadd(
      ByteProto.CLASS_TYPE, $objectadd(packageName),
      1, $objectadd(simpleName)
    );

    return $protoret();
  }

  final int $objectadd(Object value) {
    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    var result = objectIndex;

    objectArray[objectIndex++] = value;

    return result;
  }

  final void accept(JavaTemplate template) {
    autoImports.clear();

    aux = elemIndex = objectIndex = protoIndex = 0;

    $elemadd(ByteProto.COMPILATION_UNIT);

    template.execute(this);

    $elempop();

    $elemadd(ByteProto.EOF);
  }

  private void $elemadd(int v0) {
    elemArray = IntArrays.growIfNecessary(elemArray, elemIndex + 0);

    elemArray[elemIndex++] = v0;
  }

  private void $elemadd(int v0, int v1) {
    elemArray = IntArrays.growIfNecessary(elemArray, elemIndex + 1);

    elemArray[elemIndex++] = v0;
    elemArray[elemIndex++] = v1;
  }

  private void $elemcnt() {
    if (aux > 0) {
      $elemadd(ByteProto.PROTOS, aux);
    }

    aux = 0;
  }

  private void $elempop() {
    $elemcnt();

    $elemadd(ByteProto.POP);
  }

  private JavaModel.Ref $elemret(int value, int protos) {
    var diff = aux - protos;

    if (diff > 0) {
      $elemadd(ByteProto.PROTOS, diff);
    }

    aux = 0;

    $elemadd(value);

    $elemadd(ByteProto.PROTOS, protos);

    $elemadd(ByteProto.POP);

    return JavaModel.REF;
  }

  private void $protoadd(int v0) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 0);

    protoArray[protoIndex++] = v0;
  }

  private void $protoadd(int v0, int v1) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 1);

    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
  }

  private void $protoadd(int v0, int v1, int v2) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 2);

    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
  }

  private void $protoadd(int v0, int v1, int v2, int v3) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 3);

    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
    protoArray[protoIndex++] = v3;
  }

  private JavaModel.Ref $protoret() {
    aux++;

    return JavaModel.REF;
  }

  private void modifier(Keyword value) {
    $protoadd(ByteProto.MODIFIER, value.ordinal());
  }

}