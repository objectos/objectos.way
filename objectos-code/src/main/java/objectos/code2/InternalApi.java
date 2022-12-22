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

import objectos.code2.JavaModel.ClassKeyword;
import objectos.code2.JavaModel.FinalModifier;
import objectos.util.IntArrays;

class InternalApi {

  int[] elemArray = new int[128];

  int elemIndex;

  int[] protoArray = new int[256];

  int protoIndex;

  public final ClassKeyword _class(String name) {
    return null;
  }

  public final FinalModifier _final() {
    modifier(Keyword.FINAL);

    return JavaModel.REF;
  }

  private void $protoadd(int v0, int v1) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 1);

    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
  }

  private void modifier(Keyword value) {
    $protoadd(ByteProto.MODIFIER, value.ordinal());
  }

}