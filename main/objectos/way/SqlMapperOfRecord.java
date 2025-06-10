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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.RecordComponent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import objectos.way.Sql.Mapper;

final class SqlMapperOfRecord<R extends Record> implements Sql.Mapper<R> {

  private final Class<?>[] types;

  private final MethodHandle constructor;

  private SqlMapperOfRecord(Class<?>[] types, MethodHandle constructor) {
    this.types = types;

    this.constructor = constructor;
  }

  static <R extends Record> Mapper<R> of(MethodHandles.Lookup lookup, Class<R> recordType) {
    final RecordComponent[] components;
    components = recordType.getRecordComponents();

    final Class<?>[] types;
    types = new Class<?>[components.length];

    for (int idx = 0; idx < components.length; idx++) {
      final RecordComponent component;
      component = components[idx];

      types[idx] = component.getType();
    }

    final MethodType constructorType;
    constructorType = MethodType.methodType(void.class, types);

    final MethodHandle handle;

    try {
      handle = lookup.findConstructor(recordType, constructorType);
    } catch (NoSuchMethodException | IllegalAccessException e) {
      throw new Sql.MappingException("Failed to obtain record canonical constructor", e);
    }

    return new SqlMapperOfRecord<>(types, handle);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final R map(ResultSet rs, int startingIndex) throws SQLException {
    try {
      final Object[] values;
      values = new Object[types.length];

      for (int idx = 0, len = types.length; idx < len; idx++) {
        Class<?> type;
        type = types[idx];

        Object value;

        if (type == int.class) {
          value = rs.getInt(idx + 1);
        }

        else if (type == boolean.class) {
          value = rs.getBoolean(idx + 1);
        }

        else {
          value = rs.getObject(idx + 1, type);
        }

        values[idx] = value;
      }

      return (R) constructor.invokeWithArguments(values);
    } catch (Throwable t) {
      throw new Sql.MappingException("Failed to create record instance", t);
    }
  }

  @SuppressWarnings("unused")
  final void checkColumnCount(ResultSet rs) throws SQLException {
    ResultSetMetaData meta;
    meta = rs.getMetaData();

    int columnCount;
    columnCount = meta.getColumnCount();

    if (columnCount != types.length) {
      throw new Sql.MappingException("Query returned " + columnCount + " columns but record has only " + types.length + " components");
    }
  }

}