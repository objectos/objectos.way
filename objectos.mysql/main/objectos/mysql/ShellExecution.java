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
import java.io.OutputStream;
import objectos.core.io.Copy;
import objectos.util.list.UnmodifiableList;

final class ShellExecution extends Execution {

  public final UnmodifiableList<String> executeInputStream(InputStream in) throws ExecutionException, IOException {
    Process process;

    try {
      process = start();
    } catch (IOException e) {
      throw new IOException("Failed to start mysql shell process", e);
    }

    try (OutputStream out = process.getOutputStream()) {
      byte[] buffer;
      buffer = new byte[4096];

      Copy.streams(in, out, buffer);
    } catch (IOException e) {
      throwExecutionExceptionIfNecessary();
    } finally {
      end(process);
    }

    return sysout();
  }

  public final UnmodifiableList<String> executeStatement(String[] statements) throws ExecutionException, IOException {
    Process process;

    try {
      process = start();
    } catch (IOException e) {
      throw new IOException("Failed to start mysql shell process", e);
    }

    try (OutputStream out = process.getOutputStream()) {
      for (int i = 0; i < statements.length; i++) {
        String statement;
        statement = statements[i];

        if (statement == null) {
          throw new NullPointerException("statements[" + i + "] == null");
        }

        byte[] bytes;
        bytes = statement.getBytes();

        out.write(bytes);
      }

      out.flush();
    } catch (IOException e) {
      throwExecutionExceptionIfNecessary();
    } finally {
      end(process);
    }

    return sysout();
  }

}
