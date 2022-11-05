/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package objectos.code;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

class JavaSinkOfDirectory extends JavaSinkOfStringBuilder {

  private final Path directory;

  private Path file;

  public JavaSinkOfDirectory(Path directory) {
    super(new StringBuilder());

    this.directory = directory;
  }

  @Override
  public final void write(JavaTemplate template) throws IOException {
    eval(template);

    var parent = file.getParent();

    Files.createDirectories(parent);

    Files.writeString(
      file, toString(), StandardCharsets.UTF_8,
      StandardOpenOption.CREATE_NEW
    );
  }

  @Override
  protected void writeCompilationUnitEnd(PackageName packageName, String fileName) {
    var packageDirectory = packageName.resolve(directory);

    file = packageDirectory.resolve(fileName);
  }

}