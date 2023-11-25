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
package objectos.fs;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import objectos.lang.object.ToString;

/**
 * Represents a pathname in a file system.
 *
 * @since 2
 */
interface PathNameJavaAny extends Comparable<PathName>, ToString.Formattable {

  /**
   * Accept method used to implement the visitor pattern.
   *
   * @param <R>
   *        the result type of this operation
   * @param <P>
   *        the type of the additional object
   * @param visitor
   *        the visitor operating on this {@code PathName} instance
   * @param p
   *        additional object passed to the visitor
   *
   * @return the value returned from the visitor method
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  <R, P> R acceptPathNameVisitor(PathNameVisitor<R, P> visitor, P p) throws IOException;

  /**
   * Returns {@code true} if the pathname represented by this object exists.
   *
   * @return {@code true} if the pathname represented by this object exists
   */
  boolean exists();

  /**
   * Returns the name of this pathname.
   *
   * @return the name of this pathname
   */
  String getName();

  /**
   * Returns the full path of this pathname.
   *
   * @return the full path of this pathname
   */
  String getPath();

  /**
   * Returns a {@link java.io.File} representation of this pathname.
   *
   * @return a {@link java.io.File} representation of this pathname
   */
  File toFile();

  /**
   * Returns an {@link java.net.URI} representation of this pathname.
   *
   * @return an {@link java.net.URI} representation of this pathname
   */
  URI toUri();

}