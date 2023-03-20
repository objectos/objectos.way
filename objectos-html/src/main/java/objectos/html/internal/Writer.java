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
import objectos.html.HtmlTemplate.Visitor;
import objectos.html.io.HtmlEscape;

/**
 * Base {@link Visitor} implementation suitable for writing HTML files.
 *
 * @since 0.5.1
 */
public abstract class Writer implements Visitor {

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
      HtmlEscape.to(value, out);
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