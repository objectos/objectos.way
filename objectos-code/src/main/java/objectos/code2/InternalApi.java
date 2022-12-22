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

import objectos.code2.JavaModel.Body;
import objectos.code2.JavaModel.ClassKeyword;
import objectos.code2.JavaModel.FinalModifier;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

class InternalApi {

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

  public final FinalModifier _final() {
    modifier(Keyword.FINAL);

    return $protoret();
  }

  public final Body body() {
    return $elemret(ByteProto.BODY, 0);
  }

  final void accept(JavaTemplate template) {
    aux = elemIndex = objectIndex = protoIndex = 0;

    $elempsh(ByteProto.COMPILATION_UNIT);

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

  private void $elempsh(int value) {
    $elemcnt();

    $elemadd(value);
  }

  private JavaModel.Ref $elemret(int value, int protos) {
    $elempsh(value);

    $elemadd(ByteProto.PROTOS, protos);

    $elempop();

    return JavaModel.REF;
  }

  private int $objectadd(Object value) {
    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    var result = objectIndex;

    objectArray[objectIndex++] = value;

    return result;
  }

  private void $protoadd(int v0, int v1) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 1);

    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
  }

  private JavaModel.Ref $protoret() {
    aux++;

    return JavaModel.REF;
  }

  private void modifier(Keyword value) {
    $protoadd(ByteProto.MODIFIER, value.ordinal());
  }

}