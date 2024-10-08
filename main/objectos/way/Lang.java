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
import objectos.notes.NoteSink;

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
   * An {@link Iterable} which can be traversed only once.
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

  // non-public types

  sealed interface ClassReader permits LangClassReader {

    void init(String binaryName, byte[] contents);

    boolean isAnnotationPresent(Class<? extends Annotation> annotationType);

    void processStringConstants(Consumer<String> processor);

  }

  private Lang() {}

  static ClassReader createClassReader(NoteSink noteSink) {
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