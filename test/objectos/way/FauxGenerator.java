/*
 * Copyright (C) 2015-2025 Objectos Software LTDA.
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

import static java.lang.System.out;

abstract class FauxGenerator {

  int value = -1;

  public abstract void execute();

  final void comment(String string) {
    out.println();
    out.println("// " + string);
    out.println();
  }

  final void value(String string) {
    out.println("public static final byte " + string + " = " + value-- + ";");
  }

}