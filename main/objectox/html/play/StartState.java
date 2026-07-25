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

import objectox.html.rec.HtmlBytes;

final class StartState implements State {

  private final HtmlSource source;

  StartState(HtmlSource source) {
    this.source = source;
  }

  @Override
  public final BeginDocumentState compute() {
    int index;
    index = 0;

    while (source.hasByte(index)) {
      final byte proto;
      proto = source.getByte(index);

      switch (proto) {
        case HtmlBytes.xSTARTTAG8 -> throw new UnsupportedOperationException("Implement me");

        case HtmlBytes.xSTARTFLAT -> throw new UnsupportedOperationException("Implement me");

        case HtmlBytes.xSTARTFRAG -> throw new UnsupportedOperationException("Implement me");

        case HtmlBytes.xAMBIGUOUS88, HtmlBytes.xATTRIBUTE88 -> { index += 1 + 1 + 1; continue; }

        case HtmlBytes.xAMBIGUOUS816, HtmlBytes.xATTRIBUTE816 -> { index += 1 + 1 + 2; continue; }

        case HtmlBytes.xAMBIGUOUS824, HtmlBytes.xATTRIBUTE824 -> { index += 1 + 1 + 3; continue; }

        case HtmlBytes.xBOOLEAN8 -> { index += 1 + 1; continue; }

        case HtmlBytes.xRAW8, HtmlBytes.xTEXT8 -> { index += 1 + 1; continue; }

        case HtmlBytes.xRAW16, HtmlBytes.xTEXT16 -> { index += 1 + 2; continue; }

        case HtmlBytes.xRAW24, HtmlBytes.xTEXT24 -> { index += 1 + 3; continue; }
      }

      break;
    }

    return new BeginDocumentState(source, index);
  }

}
