/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import objectos.http.Result;
import objectos.http.Status;

public record StatusThrowable(Status status, Throwable cause) implements Result {

  @Override
  public final String toTestableText() {
    try (StringWriter writer = new StringWriter()) {
      final PrintWriter pw;
      pw = new PrintWriter(writer);

      cause.printStackTrace(pw);

      return status.toTestableText() + "\n" + writer;
    } catch (IOException e) {
      // StringWriter does not throw
      return status.toTestableText();
    }
  }

}
