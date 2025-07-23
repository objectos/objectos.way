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

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

final class TomlWriter implements Toml.Writer {

  private static final byte QUOTATION_MARK = '"';
  private static final byte LEFT_SQUARE_BRACKET = '[';
  private static final byte RIGHT_SQUARE_BRACKET = ']';

  private final byte[] buffer;

  private int bufferIndex;

  private final OutputStream outputStream;

  private Map<Class<?>, RecordWriter> recordWriters;

  TomlWriter(TomlWriterBuilder builder, OutputStream outputStream) {
    buffer = builder.buffer();

    this.outputStream = outputStream;
  }

  // ##################################################################
  // # BEGIN: Writing API
  // ##################################################################

  public final void writeProperty(String key, String value) throws IOException {
    name(key);

    kvsep();

    string(value);

    newLine();
  }

  public final void writeTable(String key, Record value) throws IOException {
    w(LEFT_SQUARE_BRACKET);
    name(key);
    w(RIGHT_SQUARE_BRACKET);

    newLine();

    writeRecord(value);
  }

  @Override
  public final void writeRecord(Record value) throws IOException {
    final Class<? extends Record> key;
    key = value.getClass();

    final Map<Class<?>, RecordWriter> writers;
    writers = recordWriters();

    final RecordWriter writer;
    writer = writers.computeIfAbsent(key, this::recordWriter);

    writer.write(value);
  }

  // ##################################################################
  // # END: Writing API
  // ##################################################################

  // ##################################################################
  // # BEGIN: I/O
  // ##################################################################

  @Override
  public final void close() throws IOException {
    flush();

    outputStream.close();
  }

  @Override
  public final void flush() throws IOException {
    final int len;
    len = bufferIndex;

    bufferIndex = 0;

    outputStream.write(buffer, 0, len);
  }

  private void flushIfNecessary() throws IOException {
    if (bufferIndex == buffer.length) {
      flush();
    }
  }

  private static final byte[] KV_SEP = " = ".getBytes(StandardCharsets.UTF_8);

  private void kvsep() throws IOException {
    w(KV_SEP);
  }

  private static final byte[] NAME_TABLE;

  private static final byte NAME_BARE = 1; // valid bare name/key

  static {
    final byte[] table;
    table = new byte[128];

    Http.fillTable(table, Ascii.alphaLower(), NAME_BARE);
    Http.fillTable(table, Ascii.alphaUpper(), NAME_BARE);
    Http.fillTable(table, Ascii.digit(), NAME_BARE);
    table['-'] = NAME_BARE;
    table['_'] = NAME_BARE;

    NAME_TABLE = table;
  }

  private void name(String value) throws IOException {
    final byte[] bytes;
    bytes = value.getBytes(StandardCharsets.UTF_8);

    boolean quoted;
    quoted = false;

    for (int idx = 0; idx < bytes.length; idx++) {
      final byte b;
      b = bytes[idx];

      if (b < 0) {
        quoted = true;
        break;
      }

      byte code;
      code = NAME_TABLE[b];

      if (code != NAME_BARE) {
        quoted = true;
        break;
      }
    }

    if (quoted) {
      stringBasic(bytes);
    } else {
      w(bytes);
    }
  }

  private void newLine() throws IOException {
    w(Bytes.LF);
  }

  private void string(String value) throws IOException {
    final byte[] bytes;
    bytes = value.getBytes(StandardCharsets.UTF_8);

    // currently only basic strings are supported
    stringBasic(bytes);
  }

  private static final byte[] STRING_TABLE;

  @SuppressWarnings("unused")
  private static final byte STRING_UNICODE = 0; // requires \x12 escaping
  private static final byte STRING_B = 1; // requires \b escaping
  private static final byte STRING_T = 2; // requires \t escaping
  private static final byte STRING_N = 3; // requires \n escaping
  private static final byte STRING_F = 4; // requires \f escaping
  private static final byte STRING_R = 5; // requires \r escaping
  private static final byte STRING_DQUOTE = 6; // requires \" escaping
  private static final byte STRING_BACKSLASH = 7; // requires \\ escaping
  private static final byte STRING_UNESCAPED = 8; //

  static {
    final byte[] table;
    table = new byte[128];

    table['\b'] = STRING_B;
    table['\t'] = STRING_T;
    table['\n'] = STRING_N;
    table['\f'] = STRING_F;
    table['\r'] = STRING_R;
    table['\"'] = STRING_DQUOTE;
    table['\\'] = STRING_BACKSLASH;

    table[0x20] = STRING_UNESCAPED; // space

    // basic-unescaped = wschar / %x21 / %x23-5B / %x5D-7E / non-ascii
    table[0x21] = STRING_UNESCAPED;

    for (int i = 0x23; i <= 0x5B; i++) {
      table[i] = STRING_UNESCAPED;
    }

    for (int i = 0x5D; i <= 0x7E; i++) {
      table[i] = STRING_UNESCAPED;
    }

    STRING_TABLE = table;
  }

  private void stringBasic(byte[] bytes) throws IOException {
    w(QUOTATION_MARK);

    int unescapedIndex;
    unescapedIndex = bytes.length;

    for (int idx = 0; idx < bytes.length; idx++) {
      final byte b;
      b = bytes[idx];

      if (b < 0) {
        unescapedIndex = idx;

        break;
      }

      final byte code;
      code = STRING_TABLE[b];

      if (code != STRING_UNESCAPED) {
        unescapedIndex = idx;

        break;
      }
    }

    if (unescapedIndex > 0) {
      w(bytes, 0, unescapedIndex);
    }

    if (unescapedIndex < bytes.length) {
      throw new UnsupportedOperationException("Implement me");
    }

    w(QUOTATION_MARK);
  }

  private void w(byte b) throws IOException {
    flushIfNecessary();

    buffer[bufferIndex++] = b;
  }

  private void w(byte[] bytes) throws IOException {
    w(bytes, 0, bytes.length);
  }

  private void w(byte[] bytes, int offset, int length) throws IOException {
    int bytesIndex;
    bytesIndex = offset;

    int remaining;
    remaining = length;

    while (remaining > 0) {
      int available;
      available = buffer.length - bufferIndex;

      if (available <= 0) {
        flush();

        available = buffer.length - bufferIndex;
      }

      final int bytesToCopy;
      bytesToCopy = Math.min(remaining, available);

      System.arraycopy(bytes, bytesIndex, buffer, bufferIndex, bytesToCopy);

      bufferIndex += bytesToCopy;

      bytesIndex += bytesToCopy;

      remaining -= bytesToCopy;
    }
  }

  // ##################################################################
  // # END: I/O
  // ##################################################################

  // ##################################################################
  // # BEGIN: TOML Types support
  // ##################################################################

  private enum PropKind {

    RECORD,

    STRING;

    static PropKind of(Class<?> type) {
      if (type.isRecord()) {
        return RECORD;
      }

      final String typeName;
      typeName = type.getName();

      return switch (typeName) {
        case "java.lang.String" -> STRING;

        default -> throw new IllegalArgumentException("Implement me");
      };
    }

  }

  // ##################################################################
  // # END: TOML Types support
  // ##################################################################

  // ##################################################################
  // # BEGIN: java.lang.Record support
  // ##################################################################

  private static final class RecordWriter {

    private final RecordComponentWriter[] components;

    RecordWriter(RecordComponentWriter[] components) {
      this.components = components;
    }

    public final void write(Record value) throws IOException {
      for (RecordComponentWriter c : components) {
        c.write(value);
      }
    }

  }

  private Map<Class<?>, RecordWriter> recordWriters() {
    if (recordWriters == null) {
      recordWriters = new HashMap<>();
    }

    return recordWriters;
  }

  private RecordWriter recordWriter(Class<?> key) {
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

  private class RecordComponentWriter {

    private final PropKind kind;

    private final String key;

    private final Method accessor;

    RecordComponentWriter(PropKind kind, String key, Method accessor) {
      this.kind = kind;

      this.key = key;

      this.accessor = accessor;
    }

    public final void write(Record obj) throws IOException {
      try {
        Object value;
        value = accessor.invoke(obj);

        switch (kind) {
          case RECORD -> writeTable(key, (Record) value);

          case STRING -> writeProperty(key, (String) value);
        }
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new IOException("Failed to read a record component value", e);
      }
    }

  }

  private RecordComponentWriter recordComponentWriter(RecordComponent component) {
    final Class<?> type;
    type = component.getType();

    final PropKind kind = PropKind.of(type);

    final String name;
    name = component.getName();

    final Method accessor;
    accessor = component.getAccessor();

    return new RecordComponentWriter(kind, name, accessor);
  }

  // ##################################################################
  // # END: java.lang.Record support
  // ##################################################################

}