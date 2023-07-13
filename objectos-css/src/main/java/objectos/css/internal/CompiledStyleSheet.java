/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

import java.io.IOException;
import objectos.css.StyleSheet;
import objectos.css.StyleSheetWriter;

public final class CompiledStyleSheet implements StyleSheet {

  final byte[] main;

  public CompiledStyleSheet(byte[] main) {
    this.main = main;
  }

  public final void accept(StandardStyleSheetWriter writer) throws IOException {
    int mainLength;
    mainLength = main.length;

    int rootIndex;
    rootIndex = mainIndex(mainLength - 3);

    loop: while (rootIndex < mainLength) {
      byte proto = main[rootIndex++];

      switch (proto) {
        case ByteProto.ROOT -> {}

        case ByteProto.ROOT_END -> {
          break loop;
        }

        case ByteProto.STYLE_RULE -> {
          int index = mainIndex(rootIndex);

          rootIndex += 3;

          styleRule(writer, index);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }
  }

  @Override
  public final String toString() {
    try {
      StringBuilder sb;
      sb = new StringBuilder();

      StyleSheetWriter writer;
      writer = StyleSheetWriter.of(sb);

      writer.write(this);

      return sb.toString();
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

  private void declaration(StandardStyleSheetWriter writer, int index) throws IOException {
    loop: while (index < main.length) {
      byte proto = main[index++];

      switch (proto) {
        case ByteProto.DECLARATION_END -> {
          writer.declarationEnd();

          break loop;
        }

        case ByteProto.MARKED -> {
          // skip end index
          index += 3;

          String name;
          name = standardName(index);

          writer.declarationStart(name);

          index += 2;
        }

        case ByteProto.STANDARD_NAME -> {
          String name;
          name = standardName(index);

          writer.keyword(name);

          index += 2;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }
  }

  private int mainIndex(int offset) {
    int idx0 = main[offset + 0];
    int idx1 = main[offset + 1] << 8;
    int idx2 = main[offset + 2] << 16;

    return idx2 | idx1 | idx0;
  }

  private String standardName(int offset) {
    int ordinal0 = main[offset + 0];
    int ordinal1 = main[offset + 1] << 8;

    int ordinal = ordinal1 | ordinal0;

    StandardName name;
    name = StandardName.byOrdinal(ordinal);

    return name.cssName;
  }

  private void styleRule(StandardStyleSheetWriter writer, int index) throws IOException {
    writer.styleRuleStart();

    loop: while (index < main.length) {
      byte proto = main[index++];

      switch (proto) {
        case ByteProto.DECLARATION -> {
          int elemIndex = mainIndex(index);

          index += 3;

          declaration(writer, elemIndex);
        }

        case ByteProto.STANDARD_NAME -> {
          String name;
          name = standardName(index);

          writer.selector(name);

          index += 2;
        }

        case ByteProto.STYLE_RULE -> {
          // skip end index
          index += 3;
        }

        case ByteProto.STYLE_RULE_END -> {
          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    writer.styleRuleEnd();
  }

}