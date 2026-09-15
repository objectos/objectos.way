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

import objectos.html.stream.RenderAttribute;
import objectox.html.attr.AttributeName;

public record RenderAttributeEvent(int level, AttributeName attributeName, String value) implements RenderAttribute {

  public static RenderAttributeEvent of(int level, AttributeName attributeName, String value) {
    return new RenderAttributeEvent(level, attributeName, value);
  }

  public final RenderAttributeEvent merge(String more) {
    return new RenderAttributeEvent(level, attributeName, value + ' ' + more);
  }

  @Override
  public final String name() {
    return attributeName.name();
  }

  @Override
  public final String toString() {
    return "RenderAttribute(" + name() + ", " + value + ")";
  }

}
