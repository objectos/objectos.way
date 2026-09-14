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
package objectox.html.stream;

import objectos.html.stream.EndStartTag;
import objectos.html.stream.EndStartTagKind;

public record EndStartTagEvent(EndStartTagKind kind) implements EndStartTag {

  public static EndStartTagEvent of(EndStartTagKind kind) {
    return new EndStartTagEvent(kind);
  }

  @Override
  public final String toString() {
    return "EndStartTag" + suffix();
  }

  private String suffix() {
    return switch (kind) {
      case ELEMENT -> "";

      case EMPTY_ELEMENT -> "(empty)";

      case VOID_ELEMENT -> "(void)";
    };
  }

}
