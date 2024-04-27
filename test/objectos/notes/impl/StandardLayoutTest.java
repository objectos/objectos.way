/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.notes.impl;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class StandardLayoutTest {

	private final Layout layout = new StandardLayout();

    @Test
    public void test() {
      TestingClock clock;
      clock = new TestingClock(2023, 10, 31);

      Log0 log0;
      log0 = new Log0(clock, TestingNotes.TRACE0);

      assertEquals(
          layout.formatLog0(log0),
          "2023-10-31 10:00:00.000 TRACE --- [main           ] objectos.notes.impl.TestingNotes         : TRACE0\n"
      );
    }

}
