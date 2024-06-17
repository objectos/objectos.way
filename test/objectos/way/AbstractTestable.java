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
package objectos.way;

abstract class AbstractTestable {

  private final StringBuilder log = new StringBuilder();

  final void logMethod(String methodName, Object... args) {
    log.append(methodName);
    log.append('(');

    if (args.length > 0) {
      log.append(arg(args[0]));

      for (int i = 1; i < args.length; i++) {
        log.append(", ");
        log.append(arg(args[i]));
      }
    }

    log.append(')');
    log.append(System.lineSeparator());
  }

  private String arg(Object o) {
    String s;
    s = String.valueOf(o);

    s = s.replaceAll("\\R+", " ");
    
    return s.trim();
  }

  @Override
  public final String toString() {
    return log.toString();
  }

}