/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package objectos.code;

import objectos.lang.Check;

public abstract class JavaSink extends Pass2 {

  /**
   * Sole constructor
   */
  protected JavaSink() {}

  public static JavaSink ofStringBuilder(StringBuilder output) {
    Check.notNull(output, "output == null");

    return new JavaSinkOfStringBuilder(output);
  }

  public void eval(JavaTemplate template) {
    Check.notNull(template, "template == null");

    pass0(template);

    pass1();

    pass2();
  }

}