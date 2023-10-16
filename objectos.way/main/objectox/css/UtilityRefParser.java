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
package objectox.css;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UtilityRefParser {

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

  byte[] bytes;

  int bytesIndex;

  int constantPoolCount;

  int[] constantPoolIndex;

  Entry[] entries;

  public final List<UtilityRef> parse(String binaryName) throws IOException {
    loadResource(binaryName);

    verifyMagic();

    parseConstantPoolCount();

    parseConstantPoolIndex();

    return findAll();
  }

  final List<UtilityRef> findAll() throws IOException {
    List<UtilityRef> result;
    result = new ArrayList<>();

    for (int i = 1; i < constantPoolCount; i++) {
      bytesIndex = constantPoolIndex[i];

      byte tag;
      tag = nextByte();

      if (tag != CONSTANT_Fieldref) {
        continue;
      }

      int classIndex;
      classIndex = readU2();

      int nameAndTypeIndex;
      nameAndTypeIndex = readU2();

      ClassName className;
      className = className(classIndex);

      if (!className.name.startsWith("objectos/css/util/")) {
        continue;
      }

      NameAndType nameAndType;
      nameAndType = nameAndType(nameAndTypeIndex);

      if (!nameAndType.sameType(className)) {
        continue;
      }

      String binaryName;
      binaryName = className.name.replace('/', '.');

      UtilityRef ref;
      ref = new UtilityRef(binaryName, nameAndType.name);

      result.add(ref);
    }

    return List.copyOf(result);
  }

  private ClassName className(int index) throws InvalidConstantPoolException {
    Entry entry;
    entry = entries[index];

    if (entry == null) {
      bytesIndex = constantPoolIndex[index];

      byte tag;
      tag = nextByte();

      if (tag != CONSTANT_Class) {
        throw new InvalidConstantPoolException("Expected CONSTANT_Class but found tag=" + tag);
      }

      int nameIndex;
      nameIndex = readU2();

      Utf8 name;
      name = utf8(nameIndex);

      entry = new ClassName(name.value);

      entries[index] = entry;
    } else if (!(entry instanceof ClassName)) {
      throw new InvalidConstantPoolException(
        "Expected CONSTANT_Class but found type=" + entry.getClass().getSimpleName());
    }

    return (ClassName) entry;
  }

  private NameAndType nameAndType(int index) throws InvalidConstantPoolException {
    Entry entry;
    entry = entries[index];

    if (entry == null) {
      bytesIndex = constantPoolIndex[index];

      byte tag;
      tag = nextByte();

      if (tag != CONSTANT_NameAndType) {
        throw new InvalidConstantPoolException(
          "Expected CONSTANT_NameAndType but found tag=" + tag);
      }

      int nameIndex;
      nameIndex = readU2();

      int descriptorIndex;
      descriptorIndex = readU2();

      Utf8 name;
      name = utf8(nameIndex);

      Utf8 descriptor;
      descriptor = utf8(descriptorIndex);

      entry = new NameAndType(name.value, descriptor.value);

      entries[index] = entry;
    } else if (!(entry instanceof NameAndType)) {
      throw new InvalidConstantPoolException(
        "Expected CONSTANT_NameAndType but found type=" + entry.getClass().getSimpleName());
    }

    return (NameAndType) entry;
  }

  private Utf8 utf8(int index) throws InvalidConstantPoolException {
    Entry entry;
    entry = entries[index];

    if (entry == null) {
      bytesIndex = constantPoolIndex[index];

      byte tag;
      tag = nextByte();

      if (tag != CONSTANT_Utf8) {
        throw new InvalidConstantPoolException("Expected CONSTANT_Utf8 but found tag=" + tag);
      }

      int length;
      length = readU2();

      String value;
      value = utf8Value(length);

      entry = new Utf8(value);

      entries[index] = entry;
    } else if (!(entry instanceof Utf8)) {
      throw new InvalidConstantPoolException(
        "Expected CONSTANT_Utf8 but found type=" + entry.getClass().getSimpleName());
    }

    return (Utf8) entry;
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

  final void loadResource(String binaryName) throws IOException {
    String resourceName;
    resourceName = binaryName.replace('.', '/');

    resourceName += ".class";

    ClassLoader loader;
    loader = ClassLoader.getSystemClassLoader();

    InputStream in;
    in = loader.getResourceAsStream(resourceName);

    if (in == null) {
      throw new FileNotFoundException("""
        Could not find .class resource for %s
        """.formatted(binaryName));
    }

    try (in; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      in.transferTo(out);

      bytes = out.toByteArray();
    }

    bytesIndex = 0;

    constantPoolCount = 0;

    constantPoolIndex = null;

    entries = null;
  }

  final void verifyMagic() throws IOException {
    if (bytes.length < 4) {
      throw invalidMagic();
    }

    int magic;
    magic = readU4();

    if (magic != 0xCAFEBABE) {
      throw invalidMagic();
    }
  }

  final void parseConstantPoolCount() {
    // skip minor/major
    bytesIndex += 4;

    constantPoolCount = readU2();
  }

  final void parseConstantPoolIndex() throws IOException {
    constantPoolIndex = new int[constantPoolCount];

    for (int index = 1; index < constantPoolCount; index++) {
      constantPoolIndex[index] = bytesIndex;

      byte tag;
      tag = nextByte();

      switch (tag) {
        case CONSTANT_Utf8 -> {
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

        default -> throw new InvalidConstantPoolException(
          "Unknown Tag=" + tag
        );
      }
    }

    entries = new Entry[constantPoolCount];
  }

  private int readU2() {
    byte b1;
    b1 = nextByte();

    byte b0;
    b0 = nextByte();

    return Bytes.intValue(b0, b1);
  }

  private int readU4() {
    byte b3;
    b3 = nextByte();

    byte b2;
    b2 = nextByte();

    byte b1;
    b1 = nextByte();

    byte b0;
    b0 = nextByte();

    return Bytes.intValue(b0, b1, b2, b3);
  }

  private byte nextByte() {
    return bytes[bytesIndex++];
  }

  private IOException invalidMagic() {
    return new InvalidConstantPoolException("Magic number not found");
  }

}