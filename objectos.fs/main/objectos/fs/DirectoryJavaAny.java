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
 * A façade for directory only operations.
 *
 * <p>
 * This is only a façade. More specifically, the underlying object may or may
 * not represent an actual directory. For example, the directory may have been
 * deleted between the time an instance of this interface is obtained and the
 * time a method in this interface is invoked.
 *
 * <p>
 * Unless noted, methods of this interface will reject values that resolve to a
 * pathname that is not the directory itself or a direct child. This restriction
 * is tested against pathnames only. In other words, the pathname might resolve
 * to a child but it might be a <i>symlink</i> to a directory or a file that is
 * not a direct child of this directory.
 *
 * @since 2
 */
interface DirectoryJavaAny extends PathName, PathNameResolver {

  /**
   * Recursively copies the contents of this directory to the specified
   * destination directory.
   *
   * <p>
   * This operation will not overwrite any file in the destination directory nor
   * will it proceed if a subdirectory in the destination with the same as in
   * this directory already exists. This operation will fail at the first
   * directory or file that cannot be created at the destination because it
   * already exists. In this case, the result will be an incomplete (partial)
   * copy operation.
   *
   * @param dest
   *        the destination directory
   *
   * @throws FoundException
   *         if any directory or file with the same relative pathname exists in
   *         the destination
   * @throws java.io.IOException
   *         if an I/O error occurs
   */
  void copyTo(Directory dest) throws FoundException, IOException;

  /**
   * Creates a directory with the specified name. The resulting directory will
   * be a direct child of this directory.
   *
   * @param directoryName
   *        the name of the directory to be created
   *
   * @return a {@code Directory} instance representing the newly created
   *         directory
   *
   * @throws IllegalArgumentException
   *         if the resolved pathname is not a direct child of this directory
   * @throws FoundException
   *         if the resolved pathname already exists
   * @throws java.io.IOException
   *         if an I/O error occurs
   */
  Directory createDirectory(String directoryName) throws FoundException, IOException;

  /**
   * Creates a (regular) file with the specified file name and with the
   * specified creation options. The resulting file will be a direct child of
   * this directory.
   *
   * <p>
   * The specified file name is resolved against this directory.
   *
   * @param fileName
   *        the name of the file to be created
   * @param options
   *        file creation options
   *
   * @return a new file with the specified name and with the specified creation
   *         options
   *
   * @throws IllegalArgumentException
   *         if the resolved pathname is not a direct child of this directory
   * @throws FoundException
   *         if the resolved pathname already exists
   * @throws java.io.IOException
   *         if an I/O error occurs
   */
  RegularFile createRegularFile(
                                String fileName, RegularFileCreateOption... options)
                                                                                     throws FoundException, IOException;

  /**
   * Deletes this directory or fails if the directory is not empty.
   *
   * @throws java.io.IOException
   *         if an I/O error occurs
   *
   * @see #deleteContents()
   */
  void delete() throws IOException;

  /**
   * Recursively deletes the contents of this directory. This directory will be
   * empty when this operation completes normally.
   *
   * @throws java.io.IOException
   *         if an I/O error occurs
   */
  void deleteContents() throws IOException;

  /**
   * Returns an existing subdirectory having the specified name. The resulting
   * directory will be a direct child of this directory.
   *
   * @param directoryName
   *        the name of the directory to be located
   *
   * @return an existent subdirectory
   *
   * @throws IllegalArgumentException
   *         if the resolved pathname is not a direct child of this directory
   * @throws NotFoundException
   *         if the resolved pathname does not exist
   * @throws NotDirectoryException
   *         if the resolved pathname exists but it is not a directory (it is a
   *         regular file, for example)
   * @throws java.io.IOException
   *         if an I/O error occurs
   */
  Directory getDirectory(String directoryName)
                                               throws NotFoundException, NotDirectoryException, IOException;

  /**
   * Returns the name of this directory.
   *
   * @return the name of this directory
   */
  @Override
  String getName();

  /**
   * Returns an existing (regular) file or creates new one with the specified
   * file name. The resulting file will be a direct child of this directory.
   *
   * <p>
   * The specified file name is resolved against this directory.
   *
   * @param fileName
   *        the name of the file to be created
   *
   * @return an existing file with the specified file name or a new file with
   *         the specified name
   *
   * @throws IllegalArgumentException
   *         if the resolved pathname is not a direct child of this directory
   * @throws NotRegularFileException
   *         if the resolved pathname exists but it is not a regular file (it is
   *         a directory, for example)
   * @throws java.io.IOException
   *         if an I/O error occurs
   */
  RegularFile getOrCreateRegularFile(String fileName)
                                                      throws NotRegularFileException, IOException;

  /**
   * Returns the parent directory of this directory.
   *
   * @return the parent directory of this directory
   *
   * @throws NotFoundException
   *         if a parent for this directory cannot be found
   */
  Directory getParent() throws NotFoundException;

  /**
   * Returns the full path of this directory.
   *
   * @return the full path of this directory
   */
  @Override
  String getPath();

  /**
   * Returns an existing (regular) file with the specified file name. The
   * resulting file will be a direct child of this directory.
   *
   * @param fileName
   *        the name of the file to be located
   *
   * @return an existing (regular) file with the specified file name
   *
   * @throws IllegalArgumentException
   *         if the resolved pathname is not a direct child of this directory
   * @throws NotFoundException
   *         if the resolved pathname does not exist
   * @throws NotRegularFileException
   *         if the resolved pathname exists but it is not a regular file (it is
   *         a directory, for example)
   * @throws java.io.IOException
   *         if an I/O error occurs
   */
  RegularFile getRegularFile(String fileName)
                                              throws NotFoundException, NotRegularFileException, IOException;

  /**
   * Returns {@code true} if this directory contains no entries.
   *
   * @return {@code true} if this directory contains no entries
   */
  boolean isEmpty();

  /**
   * Visits each child of this directory with the specified visitor.
   *
   * @param visitor
   *        the visitor instance
   *
   * @throws java.io.IOException
   *         if an I/O error occurs
   */
  void visitContents(DirectoryContentsVisitor visitor) throws IOException;

}