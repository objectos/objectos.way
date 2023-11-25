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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import objectos.lang.object.Check;

/**
 * Provides {@code static} methods for writing to {@link OutputStream} and
 * {@link WriterSource}.
 *
 * @since 2
 */
public final class Write {

  private Write() {}

  /**
   * Writes to the specified {@link OutputStreamSource} all of the specified
   * bytes.
   *
   * @param dst
   *        the destination object. Bytes will be written to this object
   * @param bytes
   *        the bytes to be written
   *
   * @throws IOException
   *         if an output stream cannot be obtained, if bytes cannot be written
   *         or if the output stream cannot be closed
   */
  public static void byteArray(OutputStreamSource dst, byte[] bytes) throws IOException {
    Check.notNull(dst, "dst == null");
    Check.notNull(bytes, "bytes == null");

    try (OutputStream outputStream = dst.openOutputStream()) {
      outputStream.write(bytes, 0, bytes.length);
    }
  }

  /**
   * Writes to the specified {@link WriterSource} using the specified charset
   * all of the characters from the specified string.
   *
   * @param dst
   *        the destination object. Characters will be written to this object
   * @param charset
   *        the charset to use for encoding
   * @param string
   *        the string containing the characters to be written
   *
   * @throws IOException
   *         if a writer cannot be obtained, if characters cannot be written
   *         or if the writer cannot be closed
   */
  public static void string(WriterSource dst, Charset charset, String string) throws IOException {
    Check.notNull(dst, "dst == null");
    Check.notNull(charset, "charset == null");
    Check.notNull(string, "string == null");

    try (Writer writer = dst.openWriter(charset)) {
      writer.write(string);
    }
  }

}