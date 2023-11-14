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

public class BaseAttributeDslStepTest {

	@Test
	public void execute() {
		HtmlSelfGen spec;
		spec = new HtmlSelfGen() {
			@Override
			protected final void definition() {
				template()
						.skipAttribute("title");

				rootElement()
						.attribute("lang")
						.attribute("title");

				element("a").simpleName("Anchor");

				element("title");

				element("meta").noEndTag();

				element("option")
						.attribute("disabled").booleanType()
						.attribute("label");

				element("select")
						.attribute("disabled").booleanType();
			}
		}.prepare();

		BaseAttributeDslStep template;
		template = new BaseAttributeDslStep(spec);

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
package objectos.html;

import objectos.html.internal.AttributeName;
import objectos.html.internal.HtmlTemplateApi;
import objectos.html.internal.StandardAttributeName;
import objectos.html.tmpl.Api;
import objectos.html.tmpl.Api.ClipPathAttribute;
import objectos.html.tmpl.Api.DisabledAttribute;
import objectos.html.tmpl.Api.GlobalAttribute;
import objectos.html.tmpl.Api.OptionInstruction;

/**
 * Provides methods for rendering HTML attributes.
 */
// Generated by selfgen.html.HtmlSpec. Do not edit!
public sealed abstract class BaseAttributeDsl permits BaseElementDsl {
  BaseAttributeDsl () {}

  /**
   * Generates the {@code disabled} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final DisabledAttribute disabled() {
    attribute(StandardAttributeName.DISABLED);
    return Api.ATTRIBUTE;
  }

  /**
   * Generates the {@code label} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final OptionInstruction label(String value) {
    attribute(StandardAttributeName.LABEL, value);
    return Api.ATTRIBUTE;
  }

  /**
   * Generates the {@code lang} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute lang(String value) {
    attribute(StandardAttributeName.LANG, value);
    return Api.ATTRIBUTE;
  }

  /**
   * Generates the {@code clip-path} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final ClipPathAttribute clipPath(String value) {
    api().attribute(StandardAttributeName.CLIPPATH, value);
    return Api.ATTRIBUTE;
  }

  abstract HtmlTemplateApi api();

  abstract void attribute(AttributeName name);

  abstract void attribute(AttributeName name, String value);
}
"""
		);
	}

}
