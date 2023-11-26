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

import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.lang.object.Check;

public final class GlobalOption extends AbstractOption
    implements
    ClientOrConfigEditorOption,
    ServerOption {

  private static final GlobalOption NO_DEFAULTS = new GlobalOption("no-defaults");

  private GlobalOption(String key) {
    super(key);
  }

  private GlobalOption(String key, String value) {
    super(key, value);
  }

  public static GlobalOption bindAddress(String address) {
    Check.notNull(address, "address == null");

    return new GlobalOption("bind-address", address);
  }

  public static GlobalOption defaultsFile(RegularFile file) {
    Check.notNull(file, "file == null");

    return new GlobalOption("defaults-file", file.getPath());
  }

  public static GlobalOption noDefaults() {
    return NO_DEFAULTS;
  }

  public static GlobalOption port(int value) {
    Check.notNull(value, "value == null");

    return new GlobalOption("port", Integer.toString(value));
  }

  public static GlobalOption socket(ResolvedPath path) {
    Check.notNull(path, "path == null");

    return new GlobalOption("socket", path.getPath());
  }

}
