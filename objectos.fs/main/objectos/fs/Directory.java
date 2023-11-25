package objectos.fs;

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
public interface Directory extends PathName, PathNameResolver {

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
  void copyTo(objectos.fs.Directory dest) throws objectos.fs.FoundException, java.io.IOException;

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
  objectos.fs.Directory createDirectory(java.lang.String directoryName) throws objectos.fs.FoundException, java.io.IOException;

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
  objectos.fs.RegularFile createRegularFile(java.lang.String fileName, objectos.fs.RegularFileCreateOption... options) throws objectos.fs.FoundException, java.io.IOException;

  /**
   * Deletes this directory or fails if the directory is not empty.
   *
   * @throws java.io.IOException
   *         if an I/O error occurs
   *
   * @see #deleteContents()
   */
  void delete() throws java.io.IOException;

  /**
   * Recursively deletes the contents of this directory. This directory will be
   * empty when this operation completes normally.
   *
   * @throws java.io.IOException
   *         if an I/O error occurs
   */
  void deleteContents() throws java.io.IOException;

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
  objectos.fs.Directory getDirectory(java.lang.String directoryName) throws objectos.fs.NotFoundException, objectos.fs.NotDirectoryException, java.io.IOException;

  /**
   * Returns the name of this directory.
   *
   * @return the name of this directory
   */
  @Override
  java.lang.String getName();

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
  objectos.fs.RegularFile getOrCreateRegularFile(java.lang.String fileName) throws objectos.fs.NotRegularFileException, java.io.IOException;

  /**
   * Returns the parent directory of this directory.
   *
   * @return the parent directory of this directory
   *
   * @throws NotFoundException
   *         if a parent for this directory cannot be found
   */
  objectos.fs.Directory getParent() throws objectos.fs.NotFoundException;

  /**
   * Returns the full path of this directory.
   *
   * @return the full path of this directory
   */
  @Override
  java.lang.String getPath();

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
  objectos.fs.RegularFile getRegularFile(java.lang.String fileName) throws objectos.fs.NotFoundException, objectos.fs.NotRegularFileException, java.io.IOException;

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
  void visitContents(objectos.fs.DirectoryContentsVisitor visitor) throws java.io.IOException;

  /**
   * Registers this pathname to the specified watch service for the specified
   * events.
   *
   * @param service
   *        the watch service instance
   * @param events
   *        the events to watch for
   *
   * @return a key representing the registration of this object with the given
   *         watch service
   *
   * @throws java.io.IOException
   *         if an I/O error occurs
   *
   * @see java.nio.file.Path#register(java.nio.file.WatchService,
   *      java.nio.file.WatchEvent.Kind...)
   */
  java.nio.file.WatchKey register(java.nio.file.WatchService service, java.nio.file.WatchEvent.Kind<?>... events) throws java.io.IOException;

  /**
   * Returns a new {@link ResolvedPath} object by resolving the specified path
   * against this directory.
   *
   * @param path
   *        the relative path to a descendant of this directory
   *
   * @return a {@link ResolvedPath} representing the pathname of a descendant of
   *         this directory
   *
   * @throws IllegalArgumentException
   *         if the resolved pathname is not a descendant of this directory
   */
  objectos.fs.ResolvedPath resolve(java.nio.file.Path path);

}