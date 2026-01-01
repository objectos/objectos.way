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

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.RecordComponent;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class TomlReader implements Toml.Reader {

  private enum ParseError {

    EXPRESSION_INVALID_CHAR,

    NAME_INVALID_CHAR,

    NAME_BARE_INVALID_CHAR,

    EOL_INVALID_CHAR,

    VALUE_INVALID_CHAR,

    UNEXPECTED_EOF;

  }

  private sealed interface Target {

    boolean hasNext();

    String property(String name, Object value);

    String table(String name, Map<String, Object> ctx);

    Object result() throws Throwable;

  }

  private static final byte KIND_KEYVL = 1;
  private static final byte KIND_TABLE = 2;
  private static final byte KIND_ARRAY = 3;

  private static final byte PART_BARE = 1;
  @SuppressWarnings("unused")
  private static final byte PART_SNGL = 2;
  @SuppressWarnings("unused")
  private static final byte PART_DBLE = 3;

  private final byte[] buffer;

  private int bufferIndex;

  private int bufferLimit;

  private byte ctxKind;

  @SuppressWarnings("unused")
  private byte ctxPart;

  private final Map<String, Object> document = new UtilSequencedMap<>();

  private final InputStream inputStream;

  private int int0;

  private int int1;

  private final MethodHandles.Lookup lookup;

  private List<String> names;

  private Map<Class<?>, RecordReader> recordReaders;

  private byte state;

  private byte stateNext;

  private Target target;

  private Object value;

  TomlReader(TomlReaderBuilder builder) {
    buffer = builder.buffer();

    lookup = builder.lookup;

    inputStream = builder.inputStream;
  }

  @Override
  public final void close() throws IOException {
    inputStream.close();
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T extends Record> T readRecord(Class<T> type) throws IOException {
    try {
      final RecordReader reader;
      reader = recordReader(type);

      target = reader.toTarget();

      parse();

      final Object result;
      result = target.result();

      return (T) result;
    } catch (Throwable e) {
      throw new IOException("Failed to create instance", e);
    }
  }

  // ##################################################################
  // # BEGIN: java.lang.Record support
  // ##################################################################

  private static final class RecordReader implements RecordComponentType {

    private final MethodHandle constructor;

    private final RecordComponentReader[] components;

    RecordReader(MethodHandle constructor, RecordComponentReader[] components) {
      this.constructor = constructor;

      this.components = components;
    }

    public final Target toTarget() {
      return toTarget(null);
    }

    @Override
    public final Target toTarget(String key) {
      final int length;
      length = components.length;

      final Target[] targets;
      targets = new Target[length];

      for (int idx = 0; idx < length; idx++) {
        final RecordComponentReader c;
        c = components[idx];

        targets[idx] = c.toTarget();
      }

      return new RecordTarget(key, constructor, targets);
    }

  }

  private static final class RecordTarget implements Target {

    @SuppressWarnings("unused")
    private final MethodHandle constructor;

    private final Target[] components;

    private int index;

    private final String key;

    RecordTarget(String key, MethodHandle constructor, Target[] components) {
      if (key != null) {
        index = -1;
      }

      this.key = key;

      this.constructor = constructor;

      this.components = components;
    }

    @Override
    public final boolean hasNext() {
      return index < components.length;
    }

    @Override
    public final String property(String name, Object value) {
      if (index == -1) {
        return "Expected a table " + key + " but got a property " + name;
      }

      final Target current;
      current = components[index];

      final String error;
      error = current.property(name, value);

      if (error != null) {
        return error;
      }

      if (!current.hasNext()) {
        index += 1;
      }

      return null;
    }

    @Override
    public final String table(String name, Map<String, Object> ctx) {
      if (index == -1) {
        if (name.equals(key)) {
          index = 0;

          return null;
        }

        return "Expected a table " + key + " but got " + name;
      }

      final Target current;
      current = components[index];

      final String error;
      error = current.table(name, ctx);

      if (error != null) {
        return error;
      }

      if (!current.hasNext()) {
        index += 1;
      }

      return null;
    }

    @Override
    public final Object result() throws Throwable {
      final int length;
      length = components.length;

      final Object[] args;
      args = new Object[length];

      for (int idx = 0; idx < length; idx++) {
        final Target c;
        c = components[idx];

        args[idx] = c.result();
      }

      return constructor.invokeWithArguments(args);
    }

  }

  private RecordReader recordReader(Class<?> key) {
    if (recordReaders == null) {
      recordReaders = new HashMap<>();
    }

    final RecordReader existing;
    existing = recordReaders.get(key);

    if (existing != null) {
      return existing;
    }

    final RecordComponent[] components;
    components = key.getRecordComponents();

    final Class<?>[] componentTypes;
    componentTypes = new Class<?>[components.length];

    final RecordComponentReader[] readers;
    readers = new RecordComponentReader[components.length];

    for (int idx = 0; idx < components.length; idx++) {
      final RecordComponent c;
      c = components[idx];

      componentTypes[idx] = c.getType();

      readers[idx] = recordComponentReader(c);
    }

    final MethodType constructorType;
    constructorType = MethodType.methodType(void.class, componentTypes);

    final MethodHandle constructor;

    try {
      constructor = lookup.findConstructor(key, constructorType);
    } catch (NoSuchMethodException | IllegalAccessException e) {
      throw new Toml.RecordException(e);
    }

    final RecordReader result;
    result = new RecordReader(constructor, readers);

    final RecordReader checkJustInCase;
    checkJustInCase = recordReaders.put(key, result);

    assert checkJustInCase == null;

    return result;
  }

  private static final class RecordComponentReader {

    private final RecordComponentType type;

    private final String key;

    RecordComponentReader(RecordComponentType type, String key) {
      this.type = type;

      this.key = key;
    }

    public final Target toTarget() {
      return type.toTarget(key);
    }

  }

  private sealed interface RecordComponentType {

    Target toTarget(String key);

  }

  private enum PropKind implements RecordComponentType {

    STRING;

    static PropKind of(Class<?> type) {
      final String typeName;
      typeName = type.getName();

      return switch (typeName) {
        case "java.lang.String" -> PropKind.STRING;

        default -> throw new IllegalArgumentException("Implement me");
      };
    }

    @Override
    public final Target toTarget(String key) {
      return new PropTarget(this, key);
    }

  }

  private static final class PropTarget implements Target {

    private final PropKind kind;

    private final String key;

    private Object value;

    PropTarget(PropKind kind, String key) {
      this.kind = kind;

      this.key = key;
    }

    @Override
    public final boolean hasNext() {
      return value == null;
    }

    @Override
    public final String property(String name, Object value) {
      if (!key.equals(name)) {
        return "Expected a %s '%s' property but got a property %s".formatted(kind, key, name);
      }

      final Class<? extends Object> type;
      type = value.getClass();

      final PropKind actual;
      actual = PropKind.of(type);

      if (!actual.equals(kind)) {
        return "Expected a %s '%s' property but got a property %s".formatted(kind, key, actual);
      }

      this.value = value;

      return null;
    }

    @Override
    public final String table(String name, Map<String, Object> ctx) {
      return "Expected a %s '%s' property but got a table %s".formatted(kind, key, name);
    }

    @Override
    public final Object result() {
      return value;
    }

  }

  private RecordComponentReader recordComponentReader(RecordComponent component) {
    final Class<?> type;
    type = component.getType();

    final RecordComponentType supplier;

    if (type.isRecord()) {
      supplier = recordReader(type);
    } else {
      supplier = PropKind.of(type);
    }

    final String name;
    name = component.getName();

    return new RecordComponentReader(supplier, name);
  }

  // ##################################################################
  // # END: java.lang.Record support
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parser: API
  // ##################################################################

  private void parse() {
    execute($START, $RESULT);
  }

  // ##################################################################
  // # END: Parser: API
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parser: State Machine
  // ##################################################################

  static final byte $START = 0;

  static final byte $EXP = 1;
  static final byte $EXP_OPEN_BRACKET = 2;

  static final byte $EOL = 3;
  static final byte $EOL_CR = 4;

  static final byte $NAME_BARE = 5;
  static final byte $NAME_ARRAY_CLOSE = 6;
  static final byte $NAME_WS = 7;

  static final byte $VALUE = 8;
  static final byte $VALUE_STRING = 9;
  static final byte $VALUE_STRING_DQUOTE2 = 10;
  static final byte $VALUE_STRING_MULTI_LINE = 11;
  static final byte $VALUE_STRING_UNESCAPE = 12;
  static final byte $VALUE_STRING_LITERAL = 13;

  static final byte $READ = 14;
  static final byte $READ_EOF = 15;

  static final byte $PROCESS = 16;
  static final byte $RESULT = 17;

  final void execute(byte from, byte to) {
    state = from;

    while (state < to) {
      execute();
    }
  }

  private void execute() {
    state = switch (state) {
      case $START -> executeStart();

      case $EXP -> executeExp();
      case $EXP_OPEN_BRACKET -> executeExpOpenBracket();

      case $EOL -> executeEol();

      case $NAME_BARE -> executeNameBare();
      case $NAME_WS -> executeNameWs();

      case $VALUE -> executeValue();
      case $VALUE_STRING -> executeValueString();
      case $VALUE_STRING_DQUOTE2 -> executeValueStringDquote2();
      case $VALUE_STRING_UNESCAPE -> executeValueStringUnescape();

      case $READ -> executeRead();
      case $READ_EOF -> executeReadEof();

      case $PROCESS -> executeProcess();

      default -> throw new AssertionError("Unexpected state=" + state);
    };
  }

  // ##################################################################
  // # END: Parser: State Machine
  // ##################################################################

  private byte executeStart() {
    if (names == null) {
      names = new ArrayList<>();
    } else {
      names.clear();
    }

    return $EXP;
  }

  // ##################################################################
  // # BEGIN: Parser: Expression
  // ##################################################################

  private static final byte[] EXPRESSION_TABLE;

  private static final byte EXPRESSION_WS = 1;
  private static final byte EXPRESSION_BARE = 2;
  private static final byte EXPRESSION_OPEN_BRACKET = 3;

  static {
    final byte[] table;
    table = new byte[256];

    table[' '] = EXPRESSION_WS;
    table['\t'] = EXPRESSION_WS;

    Http.fillTable(table, Ascii.alphaLower(), EXPRESSION_BARE);
    Http.fillTable(table, Ascii.alphaUpper(), EXPRESSION_BARE);
    Http.fillTable(table, Ascii.digit(), EXPRESSION_BARE);
    table['-'] = EXPRESSION_BARE;
    table['_'] = EXPRESSION_BARE;

    table['['] = EXPRESSION_OPEN_BRACKET;

    EXPRESSION_TABLE = table;
  }

  private byte executeExp() {
    while (bufferIndex < bufferLimit) {
      final int b;
      b = peek();

      final byte code;
      code = EXPRESSION_TABLE[b];

      switch (code) {
        case EXPRESSION_WS -> { continue; }

        case EXPRESSION_BARE -> { int0 = adv(); ctxKind = KIND_KEYVL; ctxPart = PART_BARE; return $NAME_BARE; }

        case EXPRESSION_OPEN_BRACKET -> { int0 = advPre(); return $EXP_OPEN_BRACKET; }

        default -> { return to(ParseError.EXPRESSION_INVALID_CHAR); }
      }
    }

    return toRead();
  }

  private byte executeExpOpenBracket() {
    while (bufferIndex < bufferLimit) {
      final int b;
      b = peek();

      final byte code;
      code = NAME_TABLE[b];

      switch (code) {
        case NAME_WS -> { adv(); continue; }

        case NAME_BARE -> { ctxKind = KIND_TABLE; ctxPart = PART_BARE; return $NAME_BARE; }

        default -> { return to(ParseError.NAME_INVALID_CHAR); }
      }
    }

    return toRead();
  }

  // ##################################################################
  // # END: Parser: Expression
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parser: End Of Line
  // ##################################################################

  private byte executeEol() {
    while (bufferIndex < bufferLimit) {
      final int b;
      b = peek();

      switch (b) {
        case ' ', '\t' -> { adv(); continue; }

        case '\r' -> { adv(); return $EOL_CR; }

        case '\n' -> { adv(); return $PROCESS; }

        default -> { return to(ParseError.EOL_INVALID_CHAR); }
      }
    }

    return toRead();
  }

  // ##################################################################
  // # END: Parser: End Of Line
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parser: Name
  // ##################################################################

  private static final byte[] NAME_TABLE;

  private static final byte NAME_WS = 1;
  private static final byte NAME_BARE = 2;
  private static final byte NAME_EQ = 3;
  private static final byte NAME_LEFT = 4;
  private static final byte NAME_RIGHT = 5;

  static {
    final byte[] table;
    table = new byte[256];

    table[' '] = NAME_WS;
    table['\t'] = NAME_WS;

    Http.fillTable(table, Ascii.alphaLower(), NAME_BARE);
    Http.fillTable(table, Ascii.alphaUpper(), NAME_BARE);
    Http.fillTable(table, Ascii.digit(), NAME_BARE);

    table['_'] = NAME_BARE;
    table['-'] = NAME_BARE;

    table['='] = NAME_EQ;

    table['['] = NAME_LEFT;

    table[']'] = NAME_RIGHT;

    NAME_TABLE = table;
  }

  private byte executeNameBare() {
    while (bufferIndex < bufferLimit) {
      final int b;
      b = peek();

      final byte code;
      code = NAME_TABLE[b];

      switch (code) {
        case NAME_WS -> { int1 = adv(); return $NAME_WS; }

        case NAME_BARE -> { adv(); continue; }

        case NAME_RIGHT -> {
          if (ctxKind == KIND_KEYVL) {
            return to(ParseError.NAME_BARE_INVALID_CHAR);
          }

          else {
            final String value;
            value = bufferToString();

            names.add(value);

            adv();

            return ctxKind == KIND_TABLE ? $EOL : $NAME_ARRAY_CLOSE;
          }
        }

        default -> { return to(ParseError.NAME_BARE_INVALID_CHAR); }
      }
    }

    return toRead();
  }

  private byte executeNameWs() {
    while (bufferIndex < bufferLimit) {
      final int b;
      b = peek();

      final byte code;
      code = NAME_TABLE[b];

      switch (code) {
        case NAME_WS -> { adv(); continue; }

        case NAME_EQ -> {
          if (ctxKind == KIND_TABLE || ctxKind == KIND_ARRAY) {
            throw new UnsupportedOperationException("Implement me");
          }

          final String value;
          value = bufferToString(int1);

          names.add(value);

          adv();

          return $VALUE;
        }

        default -> { return to(ParseError.NAME_BARE_INVALID_CHAR); }
      }
    }

    return toRead();
  }

  // ##################################################################
  // # END: Parse: Name
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse: Value
  // ##################################################################

  private byte executeValue() {
    while (bufferIndex < bufferLimit) {
      final int b;
      b = peek();

      switch (b) {
        case ' ', '\t' -> { adv(); continue; }

        case '"' -> { int0 = advPre(); return $VALUE_STRING; }

        default -> to(ParseError.VALUE_INVALID_CHAR);
      }
    }

    return toRead();
  }

  private static final byte[] STRING_TABLE;

  private static final byte STRING_VALID = 1;
  private static final byte STRING_DQUOTE = 2;
  private static final byte STRING_BACKSLASH = 3;

  static {
    final byte[] table;
    table = new byte[256];

    Http.fillTable(table, Toml.basicUnescaped(), STRING_VALID);

    table['"'] = STRING_DQUOTE;
    table['\\'] = STRING_BACKSLASH;

    STRING_TABLE = table;
  }

  private byte executeValueString() {
    while (bufferIndex < bufferLimit) {
      final int b;
      b = peek();

      final byte code;
      code = STRING_TABLE[b];

      switch (code) {
        case STRING_VALID -> { adv(); continue; }

        case STRING_DQUOTE -> {
          final int quote;
          quote = adv();

          if (quote == int0) {
            // empty string or multiline
            return $VALUE_STRING_DQUOTE2;
          }

          // end of string

          value = bufferToString(quote);

          return $EOL;
        }

        case STRING_BACKSLASH -> { adv(); return $VALUE_STRING_UNESCAPE; }

        default -> to(ParseError.VALUE_INVALID_CHAR);
      }
    }

    return toRead();
  }

  private byte executeValueStringDquote2() {
    throw new UnsupportedOperationException("Implement me");
  }

  private byte executeValueStringUnescape() {
    throw new UnsupportedOperationException("Implement me");
  }

  // ##################################################################
  // # END: Parse: Value
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse: Read
  // ##################################################################

  private byte executeRead() {
    try {
      final int read;
      read = inputStream.read(buffer, 0, buffer.length);

      if (read == -1) {
        return $READ_EOF;
      }

      bufferIndex = 0;

      bufferLimit = read;

      return stateNext;
    } catch (IOException e) {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  private byte executeReadEof() {
    return switch (stateNext) {
      case $EXP -> $RESULT;

      default -> to(ParseError.UNEXPECTED_EOF);
    };
  }

  private int adv() {
    return bufferIndex++;
  }

  private int advPre() {
    return ++bufferIndex;
  }

  private String bufferToString() {
    return bufferToString(bufferIndex);
  }

  private String bufferToString(int endIndex) {
    final int len;
    len = endIndex - int0;

    return new String(buffer, int0, len, StandardCharsets.UTF_8);
  }

  private int peek() {
    final byte b;
    b = buffer[bufferIndex];

    return b & 0xff;
  }

  private byte toRead() {
    stateNext = state;

    return $READ;
  }

  // ##################################################################
  // # END: Parse: Read
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse: Process
  // ##################################################################

  private byte executeProcess() {
    final Map<String, Object> parent;
    parent = findParent();

    if (parent == null) {
      throw new UnsupportedOperationException("Implement me :: names=" + names);
    }

    final String name;
    name = names.removeLast();

    switch (ctxKind) {
      case KIND_KEYVL -> {
        assert value != null;

        final Object existing;
        existing = parent.put(name, value);

        if (existing != null) {
          throw new UnsupportedOperationException("Implement me");
        }

        final String error;
        error = target.property(name, value);

        if (error != null) {
          throw new UnsupportedOperationException("Implement me :: error=" + error);
        }

        return $EXP;
      }

      case KIND_TABLE -> {
        assert value == null;

        final Map<String, Object> table;
        table = new UtilSequencedMap<>();

        final Object existing;
        existing = parent.put(name, table);

        if (existing != null) {
          throw new UnsupportedOperationException("Implement me");
        }

        final String error;
        error = target.table(name, table);

        if (error != null) {
          throw new UnsupportedOperationException("Implement me");
        }

        names.add(name);

        return $EXP;
      }

      case KIND_ARRAY -> {
        throw new UnsupportedOperationException("Implement me");
      }

      default -> throw new AssertionError("Unexpected context kind=" + ctxKind);
    }
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> findParent() {
    assert !names.isEmpty() : names.toString();

    final int size;
    size = names.size();

    if (size == 1) {
      return document;
    }

    Map<String, Object> parent;
    parent = document;

    for (int idx = 0, max = size - 1; idx < max; idx++) {
      final String name;
      name = names.get(idx);

      final Map<String, Object> current;

      final Object candidate;
      candidate = parent.get(name);

      if (candidate == null) {
        current = new UtilSequencedMap<>();

        parent.put(name, current);
      }

      else if (candidate instanceof Map) {
        current = (Map<String, Object>) candidate;
      }

      else {
        return null;
      }

      parent = current;
    }

    return parent;
  }

  // ##################################################################
  // # END: Parse: Process
  // ##################################################################

  // ##################################################################
  // # BEGIN: Error
  // ##################################################################

  private byte to(ParseError error) {
    throw new UnsupportedOperationException("Implement me");
  }

  // ##################################################################
  // # END: Error
  // ##################################################################

}