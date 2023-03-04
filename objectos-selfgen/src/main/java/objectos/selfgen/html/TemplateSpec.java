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

import objectos.lang.Check;
import objectos.util.GrowableSet;

public final class TemplateSpec {

  private int maxArity = 5;

  private int maxLevel = 5;

  private final GrowableSet<String> skipAttribute = new GrowableSet<>();

  private final GrowableSet<String> skipText = new GrowableSet<>();

  TemplateSpec() {}

  public final int getMaxArity() {
    return maxArity;
  }

  public final int getMaxLevel() {
    return maxLevel;
  }

  public final TemplateSpec maxArity(int newArity) {
    Check.argument(newArity > 0, "arity must be > 0");
    maxArity = newArity;
    return this;
  }

  public final TemplateSpec maxLevel(int newLevel) {
    Check.argument(newLevel > 0, "level must be > 0");
    maxLevel = newLevel;
    return this;
  }

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
