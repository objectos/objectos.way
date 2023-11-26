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
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import objectos.core.io.Read;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.lang.object.Check;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;

public final class ConfigurationFile {

  final Charset charset;

  private final RegularFile file;

  ConfigurationFile(RegularFile file, Charset charset) {
    this.file = file;
    this.charset = charset;
  }

  public static ConfigurationFile configurationFile(RegularFile file, Charset charset) {
    Check.notNull(file, "file == null");
    Check.notNull(charset, "charset == null");

    return new ConfigurationFile(file, charset);
  }

  public static ConfigurationFile configurationFile(ResolvedPath path, Charset charset, ConfigurationGroup... groups)
                                                                                                                      throws IOException {
    Check.notNull(path, "path == null");
    Check.notNull(charset, "charset == null");
    Check.notNull(groups, "groups == null");

    RegularFile file;
    file = path.createRegularFile();

    ConfigurationFile conf;
    conf = new ConfigurationFile(file, charset);

    conf.replace(groups);

    return conf;
  }

  public final void append(ConfigurationGroup... groups) throws IOException {
    Check.notNull(groups, "groups == null");

    switch (groups.length) {
      case 0:
        break;
      default:
        String contents;
        contents = toContents(groups);

        append(contents);

        break;
    }
  }

  public final UnmodifiableList<String> readAllLines() throws IOException {
    return Read.lines(file, charset);
  }

  public String readToString() throws IOException {
    return Read.string(file, charset);
  }

  public final void replace(ConfigurationGroup... groups) throws IOException {
    Check.notNull(groups, "groups == null");

    switch (groups.length) {
      case 0:
        break;
      default:
        file.truncate();

        String contents;
        contents = toContents(groups);

        writeString(contents);

        break;
    }
  }

  final void acceptExecution(Execution execution) {
    String option;
    option = toCommandOption();

    execution.addOption(option);
  }

  final void acceptProcessBuilder(ProcessBuilder builder) {
    List<String> command;
    command = builder.command();

    String option;
    option = toCommandOption();

    command.add(option);
  }

  private void append(String s) throws IOException {
    try (var w = file.openWriter(charset)) {
      w.write(s);
    }
  }

  private String toCommandOption() {
    return "--defaults-file=" + file.getPath();
  }

  private String toContents(ConfigurationGroup[] groups) {
    GrowableList<String> lines;
    lines = new GrowableList<>();

    for (int i = 0; i < groups.length; i++) {
      ConfigurationGroup group;
      group = groups[i];

      if (group == null) {
        throw new NullPointerException("group[" + i + "] == null");
      }

      group.addTo(lines);
    }

    if (!lines.isEmpty()) {
      lines.add("");
    }

    return toContents(lines);
  }

  private String toContents(GrowableList<String> lines) {
    return lines.join(System.lineSeparator());
  }

  private void writeString(String s) throws IOException {
    try (Writer w = file.openWriter(charset)) {
      w.write(s);
    }
  }

}