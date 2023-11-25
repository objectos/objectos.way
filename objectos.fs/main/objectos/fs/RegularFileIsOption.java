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
 * Queries a regular file for a certain attribute.
 *
 * @since 2
 */
public interface RegularFileIsOption {

  /**
   * Tests if the specified delegate object has the attribute represented by
   * this object.
   *
   * @param o
   *        the delegate to test. Normally a {@link java.io.File} or a
   *        {@link java.nio.file.Path}
   *
   * @return {@code true} if the test passes
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  boolean is(Object o) throws IOException;

}