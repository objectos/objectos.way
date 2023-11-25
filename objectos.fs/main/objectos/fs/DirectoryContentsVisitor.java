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
 * A visitor of the contents of a directory.
 *
 * @since 2
 *
 * @see Directory#visitContents(DirectoryContentsVisitor)
 */
public interface DirectoryContentsVisitor {

  /**
   * Visits a directory.
   *
   * @param directory
   *        the directory instance
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  void visitDirectory(Directory directory) throws IOException;

  /**
   * Visits a regular file.
   *
   * @param file
   *        the regular file instance
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  void visitRegularFile(RegularFile file) throws IOException;

}