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
package objectos.css.select;

import objectos.css.tmpl.Api.ExternalClassSelector;
import objectos.html.tmpl.Api.ExternalAttribute;
import objectos.lang.object.Check;

public final class ClassSelector implements ExternalAttribute.StyleClass, ExternalClassSelector {

	private final String className;

	private ClassSelector(String className) {
		Check.notNull(className, "className == null");

		Check.argument(!className.isBlank(), "className must not be blank");

		this.className = className;
	}

	public static ClassSelector of(String className) {
		return new ClassSelector(className);
	}

	@Override
	public final String className() {
		return className;
	}

	@Override
	public final String toString() {
		return "." + className;
	}

}