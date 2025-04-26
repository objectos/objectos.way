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

import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Consumer;
import objectos.way.Lang.InvalidClassFileException;

final class LangClassReader implements Lang.ClassReader {

  private record Notes(
      Note.Long1 started,
      Note.Long1 constantPoolTraversed,
      Note.Long1 classMembersTraversed,
      Note.Long1 classAnnotationsTraversed,

      Note.Ref2<String, Exception> invalidClass
  ) {

    static Notes get() {
      Class<?> s;
      s = Lang.ClassReader.class;

      return new Notes(
          Note.Long1.create(s, "STA", Note.TRACE),
          Note.Long1.create(s, "CPT", Note.TRACE),
          Note.Long1.create(s, "CMT", Note.TRACE),
          Note.Long1.create(s, "CAT", Note.TRACE),

          Note.Ref2.create(s, "Invalid class file", Note.ERROR)
      );
    }

  }

  private static final byte $START = 1;

  private static final byte $MAGIC = 2;
  private static final byte $MINOR = 3;
  private static final byte $MAJOR = 4;
  private static final byte $CONSTANT_POOL = 5;
  private static final byte $CONSTANT_POOL_NEXT = 6;
  private static final byte $CONSTANT_POOL_TRAVERSED = 7;

  private static final byte $TCM = 8; // traverse class members
  private static final byte $TCM_ACCESS_FLAGS = 9;
  private static final byte $TCM_THIS_CLASS = 10;
  private static final byte $TCM_SUPER_CLASS = 11;
  private static final byte $TCM_INTERFACES = 12;
  private static final byte $TCM_FIELDS = 13;
  private static final byte $TCM_METHODS = 14;
  private static final byte $TCM_STORE = 15;
  private static final byte $TCM_TRAVERSED = 16;

  private static final byte $TCA = 17; // traverse class annotations
  private static final byte $TCA_ATTRIBUTES = 18;
  private static final byte $TCA_TRAVERSED = 19;

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

  private int annotationsInvisible;

  private int annotationsVisible;

  private byte[] bytes;

  private int bytesIndex;

  private int[] constantPool;

  private int constantPoolCount;

  private int int0;

  private int int1;

  private long startTime;

  private byte state;

  LangClassReader(Note.Sink noteSink) {
    this.noteSink = noteSink;
  }

  @Override
  public final void init(byte[] value) {
    bytes = Objects.requireNonNull(value, "value == null");

    state = $START;
  }

  @Override
  public final boolean annotatedWith(Class<? extends Annotation> annotationType) throws InvalidClassFileException {
    final String annotationName;
    annotationName = name(annotationType);

    return switch (state) {
      case $START -> {
        traverseConstantPool();

        state = $TCM;

        traverseClassMembers();

        state = $TCA;

        traverseClassAnnotations();

        yield annotatedWith0(annotationName);
      }

      case $TCA_TRAVERSED -> annotatedWith0(annotationName);

      default -> throw new IllegalStateException("state=" + state);
    };
  }

  @Override
  public final void visitStrings(Consumer<? super String> visitor) throws InvalidClassFileException {
    Objects.requireNonNull(visitor, "visitor == null");

    switch (state) {
      case $START -> {
        traverseConstantPool();

        visitStrings0(visitor);
      }

      case $TCA_TRAVERSED -> visitStrings0(visitor);

      default -> throw new IllegalStateException("state=" + state);
    }
  }

  private String name(Class<? extends Annotation> annotationType) {
    final String annotationName;
    annotationName = annotationType.getName();

    return "L" + annotationName.replace('.', '/') + ";";
  }

  private void traverseConstantPool() throws Lang.InvalidClassFileException {
    while (state < $CONSTANT_POOL_TRAVERSED) {
      state = execute(state);
    }
  }

  private void traverseClassMembers() throws Lang.InvalidClassFileException {
    while (state < $TCM_TRAVERSED) {
      state = execute(state);
    }
  }

  private void traverseClassAnnotations() throws Lang.InvalidClassFileException {
    while (state < $TCA_TRAVERSED) {
      state = execute(state);
    }
  }

  // ##################################################################
  // # BEGIN: State Machine
  // ##################################################################

  private byte execute(byte state) throws InvalidClassFileException {
    return switch (state) {
      case $START -> executeStart();

      case $MAGIC -> executeMagic();
      case $MINOR -> executeMinor();
      case $MAJOR -> executeMajor();

      case $CONSTANT_POOL -> executeConstantPool();
      case $CONSTANT_POOL_NEXT -> executeConstantPoolNext();

      case $TCM -> executeTcm();
      case $TCM_ACCESS_FLAGS -> executeTcmAccessFlags();
      case $TCM_THIS_CLASS -> executeTcmThisClass();
      case $TCM_SUPER_CLASS -> executeTcmSuperClass();
      case $TCM_INTERFACES -> executeTcmInterfaces();
      case $TCM_FIELDS -> executeTcmFields();
      case $TCM_METHODS -> executeTcmMethods();
      case $TCM_STORE -> executeTcmStore();

      case $TCA -> executeTca();
      case $TCA_ATTRIBUTES -> executeTcaAttributes();

      default -> throw new AssertionError("Unexpected state=" + state);
    };
  }

  // ##################################################################
  // # END: State Machine
  // ##################################################################

  // ##################################################################
  // # BEGIN: Start
  // ##################################################################

  private byte executeStart() {
    startTime = System.nanoTime();

    noteSink.send(notes.started, bytes.length);

    annotationsInvisible = 0;

    annotationsVisible = 0;

    bytesIndex = 0;

    constantPoolCount = 0;

    int0 = 0;

    int1 = 0;

    return $MAGIC;
  }

  // ##################################################################
  // # END: Start
  // ##################################################################

  // ##################################################################
  // # BEGIN: Magic, Minor, Major
  // ##################################################################

  private byte executeMagic() throws Lang.InvalidClassFileException {
    try {
      final int magic;
      magic = readU4();

      if (magic != 0xCAFEBABE) {
        throw invalid("Invalid magic byte sequence");
      }

      return $MINOR;
    } catch (ArrayIndexOutOfBoundsException e) {
      throw invalid("EOF: no magic byte sequence found", e);
    }
  }

  private byte executeMinor() {
    bytesIndex += 2;

    return $MAJOR;
  }

  private byte executeMajor() {
    bytesIndex += 2;

    return $CONSTANT_POOL;
  }

  // ##################################################################
  // # END: Magic, Minor, Major
  // ##################################################################

  // ##################################################################
  // # BEGIN: Constant Pool
  // ##################################################################

  private byte executeConstantPool() throws Lang.InvalidClassFileException {
    try {
      constantPoolCount = readU2();

      if (constantPool == null) {
        constantPool = new int[constantPoolCount];
      }

      else {
        final int requiredIndex;
        requiredIndex = constantPoolCount;

        constantPool = Util.growIfNecessary(constantPool, requiredIndex);
      }

      // constant pool starts at 1
      int0 = 1;

      return $CONSTANT_POOL_NEXT;
    } catch (ArrayIndexOutOfBoundsException e) {
      throw invalid("EOF: no constant_pool_count found", e);
    }
  }

  private byte executeConstantPoolNext() throws Lang.InvalidClassFileException {
    if (int0 >= constantPoolCount) {
      // store first index after CP in the unused slot
      constantPool[0] = bytesIndex;

      noteElapsedTime(notes.constantPoolTraversed);

      return $CONSTANT_POOL_TRAVERSED;
    }

    try {

      constantPool[int0++] = bytesIndex;

      final byte tag;
      tag = readU1();

      switch (tag) {
        case CONSTANT_Utf8 -> { int length = readU2(); bytesIndex += length; }

        // u4 bytes;
        case CONSTANT_Integer -> bytesIndex += 4;

        // u4 bytes;
        case CONSTANT_Float -> bytesIndex += 4;

        // u4 high_bytes; u4 low_bytes; takes 2 entries
        case CONSTANT_Long -> { bytesIndex += 8; int0++; }

        // u4 high_bytes; u4 low_bytes; takes 2 entries
        case CONSTANT_Double -> { bytesIndex += 8; int0++; }

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
          throw invalid("Unknown constant pool tag=" + tag);
        }
      }

      return $CONSTANT_POOL_NEXT;

    } catch (ArrayIndexOutOfBoundsException e) {

      throw invalid("EOF: while traversing constant pool", e);

    }

  }

  // ##################################################################
  // # END: Constant Pool
  // ##################################################################

  // ##################################################################
  // # BEGIN: Visit Strings
  // ##################################################################

  private void visitStrings0(Consumer<? super String> processor) throws InvalidClassFileException {
    for (int index = 1; index < constantPoolCount; index++) {
      bytesIndex = constantPool[index];

      // process if String
      //
      // next read should be safe
      // -> we have already been at this index in the previous step

      final byte maybeString;
      maybeString = readU1();

      if (maybeString != CONSTANT_String) {
        // not String -> continue

        continue;
      }

      // keep the index in the stack
      //
      // next read should be safe
      // -> we have already been at this index in the previous step
      final int stringIndex;
      stringIndex = readU2();

      // try to load utf8

      final String utf8;
      utf8 = readUtf8(stringIndex);

      processor.accept(utf8);
    }
  }

  // ##################################################################
  // # END: Visit Strings
  // ##################################################################

  // ##################################################################
  // # BEGIN: Traverse Class Member
  // ##################################################################

  private byte executeTcm() {
    startTime = System.nanoTime();

    bytesIndex = constantPool[0];

    return $TCM_ACCESS_FLAGS;
  }

  private byte executeTcmStore() {
    noteElapsedTime(notes.classMembersTraversed);

    return $TCM_TRAVERSED;
  }

  // ##################################################################
  // # BEGIN: Traverse Class Member
  // ##################################################################

  // ##################################################################
  // # BEGIN: access_flags, this_class, super_class
  // ##################################################################

  private byte executeTcmAccessFlags() {
    bytesIndex += 2;

    return $TCM_THIS_CLASS;
  }

  private byte executeTcmThisClass() {
    bytesIndex += 2;

    return $TCM_SUPER_CLASS;
  }

  private byte executeTcmSuperClass() {
    bytesIndex += 2;

    return $TCM_INTERFACES;
  }

  // ##################################################################
  // # END: access_flags, this_class, super_class
  // ##################################################################

  // ##################################################################
  // # BEGIN: Interfaces
  // ##################################################################

  private byte executeTcmInterfaces() throws InvalidClassFileException {
    try {
      final int interfacesCount;
      interfacesCount = readU2();

      bytesIndex += interfacesCount * 2;

      return $TCM_FIELDS;
    } catch (ArrayIndexOutOfBoundsException e) {
      throw invalid("EOF: while traversing interfaces", e);
    }
  }

  // ##################################################################
  // # END: Interfaces
  // ##################################################################

  // ##################################################################
  // # BEGIN: Fields
  // ##################################################################

  private byte executeTcmFields() throws InvalidClassFileException {
    return skipFieldsOrMethods($TCM_METHODS, "EOF: while traversing fields");
  }

  // ##################################################################
  // # END: Fields
  // ##################################################################

  // ##################################################################
  // # BEGIN: Methods
  // ##################################################################

  private byte executeTcmMethods() throws InvalidClassFileException {
    return skipFieldsOrMethods($TCM_STORE, "EOF: while traversing methods");
  }

  private byte skipFieldsOrMethods(byte success, String eofMessage) throws Lang.InvalidClassFileException {
    try {

      final int count;
      count = readU2();

      for (int item = 0; item < count; item++) {
        // skip (u2) access_flags
        bytesIndex += 2;

        // skip (u2) name_index
        bytesIndex += 2;

        // skip (u2) descriptor_index
        bytesIndex += 2;

        final int attributeCount;
        attributeCount = readU2();

        for (int attr = 0; attr < attributeCount; attr++) {
          // skip (u2) attribute_name_index
          bytesIndex += 2;

          final int length;
          length = readU4(); // might overflow...

          if (length < 0) {
            throw invalidLength(length);
          }

          bytesIndex += length; // might overflow, next read will throw AIOBE
        }
      }

      return success;

    } catch (ArrayIndexOutOfBoundsException e) {

      throw invalid(eofMessage, e);

    }
  }

  // ##################################################################
  // # END: Methods
  // ##################################################################

  // ##################################################################
  // # BEGIN: Traverse Class Annotations
  // ##################################################################

  private byte executeTca() throws Lang.InvalidClassFileException {
    try {
      startTime = System.nanoTime();

      // attributes_count
      int1 = readU2();

      // attribute index
      int0 = 0;

      return $TCA_ATTRIBUTES;
    } catch (ArrayIndexOutOfBoundsException e) {
      throw invalid("EOF: no attributes_count found", e);
    }
  }

  private byte executeTcaAttributes() throws Lang.InvalidClassFileException {
    if (int0++ >= int1) {
      return toTcaTraversed();
    }

    try {

      final int startIndex;
      startIndex = bytesIndex;

      final int nameIndex;
      nameIndex = readU2();

      final int attrLength;
      attrLength = readU4(); // might overflow...

      if (attrLength < 0) {
        throw invalidLength(attrLength);
      }

      final String attrName;
      attrName = readUtf8(nameIndex);

      bytesIndex += attrLength;

      if (RUNTIME_INVISIBLE_ANNOTATIONS.equals(attrName)) {
        annotationsInvisible = startIndex;

        if (annotationsVisible > 0) {
          return toTcaTraversed();
        }
      }

      else if (RUNTIME_VISIBLE_ANNOTATIONS.equals(attrName)) {
        annotationsVisible = startIndex;

        if (annotationsInvisible > 0) {
          return toTcaTraversed();
        }
      }

      return $TCA_ATTRIBUTES;

    } catch (ArrayIndexOutOfBoundsException e) {

      throw invalid("EOF: while traversing attributes", e);

    }
  }

  private byte toTcaTraversed() {
    noteElapsedTime(notes.classAnnotationsTraversed);

    return $TCA_TRAVERSED;
  }

  // ##################################################################
  // # END: Traverse Class Annotations
  // ##################################################################

  // ##################################################################
  // # BEGIN: Annotated?
  // ##################################################################

  private boolean annotatedWith0(String value) throws Lang.InvalidClassFileException {
    return annotatedWith1(value, annotationsInvisible)
        || annotatedWith1(value, annotationsVisible);
  }

  private boolean annotatedWith1(String annotationName, int attributeIndex) throws Lang.InvalidClassFileException {
    if (attributeIndex == 0) {
      return false;
    }

    bytesIndex = attributeIndex;

    // skip (u2) name_index
    bytesIndex += 2;

    // skip (u4) attr_length
    bytesIndex += 4;

    final int numAnnotations;
    numAnnotations = readU2();

    for (int ann = 0; ann < numAnnotations; ann++) {
      final int typeIndex;
      typeIndex = readU2();

      final String typeName;
      typeName = readUtf8(typeIndex);

      if (typeName.equals(annotationName)) {
        return true;
      }

      skipAnnotationContents();
    }

    return false;
  }

  private void skipAnnotation() throws Lang.InvalidClassFileException {
    // skip typeIndex
    bytesIndex += 2;

    // skip contents
    skipAnnotationContents();
  }

  private void skipAnnotationContents() throws Lang.InvalidClassFileException {
    final int numElementValuePairs;
    numElementValuePairs = readU2();

    for (int pair = 0; pair < numElementValuePairs; pair++) {
      // skip (u2) element_name_index
      bytesIndex += 2;

      skipAnnotationElementValue();
    }
  }

  private void skipAnnotationElementValue() throws Lang.InvalidClassFileException {
    final byte tag;
    tag = readU1();

    switch (tag) {
      case 'B', 'C', 'D', 'F', 'I', 'J', 'S', 'Z', 's' -> {
        // skip const_value_index
        bytesIndex += 2;
      }

      case 'e' -> {
        // skip type_name_index
        bytesIndex += 2;

        // skip const_name_index
        bytesIndex += 2;
      }

      case 'c' -> {
        // skip class_info_index
        bytesIndex += 2;
      }

      case '@' -> {
        skipAnnotation();
      }

      case '[' -> {
        final int numValues;
        numValues = readU2();

        for (int idx = 0; idx < numValues; idx++) {
          skipAnnotationElementValue();
        }
      }

      default -> {
        throw invalid("Unknown annotation element value tag=" + tag);
      }
    }
  }

  // ##################################################################
  // # END: Annotated?
  // ##################################################################

  // ##################################################################
  // # BEGIN: Utf8
  // ##################################################################

  private String readUtf8(int contanstPoolIndex) throws Lang.InvalidClassFileException {
    final int returnTo;
    returnTo = bytesIndex;

    bytesIndex = constantPool[contanstPoolIndex];

    final byte tag;
    tag = readU1();

    if (tag != CONSTANT_Utf8) {
      throw new Lang.InvalidClassFileException("Malformed constant pool: expected Constant_Utf8_info but found tag=" + tag);
    }

    final int length;
    length = readU2();

    final String value;
    value = decodeUtf8(length);

    bytesIndex = returnTo;

    return value;
  }

  private String decodeUtf8(int length) throws Lang.InvalidClassFileException {
    // 1: assume ASCII only

    boolean asciiOnly;
    asciiOnly = true;

    for (int offset = 0; offset < length; offset++) {
      final byte b;
      b = bytes[bytesIndex + offset];

      final int i;
      i = Byte.toUnsignedInt(b);

      if (i > 0x7F) {
        asciiOnly = false;

        break;
      }
    }

    if (asciiOnly) {
      return new String(bytes, bytesIndex, length, StandardCharsets.US_ASCII);
    }

    // 2. parse modified UTF-8

    final char[] chars;
    chars = new char[length];

    final int max;
    max = bytesIndex + length;

    int charsIndex;
    charsIndex = 0;

    while (bytesIndex < max) {
      final byte byte0;
      byte0 = readU1();

      final int highBits;
      highBits = Byte.toUnsignedInt(byte0) >> 4;

      char c;
      c = switch (highBits) {
        // 0yyyzzzz
        case 0b0000, 0b0001,
             0b0010, 0b0011,
             0b0100, 0b0101, 0b0110, 0b0111 -> (char) byte0;

        // 110xxxyy 10yyzzzz
        case 0b1100, 0b1101 -> decode(byte0, readU1());

        // 1110wwww 10xxxxyy 10yyzzzz
        case 0b1110 -> decode(byte0, readU1(), readU1());

        default -> throw new Lang.InvalidClassFileException("Malformed UTF-8 value: not a valid prefix");
      };

      chars[charsIndex++] = c;
    }

    return new String(chars, 0, charsIndex);
  }

  private char decode(byte byte0, byte byte1) throws Lang.InvalidClassFileException {
    checkUtf8(byte1);

    int bits0 = (byte0 & 0b0001_1111) << 6;

    int bits1 = (byte1 & 0b0011_1111) << 0;

    return (char) (bits0 | bits1);
  }

  private char decode(byte byte0, byte byte1, byte byte2) throws Lang.InvalidClassFileException {
    checkUtf8(byte1);
    checkUtf8(byte2);

    int bits0 = (byte0 & 0b0000_1111) << 12;

    int bits1 = (byte1 & 0b0011_1111) << 6;

    int bits2 = (byte2 & 0b0011_1111) << 0;

    return (char) (bits0 | bits1 | bits2);
  }

  private void checkUtf8(byte b) throws Lang.InvalidClassFileException {
    final int topTwoBits;
    topTwoBits = b & 0b1100_0000;

    if (topTwoBits != 0b1000_0000) {
      throw new Lang.InvalidClassFileException("Malformed UTF-8 value: not a continuation byte");
    }
  }

  // ##################################################################
  // # END: Utf8
  // ##################################################################

  // ##################################################################
  // # BEGIN: Utils
  // ##################################################################

  private Lang.InvalidClassFileException invalid(String message) {
    return new Lang.InvalidClassFileException(message);
  }

  private Lang.InvalidClassFileException invalid(String message, Throwable cause) {
    return new Lang.InvalidClassFileException(message, cause);
  }

  private Lang.InvalidClassFileException invalidLength(int length) {
    return new Lang.InvalidClassFileException("Maximum supported length exceeded: length=" + Integer.toUnsignedLong(length));
  }

  private void noteElapsedTime(Note.Long1 note) {
    final long elapsedNanos;
    elapsedNanos = System.nanoTime() - startTime;

    noteSink.send(note, elapsedNanos);
  }

  private byte readU1() {
    return bytes[bytesIndex++];
  }

  private int readU2() {
    final byte b0 = bytes[bytesIndex++];
    final byte b1 = bytes[bytesIndex++];

    final int v0 = toInt(b0, 8);
    final int v1 = toInt(b1, 0);

    return v0 | v1;
  }

  private int readU4() {
    final byte b0 = bytes[bytesIndex++];
    final byte b1 = bytes[bytesIndex++];
    final byte b2 = bytes[bytesIndex++];
    final byte b3 = bytes[bytesIndex++];

    final int v0 = toInt(b0, 24);
    final int v1 = toInt(b1, 16);
    final int v2 = toInt(b2, 8);
    final int v3 = toInt(b3, 0);

    return v0 | v1 | v2 | v3;
  }

  private int toInt(byte b, int shift) {
    return Byte.toUnsignedInt(b) << shift;
  }

  // ##################################################################
  // # END: Utils
  // ##################################################################

}