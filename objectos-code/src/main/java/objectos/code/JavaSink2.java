/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package objectos.code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.lang.Check;

public abstract class JavaSink2 extends InternalInterpreter2 {

  public sealed abstract static class Option {

    private static final class OverwriteExisting extends Option {
      @Override
      final void acceptOfDirectory(JavaSinkOfDirectory2 sink) {
        sink.overwriteExising = true;
      }
    }

    private static final class SkipExisting extends Option {
      @Override
      final void acceptOfDirectory(JavaSinkOfDirectory2 sink) {
        sink.skipExising = true;
      }
    }

    static final Option OVERWRITE_EXISTING = new OverwriteExisting();

    static final Option SKIP_EXISTING = new SkipExisting();

    abstract void acceptOfDirectory(JavaSinkOfDirectory2 sink);

  }

  /**
   * Sole constructor
   */
  protected JavaSink2() {}

  public static JavaSink2 ofDirectory(Path directory) {
    Check.argument(
      Files.isDirectory(directory),
      directory, " does not exist, exists but is not a directory, or could not be accessed."
    );

    return new JavaSinkOfDirectory2(directory);
  }

  public static JavaSink2 ofDirectory(Path directory, Option option) {
    Check.argument(
      Files.isDirectory(directory),
      directory, " does not exist, exists but is not a directory, or could not be accessed."
    );

    var sink = new JavaSinkOfDirectory2(directory);

    option.acceptOfDirectory(sink);

    return sink;
  }

  public static JavaSink2 ofStringBuilder(StringBuilder output) {
    Check.notNull(output, "output == null");

    return new JavaSinkOfStringBuilder2(output);
  }

  public static Option overwriteExisting() {
    return Option.OVERWRITE_EXISTING;
  }

  public static Option skipExisting() {
    return Option.SKIP_EXISTING;
  }

  public void eval(JavaTemplate template) {
    Check.notNull(template, "template == null");

    pass0(template);

    pass1();

    pass2();
  }

  public void write(JavaTemplate template) throws IOException {
    eval(template);
  }

}