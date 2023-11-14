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

import java.util.Iterator;
import java.util.stream.Collectors;
import objectos.code.ClassName;
import objectos.code.Code;

final class ApiStep extends ThisTemplate {

  public ApiStep(HtmlSelfGen spec) {
    super(spec);
  }

  @Override
  final String contents() {
    className(API);

    ClassName iterator;
    iterator = ClassName.of(Iterator.class);

    ClassName internalFragment;
    internalFragment = ClassName.of(HTML_INTERNAL, "InternalFragment");

    ClassName internalNoOp;
    internalNoOp = ClassName.of(HTML_INTERNAL, "InternalNoOp");

    String extendsAll;
    extendsAll = extendsAll();

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
     * Provides the interfaces of the {@link objectos.html.HtmlTemplate} domain-specific language.
     */
    \{GENERATED_MSG}
    public final class \{simpleName} {
      public static final ElementContents GLOBAL_INSTRUCTION = new ElementContents();
      
      private \{simpleName} () {}

      /**
       * Represents an instruction that generates part of the output of an HTML template.
       *
       * <p>
       * Unless noted references to a particular instruction MUST NOT be reused.
       */
      public sealed interface Instruction {}

    \{elements()}
    \{attributes()}
      /**
       * An instruction which may represent either an HTML attribute or an HTML element depending
       * on the context it is used.
       *
       * <p>
       * For example, the {@code title} instruction will usually render the {@code title}
       * global attribute. However, when used as a child of the {@code head} element
       * it will render the {@code title} element.
       */
      public sealed interface AmbiguousInstruction
          extends
    \{extendsAll}
          permits \{INTERNAL_INSTRUCTION} {}

      /**
       * Represents an HTML global attribute such as the {@code id} attribute for example.
       */
      public sealed interface GlobalAttribute
          extends
    \{extendsAll}
          permits ExternalAttribute, \{INTERNAL_INSTRUCTION} {}

      /**
       * An instruction for an HTML attribute provided by an external object.
       */
      public sealed interface ExternalAttribute extends GlobalAttribute {

        /**
         * Represents a single {@code id} attribute.
         */
        non-sealed interface Id extends ExternalAttribute {
          /**
           * The value of this {@code id} attribute.
           *
           * @return the value of this {@code id} attribute
           */
          String id();
        }

        /**
         * Represents a single {@code class} attribute.
         */
        non-sealed interface StyleClass extends ExternalAttribute {
          /**
           * The value of this {@code class} attribute.
           *
           * @return the value of this {@code class} attribute
           */
          String className();
        }

        /**
         * Represents a set of {@code class} attributes.
         */
        non-sealed interface StyleClassSet extends ExternalAttribute {
          /**
           * Iterator over the {@code class} attribute values of this set.
           *
           * @return an iterator over the values of this set
           */
          \{iterator}<String> classNames();
        }
      }

      /**
       * An instruction which can be used as a child of any element.
       */
      public static final class ElementContents
          implements
    \{extendsElementContents()} {
        private ElementContents() {}
      }

      /**
       * The fragment instruction.
       */
      public sealed interface Fragment
          extends
    \{extendsAll}
          permits \{internalFragment} {}

      /**
       * The no-op instruction.
       */
      public sealed interface NoOp
          extends
    \{extendsAll}
          permits \{internalNoOp} {}
    }
    """;
  }

  private String elements() {
    StringBuilder sb;
    sb = new StringBuilder();

    for (var element : spec.elements()) {
      ClassName thisClassName;
      thisClassName = element.instructionClassName;

      String thisSimpleName;
      thisSimpleName = thisClassName.simpleName();

      sb.append(
        code."""
          /**
           * Allowed as a child of the {@code \{element.name()}} element.
           */
          public sealed interface \{thisSimpleName} extends \{INSTRUCTION} {}

        """
      );
    }

    return sb.toString();
  }

  private String attributes() {
    StringBuilder sb;
    sb = new StringBuilder();

    for (var attribute : spec.attributes()) {
      ClassName thisClassName;
      thisClassName = attribute.instructionClassName;

      if (thisClassName == null) {
        continue;
      }

      String thisSimpleName;
      thisSimpleName = thisClassName.simpleName();

      String superTypes;
      superTypes = attribute.elementInstructionMap
        .values()
        .stream()
        .map(ClassName::simpleName)
        .collect(Collectors.joining(",\n"));

      superTypes = Code.indent(superTypes, 6);

      sb.append(
        code."""
          /**
           * The {@code \{attribute.name()}} attribute.
           */
          public sealed interface \{thisSimpleName}
              extends
        \{superTypes}
              permits \{INTERNAL_INSTRUCTION} {}

        """
      );
    }

    return sb.toString();
  }

  private String extendsAll() {
    String all = spec.elements().stream()
      .map(spec -> spec.instructionClassName)
      .map(ClassName::simpleName)
      .collect(Collectors.joining(",\n"));

    return Code.indent(all, 6);
  }

  private String extendsElementContents() {
    String s = spec.elements().stream()
        .filter(ElementSpec::hasEndTag)
        .map(spec -> spec.instructionClassName)
        .map(ClassName::simpleName)
        .collect(Collectors.joining(",\n"));

    return Code.indent(s, 6);
  }
}