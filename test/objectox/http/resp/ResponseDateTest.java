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
package objectox.http.resp;

import static org.testng.Assert.assertEquals;

import java.time.Clock;
import java.time.ZoneId;
import objectos.way.Y;
import org.testng.annotations.Test;

public class ResponseDateTest {

  @Test
  public void now() {
    final Clock clock;
    clock = Y.clockFixed();

    final ResponseDate date;
    date = new ResponseDate(clock);

    assertEquals(date.now(), "Wed, 28 Jun 2023 12:08:43 GMT");
  }

  @Test
  public void now2() {
    final Clock gmt;
    gmt = Y.clockFixed();

    final Clock clock;
    clock = gmt.withZone(ZoneId.of("America/Sao_Paulo"));

    final ResponseDate date;
    date = new ResponseDate(clock);

    assertEquals(date.now(), "Wed, 28 Jun 2023 12:08:43 GMT");
  }

}
