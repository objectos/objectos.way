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

import objectos.code.JavaTemplate.Renderer;
import objectos.lang.Check;
import objectox.code.Pass0;
import objectox.code.Pass1;
import objectox.code.Pass2;
import objectox.code.State;

public final class JavaGenerator {

  Pass0 pass0 = new Pass0();

  Pass1 pass1 = new Pass1();

  Pass2 pass2 = new Pass2();

  State state = new State();

  JavaGenerator() {}

  public static JavaGenerator of() {
    return new JavaGenerator();
  }

  public final void render(JavaTemplate template, Renderer renderer) {
    Check.notNull(template, "template == null");
    Check.notNull(renderer, "renderer == null");

    pass0.execute(state, template);

    pass1.execute(state);

    pass2.execute(state, renderer);
  }

}