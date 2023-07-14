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
package objectos.css.internal;

import java.io.IOException;
import objectos.css.om.StyleDeclaration;
import objectos.css.om.StyleRule;
import objectos.css.tmpl.Length;

public enum InternalInstruction implements Length, StyleDeclaration, StyleRule {

  DECLARATION(-1),

  LENGTH_DOUBLE(10),

  LENGTH_INT(6),

  STYLE_RULE(-1);

  public final int length;

  private InternalInstruction(int length) {
    this.length = length;
  }

  @Override
  public void writeTo(Appendable dest) throws IOException {
    throw new UnsupportedOperationException();
  }

}