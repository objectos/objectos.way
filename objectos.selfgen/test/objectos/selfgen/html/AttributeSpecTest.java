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

import java.util.List;
import java.util.Set;
import org.testng.annotations.Test;

public class AttributeSpecTest extends AbstractSelfGenHtmlTest {

  @Test
  public void classAttribute() {
    specDsl()
        .rootElement()
        .attribute("class").as("_class")
        .attributeEnd();

    AttributeSpec res = attributeSpec("class");

    assertEquals(res.name(), "class");
    assertEquals(res.kindSet(), Set.of(AttributeKind.STRING));
    assertEquals(res.interfaceSet(), Set.of());
    assertEquals(res.methodNames(), List.of("_class"));
  }

  @Test
  public void hiddenAttribute() {
    specDsl()
        .rootElement()
        .attribute("hidden")
        .booleanType();

    AttributeSpec res = attributeSpec("hidden");

    assertEquals(res.name(), "hidden");
    assertEquals(res.kindSet(), Set.of(AttributeKind.BOOLEAN));
    assertEquals(res.interfaceSet(), Set.of());
    assertEquals(res.methodNames(), List.of("hidden"));
  }

  @Test
  public void hrefAttribute() {
    var dsl = specDsl();

    dsl.element("a")
        .attribute("href")
        .attributeEnd();

    dsl.element("link")
        .attribute("href")
        .attributeEnd();

    AttributeSpec res = attributeSpec("href");

    assertEquals(res.name(), "href");
    assertEquals(res.kindSet(), Set.of(AttributeKind.STRING));
    assertEquals(
      res.interfaceSet(),
      Set.of("AValue", "LinkValue")
    );
  }

  @Test
  public void idAttribute() {
    specDsl()
        .rootElement()
        .attribute("id")
        .attributeEnd();

    AttributeSpec res = attributeSpec("id");

    assertEquals(res.name(), "id");
    assertEquals(res.kindSet(), Set.of(AttributeKind.STRING));
    assertEquals(res.interfaceSet(), Set.of());
    assertEquals(res.methodNames(), List.of("id"));
  }

}