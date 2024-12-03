/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * The <strong>Objectos Lang</strong> main class.
 */
public final class Lang {

  // public types

  /**
   * An object that can write out its string representation.
   */
  @FunctionalInterface
  public interface CharWritable {

    /**
     * Writes this object's textual representation to the appendable.
     *
     * @param dest the appendable where to write characters into.
     *
     * @throws IOException if an I/O error occurs
     */
    void writeTo(Appendable dest) throws IOException;

  }

  /**
   * An {@link Iterable} which can be traversed only once. Trying to traverse
   * this object more than once is not defined.
   */
  public interface IterableOnce<T> extends Iterable<T> {

    /**
     * Returns an iterator over elements of type T.
     *
     * @return an iterator
     *
     * @throws IllegalStateException
     *         if this {@code Iterable} has already been traversed
     */
    @Override
    Iterator<T> iterator();

  }

  /**
   * A representation of media data intended for transmission over an Internet
   * protocol.
   *
   * <p>
   * The {@code MediaObject} interface defines a generic media entity, providing
   * access to its content type and raw data in byte form. Implementations of
   * this interface should specify the type of media content they represent
   * (e.g., text, image, or binary data) and the necessary encoding details.
   *
   * <p>
   * This interface is designed to be protocol-agnostic, allowing the
   * implementing class to represent media data for various communication
   * protocols, including HTTP, WebSocket, and others.
   */
  public interface MediaObject {

    /**
     * Returns the MIME content type of this media object, such as
     * {@code text/html} or {@code application/json}.
     *
     * @return the content type of this media object, in MIME type format
     */
    String contentType();

    /**
     * Provides the raw data of the media object as a byte array, ready to be
     * transmitted over a network protocol.
     *
     * @return the raw data to be sent over the wire as a byte array
     */
    byte[] mediaBytes();

  }

  /**
   * Represents a contract for components that can be tested.
   */
  public interface Testable {

    /**
     * Provides an interface for generating tabular representations of data.
     * Implementations are responsible for formatting rows and columns with
     * proper alignment and spacing.
     */
    public sealed interface Writer permits LangTestableWriter {

      /**
       * Creates a new instance of {@code Writer} with a default column
       * separator.
       *
       * @return a new instance of {@code Writer}.
       */
      static Writer create() {
        return new LangTestableWriter("|");
      }

      void heading(String value);

      /**
       * Clears the current content of the writer, resetting it to an empty
       * state.
       */
      void clear();

      /**
       * Adds a single cell value to the current row.
       * The cell content will be right-padded with spaces to match the
       * specified length.
       *
       * @param value the string value of the cell; may be {@code null}.
       * @param length the fixed width of the cell.
       *
       * @throws IllegalArgumentException if {@code length} is negative or if
       *         {@code value.length()} exceeds {@code length}.
       */
      void cell(String value, int length);

      /**
       * Moves the writer to the next row. Any subsequent column operations will
       * append to a new line.
       */
      void nextRow();

      /**
       * Adds multiple columns to a single row. Columns are specified as
       * alternating
       * value and length pairs.
       *
       * @param values an array where even indices represent column values
       *        (strings)
       *        and odd indices represent their respective lengths (integers).
       *
       * @throws IllegalArgumentException if the values array is not structured
       *         as
       *         alternating string and integer pairs or contains unsupported
       *         types.
       */
      void row(Object... values);

    }

  }

  // non-public types

  sealed interface ClassReader permits LangClassReader {

    void init(String binaryName, byte[] contents);

    boolean isAnnotationPresent(Class<? extends Annotation> annotationType);

    void processStringConstants(Consumer<String> processor);

  }

  private Lang() {}

  static ClassReader createClassReader(Note.Sink noteSink) {
    return new LangClassReader(noteSink);
  }

  private static final int UNSIGNED = 0xFF;

  static int toBigEndianInt(byte b0, byte b1) {
    int v0;
    v0 = toInt(b0, 8);

    int v1;
    v1 = toInt(b1, 0);

    return v1 | v0;
  }

  static int toBigEndianInt(byte b0, byte b1, byte b2, byte b3) {
    int v0;
    v0 = toInt(b0, 24);

    int v1;
    v1 = toInt(b1, 16);

    int v2;
    v2 = toInt(b2, 8);

    int v3;
    v3 = toInt(b3, 0);

    return v3 | v2 | v1 | v0;
  }

  static int toUnsignedInt(byte b) {
    return b & UNSIGNED;
  }

  private static int toInt(byte b, int shift) {
    return toUnsignedInt(b) << shift;
  }

}