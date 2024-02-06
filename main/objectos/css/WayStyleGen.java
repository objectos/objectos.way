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
package objectos.css;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import objectos.lang.object.Check;
import objectos.notes.NoOpNoteSink;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.NoteSink;
import objectos.util.map.GrowableSequencedMap;
import objectox.html.style.Bytes;

public class WayStyleGen implements StyleGen {

  // notes

  private static final Note1<String> CLASS_NOT_FOUND;

  private static final Note2<String, IOException> CLASS_IO_ERROR;

  private static final Note1<String> CLASS_LOADED;

  private static final Note2<String, String> INVALID_CLASS;

  private static final Note1<Utf8> UTF8;

  static {
    Class<?> s;
    s = StyleGen.class;

    CLASS_NOT_FOUND = Note1.error(s, "Class file not found");

    CLASS_IO_ERROR = Note2.error(s, "Class file I/O error");

    CLASS_LOADED = Note1.debug(s, "Class file loaded");

    INVALID_CLASS = Note2.error(s, "Invalid class file");

    UTF8 = Note1.trace(s, "Found");
  }

  private NoteSink noteSink = NoOpNoteSink.of();

  // scanner types

  private sealed interface Entry {}

  private record Utf8(String value) implements Entry {}

  private static final byte CONSTANT_Utf8 = 1;
  private static final byte CONSTANT_Integer = 3;
  private static final byte CONSTANT_Float = 4;
  private static final byte CONSTANT_Long = 5;
  private static final byte CONSTANT_Double = 6;
  private static final byte CONSTANT_Class = 7;
  private static final byte CONSTANT_String = 8;
  private static final byte CONSTANT_Fieldref = 9;
  private static final byte CONSTANT_Methodref = 10;
  private static final byte CONSTANT_InterfaceMethodref = 11;
  private static final byte CONSTANT_NameAndType = 12;
  private static final byte CONSTANT_MethodHandle = 15;
  private static final byte CONSTANT_MethodType = 16;
  private static final byte CONSTANT_Dynamic = 17;
  private static final byte CONSTANT_InvokeDynamic = 18;
  private static final byte CONSTANT_Module = 19;
  private static final byte CONSTANT_Package = 20;

  // scanner states

  private enum State {
    STOP,

    START,

    LOAD_CLASS,

    VERIFY_MAGIC,

    CONSTANT_POOL_COUNT,

    NEXT_POOL_INDEX,

    NEXT_POOL_ENTRY;
  }

  // scanner state

  private String binaryName;

  private byte[] bytes;

  private int bytesIndex;

  private int[] constantPoolIndex;

  private Entry[] constantPoolEntries;

  private int iteratorIndex;

  private State state;

  // parser state

  Map<String, Utility> utilities = new GrowableSequencedMap<>();

  public WayStyleGen() {}

  public final WayStyleGen noteSink(NoteSink noteSink) {
    this.noteSink = Check.notNull(noteSink, "noteSink == null");

    return this;
  }

  @Override
  public final void scan(Class<?> clazz) {
    binaryName = clazz.getName();

    state = State.START;

    while (state != State.STOP) {
      execute();
    }
  }

  private void execute() {
    state = switch (state) {
      case START -> executeStart();

      case LOAD_CLASS -> executeLoadClass();

      case VERIFY_MAGIC -> executeVerifyMagic();

      case CONSTANT_POOL_COUNT -> executeConstantPoolCount();

      case NEXT_POOL_INDEX -> executeNextPoolIndex();

      case NEXT_POOL_ENTRY -> executeNextPoolEntry();

      case STOP -> throw new IllegalStateException(
          "STOP is a non-executable state"
      );
    };
  }

  private State executeStart() {
    // reset scanner state if there's an input

    if (binaryName != null) {
      bytes = null;

      bytesIndex = 0;

      constantPoolIndex = null;

      constantPoolEntries = null;

      iteratorIndex = 0;

      return State.LOAD_CLASS;
    }

    // there's no input... stop

    return State.STOP;
  }

  private State executeLoadClass() {
    String resourceName;
    resourceName = binaryName.replace('.', '/');

    resourceName += ".class";

    ClassLoader loader;
    loader = ClassLoader.getSystemClassLoader();

    InputStream in;
    in = loader.getResourceAsStream(resourceName);

    if (in == null) {
      noteSink.send(CLASS_NOT_FOUND, binaryName);

      return State.STOP;
    }

    try (in; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      in.transferTo(out);

      bytes = out.toByteArray();

      noteSink.send(CLASS_LOADED, binaryName);

      return State.VERIFY_MAGIC;
    } catch (IOException e) {
      noteSink.send(CLASS_IO_ERROR, binaryName, e);

      return State.STOP;
    }
  }

  private State executeVerifyMagic() {
    if (!canRead(4)) {
      invalidClass("Magic not found");

      return State.STOP;
    }

    int magic;
    magic = readU4();

    if (magic != 0xCAFEBABE) {
      invalidClass("Magic not found");

      return State.STOP;
    }

    return State.CONSTANT_POOL_COUNT;
  }

  private State executeConstantPoolCount() {
    // minor = 2 bytes
    // major = 2 bytes
    // cp count = 2 bytes

    if (!canRead(2 + 2 + 2)) {
      invalidClass("Constant pool count not found");

      return State.STOP;
    }

    // skip minor
    bytesIndex += 2;

    // skip major
    bytesIndex += 2;

    int constantPoolCount;
    constantPoolCount = readU2();

    constantPoolIndex = new int[constantPoolCount];

    iteratorIndex = 1;

    return State.NEXT_POOL_INDEX;
  }

  private State executeNextPoolIndex() {
    if (iteratorIndex == constantPoolIndex.length) {
      iteratorIndex = 1;

      constantPoolEntries = new Entry[constantPoolIndex.length];

      return State.NEXT_POOL_ENTRY;
    }

    int index;
    index = iteratorIndex++;

    constantPoolIndex[index] = bytesIndex;

    if (!canRead(1)) {
      invalidClass("Unexpected constant pool end");

      return State.STOP;
    }

    byte tag;
    tag = nextByte();

    switch (tag) {
      case CONSTANT_Utf8 -> {
        if (!canRead(2)) {
          invalidClass("Unexpected constant pool end");

          return State.STOP;
        }

        int length;
        length = readU2();

        bytesIndex += length;
      }

      // u4 bytes;
      case CONSTANT_Integer -> bytesIndex += 4;

      // u4 bytes;
      case CONSTANT_Float -> bytesIndex += 4;

      // u4 high_bytes; u4 low_bytes;
      case CONSTANT_Long -> bytesIndex += 8;

      // u4 high_bytes; u4 low_bytes;
      case CONSTANT_Double -> bytesIndex += 8;

      // u2 name_index;
      case CONSTANT_Class -> bytesIndex += 2;

      // u2 string_index;
      case CONSTANT_String -> bytesIndex += 2;

      // u2 class_index; u2 name_and_type_index;
      case CONSTANT_Fieldref -> bytesIndex += 4;

      // u2 class_index; u2 name_and_type_index;
      case CONSTANT_Methodref -> bytesIndex += 4;

      // u2 class_index; u2 name_and_type_index;
      case CONSTANT_InterfaceMethodref -> bytesIndex += 4;

      // u2 name_index; u2 descriptor_index;
      case CONSTANT_NameAndType -> bytesIndex += 4;

      // u1 reference_kind; u2 reference_index;
      case CONSTANT_MethodHandle -> bytesIndex += 3;

      // u2 descriptor_index;
      case CONSTANT_MethodType -> bytesIndex += 2;

      // u2 bootstrap_method_attr_index; u2 name_and_type_index;
      case CONSTANT_Dynamic -> bytesIndex += 4;

      // u2 bootstrap_method_attr_index; u2 name_and_type_index;
      case CONSTANT_InvokeDynamic -> bytesIndex += 4;

      // u2 name_index;
      case CONSTANT_Module -> bytesIndex += 2;

      // u2 name_index;
      case CONSTANT_Package -> bytesIndex += 2;

      default -> {
        invalidClass("Unknown constant pool tag=" + tag);

        return State.STOP;
      }
    }

    return State.NEXT_POOL_INDEX;
  }

  private State executeNextPoolEntry() {
    if (iteratorIndex == constantPoolIndex.length) {
      return State.STOP;
    }

    int index;
    index = iteratorIndex++;

    bytesIndex = constantPoolIndex[index];

    // process if String

    byte maybeString;
    maybeString = nextByte();

    if (maybeString != CONSTANT_String) {
      // not String -> continue

      return State.NEXT_POOL_ENTRY;
    }

    // keep the index in the stack

    int stringIndex;
    stringIndex = readU2();

    // try to load utf8

    Utf8 utf8;
    utf8 = utf8(stringIndex);

    if (utf8 == null) {
      invalidClass("Malformed constant pool");

      return State.STOP;
    }

    noteSink.send(UTF8, utf8);

    parse(utf8.value);

    return State.NEXT_POOL_ENTRY;
  }

  private Utf8 utf8(int index) {
    Entry entry;
    entry = constantPoolEntries[index];

    if (entry == null) {
      bytesIndex = constantPoolIndex[index];

      byte tag;
      tag = nextByte();

      if (tag != CONSTANT_Utf8) {
        return null;
      }

      int length;
      length = readU2();

      String value;
      value = utf8Value(length);

      constantPoolEntries[index] = entry = new Utf8(value);
    }

    if (entry instanceof Utf8 utf8) {
      return utf8;
    }

    return null;
  }

  private String utf8Value(int length) {
    // 1: assume ASCII only

    boolean asciiOnly;
    asciiOnly = true;

    for (int offset = 0; offset < length; offset++) {
      byte b;
      b = bytes[bytesIndex + offset];

      int i;
      i = Bytes.toUnsignedInt(b);

      if (i > 0x7F) {
        asciiOnly = false;

        break;
      }
    }

    if (asciiOnly) {
      return new String(bytes, bytesIndex, length, StandardCharsets.UTF_8);
    }

    throw new UnsupportedOperationException("Implement me :: non ascii");
  }

  private boolean canRead(int qty) {
    return bytesIndex + qty <= bytes.length;
  }

  private void invalidClass(String message) {
    noteSink.send(INVALID_CLASS, binaryName, message);
  }

  private int readU2() {
    byte b0;
    b0 = nextByte();

    byte b1;
    b1 = nextByte();

    return Bytes.toBigEndianInt(b0, b1);
  }

  private int readU4() {
    byte b0;
    b0 = nextByte();

    byte b1;
    b1 = nextByte();

    byte b2;
    b2 = nextByte();

    byte b3;
    b3 = nextByte();

    return Bytes.toBigEndianInt(b0, b1, b2, b3);
  }

  private byte nextByte() {
    return bytes[bytesIndex++];
  }

  // parsing

  private void parse(String s) {
    int beginIndex;
    beginIndex = 0;

    int endIndex;
    endIndex = s.indexOf(' ', beginIndex);

    while (endIndex >= 0) {
      String candidate;
      candidate = s.substring(beginIndex, endIndex);

      process(candidate);

      beginIndex = endIndex;

      endIndex = s.indexOf(' ', beginIndex);
    }

    if (beginIndex == 0) {
      process(s);
    }
  }

  private void process(String candidate) {
    if (utilities.containsKey(candidate)) {
      return;
    }

    Utility utility;
    utility = Utility.parse(candidate);

    utilities.put(candidate, utility);
  }

  @Override
  public final String generate() {
    return utilities.values().stream()
        .filter(o -> o != Utility.UNKNOWN)
        .sorted()
        .map(Object::toString)
        .collect(Collectors.joining(System.lineSeparator(), "", System.lineSeparator()));
  }

}