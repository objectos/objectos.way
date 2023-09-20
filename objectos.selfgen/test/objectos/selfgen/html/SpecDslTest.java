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

import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;

public class SpecDslTest extends AbstractSelfGenHtmlTest {

  @Test
  public void category_createCategoryAndAddElements() {
    var dsl = specDsl();
    CategorySpec flow = dsl.category("flow");
    assertEquals(flow.name(), "flow");

    dsl.element("div").category(flow);
    dsl.element("form").category(flow);

    assertEquals(flow.name(), "flow");

    List<String> names;
    names = new ArrayList<>();

    for (Child child : flow.childStream()) {
      names.add(child.name());
    }

    assertEquals(names, List.of("div", "form"));
  }

  @Test
  public void element_addCategoryAsContentModel() {
    var dsl = specDsl();
    CategorySpec flow = dsl.category("flow");
    ElementSpec div = dsl.element("div")
        .category(flow)
        .contentModel(flow);
    ElementSpec form = dsl.element("form")
        .category(flow);

    dsl.prepare();

    assertEquals(toName(div.parentStream()), List.of("div"));
    assertEquals(toName(form.parentStream()), List.of("div"));
  }

  @Test
  public void element_formCanHaveFlowContentExceptForForms() {
    var dsl = specDsl();
    CategorySpec flow = dsl.category("flow");
    ElementSpec div = dsl.element("div")
        .category(flow)
        .contentModel(flow);

    ElementSpec form = dsl.element("form");
    form.category(flow)
        .contentModel(flow)
        .except(form);

    dsl.prepare();

    assertEquals(toName(div.parentStream()), List.of("div", "form"));
    assertEquals(toName(form.parentStream()), List.of("div"));
  }

  @Test
  public void text_withCategory() {
    var dsl = specDsl();
    CategorySpec flow = dsl.category("flow");
    ElementSpec div = dsl.element("div")
        .category(flow)
        .contentModel(flow);

    TextSpec text = dsl.text()
        .category(flow);

    dsl.prepare();

    assertEquals(toName(div.parentStream()), List.of("div"));
    assertEquals(toName(text.parentStream()), List.of("div"));
  }

  private Iterable<String> toName(Iterable<ElementSpec> specs) {
    List<String> result;
    result = new ArrayList<>();

    for (ElementSpec spec : specs) {
      result.add(spec.name());
    }

    return result;
  }

}