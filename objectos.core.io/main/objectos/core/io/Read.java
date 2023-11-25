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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import objectos.lang.object.Check;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;

/**
 * Provides {@code static} methods for reading from {@link InputStream},
 * {@link InputStreamSource}, {@link Reader} and {@link ReaderSource}.
 *
 * @since 2
 */
public final class Read {

  private Read() {}

  /**
   * Reads all of the bytes from the specified source.
   *
   * <p>
   * This method simply delegates to
   * {@link #byteArray(InputStreamSource, byte[])} using a newly allocated
   * buffer.
   *
   * @param source
   *        the source object. Bytes will be read from this object.
   *
   * @return a new byte array containing all of the bytes read from the source
   *
   * @throws IOException
   *         if an input stream cannot be obtained, if bytes cannot be read or
   *         if the input stream cannot be closed
   */
  public static byte[] byteArray(InputStreamSource source) throws IOException {
    return byteArray(source, Copy.createBuffer());
  }

  /**
   * Reads all of the bytes from the specified source using the specified
   * buffer.
   *
   * @param source
   *        the source object. Bytes will be read from this object.
   * @param buffer
   *        the byte array buffer of the read operation
   *
   * @return a new byte array containing all of the bytes read from the source
   *
   * @throws IOException
   *         if an input stream cannot be obtained, if bytes cannot be read or
   *         if the input stream cannot be closed
   */
  public static byte[] byteArray(InputStreamSource source, byte[] buffer) throws IOException {
    Check.notNull(source, "source == null");
    Copy.checkBuffer(buffer);

    try (var in = source.openInputStream(); var out = new ByteArrayOutputStream()) {
      int count;
      count = in.read(buffer);

      while (count > 0) {
        out.write(buffer, 0, count);

        count = in.read(buffer);
      }

      return out.toByteArray();
    }
  }

  /**
   * Reads all of the lines from the specified reader. The reader is closed
   * before this method returns (or throws).
   *
   * @param reader
   *        lines will be read from this reader.
   *
   * @return an immutable list containing all of lines read from the reader
   *
   * @throws IOException
   *         if lines cannot be read or if the reader cannot be closed
   */
  public static UnmodifiableList<String> lines(Reader reader) throws IOException {
    Check.notNull(reader, "reader == null");

    BufferedReader r;

    if (reader instanceof BufferedReader) {
      r = (BufferedReader) reader;
    } else {
      r = new BufferedReader(reader);
    }

    return lines0(r);
  }

  /**
   * Reads all of the lines from the specified source using the specified
   * charset for decoding.
   *
   * @param source
   *        lines will be read from this object.
   * @param charset
   *        the charset to use for decoding
   *
   * @return an immutable list containing all of lines read from the reader
   *
   * @throws IOException
   *         if a reader cannot be obtained, if lines cannot be read or if the
   *         reader cannot be closed
   */
  public static UnmodifiableList<String> lines(ReaderSource source, Charset charset) throws IOException {
    Check.notNull(source, "source == null");
    Check.notNull(charset, "charset == null");

    BufferedReader reader;
    reader = open(source, charset);

    return lines0(reader);
  }

  /**
   * Reads all of the characters from the specified source using the specified
   * charset for decoding.
   *
   * <p>
   * This method simply delegates to
   * {@link #string(ReaderSource, Charset, char[])} using a newly allocated
   * buffer.
   *
   * @param source
   *        characters will be read from this object.
   * @param charset
   *        the charset to use for decoding
   *
   * @return a new String containing all of characters read from the source
   *
   * @throws IOException
   *         if a reader cannot be obtained, if characters cannot be read or if
   *         the reader cannot be closed
   */
  public static String string(ReaderSource source, Charset charset) throws IOException {
    return string(source, charset, Copy.newCharBuffer());
  }

  /**
   * Reads all of the characters from the specified source using the specified
   * charset for decoding and the specified array as buffer.
   *
   * @param source
   *        characters will be read from this object.
   * @param charset
   *        the charset to use for decoding
   * @param buffer
   *        the array to use as buffer
   *
   * @return a new String containing all of characters read from the source
   *
   * @throws IOException
   *         if a reader cannot be obtained, if characters cannot be read or if
   *         the reader cannot be closed
   */
  public static String string(ReaderSource source, Charset charset, char[] buffer) throws IOException {
    Check.notNull(source, "source == null");
    Check.notNull(charset, "charset == null");
    Copy.checkBuffer(buffer);

    StringBuilder result;
    result = new StringBuilder();

    try (BufferedReader reader = open(source, charset)) {
      int count;
      count = reader.read(buffer);

      while (count > 0) {
        result.append(buffer, 0, count);

        count = reader.read(buffer);
      }
    }

    return result.toString();
  }

  private static UnmodifiableList<String> lines0(BufferedReader r) throws IOException {
    GrowableList<String> result;
    result = new GrowableList<>();

    try (r) {
      String line;
      line = r.readLine();

      while (line != null) {
        result.add(line);

        line = r.readLine();
      }
    }

    return result.toUnmodifiableList();
  }

  private static BufferedReader open(ReaderSource source, Charset charset) throws IOException {
    Reader reader;
    reader = source.openReader(charset);

    if (reader == null) {
      throw new NullPointerException("ReaderSource produced a null Reader instance");
    }

    if (reader instanceof BufferedReader) {
      return (BufferedReader) reader;
    }

    return new BufferedReader(reader);
  }

}