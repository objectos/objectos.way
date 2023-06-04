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
package objectos.css.tmpl;

import java.util.Objects;
import objectos.css.pseudom.PSelectorElement.PIdSelector;
import objectos.css.tmpl.Instruction.ExternalSelector;
import objectos.lang.Check;

public final record IdSelector(String id)
    implements ExternalSelector, PIdSelector {

  public IdSelector {
    Objects.requireNonNull(id, "id == null");

    Check.argument(!id.isBlank(), "id must no be blank");
  }

  public static IdSelector of(String id) {
    return new IdSelector(id);
  }

}
