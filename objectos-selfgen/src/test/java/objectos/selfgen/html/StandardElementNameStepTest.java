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

public class StandardElementNameStepTest {

  @Test(description = "it should generate an enum constant for each element defined")
  public void execute() {
    var template = new StandardElementNameStep();

    template.spec = new HtmlSelfGen() {
      @Override
      protected final void definition() {
        element("div");
        element("meta").noEndTag();
      }
    }.prepare();

    assertEquals(
      template.toString(),

      """
      package objectos.html.tmpl;

      public enum StandardElementName implements ElementName {
        DIV(ElementKind.NORMAL, "div"),

        META(ElementKind.VOID, "meta");

        private static final StandardElementName[] ARRAY = StandardElementName.values();

        private final ElementKind kind;

        private final String name;

        private StandardElementName(ElementKind kind, String name) {
          this.kind = kind;
          this.name = name;
        }

        public static StandardElementName getByCode(int code) {
          return ARRAY[code];
        }

        public static int size() {
          return ARRAY.length;
        }

        @Override
        public final int getCode() {
          return ordinal();
        }

        @Override
        public final ElementKind getKind() {
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
