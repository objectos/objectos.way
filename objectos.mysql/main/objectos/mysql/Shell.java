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
import java.io.InputStream;
import objectos.fs.Directory;
import objectos.fs.RegularFile;
import objectos.util.list.UnmodifiableList;

final class Shell extends Executable {

  Shell(RegularFile executable, Version version) {
    super(executable, version);
  }

  public static Shell get(Directory directory) throws IOException {
    RegularFile executable;
    executable = directory.getRegularFile("mysql");

    Version version;
    version = VersionImpl.parse(executable);

    return new Shell(executable, version);
  }

  public final UnmodifiableList<String> executeInputStream(ConfigurationFile configurationFile,
                                                           LoginPathFile loginPathFile,
                                                           LoginPath loginPath,
                                                           InputStream in) throws ExecutionException, IOException {

    ShellExecution execution;
    execution = getShellExecution(configurationFile, loginPathFile);

    loginPath.acceptExecution(execution);

    return execution.executeInputStream(in);

  }

  public final UnmodifiableList<String> executeStatement(ConfigurationFile configurationFile,
                                                         LoginPathFile loginPathFile,
                                                         LoginPath loginPath,
                                                         String[] statements) throws ExecutionException, IOException {

    ShellExecution execution;
    execution = getShellExecution(configurationFile, loginPathFile);

    loginPath.acceptExecution(execution);

    return execution.executeStatement(statements);

  }

  private ShellExecution getShellExecution(ConfigurationFile configurationFile, LoginPathFile loginPathFile) {
    ShellExecution execution;
    execution = new ShellExecution();

    setExecutable(execution);

    configurationFile.acceptExecution(execution);

    loginPathFile.acceptExecution(execution);

    return execution;
  }

}
