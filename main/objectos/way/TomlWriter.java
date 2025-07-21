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

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.HashMap;
import java.util.Map;
import objectos.way.Toml.Property;

final class TomlWriter implements Toml.Writer {

  private final Closeable closeable;

  private final Appendable out;

  private Map<Class<? extends Record>, RecordWriter> recordWriters;

  TomlWriter(TomWriterBuilder builder, Closeable closeable, Appendable out) {
    this.closeable = closeable;

    this.out = out;
  }

  @Override
  public final void close() throws IOException {
    if (closeable != null) {
      closeable.close();
    }
  }

  @Override
  public final void writeTable(String header, Record contents) throws IOException {
    Toml.checkHeader(header);

    writeln(header);

    final Class<? extends Record> key;
    key = contents.getClass(); // implicit null-check

    if (recordWriters == null) {
      recordWriters = new HashMap<>();
    }

    final RecordWriter recordWriter;
    recordWriter = recordWriters.computeIfAbsent(key, this::recordWriter);

    recordWriter.write(contents);
  }

  final void writeln(String s) throws IOException {
    out.append(s);

    newLine();
  }

  final void writeString(String s) throws IOException {
    out.append('\"');
    out.append(s);
    out.append('\"');
  }

  private void newLine() throws IOException {
    out.append('\n');
  }

  private static final class RecordWriter {

    private final RecordComponentWriter[] components;

    RecordWriter(RecordComponentWriter[] components) {
      this.components = components;
    }

    final void write(Record contents) throws IOException {
      for (RecordComponentWriter c : components) {
        c.write(contents);
      }
    }

  }

  private class RecordComponentWriter {

    private final String key;

    private final Property property;

    private final Method accessor;

    RecordComponentWriter(String key, Property property, Method accessor) {
      this.key = key;

      this.property = property;

      this.accessor = accessor;
    }

    final void write(Record obj) throws IOException {
      out.append(key);
      out.append(" = ");

      switch (property) {
        case STRING -> {
          final Object value;
          value = invoke(obj);

          final String s;
          s = (String) value;

          writeString(s);
        }
      }

      newLine();
    }

    private Object invoke(Record obj) throws IOException {
      try {
        return accessor.invoke(obj);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new IOException("Failed to write record value", e);
      }
    }

  }

  private RecordWriter recordWriter(Class<? extends Record> key) {
    final RecordComponent[] components;
    components = key.getRecordComponents();

    final int len;
    len = components.length;

    final RecordComponentWriter[] writers;
    writers = new RecordComponentWriter[len];

    for (int idx = 0; idx < len; idx++) {
      final RecordComponent component;
      component = components[idx];

      final RecordComponentWriter writer;
      writer = recordComponentWriter(component);

      writers[idx] = writer;
    }

    return new RecordWriter(writers);
  }

  private RecordComponentWriter recordComponentWriter(RecordComponent component) {
    final String name;
    name = component.getName();

    final String key;
    key = toKey(name);

    final Class<?> type;
    type = component.getType();

    final Toml.Property property;
    property = Toml.Property.of(type);

    final Method accessor;
    accessor = component.getAccessor();

    return new RecordComponentWriter(key, property, accessor);
  }

  private String toKey(String name) {
    // TODO convert to valid TOML key
    return name;
  }

}