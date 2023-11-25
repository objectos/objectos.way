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
import java.nio.channels.FileChannel;

/**
 * A source that can provide file channels opened in read-only mode.
 *
 * @since 3
 */
public interface ReadableFileChannelSource {

  /**
   * Returns a new {@link FileChannel} for reading from this source.
   *
   * @return a new {@link FileChannel} for reading from this source
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  FileChannel openReadChannel() throws IOException;

}