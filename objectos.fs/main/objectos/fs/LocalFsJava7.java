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
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import objectos.lang.object.Check;

class LocalFsJava7 extends LocalFsJavaAny {

  /**
   * Returns a {@code ResolvedPath} by converting a {@code java.nio.file.Path}.
   *
   * @param path
   *        the {@code java.nio.file.Path} to convert
   *
   * @return a {@code ResolvedPath}
   */
  public static ResolvedPath resolve(Path path) {
    Check.notNull(path, "path == null");

    return new ObjectImpl(path);
  }

  @Override
  final ObjectJavaAny create(File file) {
    Check.notNull(file, "file == null");

    Path path;
    path = file.toPath();

    return newImpl(path);
  }

  final ObjectJavaAny create(Path path) {
    Check.notNull(path, "path == null");

    return newImpl(path);
  }

  @Override
  final ObjectJavaAny create(String first, String... more) {
    Path path;
    path = Paths.get(first, more);

    return newImpl(path);
  }

  @Override
  final ObjectJavaAny create(URI uri) {
    Path path;
    path = Paths.get(uri);

    return newImpl(path);
  }

  private ObjectImpl newImpl(Path path) {
    return new ObjectImpl(path);
  }

}