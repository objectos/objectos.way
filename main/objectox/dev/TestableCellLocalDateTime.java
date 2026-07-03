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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TestableCellLocalDateTime {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private final LocalDateTime value;

  TestableCellLocalDateTime(LocalDateTime value) {
    this.value = value;
  }

  @Override
  public final String toString() {
    if (value != null) {
      return FORMATTER.format(value);
    } else {
      return "---------- --------";
    }
  }

}
