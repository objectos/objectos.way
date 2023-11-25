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

interface PathNameResolver {

  /**
   * Returns a new {@link ResolvedPath} object representing the resolved
   * pathname of a descendant of this directory or returns this object if the
   * specified names result in the empty string.
   *
   * <p>
   * All of the specified names are combined together, in a system dependant
   * manner, resulting in a relative pathname. This pathname is resolved against
   * this directory.
   *
   * <p>
   * Usage example (for a system with a name separator {@code "/"}):
   *
   * <pre>
   *     Directory tmp = ... // for example "/tmp"
   *     tmp.resolve("a"); // resolves to "/tmp/a"
   *     tmp.resolve("x", "..", "z"); // resolves to "/tmp/x/../z" (eq. to "/tmp/z")
   *     tmp.resolve(""); // resolves to "/tmp"
   *     tmp.resolve("..", "root", ".ssh"); // fails with IllegalArgumentException (not descendant)
   *     tmp.resolve("/root"); // fails with IllegalArgumentException (absolute)
   * </pre>
   *
   * @param first
   *        the first part of the pathname relative to this directory
   * @param more
   *        the remaining parts of the pathname relative to this directory
   *
   * @return a {@link ResolvedPath} representing the pathname of a descendant of
   *         this directory
   *
   * @throws IllegalArgumentException
   *         if the resolved pathname is not a descendant of this directory
   */
  ResolvedPath resolve(String first, String... more);

}