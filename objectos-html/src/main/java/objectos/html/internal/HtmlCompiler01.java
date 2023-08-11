/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html.internal;

import objectos.html.tmpl.StandardElementName;
import objectos.util.ByteArrays;

class HtmlCompiler01 extends HtmlTemplateApi2 {

  byte[] aux;

  int auxIndex;

  int auxStart;

  byte[] main;

  int mainContents;

  int mainIndex;

  int mainStart;

  @Override
  public final void compilationBegin() {
    aux = new byte[128];

    auxIndex = 0;

    main = new byte[256];

    mainIndex = 0;
  }

  @Override
  public final void compilationEnd() {
    // noop
  }

  @Override
  public final void elementBegin(StandardElementName name) {
    // we mark the start of our aux list
    auxStart = auxIndex;

    // we mark:
    // 1) the start of the contents of the current declaration
    // 2) the start of our main list
    mainContents = mainStart = mainIndex;

    mainAdd(
      ByteProto2.ELEMENT,

      // length takes 2 bytes
      ByteProto2.NULL,
      ByteProto2.NULL,

      ByteProto2.ELEMENT_STANDARD,

      Bytes.encodeName(name)
    );
  }

  @SuppressWarnings("unused")
  @Override
  public final void elementEnd() {
    // we iterate over each value added via elementValue(Instruction)
    int index;
    index = auxStart;

    int indexMax;
    indexMax = auxIndex;

    int contents;
    contents = mainContents;

    loop: while (index < indexMax) {
      byte mark;
      mark = aux[index++];

      switch (mark) {
        default -> throw new UnsupportedOperationException(
          "Implement me :: mark=" + mark
        );
      }
    }

    // ensure main can hold 4 more elements
    main = ByteArrays.growIfNecessary(main, mainIndex + 3);

    // mark the end
    main[mainIndex++] = ByteProto2.END;

    // store the distance to the contents (yes, reversed)
    int length;
    length = mainIndex - mainContents - 1;

    mainIndex = Bytes.encodeVarIntR(main, mainIndex, length);

    // trailer proto
    main[mainIndex++] = ByteProto2.INTERNAL;

    // set the end index of the declaration
    length = mainIndex - mainStart;

    // skip ByteProto.FOO + len0 + len1
    length -= 3;

    // we skip the first byte proto
    main[mainStart + 1] = Bytes.encodeInt0(length);
    main[mainStart + 2] = Bytes.encodeInt1(length);

    // we clear the aux list
    auxIndex = auxStart;
  }

  final void auxAdd(byte b0) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 0);
    aux[auxIndex++] = b0;
  }

  final void auxAdd(byte b0, byte b1) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 1);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
  }

  final void auxAdd(byte b0, byte b1, byte b2) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 2);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3, byte b4) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 4);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
    main[mainIndex++] = b4;
  }

}