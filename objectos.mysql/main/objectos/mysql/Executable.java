/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.mysql;

import java.util.List;
import objectos.fs.RegularFile;

abstract class Executable {

  private final RegularFile file;

  private final Version version;

  Executable(RegularFile file, Version version) {
    this.file = file;
    this.version = version;
  }

  public final Release getRelease() {
    return version.getRelease();
  }

  final void setExecutable(Execution execution) {
    execution.addOption(file.getPath());
  }

  final void setExecutable(ProcessBuilder builder) {
    List<String> command;
    command = builder.command();

    command.add(file.getPath());
  }

}