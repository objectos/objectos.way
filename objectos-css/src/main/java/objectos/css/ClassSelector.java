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
package objectos.css;

import java.io.IOException;
import java.util.Objects;
import objectos.css.om.Selector;
import objectos.html.tmpl.Instruction.ExternalAttribute;
import objectos.lang.Check;

/**
 * @since 0.7
 */
public record ClassSelector(String className) implements ExternalAttribute.StyleClass, Selector {

  public ClassSelector {
    Objects.requireNonNull(className, "className == null");

    Check.argument(!className.isBlank(), "className must not be blank");
  }

  public static ClassSelector of(String className) {
    return new ClassSelector(className);
  }

  @Override
  public final String toString() {
    return "." + className;
  }

  @Override
  public final String value() {
    return className;
  }

  @Override
  public final void writeTo(Appendable dest) throws IOException {
    dest.append('.');
    dest.append(className);
  }

}