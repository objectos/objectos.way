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
package objectos.way;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import objectos.lang.object.Check;
import objectos.notes.Note2;
import objectos.notes.NoteSink;

final class LangClassReader implements Lang.ClassReader {

  private static final Note2<String, String> INVALID_CLASS;

  static {
    Class<?> s;
    s = Lang.ClassReader.class;

    INVALID_CLASS = Note2.error(s, "Invalid class file");
  }

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

  private final NoteSink noteSink;

  private String binaryName;

  private byte[] bytes;

  private int bytesIndex;

  private int[] constantPoolIndex;

  LangClassReader(NoteSink noteSink) {
    this.noteSink = noteSink;
  }

  @Override
  public final void init(String binaryName, byte[] contents) {
    this.binaryName = binaryName;

    bytes = contents;

    bytesIndex = 0;

    constantPoolIndex = null;
  }

  @Override
  public final void processStringConstants(Consumer<String> processor) {
    Check.notNull(processor, "processor == null");

    if (!readConstantPool()) {
      return;
    }

    for (int index = 1; index < constantPoolIndex.length; index++) {
      bytesIndex = constantPoolIndex[index];

      // process if String
      //
      // next read should be safe
      // -> we have already been at this index in the previous step

      byte maybeString;
      maybeString = bytes[bytesIndex++];

      if (maybeString != CONSTANT_String) {
        // not String -> continue

        continue;
      }

      // keep the index in the stack
      //
      // next read should be safe
      // -> we have already been at this index in the previous step
      int stringIndex;
      stringIndex = readU2();

      // try to load utf8

      bytesIndex = constantPoolIndex[stringIndex];

      byte tag;
      tag = bytes[bytesIndex++];

      if (tag != CONSTANT_Utf8) {
        noteSink.send(INVALID_CLASS, binaryName, "Malformed constant pool");

        return;
      }

      int length;
      length = readU2();

      String utf8;
      utf8 = utf8Value(length);

      processor.accept(utf8);
    }
  }

  private boolean readConstantPool() {
    // 1. verify magic

    if (bytes.length < 4) {
      // the class file has less bytes than the magic length
      // -> invalid class

      noteSink.send(INVALID_CLASS, binaryName, "Magic not found");

      return false;
    }

    final int magic;
    magic = readU4();

    if (magic != 0xCAFEBABE) {
      // magic does not match expected value
      // -> invalid class

      noteSink.send(INVALID_CLASS, binaryName, "Magic not found");

      return false;
    }

    // 2. skip minor

    if (!canReadU2()) {
      noteSink.send(INVALID_CLASS, binaryName, "Version minor not found");

      return false;
    }

    skipU2();

    // 3. skip major

    if (!canReadU2()) {
      noteSink.send(INVALID_CLASS, binaryName, "Version major not found");

      return false;
    }

    skipU2();

    // 4. load constant pool count

    if (!canReadU2()) {
      noteSink.send(INVALID_CLASS, binaryName, "Constant pool count not found");

      return false;
    }

    int constantPoolCount;
    constantPoolCount = readU2();

    // 5. load constant pool index

    constantPoolIndex = new int[constantPoolCount];

    for (int index = 1; index < constantPoolCount; index++) {
      if (!canRead()) {
        noteSink.send(INVALID_CLASS, binaryName, "Unexpected constant pool end");

        return false;
      }

      constantPoolIndex[index] = bytesIndex;

      byte tag;
      tag = bytes[bytesIndex++];

      switch (tag) {
        case CONSTANT_Utf8 -> {
          if (!canReadU2()) {
            noteSink.send(INVALID_CLASS, binaryName, "Unexpected constant pool end");

            return false;
          }

          int length;
          length = readU2();

          bytesIndex += length;
        }

        // u4 bytes;
        case CONSTANT_Integer -> bytesIndex += 4;

        // u4 bytes;
        case CONSTANT_Float -> bytesIndex += 4;

        // u4 high_bytes; u4 low_bytes; takes 2 entries
        case CONSTANT_Long -> { bytesIndex += 8; index++; }

        // u4 high_bytes; u4 low_bytes; takes 2 entries
        case CONSTANT_Double -> { bytesIndex += 8; index++; }

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
          noteSink.send(INVALID_CLASS, binaryName, "Unknown constant pool tag=" + tag);

          return false;
        }
      }
    }

    return true;
  }

  private boolean canRead() {
    return bytesIndex < bytes.length;
  }

  private boolean canReadU2() {
    return bytesIndex <= bytes.length - 2;
  }

  private int readU2() {
    byte b0;
    b0 = bytes[bytesIndex++];

    byte b1;
    b1 = bytes[bytesIndex++];

    return Lang.toBigEndianInt(b0, b1);
  }

  private int readU4() {
    byte b0;
    b0 = bytes[bytesIndex++];

    byte b1;
    b1 = bytes[bytesIndex++];

    byte b2;
    b2 = bytes[bytesIndex++];

    byte b3;
    b3 = bytes[bytesIndex++];

    return Lang.toBigEndianInt(b0, b1, b2, b3);
  }

  private boolean skipU2() {
    bytesIndex += 2;

    return bytesIndex <= bytes.length;
  }

  private String utf8Value(int length) {
    // 1: assume ASCII only

    boolean asciiOnly;
    asciiOnly = true;

    for (int offset = 0; offset < length; offset++) {
      byte b;
      b = bytes[bytesIndex + offset];

      int i;
      i = Lang.toUnsignedInt(b);

      if (i > 0x7F) {
        asciiOnly = false;

        break;
      }
    }

    if (asciiOnly) {
      return new String(bytes, bytesIndex, length, StandardCharsets.UTF_8);
    }

    // 2. parse modified UTF-8

    char[] chars;
    chars = new char[length];

    int index;
    index = bytesIndex;

    int max;
    max = bytesIndex + length;

    int charIndex;
    charIndex = 0;

    while (index < max) {
      int byte0 = Lang.toUnsignedInt(
          bytes[index++]
      );

      int highBytes;
      highBytes = byte0 >> 4;

      switch (highBytes) {
        case 0, 1, 2, 3, 4, 5, 6, 7:
          chars[charIndex++] = (char) byte0;

          break;

        case 12, 13:
          if (index >= max) {
            // invalid
            return "";
          }

          int byte1 = bytes[index++];

          if ((byte1 & 0xC0) != 0x80) {
            // invalid
            return "";
          }

          chars[charIndex++] = (char) (((byte0 & 0x1F) << 6) | (byte1 & 0x3F));

          break;

        case 14:
          if (index >= max) {
            // invalid
            return "";
          }

          byte1 = bytes[index++];

          if (index >= max) {
            // invalid
            return "";
          }

          int byte2 = bytes[index++];

          if (((byte1 & 0xC0) != 0x80) || ((byte2 & 0xC0) != 0x80)) {
            // invalid
            return "";
          }

          chars[charIndex++] = (char) (((byte0 & 0x0F) << 12) | ((byte1 & 0x3F) << 6) | (byte2 & 0x3F));

          break;

        default:
          // invalid
          return "";
      }
    }

    return new String(chars, 0, charIndex);
  }

}