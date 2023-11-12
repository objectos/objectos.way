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
package objectos.selfgen.css;

import static objectos.selfgen.css.Util.generate;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CssSelfGen14UrlTypeTest {

	private Map<String, String> result;

	@BeforeClass
	public void _generate() throws IOException {
		var spec = new CssSelfGen() {
			@Override
			protected void definition() {
				var image = t(
						"Image",

						url()
				);

				property(
						"list-style-image",

						sig(image, "value")
				);
			}
		};

		result = generate(spec);
	}

	@Test
	public void generatedCssTemplate() {
		assertEquals(
				result.get("objectox/css/GeneratedCssTemplate.java"),

				"""
      /*
       * Copyright (C) 2016-2023 Objectos Software LTDA.
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
      package objectox.css;

      import objectos.core.object.Check;
      import objectos.css.tmpl.Api.Image;
      import objectos.css.tmpl.Api.PropertyValue;
      import objectos.css.tmpl.Api.Selector;
      import objectos.css.tmpl.Api.StyleDeclaration;

      // Generated by selfgen.css.CssSpec. Do not edit!
      abstract class GeneratedCssTemplate {

        protected static final Selector any = StandardName.any;

        protected final StyleDeclaration listStyleImage(Image value) {
          Check.notNull(value, "value == null");
          declaration(Property.LIST_STYLE_IMAGE, value);
          return InternalInstruction.INSTANCE;
        }

        abstract void declaration(Property name, PropertyValue value);

      }
      """
		);
	}

}