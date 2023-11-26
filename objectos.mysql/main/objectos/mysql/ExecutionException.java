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
import objectos.util.list.UnmodifiableList;

public class ExecutionException extends IOException {

  private static final long serialVersionUID = 1L;

  private final int exitValue;

  private final UnmodifiableList<String> stderr;

  private final UnmodifiableList<String> stdout;

  public ExecutionException(int exitValue,
                            UnmodifiableList<String> stderr,
                            UnmodifiableList<String> stdout) {
    this.exitValue = exitValue;
    this.stderr = stderr;
    this.stdout = stdout;
  }

  public final int exitValue() {
    return exitValue;
  }

  public final UnmodifiableList<String> getErrorLines() {
    return stderr;
  }

  public final UnmodifiableList<String> getOutputLines() {
    return stdout;
  }

  public final void printError() {
    for (int i = 0; i < stderr.size(); i++) {
      String e;
      e = stderr.get(i);

      System.err.println(e);
    }
  }

  public final void printOutput() {
    for (int i = 0; i < stdout.size(); i++) {
      String e;
      e = stdout.get(i);

      System.out.println(e);
    }
  }

}
