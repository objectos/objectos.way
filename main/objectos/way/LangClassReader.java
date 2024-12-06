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

import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;

final class LangClassReader implements Lang.ClassReader {

  private static final class InvalidClassException extends Exception {
    private static final long serialVersionUID = -601141059152548162L;

    InvalidClassException(String message) {
      super(message);
    }
  }

  private record Notes(
      Note.Ref2<String, Exception> invalidClass
  ) {

    static Notes get() {
      Class<?> s;
      s = Lang.ClassReader.class;

      return new Notes(
          Note.Ref2.create(s, "Invalid class file", Note.ERROR)
      );
    }

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

  private static final String RUNTIME_INVISIBLE_ANNOTATIONS = "RuntimeInvisibleAnnotations";
  private static final String RUNTIME_VISIBLE_ANNOTATIONS = "RuntimeVisibleAnnotations";

  private final Notes notes = Notes.get();

  private final Note.Sink noteSink;

  private String binaryName;

  private byte[] bytes;

  private int bytesIndex;

  private int[] constantPoolIndex;

  LangClassReader(Note.Sink noteSink) {
    this.noteSink = noteSink;
  }

  @Override
  public final void init(String binaryName, byte[] contents) {
    this.binaryName = binaryName;

    bytes = contents;
  }

  @Override
  public final boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
    String annotationName;
    annotationName = annotationType.getName();

    String nameToLookFor;
    nameToLookFor = "L" + annotationName.replace('.', '/') + ";";

    boolean result;
    result = false;

    try {
      result = isAnnotated0(nameToLookFor);
    } catch (ArrayIndexOutOfBoundsException e) {
      noteSink.send(notes.invalidClass, binaryName, e);
    } catch (InvalidClassException e) {
      noteSink.send(notes.invalidClass, binaryName, e);
    }

    return result;
  }

  private boolean isAnnotated0(String annotationName) throws InvalidClassException {
    reset();

    readConstantPool();

    // skip access_flags

    skipU2();

    // skip this_class

    skipU2();

    // skip super_class

    skipU2();

    // skip interfaces

    int interfacesCount;
    interfacesCount = readU2();

    bytesIndex += interfacesCount * 2;

    // skip fields

    skipFieldsOrMethods();

    // skip methods

    skipFieldsOrMethods();

    int attributesCount;
    attributesCount = readU2();

    for (int attr = 0; attr < attributesCount; attr++) {
      int nameIndex;
      nameIndex = readU2();

      int attrLength;
      attrLength = readU4();

      if (attrLength < 0) {
        throw new UnsupportedOperationException("Implement me :: u4 as int overflow");
      }

      String attrName;
      attrName = readUtf8(nameIndex);

      if (RUNTIME_INVISIBLE_ANNOTATIONS.equals(attrName) || RUNTIME_VISIBLE_ANNOTATIONS.equals(attrName)) {
        int numAnnotations;
        numAnnotations = readU2();

        for (int ann = 0; ann < numAnnotations; ann++) {
          int typeIndex;
          typeIndex = readU2();

          String typeName;
          typeName = readUtf8(typeIndex);

          if (typeName.equals(annotationName)) {
            return true;
          }

          skipAnnotationContents();
        }
      } else {
        bytesIndex += attrLength;
      }
    }

    return false;
  }

  private void skipAnnotation() throws InvalidClassException {
    // skip typeIndex
    skipU2();

    // skip contents
    skipAnnotationContents();
  }

  private void skipAnnotationContents() throws InvalidClassException {
    int numElementValuePairs;
    numElementValuePairs = readU2();

    for (int pair = 0; pair < numElementValuePairs; pair++) {
      // skip element_name_index
      skipU2();

      skipAnnotationElementValue();
    }
  }

  private void skipAnnotationElementValue() throws InvalidClassException {
    byte tag;
    tag = readU1();

    switch (tag) {
      case 'B', 'C', 'D', 'F', 'I', 'J', 'S', 'Z', 's' -> {
        // skip const_value_index
        skipU2();
      }

      case 'e' -> {
        // skip type_name_index
        skipU2();

        // skip const_name_index
        skipU2();
      }

      case 'c' -> {
        // skip class_info_index
        skipU2();
      }

      case '@' -> {
        skipAnnotation();
      }

      case '[' -> {
        int numValues;
        numValues = readU2();

        for (int idx = 0; idx < numValues; idx++) {
          skipAnnotationElementValue();
        }
      }

      default -> {
        throw new InvalidClassException("Unknown annotation element value tag=" + tag);
      }
    }
  }

  @Override
  public final void processStringConstants(StringConstantProcessor processor) {
    Check.notNull(processor, "processor == null");

    try {
      processStringConstants0(processor);
    } catch (ArrayIndexOutOfBoundsException e) {
      noteSink.send(notes.invalidClass, binaryName, e);
    } catch (InvalidClassException e) {
      noteSink.send(notes.invalidClass, binaryName, e);
    }
  }

  private void processStringConstants0(StringConstantProcessor processor) throws InvalidClassException {
    reset();

    readConstantPool();

    for (int index = 1; index < constantPoolIndex.length; index++) {
      bytesIndex = constantPoolIndex[index];

      // process if String
      //
      // next read should be safe
      // -> we have already been at this index in the previous step

      byte maybeString;
      maybeString = readU1();

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

      String utf8;
      utf8 = readUtf8(stringIndex);

      processor.processStringConstant(utf8);
    }
  }

  private void reset() {
    bytesIndex = 0;

    constantPoolIndex = null;
  }

  private void readConstantPool() throws InvalidClassException {
    // 1. verify magic

    final int magic;
    magic = readU4();

    if (magic != 0xCAFEBABE) {
      // magic does not match expected value
      // -> invalid class

      throw new InvalidClassException("Magic not found");
    }

    // 2. skip minor

    skipU2();

    // 3. skip major

    skipU2();

    // 4. load constant pool count

    int constantPoolCount;
    constantPoolCount = readU2();

    // 5. load constant pool index

    constantPoolIndex = new int[constantPoolCount];

    for (int index = 1; index < constantPoolCount; index++) {
      constantPoolIndex[index] = bytesIndex;

      byte tag;
      tag = readU1();

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
          throw new InvalidClassException("Unknown constant pool tag=" + tag);
        }
      }
    }
  }

  private void skipFieldsOrMethods() {
    int count;
    count = readU2();

    for (int item = 0; item < count; item++) {
      // skip access_flags

      skipU2();

      // skip name_index

      skipU2();

      // skip descriptor_index

      skipU2();

      int attributeCount;
      attributeCount = readU2();

      for (int attr = 0; attr < attributeCount; attr++) {
        // skip attribute_name_index

        skipU2();

        int length;
        length = readU4(); // might overflow...

        if (length < 0) {
          throw new UnsupportedOperationException("Implement me :: u4 as int overflow");
        }

        bytesIndex += length;
      }
    }
  }

  private byte readU1() {
    return bytes[bytesIndex++];
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

  private String readUtf8(int contanstPoolIndex) throws InvalidClassException {
    int returnTo;
    returnTo = bytesIndex;

    bytesIndex = constantPoolIndex[contanstPoolIndex];

    byte tag;
    tag = readU1();

    if (tag != CONSTANT_Utf8) {
      throw new InvalidClassException("Malformed constant pool");
    }

    int length;
    length = readU2();

    String value;
    value = utf8Value(length);

    bytesIndex = returnTo;

    return value;
  }

  private void skipU2() {
    bytesIndex += 2;
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