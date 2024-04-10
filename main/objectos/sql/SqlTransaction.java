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

import java.sql.SQLException;

public interface SqlTransaction extends AutoCloseable {
  
  void commit() throws SQLException;
  
  void rollback() throws SQLException;

  @Override
  void close() throws SQLException;

  int count(String sql, Object... args) throws SQLException;

  void queryPage(String sql, ResultSetHandler handler, Page page, Object... args) throws SQLException;

}