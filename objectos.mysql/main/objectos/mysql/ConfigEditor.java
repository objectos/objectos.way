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

import java.io.IOException;
import objectos.fs.Directory;
import objectos.fs.RegularFile;

final class ConfigEditor extends Executable {

  ConfigEditor(RegularFile executable, Version version) {
    super(executable, version);
  }

  public static ConfigEditor get(Directory directory) throws IOException {
    RegularFile executable;
    executable = directory.getRegularFile("mysql_config_editor");

    Version version;
    version = VersionImpl.parse(executable);

    return new ConfigEditor(executable, version);
  }

  public final void setLoginPath(
                                 LoginPathFile file, ConfigEditorOption[] options) throws ExecutionException, IOException {
    ConfigEditorExecution execution;
    execution = new ConfigEditorExecution();

    file.acceptExecution(execution);

    execution.addOption("setsid");

    setExecutable(execution);

    execution.addOption("set");

    execution.addOption("--skip-warn");

    for (int i = 0; i < options.length; i++) {
      ConfigEditorOption option;
      option = options[i];

      if (option == null) {
        throw new NullPointerException("options[" + i + "] == null");
      }

      option.acceptExecution(execution);
    }

    execution.execute();
  }

}
