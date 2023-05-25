/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.css;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.code.JavaSink;
import objectos.code.JavaTemplate;
import objectos.selfgen.css.spec.CssSpecDsl;
import objectos.selfgen.css.spec.CssStep;
import objectos.selfgen.css.spec.StepAdapter;

public class CssBoot extends StepAdapter {

  private final JavaSink sink;

  CssBoot(JavaSink sink) {
    this.sink = sink;
  }

  public static void main(String[] args) throws IOException {
    var srcDirectoryPath = args[0];

    var resolved = Path.of(srcDirectoryPath);

    Files.createDirectories(resolved);

    var sink = JavaSink.ofDirectory(
      resolved,
      JavaSink.overwriteExisting()
    );

    var boot = new CssBoot(sink);

    boot.execute();
  }

  @Override
  public final void write(JavaTemplate template) {
    try {
      sink.write(template);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void execute() {
    var step = new CssStep(this);

    var dsl = new CssSpecDsl(step);

    var module = new CssModule();

    module.acceptCssSpecDsl(dsl);

    dsl.execute();
  }

}
