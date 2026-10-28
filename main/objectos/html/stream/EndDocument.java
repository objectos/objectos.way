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
package objectos.html.stream;

import objectox.html.stream.EndDocumentEvent;

/// This event marks the end of an HTML document.
public sealed interface EndDocument
    extends Event
    permits EndDocumentEvent {

  /// This event is always at the document root. Because of this, its level is
  /// always `0`.
  ///
  /// @return always `0`
  @Override
  default int level() {
    return 0;
  }

}
