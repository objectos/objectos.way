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
package objectos.selfgen.css2;

import java.io.IOException;
import java.nio.file.Path;
import objectos.code.JavaSink;

public abstract class CssSelfGen {

  private CompiledSpecBuilder builder;

  public final void execute(String[] args) throws IOException {
    var srcDir = args[0];

    var directory = Path.of(srcDir);

    var sink = JavaSink.ofDirectory(
      directory,
      JavaSink.overwriteExisting()
    );

    var spec = compile();

    spec.write(sink, new GeneratedCssTemplateStep());
  }

  protected final CompiledSpec compile() {
    try {
      builder = new CompiledSpecBuilder();

      definition();

      return builder.build();
    } finally {
      builder = null;
    }
  }

  protected abstract void definition();

  protected final void selectors(String... names) {
    for (var name : names) {
      builder.addSelector(name);
    }
  }

}
