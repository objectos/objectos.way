/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.html;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import objectos.code.ClassName;

final class BaseAttributeDslStep extends ThisTemplate {
  public BaseAttributeDslStep(HtmlSelfGen spec) {
    super(spec);
  }

  @Override
  final String contents() {
    className(
      ClassName.of("objectos.html", "BaseAttributeDsl")
    );

    ClassName CLIP_PATH_ATTRIBUTE = ClassName.of(API, "ClipPathAttribute");

    return code."""
    /*
     * Copyright (C) 2015-2023 Objectos Software LTDA.
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
    package \{packageName};
    \{importList}
    /**
     * Provides methods for rendering HTML attributes.
     */
    \{GENERATED_MSG}
    public sealed abstract class \{simpleName} permits BaseElementDsl {
      \{simpleName} () {}

    \{attributes()}
      /**
       * Generates the {@code clip-path} attribute with the specified value.
       *
       * @param value
       *        the value of the attribute
       *
       * @return an instruction representing this attribute.
       */
      protected final \{CLIP_PATH_ATTRIBUTE} clipPath(String value) {
        api().attribute(\{STD_ATTR_NAME}.CLIPPATH, value);

        return \{INTERNAL_INSTRUCTION}.INSTANCE;
      }

      abstract \{HTML_TEMPLATE_API} api();

      abstract void attribute(\{ATTRIBUTE_NAME} name);

      abstract void attribute(\{ATTRIBUTE_NAME} name, String value);
    }
    """;
  }

  private String attributes() {
    List<String> methods;
    methods = new ArrayList<>();

    TemplateSpec template;
    template = spec.template();

    for (var attribute : spec.attributes()) {
      for (String name : attribute.methodNames()) {
        if (!template.shouldIncludeAttribute(name)) {
          continue;
        }

        ClassName returnType;
        returnType = attribute.instructionClassName;

        if (returnType == null) {
          if (attribute.global()) {
            returnType = GLOBAL_ATTRIBUTE;
          } else {
            returnType = attribute.elementInstructionMap
                .values()
                .iterator()
                .next();
          }
        }

        String constantName;
        constantName = attribute.constantName;

        AttributeKind kind;
        kind = attribute.kind();

        if (kind.isString()) {

          methods.add(
            code."""
              /**
               * Generates the {@code \{attribute.name()}} attribute with the specified value.
               *
               * @param value
               *        the value of the attribute
               *
               * @return an instruction representing this attribute.
               */
              protected final \{returnType} \{name}(String value) {
                attribute(\{STD_ATTR_NAME}.\{constantName}, value);
                return \{INTERNAL_INSTRUCTION}.INSTANCE;
              }
            """
         );

        } else {

          methods.add(
            code."""
              /**
               * Generates the {@code \{attribute.name()}} boolean attribute.
               *
               * @return an instruction representing this attribute.
               */
              protected final \{returnType} \{name}() {
                attribute(\{STD_ATTR_NAME}.\{constantName});
                return \{INTERNAL_INSTRUCTION}.INSTANCE;
              }
            """
         );

        }
      }
    }

    return methods.stream().collect(Collectors.joining("\n"));
  }

}
