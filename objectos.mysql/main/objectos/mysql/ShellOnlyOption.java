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

final class ShellOnlyOption extends AbstractOption implements ShellOption {

  private static final ShellOnlyOption BINARY_MODE = new ShellOnlyOption("binary-mode");

  private static final ShellOnlyOption SKIP_COLUMN_NAMES = new ShellOnlyOption("skip-column-names");

  private ShellOnlyOption(String key) {
    super(key);
  }

  private ShellOnlyOption(String key, String value) {
    super(key, value);
  }

  public static ShellOption binaryMode() {
    return BINARY_MODE;
  }

  public static ShellOption skipColumnNames() {
    return SKIP_COLUMN_NAMES;
  }

}
