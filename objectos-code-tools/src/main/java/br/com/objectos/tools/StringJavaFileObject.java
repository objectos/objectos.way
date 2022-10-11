/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.tools;

import java.io.IOException;
import java.net.URI;
import objectos.util.UnmodifiableSet;

class StringJavaFileObject extends AbstractJavaFileObject {

  private static final int[] EMPTY_INT_ARRAY = new int[0];

  private static final UnmodifiableSet<String> TYPES_KEYWORDS = UnmodifiableSet.of(
    "@interface",
    "class",
    "enum",
    "interface",
    "record"
  );

  private CanonicalName canonicalName;

  private final String source;
  private URI uri;

  StringJavaFileObject(String source) {
    this.source = source;
  }

  @Override
  public final CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
    return source;
  }

  @Override
  public final Kind getKind() {
    return Kind.SOURCE;
  }

  @Override
  public final String getName() {
    URI uri;
    uri = getUri();

    return uri.getPath();
  }

  @Override
  public final boolean isNameCompatible(String simpleName, Kind kind) {
    return getKind().equals(kind)
        && getSimpleName().endsWith(simpleName);
  }

  @Override
  public final URI toUri() {
    return getUri();
  }

  private CanonicalName getCanonicalName() {
    if (canonicalName == null) {
      canonicalName = getCanonicalName0();
    }

    return canonicalName;
  }

  private CanonicalName getCanonicalName0() {
    Lexer lexer;
    lexer = new Lexer(source);

    boolean simpleNameFound;
    simpleNameFound = false;

    String packageName = "";
    String simpleName = "";

    while (lexer.hasNext() && !simpleNameFound) {
      String word;
      word = lexer.next();

      if (word.equals("package") && lexer.hasNext()) {
        packageName = lexer.next();
        continue;
      }

      if (word.equals("module")) {
        simpleName = "module-info";
        break;
      }

      if (TYPES_KEYWORDS.contains(word) && lexer.hasNext()) {
        simpleName = lexer.next();
        simpleNameFound = true;
        continue;
      }
    }

    if (simpleName.equals("") && !packageName.isEmpty()) {
      simpleName = "package-info";
    }

    return new CanonicalName(packageName, simpleName);
  }

  private String getPackageName() {
    CanonicalName canonicalName;
    canonicalName = getCanonicalName();

    return canonicalName.packageName;
  }

  private String getSimpleName() {
    CanonicalName canonicalName;
    canonicalName = getCanonicalName();

    return canonicalName.simpleName;
  }

  private URI getUri() {
    if (uri == null) {
      uri = getUri0();
    }

    return uri;
  }

  private URI getUri0() {
    StringBuilder uri;
    uri = new StringBuilder();

    String packageName;
    packageName = getPackageName();

    String packagePath;
    packagePath = packageName.replace('.', '/');

    if (!packagePath.isEmpty()) {
      uri.append(packagePath);

      uri.append('.');
    }

    String simpleName;
    simpleName = getSimpleName();

    uri.append(simpleName);

    uri.append(".java");

    return URI.create(uri.toString());
  }

  private static class CanonicalName {

    final String packageName;
    final String simpleName;

    CanonicalName(String packageName, String simpleName) {
      this.packageName = packageName;
      this.simpleName = simpleName;
    }

  }

  private static class Lexer {

    private final int[] codePoints;
    private int index;

    private String nextWord;
    private boolean nextWordComputed;

    Lexer(String source) {
      codePoints = toCodePoints(source);
    }

    final boolean hasNext() {
      if (!nextWordComputed) {
        hasNextWord0();

        nextWordComputed = true;
      }

      return nextWord != null;
    }

    final String next() {
      String result = nextWord;

      nextWord = null;
      nextWordComputed = false;

      return result;
    }

    private void hasNextWord0() {
      if (index < codePoints.length) {
        hasNextWord1();
      }
    }

    private void hasNextWord1() {
      trim();

      StringBuilder word;
      word = new StringBuilder();

      for (; index < codePoints.length; index++) {
        int codePoint;
        codePoint = codePoints[index];

        if (Character.isWhitespace(codePoint)) {
          break;
        }

        if (codePoint == '<') {
          break;
        }

        if (codePoint != ';') {
          word.appendCodePoint(codePoint);
        }
      }

      if (word.length() > 0) {
        nextWord = word.toString();
      }
    }

    private int[] toCodePoints(String s) {
      int stringLength;
      stringLength = s.length();

      if (stringLength == 0) {
        return EMPTY_INT_ARRAY;
      }

      if (stringLength == 1) {
        char onlyChar = s.charAt(0);

        return new int[] {onlyChar};
      }

      int[] intArray;
      intArray = new int[stringLength];

      int intIndex;
      intIndex = 0;

      for (int charIndex = 0; charIndex < stringLength; charIndex++) {
        char highOrBmp;
        highOrBmp = s.charAt(charIndex);

        intArray[intIndex] = highOrBmp;

        if (!Character.isHighSurrogate(highOrBmp)) {
          intIndex++;

          continue;
        }

        char low;
        low = s.charAt(charIndex + 1);

        if (!Character.isLowSurrogate(low)) {
          intIndex++;

          continue;
        }

        charIndex++;

        int codePoint;
        codePoint = Character.toCodePoint(highOrBmp, low);

        intArray[intIndex] = codePoint;

        intIndex++;
      }

      if (intIndex == intArray.length) {
        return intArray;
      } else {
        int[] result;
        result = new int[intIndex];

        System.arraycopy(intArray, 0, result, 0, intIndex);

        return result;
      }
    }

    private void trim() {
      for (; index < codePoints.length; index++) {
        int codePoint;
        codePoint = codePoints[index];

        if (!Character.isWhitespace(codePoint)) {
          break;
        }
      }
    }

  }

}