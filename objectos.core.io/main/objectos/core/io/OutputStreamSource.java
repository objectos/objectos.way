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
package objectos.core.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An object that can provide {@link OutputStream} instances.
 *
 * @since 2
 *
 * @see Copy
 * @see Write
 */
public interface OutputStreamSource {

  /**
   * Returns a new {@link OutputStream} for writing to this source.
   *
   * @return a new {@link OutputStream} for writing to this source
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  OutputStream openOutputStream() throws IOException;

}