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
import objectos.css.om.StyleSheet;
import objectos.util.UnmodifiableIterator;
import objectos.util.UnmodifiableList;

public record InternalStyleSheet(UnmodifiableList<TopLevelElement> elements)
    implements StyleSheet {

  @Override
  public final String toString() {
    try {
      StringBuilder sb;
      sb = new StringBuilder();

      writeTo(sb);

      return sb.toString();
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

  @Override
  public final void writeTo(Appendable dest) throws IOException {
    String nl;
    nl = System.lineSeparator();

    UnmodifiableIterator<TopLevelElement> iterator;
    iterator = elements.iterator();

    if (iterator.hasNext()) {
      TopLevelElement element;
      element = iterator.next();

      element.writeTo(dest);

      dest.append(nl);

      while (iterator.hasNext()) {
        dest.append(nl);

        element = iterator.next();

        element.writeTo(dest);

        dest.append(nl);
      }
    }
  }

}