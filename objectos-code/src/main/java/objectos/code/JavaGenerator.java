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
import objectox.code.Pass2;

public class JavaGenerator {

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
    pass2.compilationUnitStart();

    template.eval(pass2);

    pass2.compilationUnitEnd();

    pass2.executePass1();

    pass2.executePass2(writer);

    return writer.toString();
  }

}