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

import java.nio.file.Files;
import java.nio.file.Path;
import objectos.lang.Check;
import objectox.code.Pass0;
import objectox.code.Pass1;
import objectox.code.Pass2;

public class JavaGenerator {

  Pass0 pass0 = new Pass0();

  Pass1 pass1 = new Pass1();

  Pass2 pass2 = new Pass2();

  private final JavaWriter writer = new JavaWriter();

  JavaGenerator() {}

  public static JavaGenerator of() {
    return new JavaGenerator();
  }

  public static JavaGenerator ofDirectory(Path directory) {
    Check.argument(
      Files.isDirectory(directory),
      directory, " does not exist, is not a directory, or could not be accessed."
    );

    return null;
  }

  final String toString(JavaTemplate template) {
    pass0.compilationUnitStart();

    template.eval(pass0);

    pass0.compilationUnitEnd();

    pass1.execute(pass0);

    pass2.execute(pass1, writer);

    return writer.toString();
  }

}