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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.lang.invoke.MethodHandles;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import objectos.way.Sql.Mapper;
import org.testng.annotations.Test;

public class SqlMapperOfRecordTest {

  @Test(description = "single int field")
  public void testCase01() throws SQLException {
    record Subject(int id) {}

    Mapper<Subject> mapper;
    mapper = Sql.Mapper.ofRecord(MethodHandles.lookup(), Subject.class);

    ResultSet rs;
    rs = new TestingResultSet(Map.of("1", 123));

    assertEquals(rs.next(), true);
    assertEquals(mapper.map(rs, 1), new Subject(123));
  }

}