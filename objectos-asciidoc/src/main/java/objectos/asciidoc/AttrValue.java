/*
 * Copyright (C) 2021-2025 Objectos Software LTDA.
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
package objectos.asciidoc;

import objectos.asciidoc.AsciiDoc.Processor;
import objectos.asciidoc.AttrValue.Empty;
import objectos.asciidoc.AttrValue.StringAttr;
import objectos.asciidoc.AttrValue.TextAttr;

sealed interface AttrValue permits Empty, StringAttr, TextAttr {

  static final class Empty implements AttrValue {
    @Override
    public void render(Processor processor) {
      // noop
    }

    @Override
    public String stringValue() { return ""; }
  }

  record StringAttr(String value) implements AttrValue {
    @Override
    public void render(Processor processor) {
      processor.text(value);
    }

    @Override
    public String stringValue() { return value; }
  }

  record TextAttr(int[] value, String source) implements AttrValue {
    @Override
    public void render(Processor processor) {
      for (int index = 0; index < value.length;) {
        var t = value[index++];

        switch (t) {
          case Text.REGULAR -> {
            var begin = value[index++];
            var end = value[index++];
            var s = source.substring(begin, end);

            processor.text(s);
          }

          case Text.MONOSPACE_START -> processor.monospaceStart();

          case Text.MONOSPACE_END -> processor.monospaceEnd();

          default -> throw new UnsupportedOperationException("Implement me :: t=" + t);
        }
      }
    }

    @Override
    public String stringValue() { throw new UnsupportedOperationException("Implement me"); }
  }

  static Empty EMPTY = new Empty();

  static AttrValue string(String s) {
    return new StringAttr(s);
  }

  static AttrValue text(int[] values, String source) {
    return new TextAttr(values, source);
  }

  void render(Processor processor);

  String stringValue();

}