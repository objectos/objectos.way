/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.mysql;

final class DumpOnlyOption extends AbstractOption implements DumpOption {

  private static final DumpOnlyOption ALL_DATABASES = new DumpOnlyOption("all-databases");

  private static final DumpOnlyOption DELETE_MASTER_LOGS = new DumpOnlyOption("delete-master-logs");

  private static final DumpOnlyOption EVENTS = new DumpOnlyOption("events");

  private static final DumpOnlyOption FLUSH_LOGS = new DumpOnlyOption("flush-logs");

  private static final DumpOnlyOption ROUTINES = new DumpOnlyOption("routines");

  private static final DumpOnlyOption SINGLE_TRANSACTION = new DumpOnlyOption("single-transaction");

  private DumpOnlyOption(String key) {
    super(key);
  }

  private DumpOnlyOption(String key, String value) {
    super(key, value);
  }

  public static DumpOption allDatabases() {
    return ALL_DATABASES;
  }

  public static DumpOption deleteMasterLogs() {
    return DELETE_MASTER_LOGS;
  }

  public static DumpOption events() {
    return EVENTS;
  }

  public static DumpOption flushLogs() {
    return FLUSH_LOGS;
  }

  public static DumpOption masterData(int value) {
    switch (value) {
      case 1:
        return new DumpOnlyOption("master-data", "1");
      case 2:
        return new DumpOnlyOption("master-data", "2");
      default:
        throw new IllegalArgumentException("value must be 1 or 2");
    }
  }

  public static DumpOption routines() {
    return ROUTINES;
  }

  public static DumpOption singleTransaction() {
    return SINGLE_TRANSACTION;
  }

}
