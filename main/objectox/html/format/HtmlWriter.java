/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html.format;

public class HtmlWriter implements Appendable {

  private final char[] buffer;

  private int index;

  private final int limit;

  private final StringBuilder sb = new StringBuilder();

  private int written;

  private HtmlWriter(char[] buffer, int index, int limit) {
    this.buffer = buffer;

    this.index = index;

    this.limit = limit;
  }

  public static HtmlWriter wrap(char[] cbuf, int off, int len) {
    final int limit;
    limit = off + len;

    return new HtmlWriter(cbuf, off, limit);
  }

  @Override
  public final Appendable append(char c) {
    written += 1;

    if (index < limit) {
      buffer[index++] = c;
    } else {
      sb.append(c);
    }

    return this;
  }

  @Override
  public final Appendable append(CharSequence csq) {
    final CharSequence s;
    s = normalize(csq);

    final int len;
    len = s.length();

    append0(s, 0, len);

    return this;
  }

  @Override
  public final Appendable append(CharSequence csq, int start, int end) {
    final CharSequence s;
    s = normalize(csq);

    append0(s, start, end);

    return this;
  }

  private void append0(CharSequence s, int start, int end) {
    final int len;
    len = end - start;

    written += len;

    final int remaining;
    remaining = limit - index;

    if (len <= remaining) {
      s.getChars(start, end, buffer, index);

      index += len;
    } else {
      s.getChars(start, start + remaining, buffer, index);

      index = limit;

      sb.append(s, start + remaining, end);
    }
  }

  private CharSequence normalize(CharSequence csq) {
    return csq != null ? csq : "null";
  }

  public final boolean exhausted() {
    return index == limit;
  }

  public final String overflow() {
    return sb.toString();
  }

  public final int written() {
    return written == 0 ? -1 : written;
  }

}