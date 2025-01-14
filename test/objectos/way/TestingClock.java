/*
 * Copyright (C) 2025 Objectos Software LTDA.
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
package objectos.way;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class TestingClock {

  public static final Clock FIXED;

  static {
    LocalDateTime dateTime;
    dateTime = LocalDateTime.of(2023, 6, 28, 12, 8, 43);

    ZoneId zone;
    zone = ZoneId.of("GMT");

    ZonedDateTime zoned;
    zoned = dateTime.atZone(zone);

    Instant fixedInstant;
    fixedInstant = zoned.toInstant();

    FIXED = Clock.fixed(fixedInstant, zone);
  }

  private TestingClock() {}

}
