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
package objectox.html.rec;

import static org.testng.Assert.assertEquals;

import objectox.html.HtmlByteProto;
import org.testng.annotations.Test;

public class ReverseOffsetRecorderTest {

  @Test(description = "offset <= 127")
  public void record01() {
    final ByteArray main;
    main = ByteArray.of(new int[127]);

    assertEquals(main.size(), 127);

    final ReverseOffsetRecorder subject;
    subject = new ReverseOffsetRecorder(main);

    subject.record(0);

    assertEquals(
        new byte[] {
            main.get(126),
            main.get(127),
            main.get(128),
            main.get(129)
        },

        new byte[] {
            0,
            HtmlByteProto.END,
            127,
            HtmlByteProto.INTERNAL
        }
    );
  }

}
