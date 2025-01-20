/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Iterator;

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

  // non-public types

  sealed interface ClassReader permits LangClassReader {

    @FunctionalInterface
    interface StringConstantProcessor {

      void processStringConstant(String value);

    }

    void init(String binaryName, byte[] contents);

    boolean isAnnotationPresent(Class<? extends Annotation> annotationType);

    void processStringConstants(StringConstantProcessor processor);

  }

  @Retention(RetentionPolicy.SOURCE)
  @Target({ElementType.TYPE, ElementType.METHOD})
  @interface VisibleForTesting {}

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