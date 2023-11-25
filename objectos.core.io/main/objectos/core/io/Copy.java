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
import java.io.InputStream;
import java.io.OutputStream;
import objectos.lang.object.Check;

/**
 * Provides {@code static} methods for copying all bytes from one object to
 * another object.
 *
 * @since 2
 */
public final class Copy {

  private static final int DEFAULT_BUFFER_SIZE = 8192;

  private Copy() {}

  /**
   * Copies all of the bytes from the specified {@link InputStreamSource} to the
   * specified {@link OutputStreamSource}.
   *
   * <p>
   * This method simply delegates to
   * {@link #sources(InputStreamSource, OutputStreamSource, byte[])} using a
   * newly allocated buffer.
   *
   * @param src
   *        the source object. Bytes will be read from this object
   * @param dst
   *        the destination object. Bytes will be written to this object
   *
   * @return the total number of bytes copied
   *
   * @throws IOException
   *         if data streams cannot be obtained, if bytes cannot be read, if
   *         bytes cannot be written or if streams cannot be closed
   */
  public static long sources(InputStreamSource src, OutputStreamSource dst) throws IOException {
    return sources(src, dst, createBuffer());
  }

  /**
   * Copies all of the bytes from the specified {@link InputStreamSource} to the
   * specified {@link OutputStreamSource} using the specified buffer.
   *
   * <p>
   * More formally:
   *
   * <ol>
   * <li>obtain an {@code InputStream} from the specified source by invoking
   * its {@link InputStreamSource#openInputStream()} method;</li>
   * <li>obtain an {@code OutputStream} from the specified destination by
   * invoking its {@link OutputStreamSource#openOutputStream()} method;</li>
   * <li>copies all bytes from the input stream to the output stream using the
   * specified buffer;</li>
   * <li>closes the output stream;</li>
   * <li>closes the input stream;</li>
   * <li>if the copy and close operations complete normally returns the total
   * number of bytes copied; and</li>
   * <li>if the copy or the close operation completes abruptly throws an
   * exception.</li>
   * </ol>
   *
   * <p>
   * Please note that the result of this operation is undefined if the buffer is
   * modified during the copy operation.
   *
   * @param src
   *        the source object. Bytes will be read from this object
   * @param dst
   *        the destination object. Bytes will be written to this object
   * @param buffer
   *        the byte array buffer of the copy operation
   *
   * @return the total number of bytes copied
   *
   * @throws IOException
   *         if data streams cannot be obtained, if bytes cannot be read, if
   *         bytes cannot be written or if streams cannot be closed
   */
  public static long sources(InputStreamSource src, OutputStreamSource dst, byte[] buffer) throws IOException {
    Check.notNull(src, "src == null");
    Check.notNull(dst, "dst == null");
    checkBuffer(buffer);

    try (var in = src.openInputStream(); var out = dst.openOutputStream()) {
      return Copy.copyUnchecked(in, out, buffer);
    }
  }

  /**
   * Copies all of the bytes from the specified {@link InputStream} to the
   * specified {@link OutputStream} using the specified buffer. Both the input
   * stream and the output stream are closed before the method returns (or
   * throws).
   *
   * <p>
   * More formally:
   *
   * <ol>
   * <li>copies all bytes from the input stream to the output stream using the
   * specified buffer;</li>
   * <li>closes the output stream;</li>
   * <li>closes the input stream;</li>
   * <li>if the copy and close operations complete normally returns the total
   * number of bytes copied; and</li>
   * <li>if the copy or the close operation completes abruptly throws an
   * exception.</li>
   * </ol>
   *
   * <p>
   * Please note that the result of this operation is undefined if the buffer is
   * modified during the copy operation.
   *
   * @param src
   *        the input stream. Bytes will be read from this object
   * @param dst
   *        the output stream. Bytes will be written to this object
   * @param buffer
   *        the byte array buffer of the copy operation
   *
   * @return the total number of bytes copied
   *
   * @throws IOException
   *         if bytes cannot be read, if bytes cannot be written or if streams
   *         cannot be closed
   */
  public static long streams(InputStream src, OutputStream dst, byte[] buffer) throws IOException {
    Check.notNull(src, "src == null");
    Check.notNull(dst, "dst == null");
    Check.notNull(buffer, "buffer == null");

    return copyUnchecked(src, dst, buffer);
  }

  /**
   * Copies all of the bytes from the specified {@link InputStream} to the
   * specified {@link OutputStreamSource}.
   *
   * <p>
   * This method simply delegates to
   * {@link #streamToSource(InputStream, OutputStreamSource, byte[])} using a
   * newly allocated buffer.
   *
   * @param src
   *        the input stream. Bytes will be read from this object
   * @param dst
   *        the destination object. Bytes will be written to this object
   *
   * @return the total number of bytes copied
   *
   * @throws IOException
   *         if the output stream cannot be obtained, if bytes cannot be read,
   *         if bytes cannot be written or if streams cannot be closed
   */
  public static long streamToSource(
                                    InputStream src, OutputStreamSource dst) throws IOException {
    return streamToSource(src, dst, createBuffer());
  }

  /**
   * Copies all of the bytes from the specified {@link InputStream} to the
   * specified {@link OutputStreamSource} using the specified buffer. The input
   * stream is closed before this method returns (or throws).
   *
   * <p>
   * More formally:
   *
   * <ol>
   * <li>obtain an {@code OutputStream} from the specified destination by
   * invoking its {@link OutputStreamSource#openOutputStream()} method;</li>
   * <li>copies all bytes from the input stream to the output stream using the
   * specified buffer;</li>
   * <li>closes the output stream;</li>
   * <li>closes the input stream;</li>
   * <li>if the copy and close operations complete normally returns the total
   * number of bytes copied; and</li>
   * <li>if the copy or the close operation completes abruptly throws an
   * exception.</li>
   * </ol>
   *
   * <p>
   * Please note that the result of this operation is undefined if the buffer is
   * modified during the copy operation.
   *
   * @param src
   *        the input stream. Bytes will be read from this object
   * @param dst
   *        the output stream. Bytes will be written to this object
   * @param buffer
   *        the byte array buffer of the copy operation
   *
   * @return the total number of bytes copied
   *
   * @throws IOException
   *         if the output stream cannot be obtained, if bytes cannot be read,
   *         if bytes cannot be written or if streams cannot be closed
   */
  public static long streamToSource(
                                    InputStream src, OutputStreamSource dst, byte[] buffer) throws IOException {
    Check.notNull(src, "src == null");
    Check.notNull(dst, "dst == null");
    checkBuffer(buffer);

    try (var out = dst.openOutputStream()) {
      return Copy.copyUnchecked(src, out, buffer);
    }
  }

  static void checkBuffer(byte[] buffer) {
    Check.notNull(buffer, "buffer == null");
    Check.argument(buffer.length > 0, "buffer.length == 0");
  }

  static void checkBuffer(char[] buffer) {
    Check.notNull(buffer, "buffer == null");
    Check.argument(buffer.length > 0, "buffer.length == 0");
  }

  static long copyUnchecked(InputStream in, OutputStream out, byte[] buffer) throws IOException {
    long total;
    total = 0;

    int count;
    count = in.read(buffer);

    while (count > 0) {
      out.write(buffer, 0, count);

      total += count;

      count = in.read(buffer);
    }

    return total;
  }

  static byte[] createBuffer() {
    return new byte[DEFAULT_BUFFER_SIZE];
  }

  static char[] newCharBuffer() {
    return new char[DEFAULT_BUFFER_SIZE];
  }

  static long transferTo(
                         InputStreamSource source, OutputStreamSource destination)
                                                                                   throws IOException {
    return transferTo(source, destination, Copy.createBuffer());
  }

  static long transferTo(
                         InputStreamSource source, OutputStreamSource destination, byte[] buffer)
                                                                                                  throws IOException {
    Check.notNull(destination, "destination == null");
    Check.notNull(buffer, "buffer == null");

    try (var in = source.openInputStream(); var out = destination.openOutputStream()) {
      return Copy.copyUnchecked(in, out, buffer);
    }
  }

}