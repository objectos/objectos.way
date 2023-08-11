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

import java.io.IOException;
import objectos.html.CompiledHtml;
import objectos.html.HtmlWriter;
import objectos.html.tmpl.StandardElementName;

public final class StandardHtmlWriter implements HtmlWriter {

  private static final String NL = System.lineSeparator();

  private final Appendable appendable;

  public StandardHtmlWriter(Appendable appendable) {
    this.appendable = appendable;
  }

  @Override
  public final void write(CompiledHtml html) throws IOException {
    CompiledMarkup compiled;
    compiled = (CompiledMarkup) html;

    int index;
    index = 0;

    byte[] bytes;
    bytes = compiled.main;

    while (index < bytes.length) {
      byte code;
      code = bytes[index++];

      switch (code) {
        case ByteCode.EMPTY_ELEMENT -> index++;

        case ByteCode.END_TAG -> {
          int ordinal;
          ordinal = Bytes.decodeInt(bytes[index++]);

          StandardElementName name;
          name = StandardElementName.getByCode(ordinal);

          appendable.append('<');
          appendable.append('/');
          appendable.append(name.getName());
          appendable.append('>');
        }

        case ByteCode.GT -> appendable.append('>');

        case ByteCode.NL -> appendable.append(NL);

        case ByteCode.START_TAG -> {
          int ordinal;
          ordinal = Bytes.decodeInt(bytes[index++]);

          StandardElementName name;
          name = StandardElementName.getByCode(ordinal);

          appendable.append('<');
          appendable.append(name.getName());
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: code=" + code
        );
      }
    }
  }

}
