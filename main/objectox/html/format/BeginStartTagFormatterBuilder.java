/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html.format;

import java.util.Objects;
import objectos.html.format.BeginStartTagOptions;
import objectos.html.format.IndentationStyle;

public final class BeginStartTagFormatterBuilder implements BeginStartTagOptions {

  private IndentationStyle indentationStyle = IndentationStyle.startOfLine();

  public final BeginStartTagFormatter build(Indentation indentation) {
    return new BeginStartTagFormatter(
        indentation,

        indentationStyle
    );
  }

  @Override
  public final void indentationStyle(IndentationStyle value) {
    indentationStyle = Objects.requireNonNull(value, "value == null");
  }

}
