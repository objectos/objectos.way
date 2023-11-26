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
import java.util.Map;
import objectos.fs.Directory;
import objectos.fs.PathName;
import objectos.fs.SimplePathNameVisitor;
import objectos.lang.object.Check;

public final class LoginPathFile {

  private static final Factory FACTORY = new Factory();

  private final PathName path;

  LoginPathFile(PathName path) {
    this.path = path;
  }

  public static LoginPathFile loginPathFile(PathName path) {
    try {
      Check.notNull(path, "path == null");

      return path.acceptPathNameVisitor(FACTORY, null);
    } catch (IOException e) {
      AssertionError error;
      error = new AssertionError("Factory does not throw IOException");

      error.initCause(e);

      throw error;
    }
  }

  final void acceptExecution(Execution execution) {
    execution.putEnvironment("MYSQL_TEST_LOGIN_FILE", path.getPath());
  }

  final void acceptProcessBuilder(ProcessBuilder builder) {
    Map<String, String> environment;
    environment = builder.environment();

    environment.put("MYSQL_TEST_LOGIN_FILE", path.getPath());
  }

  final boolean exists() {
    return path.exists();
  }

  private static class Factory extends SimplePathNameVisitor<LoginPathFile, Void> {
    @Override
    public final LoginPathFile visitDirectory(Directory directory, Void p) {
      throw new IllegalArgumentException("path resolves to a directory");
    }

    @Override
    protected final LoginPathFile defaultAction(PathName pathName, Void p) {
      return new LoginPathFile(pathName);
    }
  }

}
