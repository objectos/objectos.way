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
package objectos.http;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

final class ResponseOutput1Appendable implements Appendable {

  private final Charset charset;

  private final OutputStream chunked;

  ResponseOutput1Appendable(Charset charset, OutputStream chunked) {
    this.charset = charset;

    this.chunked = chunked;
  }

  @Override
  public final Appendable append(char c) throws IOException {
    final String s;
    s = Character.toString(c);

    return append(s);
  }

  @Override
  public final Appendable append(CharSequence csq, int start, int end) throws IOException {
    final CharSequence sub;
    sub = csq.subSequence(start, end);

    return append(sub);
  }

  @Override
  public final Appendable append(CharSequence csq) throws IOException {
    final String s;
    s = csq.toString();

    final byte[] bytes;
    bytes = s.getBytes(charset);

    chunked.write(bytes);

    return this;
  }

}
