/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import objectos.way.Css.MediaQuery;

final class CssGeneratorContextOfMediaQuery extends CssGeneratorContext {

  private final Css.MediaQuery query;

  CssGeneratorContextOfMediaQuery(Css.MediaQuery query) {
    this.query = query;
  }

  public final CssGeneratorContextOfMediaQuery nest(MediaQuery next) {
    return mediaQueries.computeIfAbsent(next, CssGeneratorContextOfMediaQuery::new);
  }

  @Override
  final void write(StringBuilder out, Css.Indentation indentation) {
    query.writeMediaQueryStart(out, indentation);

    Css.Indentation blockIndentation;
    blockIndentation = indentation.increase();

    writeContents(out, blockIndentation);

    indentation.writeTo(out);

    out.append('}');

    out.append(System.lineSeparator());
  }

}