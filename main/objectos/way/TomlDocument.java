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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

final class TomlDocument implements Toml.Document {

  private final Map<String, Object> document = new UtilSequencedMap<>();

  private Map<Class<?>, RecordMapper> recordMappers;

  @Override
  public final void add(String key, Record value) {
    Objects.requireNonNull(key, "key == null");

    if (document.containsKey(key)) {
      throw new IllegalArgumentException("Duplicate key=" + key);
    }

    final Map<String, Object> map;
    map = toMap(value);

    document.put(key, map);
  }

  @Override
  public final String toString() {
    final StringBuilder out;
    out = new StringBuilder();

    writeMap(out, 0, document);

    return out.toString();
  }

  private void writeKey(StringBuilder out, String key) {
    // TODO escape key if necessary

    out.append(key);
  }

  private void writeMap(StringBuilder out, int level, Map<String, Object> map) {
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      writeMapEntry(out, level, entry);
    }
  }

  private void writeMapEntry(StringBuilder out, int level, Map.Entry<String, Object> entry) {
    final String name;
    name = entry.getKey();

    final Object value;
    value = entry.getValue();

    switch (value) {
      case String string -> {
        writeKey(out, name);

        out.append(" = ");

        writeString(out, string);

        out.append('\n');
      }

      case Map<?, ?> table -> {
        if (level == 0) {
          out.append('[');
          writeKey(out, name);
          out.append(']');
          out.append('\n');
        } else {
          throw new UnsupportedOperationException("Implement me");
        }

        @SuppressWarnings("unchecked")
        final Map<String, Object> map = (Map<String, Object>) table;

        writeMap(out, level + 1, map);
      }

      default -> throw new AssertionError("Implement me");
    }
  }

  private void writeString(StringBuilder out, String s) {
    // TODO escape string or write multi-line if necessary

    out.append('"');
    out.append(s);
    out.append('"');
  }

  private Map<String, Object> toMap(Record value) {
    final Class<? extends Record> recordClass;
    recordClass = value.getClass();

    final RecordMapper mapper;
    mapper = recordMappers().computeIfAbsent(recordClass, this::recordMapper);

    return mapper.map(value);
  }

  private static final class RecordMapper {

    private final RecordComponentMapper[] components;

    RecordMapper(RecordComponentMapper[] components) {
      this.components = components;
    }

    final Map<String, Object> map(Record value) {
      final Map<String, Object> map;
      map = new UtilSequencedMap<>();

      for (RecordComponentMapper c : components) {
        c.put(map, value);
      }

      return map;
    }

  }

  private class RecordComponentMapper {

    private final String key;

    private final Method accessor;

    RecordComponentMapper(String key, Method accessor) {
      this.key = key;

      this.accessor = accessor;
    }

    final void put(Map<String, Object> map, Record obj) {
      try {
        Object value;
        value = accessor.invoke(obj);

        if (value instanceof Record rec) {
          value = toMap(rec);
        }

        map.put(key, value);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new Toml.RecordException(e);
      }
    }

  }

  private Map<Class<?>, RecordMapper> recordMappers() {
    if (recordMappers == null) {
      recordMappers = new HashMap<>();
    }

    return recordMappers;
  }

  private RecordMapper recordMapper(Class<?> key) {
    final RecordComponent[] components;
    components = key.getRecordComponents();

    final int len;
    len = components.length;

    final RecordComponentMapper[] mappers;
    mappers = new RecordComponentMapper[len];

    for (int idx = 0; idx < len; idx++) {
      final RecordComponent component;
      component = components[idx];

      final RecordComponentMapper writer;
      writer = recordComponentMapper(component);

      mappers[idx] = writer;
    }

    return new RecordMapper(mappers);
  }

  private static final Set<String> SUPPORTED_TYPES = Set.of(
      String.class.getName()
  );

  private RecordComponentMapper recordComponentMapper(RecordComponent component) {
    final String name;
    name = component.getName();

    final Class<?> type;
    type = component.getType();

    if (type.isRecord()) {
      recordMappers().computeIfAbsent(type, this::recordMapper);
    }

    else {
      final String typeName;
      typeName = type.getName();

      if (!SUPPORTED_TYPES.contains(typeName)) {
        throw new IllegalArgumentException(
            "Record " + component.getDeclaringRecord() + " declares an unsupported component type: " + typeName);
      }
    }

    final Method accessor;
    accessor = component.getAccessor();

    return new RecordComponentMapper(name, accessor);
  }

}