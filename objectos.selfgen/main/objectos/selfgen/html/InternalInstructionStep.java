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

import objectos.code.ClassName;

final class InternalInstructionStep extends ThisTemplate {
	public InternalInstructionStep(HtmlSelfGen spec) {
		super(spec);
	}

	@Override
  final String contents() {
    className(INTERNAL_INSTRUCTION);

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
     * TODO
     */
    \{GENERATED_MSG}
    public enum \{simpleName} implements
    \{superTypes()}
        \{GLOBAL_ATTRIBUTE} {
      INSTANCE;
    }
    """;
  }

	private String superTypes() {
    StringBuilder sb;
    sb = new StringBuilder();

    for (var attribute : spec.attributes()) {
      ClassName className;
      className = attribute.instructionClassName;

      if (className != null) {
        sb.append(
          code."    \{className},\n"
        );
      }
    }

    return sb.toString();
  }
}