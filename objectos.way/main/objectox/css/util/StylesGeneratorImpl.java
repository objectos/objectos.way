/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectox.css.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import objectos.css.util.StylesGenerator;
import objectos.html.tmpl.Api.ExternalAttribute.StyleClass;
import objectos.lang.Note1;
import objectos.lang.Note2;
import objectos.lang.NoteSink;
import objectos.util.GrowableMap;
import objectos.util.GrowableSet;
import objectox.lang.Bytes;
import objectox.lang.Check;

public final class StylesGeneratorImpl implements StylesGenerator {

  // notes

  static final Note1<String> CLASS_NOT_FOUND;

  static final Note2<String, IOException> CLASS_IO_ERROR;

  static final Note1<String> CLASS_LOADED;

  static final Note2<String, String> INVALID_CLASS;

  static final Note1<String> UNKNOWN_PREFIX;

  static final Note1<String> UNKNOWN_PROPERTY;

  static final Note2<String, String> UNKNOWN_UTILITY;

  static {
    Class<?> s;
    s = StylesGenerator.class;

    CLASS_NOT_FOUND = Note1.error(s, "Class file not found");

    CLASS_IO_ERROR = Note2.error(s, "Class file I/O error");

    CLASS_LOADED = Note1.debug(s, "Class file loaded");

    INVALID_CLASS = Note2.error(s, "Invalid class file");

    UNKNOWN_PREFIX = Note1.error(s, "Unknown prefix");

    UNKNOWN_PROPERTY = Note1.error(s, "Unknown property");

    UNKNOWN_UTILITY = Note2.error(s, "Unknown utility");
  }

  private final NoteSink noteSink;

  // scanner types

  sealed interface Entry {}

  record ClassName(String name) implements Entry {
    public final String asDescriptor() {
      return "L" + name + ";";
    }
  }

  record NameAndType(String name, String descriptor) implements Entry {
    public final boolean sameType(ClassName className) {
      String other;
      other = className.asDescriptor();

      return descriptor.equals(other);
    }
  }

  record Utf8(String value) implements Entry {}

  static final byte CONSTANT_Utf8 = 1;
  static final byte CONSTANT_Integer = 3;
  static final byte CONSTANT_Float = 4;
  static final byte CONSTANT_Long = 5;
  static final byte CONSTANT_Double = 6;
  static final byte CONSTANT_Class = 7;
  static final byte CONSTANT_String = 8;
  static final byte CONSTANT_Fieldref = 9;
  static final byte CONSTANT_Methodref = 10;
  static final byte CONSTANT_InterfaceMethodref = 11;
  static final byte CONSTANT_NameAndType = 12;
  static final byte CONSTANT_MethodHandle = 15;
  static final byte CONSTANT_MethodType = 16;
  static final byte CONSTANT_Dynamic = 17;
  static final byte CONSTANT_InvokeDynamic = 18;
  static final byte CONSTANT_Module = 19;
  static final byte CONSTANT_Package = 20;

  // scanner states

  enum State {
    STOP,

    START,

    LOAD_CLASS,

    VERIFY_MAGIC,

    CONSTANT_POOL_COUNT,

    NEXT_POOL_INDEX,

    NEXT_POOL_ENTRY;
  }

  // scanner state

  String binaryName;

  byte[] bytes;

  int bytesIndex;

  int[] constantPoolIndex;

  Entry[] constantPoolEntries;

  int iteratorIndex;

  State state;

  // generator state

  Map<String, Map<String, Set<String>>> utilities;

  StringBuilder out;

  public StylesGeneratorImpl(NoteSink noteSink) {
    this.noteSink = noteSink;
  }

  @Override
  public final void scan(Class<?> clazz) {
    Check.notNull(clazz, "clazz == null");

    binaryName = clazz.getName();

    state = State.START;

    while (state != State.STOP) {
      execute();
    }
  }

  final void execute() {
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

  private static final String CSS_UTIL = "objectos.css.util.";

  private State executeNextPoolEntry() {
    if (iteratorIndex == constantPoolIndex.length) {
      return State.STOP;
    }

    int index;
    index = iteratorIndex++;

    bytesIndex = constantPoolIndex[index];

    // process if Fieldref

    byte maybeConstantFieldRef;
    maybeConstantFieldRef = nextByte();

    if (maybeConstantFieldRef != CONSTANT_Fieldref) {
      // not Fieldref -> continue

      return State.NEXT_POOL_ENTRY;
    }

    // keep two indices in the stack

    int classIndex;
    classIndex = readU2();

    int nameAndTypeIndex;
    nameAndTypeIndex = readU2();

    // try to load class info

    ClassName className;
    className = className(classIndex);

    if (className == null) {
      invalidClass("Malformed constant pool");

      return State.STOP;
    }

    String fullName;
    fullName = className.name.replace('/', '.');

    if (!fullName.startsWith(CSS_UTIL)) {
      // not in our package -> continue

      return State.NEXT_POOL_ENTRY;
    }

    // let's try to get the simple name of the class

    int length;
    length = CSS_UTIL.length();

    String simpleName;
    simpleName = className.name.substring(length);

    int dotIndex;
    dotIndex = simpleName.indexOf('.');

    if (dotIndex >= 0) {
      // we're not interested in classes in a sub-package

      return State.NEXT_POOL_ENTRY;
    }

    NameAndType nameAndType;
    nameAndType = nameAndType(nameAndTypeIndex);

    if (nameAndType == null) {
      invalidClass("Malformed constant pool");

      return State.STOP;
    }

    if (!nameAndType.sameType(className)) {
      // fieldref is not of the same type

      return State.NEXT_POOL_ENTRY;
    }

    // assume no prefix

    String prefix;
    prefix = "";

    // it might be an inner type like Max$Display

    int dollarIndex;
    dollarIndex = simpleName.indexOf('$');

    if (dollarIndex > 0) {
      prefix = simpleName.substring(0, dollarIndex);

      simpleName = simpleName.substring(dollarIndex + 1);
    }

    if (utilities == null) {
      utilities = new GrowableMap<>();
    }

    Map<String, Set<String>> properties;
    properties = utilities.get(prefix);

    if (properties == null) {
      properties = new GrowableMap<>();

      utilities.put(prefix, properties);
    }

    Set<String> propertyValues;
    propertyValues = properties.get(fullName);

    if (propertyValues == null) {
      propertyValues = new GrowableSet<>();

      properties.put(fullName, propertyValues);
    }

    propertyValues.add(nameAndType.name);

    return State.NEXT_POOL_ENTRY;
  }

  private ClassName className(int index) {
    Entry entry;
    entry = constantPoolEntries[index];

    if (entry == null) {
      bytesIndex = constantPoolIndex[index];

      byte tag;
      tag = nextByte();

      if (tag != CONSTANT_Class) {
        return null;
      }

      int nameIndex;
      nameIndex = readU2();

      Utf8 name;
      name = utf8(nameIndex);

      if (name == null) {
        return null;
      }

      constantPoolEntries[index] = entry = new ClassName(name.value);
    }

    if (entry instanceof ClassName name) {
      return name;
    }

    return null;
  }

  private NameAndType nameAndType(int index) {
    Entry entry;
    entry = constantPoolEntries[index];

    if (entry == null) {
      bytesIndex = constantPoolIndex[index];

      byte tag;
      tag = nextByte();

      if (tag != CONSTANT_NameAndType) {
        return null;
      }

      int nameIndex;
      nameIndex = readU2();

      int descriptorIndex;
      descriptorIndex = readU2();

      Utf8 name;
      name = utf8(nameIndex);

      if (name == null) {
        return null;
      }

      Utf8 descriptor;
      descriptor = utf8(descriptorIndex);

      if (descriptor == null) {
        return null;
      }

      constantPoolEntries[index] = entry = new NameAndType(name.value, descriptor.value);
    }

    if (entry instanceof NameAndType name) {
      return name;
    }

    return null;
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

  @Override
  public final String generate() {
    if (utilities == null) {
      return "";
    }

    if (utilities.isEmpty()) {
      return "";
    }

    out = new StringBuilder();

    for (var entry : utilities.entrySet()) {
      String prefix;
      prefix = entry.getKey();

      Map<String, Set<String>> values;
      values = entry.getValue();

      generate0(prefix, values);
    }

    return out.toString();
  }

  private void generate0(String prefix, Map<String, Set<String>> properties) {
    switch (prefix) {
      case "" -> generate1("", properties);

      default -> noteSink.send(UNKNOWN_PREFIX, prefix);
    }
  }

  private void generate1(String indentation, Map<String, Set<String>> properties) {
    for (var entry : properties.entrySet()) {
      String propertyName;
      propertyName = entry.getKey();

      Set<String> constants;
      constants = entry.getValue();

      generate2(indentation, propertyName, constants);
    }
  }

  @SuppressWarnings("unchecked")
  private <T extends Enum<T>> void generate2(
      String indentation, String propertyName, Set<String> constants) {
    Class<? extends StylesGeneratorImpl> thisClass;
    thisClass = getClass();

    ClassLoader loader;
    loader = thisClass.getClassLoader();

    Class<?> clazz;

    try {
      clazz = loader.loadClass(propertyName);
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();

      return;
    }

    if (!clazz.isEnum()) {
      return;
    }

    if (!StyleClass.class.isAssignableFrom(clazz)) {
      return;
    }

    Class<T> enumClass;
    enumClass = (Class<T>) clazz;

    for (var constant : constants) {
      T instance;

      try {
        instance = Enum.valueOf(enumClass, constant);
      } catch (IllegalArgumentException e) {
        return;
      }

      out.append(indentation);

      out.append(instance.toString());

      out.append('\n');
    }
  }

}