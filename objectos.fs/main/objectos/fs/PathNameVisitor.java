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

import java.io.IOException;

/**
 * A visitor of {@link PathName} instances.
 *
 * @param <R>
 *        the type of the result of the visit operation
 * @param <P>
 *        the type of the additional value
 *
 * @since 2
 */
public interface PathNameVisitor<R, P> {

  /**
   * Visits a directory.
   *
   * @param directory
   *        the directory instance
   * @param p
   *        an additional value
   *
   * @return the result of the visit operation
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  R visitDirectory(Directory directory, P p) throws IOException;

  /**
   * Visits a nonexistent file.
   *
   * @param notFound
   *        the nonexistent file
   * @param p
   *        an additional value
   *
   * @return the result of the visit operation
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  R visitNotFound(ResolvedPath notFound, P p) throws IOException;

  /**
   * Visits a regular file.
   *
   * @param file
   *        the regular file
   * @param p
   *        an additional value
   *
   * @return the result of the visit operation
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  R visitRegularFile(RegularFile file, P p) throws IOException;

}