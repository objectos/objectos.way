/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.util.Set;

public class HtmlMarkupOfAttributeGen {
  private String booleanAttrs;

  private String regularAttrs;

  public static void main(String[] args) {
    System.out.println(new HtmlMarkupOfAttributeGen());
  }

  @Override
  public final String toString() {
    prepare();

    return """
    /// Declares the structure of an HTML document using pure Java (attributes).
    public sealed static abstract class MarkupOfAttribute extends MarkupOfAmbiguous {
    %s
      MarkupOfAttribute() {}

      abstract Html.Instruction.OfAttribute attribute0(Html.AttributeName name);

      abstract Html.AttributeOrNoOp attribute0(Html.AttributeName name, Object value);

      /// Declares an attribute with the specified name.
      /// @param name the attribute name
      /// @return an instruction representing the attribute
      public final Html.Instruction.OfAttribute attr(Html.AttributeName name) {
        Objects.requireNonNull(name, "name == null");
        return attribute0(name);
      }

      /// Declares an attribute with the specified name and value.
      /// @param name the attribute name
      /// @param value the attribute value
      /// @return an instruction representing the attribute
      public final Html.Instruction.OfAttribute attr(Html.AttributeName name, String value) {
        Objects.requireNonNull(name, "name == null");
        Objects.requireNonNull(value, "value == null");
        return attribute0(name, value);
      }
    %s
    }
    """.formatted(booleanAttrs, regularAttrs);
  }

  private void prepare() {
    final StringBuilder booleanBuilder;
    booleanBuilder = new StringBuilder();

    final StringBuilder regularBuilder;
    regularBuilder = new StringBuilder();

    final Set<String> ambiguous;
    ambiguous = HtmlSpec.ambiguousAttrNames();

    for (HtmlSpec.AttributeSpec attr : HtmlSpec.attributes()) {
      if (ambiguous.contains(attr.htmlName())) {
        continue;
      }

      else if (attr.booleanAttribute()) {
        booleanBuilder.append("""

          /// The `%s` boolean attribute.
          public static final Html.AttributeObject %s = Html.AttributeObject.of(Html.AttributeName.%s);
        """.formatted(attr.htmlName(), attr.methodName(), attr.constantName()));
      }

      else {
        regularBuilder.append("""

          /// Declares the `%s` attribute with the specified value.
          /// @param value the attribute value
          /// @return an instruction representing the attribute
          public final Html.Instruction.OfAttribute %s(String value) {
            Objects.requireNonNull(value, "value == null");
            return attribute0(Html.AttributeName.%s, value);
          }
        """.formatted(attr.htmlName(), attr.methodName(), attr.constantName()));
      }
    }

    booleanAttrs = booleanBuilder.toString();

    regularAttrs = regularBuilder.toString();
  }
}
