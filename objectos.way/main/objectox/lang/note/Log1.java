/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectox.lang.note;

import objectos.lang.Note1;

public final class Log1 extends Log {

  final Object value;

  Log1(Note1<?> note, Object value) {
    super(note);

    this.value = value;
  }

  @Override
  final String format(Layout layout) {
    return layout.formatLog1(this);
  }

}