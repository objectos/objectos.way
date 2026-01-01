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
package objectos.way;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * The <strong>Objectos Media</strong> main interface. Represents an entity
 * associated to a content type to be transmitted over the Internet.
 */
public sealed interface Media {

  /**
   * A media entity which exposes its data as an array of bytes.
   */
  public non-sealed interface Bytes extends Media {

    /**
     * Creates a new {@code text/plain; charset=utf-8} media by encoding the
     * specified string using the UTF-8 charset.
     *
     * @param text
     *        the string whose characters are to encoded
     *
     * @return a newly created {@code text/plain; charset=utf-8} media instance
     */
    static Bytes textPlain(String text) {
      Objects.requireNonNull(text, "text == null");

      return new MediaBytes(text, StandardCharsets.UTF_8);
    }

    /**
     * Creates a new {@code text/plain; charset=<specified charset>} media by
     * encoding the specified string using the specified charset.
     *
     * @param text
     *        the string whose characters are to encoded
     * @param charset
     *        the charset to use
     *
     * @return a newly created {@code text/plain; charset=<specified charset>}
     *         media instance
     */
    static Bytes textPlain(String text, Charset charset) {
      Objects.requireNonNull(text, "text == null");
      Objects.requireNonNull(charset, "charset == null");

      return new MediaBytes(text, charset);
    }

    /**
     * Returns the data of this entity as an array of bytes.
     *
     * @return the data of this entity
     */
    byte[] toByteArray();

  }

  /**
   * A media entity which writes out its data as a sequence of bytes.
   */
  public non-sealed interface Stream extends Media {

    /**
     * Writes the contents of this entity to the specified output stream.
     *
     * @param out
     *        where to write bytes into.
     *
     * @throws IOException if an I/O error occurs
     */
    void writeTo(OutputStream out) throws IOException;

  }

  /**
   * A media entity which writes out its data as a sequence of characters.
   */
  public non-sealed interface Text extends Media {

    /**
     * The charset associated to the content type of this entity.
     *
     * <p>
     * Implementations must never return {@code null}.
     *
     * @return the charset associated to the content type of this entity
     */
    Charset charset();

    /**
     * Writes the contents of this entity to the specified appendable.
     *
     * @param out
     *        where to write characters into.
     *
     * @throws IOException if an I/O error occurs
     */
    void writeTo(Appendable out) throws IOException;

  }

  public sealed interface Type permits MediaType {

    static Type parse(String s) {
      Objects.requireNonNull(s, "s == null");

      return MediaType.parse(s);
    }

    static Type wildcard() {
      return MediaType.WILDCARD;
    }

    String type();

    String subtype();

    String param(String name);

    String toString(byte[] bytes);

  }

  /**
   * Returns the content type of this media, such as
   * {@code text/html; charset=utf-8} or {@code application/json}.
   *
   * @return the content type of this media
   */
  String contentType();

}