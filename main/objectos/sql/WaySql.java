/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

final class WaySql {

  private WaySql() {}

  public static void set(PreparedStatement stmt, int index, Object value) throws SQLException {
    switch (value) {
      case Boolean b -> stmt.setBoolean(index, b.booleanValue());

      case Integer i -> stmt.setInt(index, i.intValue());

      case String s -> stmt.setString(index, s);

      default -> throw new IllegalArgumentException("Unexpected type: " + value.getClass());
    }
  }

}