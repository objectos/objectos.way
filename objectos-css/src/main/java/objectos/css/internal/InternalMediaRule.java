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
import objectos.css.om.StyleRule;
import objectos.css.tmpl.MediaQuery;
import objectos.util.UnmodifiableIterator;
import objectos.util.UnmodifiableList;

public record InternalMediaRule(UnmodifiableList<MediaQuery> queries,
                                UnmodifiableList<StyleRule> rules)
    implements TopLevelElement {

  @Override
  public final void writeTo(Appendable dest) throws IOException {
    UnmodifiableIterator<MediaQuery> queryIterator;
    queryIterator = queries.iterator();

    if (queryIterator.hasNext()) {
      MediaQuery query;
      query = queryIterator.next();

      query.writeTo(dest);

      while (queryIterator.hasNext()) {
        dest.append(", ");

        query = queryIterator.next();

        query.writeTo(dest);
      }
    }

    dest.append(" {");

    String nl;
    nl = System.lineSeparator();

    for (StyleRule rule : rules) {
      dest.append(nl);

      rule.writeTo(dest);

      dest.append(nl);
    }

    dest.append('}');
  }

}