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

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

sealed abstract class SqlCause implements Sql.Cause {

  static final class IntegrityConstraintViolation extends SqlCause implements Sql.IntegrityConstraintViolation {
    IntegrityConstraintViolation(SQLException original) {
      super(original);
    }
  }

  static final class OtherDatabaseError extends SqlCause implements Sql.OtherDatabaseError {
    OtherDatabaseError(SQLException original) {
      super(original);
    }
  }

  private final SQLException original;

  SqlCause(SQLException original) {
    this.original = original;
  }

  static List<Sql.Cause> allOf(SqlDialect dialect, SQLException e) {
    final UtilList<Sql.Cause> causes;
    causes = new UtilList<>();

    SQLException next;
    next = e;

    while (next != null) {
      final Sql.Cause cause;
      cause = SqlCause.of(dialect, next);

      causes.add(cause);

      next = next.getNextException();
    }

    return causes.toUnmodifiableList();
  }

  private static Sql.Cause of(SqlDialect dialect, SQLException e) {
    return switch (e) {
      case SQLIntegrityConstraintViolationException ex -> new IntegrityConstraintViolation(ex);

      default -> new OtherDatabaseError(e);
    };
  }

  @Override
  public final SQLException unwrap() {
    return original;
  }

}