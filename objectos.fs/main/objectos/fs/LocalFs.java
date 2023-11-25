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
import objectos.core.io.Resource;
import objectos.lang.object.Check;

/**
 * Provides {@code static} methods for obtaining {@link PathName} instances,
 * such as directories and files, from the local file system. The local file
 * system is the one accessible to the Java virtual machine process.
 *
 * @since 2
 */
public final class LocalFs extends AbstractLocalFs {

  private static final LocalFs INSTANCE = new LocalFs();

  private LocalFs() {}

  /**
   * Returns an existing directory whose pathname is given by the specified
   * {@code java.io.File}.
   *
   * <p>
   * This is a convenience method for which the implementation is equivalent to:
   *
   * <pre>
   *     ResolvedPath path = LocalFs.resolve(file);
   *     return path.toDirectory();</pre>
   *
   * @param file
   *        the {@code java.io.File} instance
   *
   * @return an existing directory
   *
   * @throws NotFoundException
   *         if the pathname does not exist
   * @throws NotDirectoryException
   *         if the pathname exists but it is not a directory (is a regular file
   *         for instance)
   * @throws IOException
   *         if an I/O error occurs
   *
   * @see ResolvedPath#toDirectory()
   */
  public static Directory getDirectory(
                                       File file) throws NotFoundException, NotDirectoryException, IOException {
    ObjectJavaAny object;
    object = INSTANCE.create(file);

    return object.toDirectory();
  }

  /**
   * Returns an existing directory whose pathname is given by combining all of
   * the specified names.
   *
   * <p>
   * This is a convenience method for which the implementation is equivalent to:
   *
   * <pre>
   *     ResolvedPath path = LocalFs.resolve(first, more);
   *     return path.toDirectory();</pre>
   *
   * @param first
   *        the first part of the pathname
   * @param more
   *        the remaining parts of the pathname
   *
   * @return an existing directory
   *
   * @throws NotFoundException
   *         if the pathname does not exist
   * @throws NotDirectoryException
   *         if the pathname exists but it is not a directory (is a regular file
   *         for instance)
   * @throws IOException
   *         if an I/O error occurs
   *
   * @see ResolvedPath#toDirectory()
   */
  public static Directory getDirectory(
                                       String first, String... more) throws NotFoundException, NotDirectoryException, IOException {
    ObjectJavaAny object;
    object = INSTANCE.create(first, more);

    return object.toDirectory();
  }

  /**
   * Returns an existing file given by the specified {@code ClassLoader}
   * resource.
   *
   * <p>
   * This is a convenience method for which the implementation is equivalent to:
   *
   * <pre>
   *     URI uri = resource.toUri();
   *     ResolvedPath path = LocalFs.resolve(uri);
   *     return path.toRegularFile();</pre>
   *
   * @param resource
   *        a class loader resource
   *
   * @return an existing file
   *
   * @throws NotFoundException
   *         if the pathname does not exist
   * @throws NotRegularFileException
   *         if the pathname exists but it is not a file (is a directory for
   *         instance)
   * @throws IOException
   *         if an I/O error occurs
   *
   * @see ResolvedPath#toRegularFile()
   */
  public static RegularFile getRegularFile(Resource resource) throws NotFoundException, NotRegularFileException, IOException {
    Check.notNull(resource, "resource == null");

    URI uri;
    uri = resource.toUri();

    return getRegularFile(uri);
  }

  /**
   * Returns an existing file whose pathname is given by combining all of
   * the specified names.
   *
   * <p>
   * This is a convenience method for which the implementation is equivalent to:
   *
   * <pre>
   *     ResolvedPath path = LocalFs.resolve(first, more);
   *     return path.toRegularFile();</pre>
   *
   * @param first
   *        the first part of the pathname
   * @param more
   *        the remaining parts of the pathname
   *
   * @return an existing file
   *
   * @throws NotFoundException
   *         if the pathname does not exist
   * @throws NotRegularFileException
   *         if the pathname exists but it is not a file (is a directory for
   *         instance)
   * @throws IOException
   *         if an I/O error occurs
   *
   * @see ResolvedPath#toRegularFile()
   */
  public static RegularFile getRegularFile(String first, String... more) throws NotFoundException, NotRegularFileException, IOException {
    ObjectJavaAny object;
    object = INSTANCE.create(first, more);

    return object.toRegularFile();
  }

  /**
   * Returns an existing file whose pathname is given by the specified
   * {@code java.net.URI}.
   *
   * <p>
   * This is a convenience method for which the implementation is equivalent to:
   *
   * <pre>
   *     ResolvedPath path = LocalFs.resolve(uri);
   *     return path.toRegularFile();
   * </pre>
   *
   * @param uri
   *        the {@code java.net.URI} instance
   *
   * @return an existing file
   *
   * @throws NotFoundException
   *         if the pathname does not exist
   * @throws NotRegularFileException
   *         if the pathname exists but it is not a file (is a directory for
   *         instance)
   * @throws IOException
   *         if an I/O error occurs
   *
   * @see ResolvedPath#toRegularFile()
   */
  public static RegularFile getRegularFile(URI uri) throws NotFoundException, NotRegularFileException, IOException {
    ObjectJavaAny object;
    object = INSTANCE.create(uri);

    return object.toRegularFile();
  }

  /**
   * Returns a {@code ResolvedPath} by converting a {@code java.io.File}.
   *
   * @param file
   *        the {@code java.io.File} to convert
   *
   * @return a {@code ResolvedPath}
   */
  public static ResolvedPath resolve(File file) {
    return INSTANCE.create(file);
  }

  /**
   * Returns a {@code ResolvedPath} whose pathname is given by combining all of
   * the specified names.
   *
   * <p>
   * All of the specified names are combined together, in a system dependant
   * manner, resulting in a pathname. This pathname is used to create the
   * {@code ResolvedPath} instance.
   *
   * @param first
   *        the first part of the pathname
   * @param more
   *        the remaining parts of the pathname
   *
   * @return a {@code ResolvedPath}
   */
  public static ResolvedPath resolve(String first, String... more) {
    return INSTANCE.create(first, more);
  }

  /**
   * Returns a {@code ResolvedPath} by converting a {@code java.net.URI}.
   *
   * @param uri
   *        the {@code java.net.URI} to convert
   *
   * @return a {@code ResolvedPath}
   */
  public static ResolvedPath resolve(URI uri) {
    return INSTANCE.create(uri);
  }

}