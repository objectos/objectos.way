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
import java.io.OutputStream;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import objectos.core.io.OutputStreamSource;
import objectos.core.io.Write;
import objectos.core.io.WriterSource;

/**
 * A façade for writing operations. Instances of this interface may represent
 * an existing file or the pathname of a file that will be created.
 *
 * <p>
 * This interface can be used as the destination in methods from the
 * {@link Copy} and {@link Write} classes.
 *
 * @since 2
 *
 * @see Copy
 * @see Write
 */
public interface WritablePathName
    extends
    PathName,
    OutputStreamSource,
    WriterSource {

  /**
   * Returns a new output stream for writing to this pathname.
   *
   * <p>
   * The file is created if it does not exist. If the file exists then the
   * returned output stream will be in append mode.
   *
   * @return a new output stream for writing to this file
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  @Override
  OutputStream openOutputStream() throws IOException;

  /**
   * Returns a new channel for writing to this pathname.
   *
   * <p>
   * The returned channel is open in write-only mode. The file is created if it
   * does not exist. If the file exists then the returned channel's initial
   * position is the file size.
   *
   * @return a new channel for writing to this file. The returned channel is
   *         open in write-only mode and its initial position is the file size
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  FileChannel openWriteChannel() throws IOException;

  /**
   * Returns a new writer for writing characters to this pathname.
   *
   * <p>
   * The file is created if it does not exist. If the file exists then the
   * returned writer will be in append mode.
   *
   * @param charset
   *        the charset to use for encoding
   *
   * @return a new writer for writing characters to this pathname
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  @Override
  Writer openWriter(Charset charset) throws IOException;

}