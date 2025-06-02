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

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.List;
import objectos.way.Sql.Cause;

record SqlBatchUpdateFailed(BatchUpdateException original, List<Sql.Cause> causes) implements Sql.BatchUpdateFailed {

  SqlBatchUpdateFailed(BatchUpdateException original, Sql.Dialect dialect, SQLException cause) {
    this(
        original,

        SqlCause.allOf(dialect, cause)
    );
  }

  static SqlBatchUpdateFailed create(Sql.Dialect dialect, SQLException e) {
    return switch (e) {
      case BatchUpdateException original -> new SqlBatchUpdateFailed(
          original,

          dialect,

          original.getNextException()
      );

      default -> new SqlBatchUpdateFailed(
          null,

          dialect,

          e
      );
    };
  }

  @Override
  public final List<Cause> causes() {
    return causes;
  }

  @Override
  public final long[] largeCounts() {
    return original != null ? original.getLargeUpdateCounts() : new long[] {};
  }

  @Override
  public final int[] counts() {
    return original != null ? original.getUpdateCounts() : new int[] {};
  }

}
