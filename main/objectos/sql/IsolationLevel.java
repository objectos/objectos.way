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

import java.sql.Connection;

public enum IsolationLevel {

  READ_UNCOMMITED(Connection.TRANSACTION_READ_UNCOMMITTED),

  READ_COMMITED(Connection.TRANSACTION_READ_COMMITTED),

  REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

  SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

  public final int jdbcValue;

  private IsolationLevel(int level) {
    this.jdbcValue = level;
  }

}