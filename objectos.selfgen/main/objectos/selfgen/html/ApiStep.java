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
      public static final Attribute ATTRIBUTE = new Attribute();

      public static final Element ELEMENT = new Element();

      public static final Fragment FRAGMENT = new Fragment();

      public static final NoOp NOOP = new NoOp();

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
       * Represents an HTML global attribute such as the {@code id} attribute for example.
       */
      public sealed interface GlobalAttribute
          extends
    \{extendsAll} {}

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
       * The attribute instruction.
       */
      public static final class Attribute
          implements 
    \{extendsAttribute()},
          \{GLOBAL_ATTRIBUTE} {
        private Attribute() {}
      }

      /**
       * The element instruction.
       */
      public static final class Element
          implements
    \{extendsElementContents()} {
        private Element() {}
      }

      /**
       * The fragment instruction.
       */
      public static final class Fragment
          implements
    \{extendsAll} {
        private Fragment() {}
      }

      /**
       * The no-op instruction.
       */
      public static final class NoOp
          implements
    \{extendsAll} {
        private NoOp() {}
      }
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
        \{superTypes} {}

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
  
  private String extendsAttribute() {
    String s = spec.attributes().stream()
        .map(spec -> spec.instructionClassName)
        .filter(cn -> cn != null)
        .map(ClassName::simpleName)
        .collect(Collectors.joining(",\n"));

    return Code.indent(s, 6);
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