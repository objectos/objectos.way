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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import objectos.notes.NoOpNoteSink;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.NoteSink;

final class CssGeneratorScanner {

  private static final Note1<String> CLASS_NOT_FOUND;

  private static final Note2<String, IOException> CLASS_IO_ERROR;

  private static final Note1<String> CLASS_LOADED;

  private static final Note2<String, String> INVALID_CLASS;

  private static final Note1<String> UTF8;

  static {
    Class<?> s;
    s = Css.Generator.class;

    CLASS_NOT_FOUND = Note1.error(s, "Class file not found");

    CLASS_IO_ERROR = Note2.error(s, "Class file I/O error");

    CLASS_LOADED = Note1.debug(s, "Class file loaded");

    INVALID_CLASS = Note2.error(s, "Invalid class file");

    UTF8 = Note1.trace(s, "Found");
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

  NoteSink noteSink = NoOpNoteSink.of();

  int intValue;

  public CssGeneratorScanner() {}

  public CssGeneratorScanner(NoteSink noteSink) {
    this.noteSink = noteSink;
  }

  public final void scan(Class<?> clazz, Consumer<String> stringProcessor) {
    String binaryName;
    binaryName = clazz.getName();

    // 0. load class file

    String resourceName;
    resourceName = binaryName.replace('.', '/');

    resourceName += ".class";

    ClassLoader loader;
    loader = ClassLoader.getSystemClassLoader();

    InputStream in;
    in = loader.getResourceAsStream(resourceName);

    if (in == null) {
      noteSink.send(CLASS_NOT_FOUND, binaryName);

      return;
    }

    byte[] bytes;

    try (in; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      in.transferTo(out);

      bytes = out.toByteArray();

      noteSink.send(CLASS_LOADED, binaryName);
    } catch (IOException e) {
      noteSink.send(CLASS_IO_ERROR, binaryName, e);

      return;
    }

    // 1. verify magic

    if (bytes.length < 4) {
      // the class file has less bytes than the magic length
      // -> invalid class

      noteSink.send(INVALID_CLASS, binaryName, "Magic not found");
    }

    int bytesIndex;
    bytesIndex = 0;

    bytesIndex = readU4(bytes, bytesIndex);

    if (intValue != 0xCAFEBABE) {
      // magic does not match expected value
      // -> invalid class

      noteSink.send(INVALID_CLASS, binaryName, "Magic not found");

      return;
    }

    // 2. skip minor

    if (!canReadU2(bytes, bytesIndex)) {
      noteSink.send(INVALID_CLASS, binaryName, "Version minor not found");

      return;
    }

    bytesIndex = readU2(bytes, bytesIndex);

    // 3. skip major

    if (!canReadU2(bytes, bytesIndex)) {
      noteSink.send(INVALID_CLASS, binaryName, "Version major not found");

      return;
    }

    bytesIndex = readU2(bytes, bytesIndex);

    // 4. load constant pool count

    if (!canReadU2(bytes, bytesIndex)) {
      noteSink.send(INVALID_CLASS, binaryName, "Constant pool count not found");

      return;
    }

    bytesIndex = readU2(bytes, bytesIndex);

    int constantPoolCount;
    constantPoolCount = intValue;

    // 5. load constant pool index

    int[] constantPoolIndex;
    constantPoolIndex = new int[constantPoolCount];

    for (int index = 1; index < constantPoolCount; index++) {
      if (!canRead(bytes, bytesIndex)) {
        noteSink.send(INVALID_CLASS, binaryName, "Unexpected constant pool end");

        return;
      }

      constantPoolIndex[index] = bytesIndex;

      byte tag;
      tag = bytes[bytesIndex++];

      switch (tag) {
        case CONSTANT_Utf8 -> {
          if (!canReadU2(bytes, bytesIndex)) {
            noteSink.send(INVALID_CLASS, binaryName, "Unexpected constant pool end");

            return;
          }

          bytesIndex = readU2(bytes, bytesIndex);

          int length;
          length = intValue;

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

          return;
        }
      }
    }

    // 6. process constant pool entries

    for (int index = 1; index < constantPoolCount; index++) {
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
      bytesIndex = readU2(bytes, bytesIndex);

      int stringIndex;
      stringIndex = intValue;

      // try to load utf8

      bytesIndex = constantPoolIndex[stringIndex];

      byte tag;
      tag = bytes[bytesIndex++];

      if (tag != CONSTANT_Utf8) {
        noteSink.send(INVALID_CLASS, binaryName, "Malformed constant pool");

        return;
      }

      bytesIndex = readU2(bytes, bytesIndex);

      int length;
      length = intValue;

      String utf8;
      utf8 = utf8Value(bytes, bytesIndex, length);

      noteSink.send(UTF8, utf8);

      stringProcessor.accept(utf8);
    }
  }

  private boolean canRead(byte[] bytes, int bytesIndex) {
    return bytesIndex < bytes.length;
  }

  private boolean canReadU2(byte[] bytes, int bytesIndex) {
    return bytesIndex <= bytes.length - 2;
  }

  private int readU2(byte[] bytes, int bytesIndex) {
    byte b0;
    b0 = bytes[bytesIndex++];

    byte b1;
    b1 = bytes[bytesIndex++];

    intValue = Css.toBigEndianInt(b0, b1);

    return bytesIndex;
  }

  private int readU4(byte[] bytes, int bytesIndex) {
    byte b0;
    b0 = bytes[bytesIndex++];

    byte b1;
    b1 = bytes[bytesIndex++];

    byte b2;
    b2 = bytes[bytesIndex++];

    byte b3;
    b3 = bytes[bytesIndex++];

    intValue = Css.toBigEndianInt(b0, b1, b2, b3);

    return bytesIndex;
  }

  private String utf8Value(byte[] bytes, int bytesIndex, int length) {
    // 1: assume ASCII only

    boolean asciiOnly;
    asciiOnly = true;

    for (int offset = 0; offset < length; offset++) {
      byte b;
      b = bytes[bytesIndex + offset];

      int i;
      i = Css.toUnsignedInt(b);

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

}