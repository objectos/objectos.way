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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class StandardAttributeNameStepTest {

  @Test(description = "it should generate an class for each distinct attribute defined")
  public void execute() {
    HtmlSelfGen spec;
    spec = new HtmlSelfGen() {
      @Override
      protected final void definition() {
        rootElement()
            .attribute("hidden").booleanType()
            .attributeEnd();

        element("meta")
            .attribute("charset")
            .noEndTag();
      }
    }.prepare();

    StandardAttributeNameStep template;
    template = new StandardAttributeNameStep(spec);

    assertEquals(
      template.toString(),

      """
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
      package objectos.html.internal;

      import objectos.util.UnmodifiableMap;

      /**
       * TODO
       */
      // Generated by selfgen.html.HtmlSpec. Do not edit!
      public enum StandardAttributeName implements AttributeName {
        CHARSET(AttributeKind.STRING, "charset"),

        HIDDEN(AttributeKind.BOOLEAN, "hidden");

        private static final StandardAttributeName[] ARRAY = StandardAttributeName.values();

        private static final UnmodifiableMap<String, StandardAttributeName> MAP = mapInit();

        private final AttributeKind kind;

        private final String name;

        StandardAttributeName(AttributeKind kind, String name) {
          this.kind = kind;
          this.name = name;
        }

        public static StandardAttributeName getByCode(int code) {
          return ARRAY[code];
        }

        public static StandardAttributeName getByName(String name) {
          return MAP.get(name);
        }

        public static int size() {
          return ARRAY.length;
        }

        private static UnmodifiableMap<String, StandardAttributeName> mapInit() {
          var builder = new NamesBuilder();
          builder.put("charset", CHARSET);
          builder.put("hidden", HIDDEN);

          return builder.build();
        }

        @Override
        public final int getCode() {
          return ordinal();
        }

        @Override
        public final AttributeKind getKind() {
          return kind;
        }

        @Override
        public final String getName() {
          return name;
        }
      }
      """
    );
  }

}