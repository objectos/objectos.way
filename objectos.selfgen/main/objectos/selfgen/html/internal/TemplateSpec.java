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
package objectos.selfgen.html.internal;

import java.util.HashSet;
import java.util.Set;

public final class TemplateSpec {

  private final Set<String> skipAttribute = new HashSet<>();

  private final Set<String> skipText = new HashSet<>();

  TemplateSpec() {}

  public final boolean shouldIncludeAttribute(String method) {
    return !skipAttribute.contains(method);
  }

  public final boolean shouldIncludeText(ElementSpec elementSpec) {
    String methodName = elementSpec.methodName();
    return elementSpec.hasEndTag()
        && !skipAttribute.contains(methodName)
        && !skipText.contains(elementSpec.methodName());
  }

  public final TemplateSpec skipAttribute(String methodName) {
    skipAttribute.add(methodName);
    return this;
  }

  public final TemplateSpec skipText(String methodName) {
    skipText.add(methodName);
    return this;
  }

}
