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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import objectos.core.io.InputStreamSource;
import objectos.core.io.Read;
import objectos.core.io.ReaderSource;
import objectos.core.io.Write;

/**
 * A façade for regular file only operations.
 *
 * <p>
 * This is only a façade. More specifically, the underlying object may or may
 * not represent an actual regular file. For example, the file may have been
 * deleted between the time an instance of this interface is obtained and the
 * time a method in this interface is invoked.
 *
 * <p>
 * This interface can be used as both the source and destination in methods from
 * the {@link Copy}, {@link Read} and {@link Write} classes.
 *
 * @since 2
 *
 * @see Copy
 * @see Read
 * @see Write
 */
public interface RegularFile extends
    PathName,
    InputStreamSource,
    ReadableFileChannelSource,
    ReaderSource,
    WritablePathName {

  /**
   * Deletes this file.
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  void delete() throws IOException;

  /**
   * Returns the last modification time in millis.
   *
   * @return the last modification time in millis.
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  long getLastModifiedMillis() throws IOException;

  /**
   * Returns the name of this file.
   *
   * @return the name of this file
   */
  @Override
  String getName();

  /**
   * Returns the parent of this object.
   *
   * <p>
   * If this object represents a nonexistent pathname then its parent might also
   * be nonexistent.
   *
   * @return the parent of this object
   *
   * @throws NotFoundException
   *         if a parent for this object cannot be found
   */
  Directory getParent() throws NotFoundException;

  /**
   * Tests if this file has the specified attribute.
   *
   * @param attribute
   *        the attribute to check for
   *
   * @return {@code true} if this file has the specified attribute
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  boolean is(RegularFileIsOption attribute) throws IOException;

  /**
   * Returns a new input stream for reading from this file.
   *
   * @return a new input stream for reading from this file
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  @Override
  InputStream openInputStream() throws IOException;

  /**
   * Returns a new output stream, in append mode, for writing to this file.
   *
   * @return a new output stream, in append mode, for writing to this file
   *
   * @throws IOException
   *         if an I/O error occurs
   *
   * @see #truncate()
   */
  @Override
  OutputStream openOutputStream() throws IOException;

  /**
   * Returns a new channel for reading from and writing to this file.
   *
   * <p>
   * The returned channel is open in read and write mode and its initial
   * position is {@code 0L}.
   *
   * @return a new channel for reading from and writing to this file. The
   *         returned channel is open in read and write mode and its initial
   *         position is {@code 0L}
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  FileChannel openReadAndWriteChannel() throws IOException;

  /**
   * Returns a new channel for reading from this file.
   *
   * <p>
   * The returned channel is open in read-only mode and its initial position is
   * {@code 0L}.
   *
   * @return a new channel for reading from this file. The returned channel is
   *         open in read-only mode and its initial position is {@code 0L}
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  @Override
  FileChannel openReadChannel() throws IOException;

  /**
   * Returns the file size in number of bytes.
   *
   * @return the file size in number of bytes
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  long size() throws IOException;

  /**
   * Returns a {@link java.io.File} representation of this file.
   *
   * @return a {@link java.io.File} representation of this file
   */
  @Override
  File toFile();

  /**
   * Truncates this file. The size of this file will be zero after this method
   * returns.
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  void truncate() throws IOException;

}