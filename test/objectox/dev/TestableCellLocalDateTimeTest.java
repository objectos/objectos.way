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
package objectox.dev;

import static org.testng.Assert.assertEquals;

import java.time.LocalDateTime;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestableCellLocalDateTimeTest {

  @DataProvider
  public Object[][] format01Provider() {
    return new Object[][] {
        {LocalDateTime.of(2026, 1, 1, 13, 0), "2026-01-01 13:00:00", "2026-01-01 13:00:00"},
        {null, "---------- --------", "---------- --------"}
    };
  }

  @Test(dataProvider = "format01Provider", description = "format")
  public void format01(LocalDateTime value, String lastFalse, String lastTrue) {
    final TestableCellLocalDateTime subject;
    subject = new TestableCellLocalDateTime(value);

    assertEquals(subject.format(false), lastFalse);

    assertEquals(subject.format(true), lastTrue);
  }

}
