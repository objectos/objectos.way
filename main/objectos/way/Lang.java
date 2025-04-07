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
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * The <strong>Objectos Lang</strong> main class.
 */
public final class Lang {

  // public types

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

  public interface MediaWriter {

    /**
     * Returns the MIME content type of this media writer, such as
     * {@code text/html} or {@code application/json}.
     *
     * @return the content type of this media writer, in MIME type format
     */
    String contentType();

    Charset mediaCharset();

    /**
     * Writes this media's contents to the appendable.
     *
     * @param dest the appendable where to write characters into.
     *
     * @throws IOException if an I/O error occurs
     */
    void mediaTo(Appendable dest) throws IOException;

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
  @Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD})
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