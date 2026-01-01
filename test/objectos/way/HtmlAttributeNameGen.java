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
package objectos.way;

import objectos.way.HtmlSpec.AttributeSpec;

public class HtmlAttributeNameGen {

  private final StringBuilder fields = new StringBuilder();

  public static void main(String[] args) {
    System.out.println(new HtmlAttributeNameGen());
  }

  @Override
  public final String toString() {
    prepare();

    return fields.toString();
  }

  private void prepare() {
    for (AttributeSpec spec : HtmlSpec.attributes()) {
      spec(spec);
    }
  }

  private void spec(AttributeSpec spec) {
    fields.append("""

      /// The `%2$s` HTML attribute.
      Html.AttributeName %1$s = HtmlAttributeName.%1$s;
    """.formatted(spec.constantName(), spec.htmlName()));
  }

}
