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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.Temporal;
import org.testng.annotations.Test;

public class SqlDialectTest2TrxParam extends SqlDialectTest0Support {

  // ##################################################################
  // # BEGIN: Boolean Type
  // ##################################################################

  private record TypesBoolean(
      int id,
      boolean booleanValue,
      Boolean booleanNullable
  ) {
    TypesBoolean(ResultSet rs, int idx) throws SQLException {
      this(
          rs.getInt(idx++),
          rs.getBoolean(idx++),
          rs.getObject(idx++, Boolean.class)
      );
    }
  }

  private Sql.Transaction typesBoolean(Sql.Database db) {
    db.migrate(migrations -> {
      migrations.add("Create test table", """
        create table TYPES_BOOLEAN (
        ID int not null,

        T_BOOLEAN boolean not null,
        T_BOOLEAN_NULL boolean null,

        primary key (ID)
      );
      """);
    });

    return db.connect();
  }

  private TypesBoolean typesBooleanTest(
      Sql.Transaction trx,
      int id,
      boolean booleanValue,
      Boolean booleanNullable
  ) {
    trx.sql("""
    insert into TYPES_BOOLEAN (
      ID, T_BOOLEAN, T_BOOLEAN_NULL
    ) values (
      ?, ?, ?
    )
    """);

    trx.add(id);
    trx.add(booleanValue);
    trx.add(booleanNullable, Types.BOOLEAN);

    trx.update();

    trx.sql("select * from TYPES_BOOLEAN");

    return trx.querySingle(TypesBoolean::new);
  }

  @Test(description = "true value", dataProvider = "dbProvider")
  public void typesBoolean01(Sql.Database db) {
    final Sql.Transaction trx;
    trx = typesBoolean(db);

    try {
      final TypesBoolean result;
      result = typesBooleanTest(trx, 1, true, true);

      assertEquals(result.id, 1);
      assertEquals(result.booleanValue, true);
      assertEquals(result.booleanNullable, Boolean.TRUE);
    } finally {
      Sql.rollbackAndClose(trx);
    }
  }

  @Test(description = "false value", dataProvider = "dbProvider")
  public void typesBoolean02(Sql.Database db) {
    final Sql.Transaction trx;
    trx = typesBoolean(db);

    try {
      final TypesBoolean result;
      result = typesBooleanTest(trx, 2, false, false);

      assertEquals(result.id, 2);
      assertEquals(result.booleanValue, false);
      assertEquals(result.booleanNullable, Boolean.FALSE);
    } finally {
      Sql.rollbackAndClose(trx);
    }
  }

  @Test(description = "null value", dataProvider = "dbProvider")
  public void typesBoolean03(Sql.Database db) {
    final Sql.Transaction trx;
    trx = typesBoolean(db);

    try {
      final TypesBoolean result;
      result = typesBooleanTest(trx, 2, false, null);

      assertEquals(result.id, 2);
      assertEquals(result.booleanValue, false);
      assertEquals(result.booleanNullable, null);
    } finally {
      Sql.rollbackAndClose(trx);
    }
  }

  // ##################################################################
  // # END: Boolean Type
  // ##################################################################

  // ##################################################################
  // # BEGIN: Date/Time Types
  // ##################################################################

  private record TypesDateTime(
      int id,
      LocalDate date,
      LocalTime time,
      LocalTime timeWithZone,
      LocalDateTime timestamp,
      LocalDateTime timestampWithZone
  ) {
    TypesDateTime(ResultSet rs, int idx) throws SQLException {
      this(
          rs.getInt(idx++),
          rs.getObject(idx++, LocalDate.class),
          rs.getObject(idx++, LocalTime.class),
          rs.getObject(idx++, LocalTime.class),
          rs.getObject(idx++, LocalDateTime.class),
          rs.getObject(idx++, LocalDateTime.class)
      );
    }
  }

  private Sql.Transaction typesDateTime(Sql.Database db) {
    final SqlDialect dialect;
    dialect = dialect(db);

    final String sql = switch (dialect) {
      case H2 -> """
      create table TYPES_DATE_TIME (
        ID int not null,

        T_DATE date not null,
        T_TIME time not null,
        T_TIME_TZ time with time zone not null,
        T_TIMESTAMP timestamp not null,
        T_TIMESTAMP_TZ timestamp with time zone not null,

        primary key (ID)
      );
      """;

      case MYSQL -> """
      create table TYPES_DATE_TIME (
        ID int not null,

        T_DATE date not null,
        T_TIME time not null,
        T_TIME_TZ time not null,
        T_TIMESTAMP datetime not null,
        T_TIMESTAMP_TZ timestamp not null,

        primary key (ID)
      );
      """;

      case TESTING -> "";
    };

    db.migrate(migrations -> {
      migrations.add("Create test table", sql);
    });

    return db.connect();
  }

  private TypesDateTime typesDateTimeTest(
      Sql.Transaction trx,
      int id,
      LocalDate date,
      LocalTime time,
      Temporal timeWithZone,
      LocalDateTime timestamp,
      Temporal timestampWithZone
  ) {
    trx.sql("""
    insert into TYPES_DATE_TIME (
      ID, T_DATE, T_TIME, T_TIME_TZ, T_TIMESTAMP, T_TIMESTAMP_TZ
    ) values (
      ?, ?, ?, ?, ?, ?
    )
    """);

    trx.add(id);
    trx.add(date);
    trx.add(time);
    trx.add(timeWithZone);
    trx.add(timestamp);
    trx.add(timestampWithZone);

    trx.update();

    trx.sql("select * from TYPES_DATE_TIME");

    return trx.querySingle(TypesDateTime::new);
  }

  @Test(description = "same TZ", dataProvider = "dbProvider")
  public void typesDateTime01(Sql.Database db) {
    final Sql.Transaction trx;
    trx = typesDateTime(db);

    try {
      final TypesDateTime result;
      result = typesDateTimeTest(
          trx,
          1,
          LocalDate.of(2025, 5, 31),
          LocalTime.of(10, 20, 30),
          LocalTime.of(16, 26, 36),
          LocalDateTime.of(2025, 5, 31, 10, 20, 30),
          LocalDateTime.of(2020, 12, 31, 16, 26, 36)
      );

      assertEquals(result.id, 1);
      assertEquals(result.date, LocalDate.of(2025, 5, 31));
      assertEquals(result.time, LocalTime.of(10, 20, 30));
      assertEquals(result.timeWithZone, LocalTime.of(16, 26, 36));
      assertEquals(result.timestamp, LocalDateTime.of(2025, 5, 31, 10, 20, 30));
      assertEquals(result.timestampWithZone, LocalDateTime.of(2020, 12, 31, 16, 26, 36));
    } finally {
      Sql.rollbackAndClose(trx);
    }
  }

  @Test(description = "read/write have different TZ", dataProvider = "dbProvider")
  public void typesDateTime02(Sql.Database db) {
    final Sql.Transaction trx;
    trx = typesDateTime(db);

    final SqlDialect dialect;
    dialect = dialect(db);

    // insert at different TZ / read at default TZ
    final ZoneOffset minusTwo;
    minusTwo = ZoneOffset.ofHours(-2);

    try {
      final TypesDateTime result;
      result = typesDateTimeTest(
          trx,

          2,

          LocalDate.of(2025, 5, 31),

          LocalTime.of(10, 20, 30),

          switch (dialect) {
            case H2 -> LocalTime.of(16, 26, 36).atOffset(minusTwo);

            case MYSQL -> LocalTime.of(15, 26, 36);

            case TESTING -> throw new UnsupportedOperationException();
          },

          LocalDateTime.of(2025, 5, 31, 10, 20, 30),

          LocalDateTime.of(2020, 12, 31, 16, 26, 36).atZone(minusTwo)
      );

      assertEquals(result.id, 2);
      assertEquals(result.date, LocalDate.of(2025, 5, 31));
      assertEquals(result.time, LocalTime.of(10, 20, 30));
      assertEquals(result.timeWithZone, LocalTime.of(15, 26, 36));
      assertEquals(result.timestamp, LocalDateTime.of(2025, 5, 31, 10, 20, 30));
      assertEquals(result.timestampWithZone, LocalDateTime.of(2020, 12, 31, 15, 26, 36));
    } finally {
      Sql.rollbackAndClose(trx);
    }
  }

  // ##################################################################
  // # END: Date/Time Types
  // ##################################################################

  // ##################################################################
  // # BEGIN: Floating Point Types
  // ##################################################################

  private record TypesFloating(
      int id,
      float floatValue,
      double doubleValue
  ) {
    TypesFloating(ResultSet rs, int idx) throws SQLException {
      this(
          rs.getInt(idx++),
          rs.getFloat(idx++),
          rs.getDouble(idx++)
      );
    }
  }

  private Sql.Transaction typesFloating(Sql.Database db) {
    db.migrate(migrations -> {
      migrations.add("Create test table", """
      create table TYPES_FLOATING (
        ID int not null,

        T_FLOAT float not null,
        T_DOUBLE double precision not null,

        primary key (ID)
      );
      """);
    });

    return db.connect();
  }

  private TypesFloating typesFloatingTest(
      Sql.Transaction trx,
      int id,
      float floatValue,
      double doubleValue
  ) {
    trx.sql("""
    insert into TYPES_FLOATING (
      ID, T_FLOAT, T_DOUBLE
    ) values (
      ?, ?, ?
    )
    """);

    trx.add(id);
    trx.add(floatValue);
    trx.add(doubleValue);

    trx.update();

    trx.sql("select * from TYPES_FLOATING");

    return trx.querySingle(TypesFloating::new);
  }

  @Test(description = "max value", dataProvider = "dbProvider")
  public void typesFloating01(Sql.Database db) {
    final Sql.Transaction trx;
    trx = typesFloating(db);

    final SqlDialect dialect;
    dialect = dialect(db);

    try {
      final float f = switch (dialect) {
        case H2, TESTING -> Float.MAX_VALUE;

        case MYSQL -> Float.valueOf("3.40282e38");
      };

      final double d = Double.MAX_VALUE;

      final TypesFloating result;
      result = typesFloatingTest(trx, 1, f, d);

      assertEquals(result.id, 1);
      assertEquals(Math.abs(result.floatValue - f) < 0.0001, true);
      assertEquals(Math.abs(result.doubleValue - d) < 0.0000001, true);
    } finally {
      Sql.rollbackAndClose(trx);
    }
  }

  @Test(description = "min value", dataProvider = "dbProvider")
  public void typesFloating02(Sql.Database db) {
    final Sql.Transaction trx;
    trx = typesFloating(db);

    try {
      final float f = Float.MIN_VALUE;
      final double d = Double.MIN_VALUE;

      final TypesFloating result;
      result = typesFloatingTest(trx, 1, f, d);

      assertEquals(result.id, 1);
      assertEquals(Math.abs(result.floatValue - f) < 0.0001, true);
      assertEquals(Math.abs(result.doubleValue - d) < 0.0000001, true);
    } finally {
      Sql.rollbackAndClose(trx);
    }
  }

  // ##################################################################
  // # END: Floating Point Types
  // ##################################################################

  // ##################################################################
  // # BEGIN: Integer Types
  // ##################################################################

  private record TypesInteger(
      int id,
      byte tinyint,
      short smallint,
      int integer,
      long bigint
  ) {
    TypesInteger(ResultSet rs, int idx) throws SQLException {
      this(
          rs.getInt(idx++),
          rs.getByte(idx++),
          rs.getShort(idx++),
          rs.getInt(idx++),
          rs.getLong(idx++)
      );
    }
  }

  private Sql.Transaction typesInteger(Sql.Database db) {
    db.migrate(migrations -> {
      migrations.add("Create test table", """
      create table TYPES_INTEGER (
        ID int not null,

        T_TINYINT tinyint not null,
        T_SMALLINT smallint not null,
        T_INT int not null,
        T_BIGINT bigint not null,

        primary key (ID)
      );
      """);
    });

    return db.connect();
  }

  private TypesInteger typesIntegerTest(
      Sql.Transaction trx,
      int id,
      byte tinyint,
      short smallint,
      int integer,
      long bigint
  ) {
    trx.sql("""
    insert into TYPES_INTEGER (
      ID, T_TINYINT, T_SMALLINT, T_INT, T_BIGINT
    ) values (
      ?, ?, ?, ?, ?
    )
    """);

    trx.add(id);
    trx.add(tinyint);
    trx.add(smallint);
    trx.add(integer);
    trx.add(bigint);

    trx.update();

    trx.sql("select * from TYPES_INTEGER");

    return trx.querySingle(TypesInteger::new);
  }

  @Test(description = "max value", dataProvider = "dbProvider")
  public void typesInteger01(Sql.Database db) {
    final Sql.Transaction trx;
    trx = typesInteger(db);

    try {
      final TypesInteger result;
      result = typesIntegerTest(
          trx,
          1,
          Byte.MAX_VALUE,
          Short.MAX_VALUE,
          Integer.MAX_VALUE,
          Long.MAX_VALUE
      );

      assertEquals(result.id, 1);
      assertEquals(result.tinyint, Byte.MAX_VALUE);
      assertEquals(result.smallint, Short.MAX_VALUE);
      assertEquals(result.integer, Integer.MAX_VALUE);
      assertEquals(result.bigint, Long.MAX_VALUE);
    } finally {
      Sql.rollbackAndClose(trx);
    }
  }

  @Test(description = "min value", dataProvider = "dbProvider")
  public void typesInteger02(Sql.Database db) {
    final Sql.Transaction trx;
    trx = typesInteger(db);

    try {
      final TypesInteger result;
      result = typesIntegerTest(
          trx,
          2,
          Byte.MIN_VALUE,
          Short.MIN_VALUE,
          Integer.MIN_VALUE,
          Long.MIN_VALUE
      );

      assertEquals(result.id, 2);
      assertEquals(result.tinyint, Byte.MIN_VALUE);
      assertEquals(result.smallint, Short.MIN_VALUE);
      assertEquals(result.integer, Integer.MIN_VALUE);
      assertEquals(result.bigint, Long.MIN_VALUE);
    } finally {
      Sql.rollbackAndClose(trx);
    }
  }

  // ##################################################################
  // # END: Integer Types
  // ##################################################################

  // ##################################################################
  // # BEGIN: String Types
  // ##################################################################

  private record TypesString(int id, String char5, String varchar5) {
    TypesString(ResultSet rs, int idx) throws SQLException {
      this(
          rs.getInt(idx++),
          rs.getString(idx++),
          rs.getString(idx++)
      );
    }
  }

  private Sql.Transaction typesString(Sql.Database db) {
    db.migrate(migrations -> {
      migrations.add("Create test table", """
      create table TYPES_STRING (
        ID int not null,

        T_CHAR_5 char(5) not null,
        T_VARCHAR_5 varchar(5) not null,

        primary key (ID)
      );
      """);
    });

    return db.connect();
  }

  private TypesString typesStringTest(
      Sql.Transaction trx,
      int id,
      String char5,
      String varchar5) {
    trx.sql("""
    insert into TYPES_STRING (
      ID, T_CHAR_5, T_VARCHAR_5
    ) values (
      ?, ?, ?
    )
    """);

    trx.add(id);
    trx.add(char5);
    trx.add(varchar5);

    trx.update();

    trx.sql("select * from TYPES_STRING");

    return trx.querySingle(TypesString::new);
  }

  @Test(description = "value length == column length", dataProvider = "dbProvider")
  public void typesString01(Sql.Database db) {
    final Sql.Transaction trx;
    trx = typesString(db);

    try {
      final TypesString result;
      result = typesStringTest(
          trx,
          1,
          "abcde",
          "ABCDE"
      );

      assertEquals(result.id, 1);
      assertEquals(result.char5, "abcde");
      assertEquals(result.varchar5, "ABCDE");
    } finally {
      Sql.rollbackAndClose(trx);
    }
  }

  @Test(description = "value length < column length", dataProvider = "dbProvider")
  public void typesString02(Sql.Database db) {
    final Sql.Transaction trx;
    trx = typesString(db);

    try {
      final TypesString result;
      result = typesStringTest(
          trx,
          1,
          "abc",
          "ABC"
      );

      final SqlDialect dialect;
      dialect = dialect(db);

      switch (dialect) {
        case H2 -> {
          assertEquals(result.id, 1);
          assertEquals(result.char5, "abc  ");
          assertEquals(result.varchar5, "ABC");
        }

        case MYSQL, TESTING -> {
          assertEquals(result.id, 1);
          assertEquals(result.char5, "abc");
          assertEquals(result.varchar5, "ABC");
        }
      }
    } finally {
      Sql.rollbackAndClose(trx);
    }
  }

  // ##################################################################
  // # END: String Types
  // ##################################################################

}
