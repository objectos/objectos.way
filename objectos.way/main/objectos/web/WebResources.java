/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.web;

import java.io.IOException;
import java.nio.file.Path;
import objectos.notes.NoteSink;

public interface WebResources {

  interface Bootstrapper extends AutoCloseable, WebResources {

    void noteSink(NoteSink noteSink);

    void copyDirectory(Path directory);

    @Override
    void close() throws IOException;

  }

  static Bootstrapper create() {
    throw new UnsupportedOperationException("Implement me");
  }

  Path resolve(Path path);

}