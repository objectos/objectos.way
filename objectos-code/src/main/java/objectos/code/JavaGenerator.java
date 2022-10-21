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

import objectox.code.Pass0;

public final class JavaGenerator {

  Pass0 pass0 = new Pass0();

  JavaGenerator() {}

  final void _class(int length) {
    pass0._class(length);
  }

  final void id(String name) {
    pass0.id(name);
  }

  final void templateEnd() {
    pass0.compilationUnitEnd();
  }

  final void templateStart() {
    pass0.compilationUnitStart();
  }

}