/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html.play;

import objectos.html.play.Piece;

interface State {

  static UnsupportedOperationException implMe(byte proto) {
    final String msg;
    msg = "Implement me :: proto=%d".formatted(proto);

    throw new UnsupportedOperationException(msg);
  }

  State compute();

  default boolean hasNext() {
    throw new IllegalStateException("This is an intermediate state: " + getClass());
  }

  default Piece next() {
    throw new IllegalStateException("This is an intermediate state: " + getClass());
  }

}
