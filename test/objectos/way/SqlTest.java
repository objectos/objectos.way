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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.testng.annotations.Test;

public class SqlTest {

  @Test
  public void set() throws SQLException {
    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    Sql.set(stmt, 1, "ABC");
    Sql.set(stmt, 2, Boolean.TRUE);
    Sql.set(stmt, 3, Integer.valueOf(123));
    Sql.set(stmt, 4, Long.valueOf(567L));
    Sql.set(stmt, 5, Double.valueOf(4.56));
    Sql.set(stmt, 6, LocalDate.of(2024, 9, 25));
    Sql.set(stmt, 7, LocalDateTime.of(2024, 9, 25, 13, 0));

    assertEquals(
        stmt.toString(),

        """
        setString(1, ABC)
        setBoolean(2, true)
        setInt(3, 123)
        setLong(4, 567)
        setDouble(5, 4.56)
        setObject(6, 2024-09-25)
        setObject(7, 2024-09-25T13:00)
        """
    );
  }

}