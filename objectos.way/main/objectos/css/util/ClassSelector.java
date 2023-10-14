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
package objectos.css.util;

import objectos.css.internal.ClassSelectorSeqId;
import objectos.css.internal.RandomStringGenerator;
import objectos.css.tmpl.Api;
import objectos.html.tmpl.Api.ExternalAttribute;
import objectox.lang.Check;

public record ClassSelector(String className)
    implements ExternalAttribute.StyleClass, Api.SelectorInstruction {

  public ClassSelector {
    Check.notNull(className, "className == null");

    Check.argument(!className.isBlank(), "className must not be blank");
  }

  /**
   * Returns a new distinct class selector whose value is 4 characters in
   * length. Each returned value is distinct from any of the previously returned
   * values.
   *
   * @return a newly created class selector
   */
  public static ClassSelector next() {
    String id;
    id = ClassSelectorSeqId.next();

    return new ClassSelector(id);
  }

  public static ClassSelector of(String className) {
    return new ClassSelector(className);
  }

  public static ClassSelector randomClassSelector(int length) {
    String name;
    name = RandomStringGenerator.nextName(length);

    return new ClassSelector(name);
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