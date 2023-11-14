/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package selfgen.css.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import objectos.code.ClassName;
import objectos.code.Code;

final class PrefixClassStep extends ThisTemplate {

	private Prefix prefix;

	PrefixClassStep(CssUtilSelfGen spec) {
		super(spec);
	}

	@Override
	public final void writeTo(Path directory) throws IOException {
		for (var prefix : Prefix.values()) {
			this.prefix = prefix;

			super.writeTo(directory);
		}
	}

	@Override
  final String contents() {
    className(ClassName.of(HTML_STYLE, prefix.simpleName));

    return code."""
    /*
     * Copyright (C) 2023 Objectos Software LTDA.
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
    \{GENERATED_MSG}
    \{generatePrefix()}
    """;
  }

	final String generatePrefix() {
		return prefix.generate(code);
	}

}