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
package objectox.html.play;

import objectos.html.play.BeginDocument;
import objectos.html.play.EndDocument;

public final class HtmlEventPojo
    implements
    BeginDocument,
    EndDocument {

  static final BeginDocument BEGIN_DOCUMENT = new HtmlEventPojo(BeginDocument.class);

  static final EndDocument END_DOCUMENT = new HtmlEventPojo(EndDocument.class);

  private final String name;

  private HtmlEventPojo(Class<?> type) {
    this.name = type.getSimpleName();
  }

  @Override
  public final String toString() {
    return name;
  }

}
