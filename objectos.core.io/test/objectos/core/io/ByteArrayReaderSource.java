/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.core.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

final class ByteArrayReaderSource implements ReaderSource {

  private final String value;

  ByteArrayReaderSource(String value) {
    this.value = value;
  }

  @Override
  public final Reader openReader(Charset charset) throws IOException {
    byte[] bytes;
    bytes = value.getBytes(charset);

    ByteArrayInputStream in;
    in = new ByteArrayInputStream(bytes);

    return new InputStreamReader(in);
  }

}