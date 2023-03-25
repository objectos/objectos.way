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
import objectos.html.pseudom.DocumentProcessor;

/**
 * Base {@link DocumentProcessor} implementation suitable for writing HTML
 * files.
 *
 * @since 0.5.1
 */
public abstract class Writer implements DocumentProcessor {

  private IOException ioException;

  public Appendable out;

  public final void throwIfNecessary() throws IOException {
    if (ioException == null) {
      return;
    }

    var toThrow = ioException;

    ioException = null;

    throw toThrow;
  }

  protected final void escaped(String value) {
    if (ioException != null) {
      return;
    }

    try {
      for (int i = 0, len = value.length(); i < len; i++) {
        var c = value.charAt(i);

        switch (c) {
          case '"' -> out.append("&quot;");
          case '&' -> out.append("&amp;");
          case '<' -> out.append("&lt;");
          case '>' -> out.append("&gt;");
          case '\u00A9' -> out.append("&copy;");
          default -> out.append(c);
        }
      }
    } catch (IOException e) {
      ioException = e;
    }
  }

  protected final void write(char c) {
    if (ioException != null) {
      return;
    }

    try {
      out.append(c);
    } catch (IOException e) {
      ioException = e;
    }
  }

  protected final void write(String s) {
    if (ioException != null) {
      return;
    }

    try {
      out.append(s);
    } catch (IOException e) {
      ioException = e;
    }
  }

}