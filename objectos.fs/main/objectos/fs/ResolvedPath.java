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
 * A façade for operating on a pathname in a file system for which is not known
 * if it is a directory, a file or if it does not exist.
 *
 * <p>
 * Instances of this interface are usually obtained from the
 * {@link Directory#resolve(String, String...)} method hence the name
 * <em>resolved path</em>.
 *
 * @since 2
 */
public interface ResolvedPath
    extends
    PathName,
    WritablePathName {

  /**
   * Creates a directory whose pathname is given by this object or fails if the
   * pathname already exists.
   *
   * <p>
   * Nonexistent parent directories are <i>not</i> created by this method, use
   * {@link #createParents()} if necessary.
   *
   * @return a façade representing a newly created directory
   *
   * @throws FoundException
   *         if this pathname exists (even if it is a directory)
   * @throws IOException
   *         if an I/O error occurs
   */
  Directory createDirectory() throws FoundException, IOException;

  /**
   * Creates all nonexistent parent directories of this pathname.
   *
   * @return a reference to this object
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  ResolvedPath createParents() throws IOException;

  /**
   * Creates a new (regular) file whose pathname is given by this object with
   * the specified creation options or fails if the pathname already exists.
   *
   * <p>
   * Nonexistent parent directories are <i>not</i> created by this method, use
   * {@link #createParents()} if necessary.
   *
   * @param options
   *        the file creation options
   *
   * @return a façade representing a newly created file
   *
   * @throws FoundException
   *         if this pathname exists (even if it is a regular file)
   * @throws IOException
   *         if an I/O error occurs
   */
  RegularFile createRegularFile(RegularFileCreateOption... options)
      throws FoundException, IOException;

  /**
   * Returns the directory denoted by this pathname; fails if the directory does
   * not exist or if the pathname is not a directory.
   *
   * @return a façade for directory only operations
   *
   * @throws NotFoundException
   *         if this pathname does not exist
   * @throws NotDirectoryException
   *         if this pathname exists but it is not a directory (it is a regular
   *         file for instance)
   * @throws IOException
   *         if an I/O error occurs
   */
  Directory toDirectory() throws NotFoundException, NotDirectoryException, IOException;

  /**
   * Returns the directory denoted by this pathname; creates a new directory if
   * it does not exist or fails if the pathname is not a directory.
   *
   * <p>
   * Nonexistent parent directories are <i>not</i> created by this method, use
   * {@link #createParents()} if necessary.
   *
   * @return a façade for directory only operations
   *
   * @throws NotDirectoryException
   *         if this pathname exists but it is not a directory (it is a file for
   *         instance)
   * @throws IOException
   *         if an I/O error occurs
   */
  Directory toDirectoryCreateIfNotFound() throws NotDirectoryException, IOException;

  /**
   * Returns the file denoted by this pathname; fails if the file does not exist
   * or if the pathname is not a regular file.
   *
   * @return a façade for regular file only operations
   *
   * @throws NotFoundException
   *         if this pathname does not exist
   * @throws NotRegularFileException
   *         if this pathname exists but it is not a regular file (it is a
   *         directory for instance)
   * @throws IOException
   *         if an I/O error occurs
   */
  RegularFile toRegularFile() throws NotFoundException, NotRegularFileException, IOException;

}