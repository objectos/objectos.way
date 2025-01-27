/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
import static org.testng.Assert.assertSame;

import java.sql.BatchUpdateException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import objectos.way.Sql.Cause;
import org.testng.annotations.Test;

public class SqlBatchUpdateFailedTest {

  @Test
  public void testing01() {
    final BatchUpdateException error;
    error = new BatchUpdateException(new int[] {256});

    final SQLIntegrityConstraintViolationException next0;
    next0 = new SQLIntegrityConstraintViolationException();

    error.setNextException(next0);

    final SqlBatchUpdateFailed failed;
    failed = SqlBatchUpdateFailed.create(SqlDialect.TESTING, error);

    assertEquals(failed.counts(), new int[] {256});

    final List<Cause> causes;
    causes = failed.causes();

    assertEquals(causes.size(), 1);

    final Cause cause0;
    cause0 = causes.get(0);

    assertSame(cause0.unwrap(), next0);
  }

}