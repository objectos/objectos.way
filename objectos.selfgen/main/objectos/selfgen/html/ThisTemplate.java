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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import objectos.code.ClassName;
import objectos.code.Code;
import objectos.code.Code.ImportList;
import objectos.lang.Check;

abstract class ThisTemplate {

	static final String GENERATED_MSG = "// Generated by selfgen.html.HtmlSpec. Do not edit!";

	static final ClassName OVERRIDE = ClassName.of(Override.class);

	static final ClassName STRING = ClassName.of(String.class);

	static final ClassName UNMODIFIABLE_MAP = ClassName.of("objectos.util.map", "UnmodifiableMap");

	static final String HTML_INTERNAL = "objectos.html.internal";

	static final ClassName INTERNAL_INSTRUCTION = ClassName.of(HTML_INTERNAL, "InternalInstruction");

	static final String HTML_TMPL = "objectos.html.tmpl";

	static final ClassName API = ClassName.of(HTML_TMPL, "Api");

	static final ClassName ATTRIBUTE_KIND = ClassName.of(HTML_INTERNAL, "AttributeKind");

	static final ClassName ATTRIBUTE_NAME = ClassName.of(HTML_INTERNAL, "AttributeName");

	static final ClassName INSTRUCTION = ClassName.of(API, "Instruction");

	static final ClassName GLOBAL_ATTRIBUTE = ClassName.of(API, "GlobalAttribute");

	static final ClassName EXTERNAL_ATTRIBUTE = ClassName.of(API, "ExternalAttribute");

	static final ClassName ELEMENT_CONTENTS = ClassName.of(API, "ElementContents");

	static final ClassName HTML_TEMPLATE_API = ClassName.of(HTML_INTERNAL, "HtmlTemplateApi");

	static final ClassName NAMES_BUILDER = ClassName.of(HTML_INTERNAL, "NamesBuilder");

	static final ClassName STD_ATTR_NAME = ClassName.of(HTML_INTERNAL, "StandardAttributeName");

	static final ClassName ELEMENT_KIND = ClassName.of(HTML_INTERNAL, "ElementKind");

	static final ClassName ELEMENT_NAME = ClassName.of(HTML_INTERNAL, "ElementName");

	static final ClassName STD_ELEMENT_NAME = ClassName.of(HTML_INTERNAL, "StandardElementName");

	final HtmlSelfGen spec;

	final Code code;

	ClassName className;

	ImportList importList;

	String packageName;

	String simpleName;

	public ThisTemplate(HtmlSelfGen spec) {
		this.spec = spec;

		code = spec.code;
	}

	@Override
	public final String toString() {
		return contents();
	}

	public void writeTo(Path directory) throws IOException {
		String contents;
		contents = contents();

		Path file;
		file = className.toPath(directory);

		Files.writeString(
				file, contents, StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}

	final void className(ClassName className) {
		this.className = Check.notNull(className, "className == null");

		packageName = this.className.packageName();

		simpleName = this.className.simpleName();

		importList = code.importList(packageName);
	}

	abstract String contents();

}