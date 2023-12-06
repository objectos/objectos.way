/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

import static org.testng.Assert.assertEquals;

import java.util.TimeZone;
import org.testng.annotations.Test;

public class TimeZoneTest {

  @Test
  public void getTimeZone() {
    TimeZone tz;
    tz = TimeZone.getTimeZone("GMT-0300");

    assertEquals(tz.getDisplayName(), "GMT-03:00");

    tz = TimeZone.getTimeZone("GMT+0300");

    assertEquals(tz.getDisplayName(), "GMT+03:00");

    tz = TimeZone.getTimeZone("GMT+0730");

    assertEquals(tz.getDisplayName(), "GMT+07:30");
  }

}