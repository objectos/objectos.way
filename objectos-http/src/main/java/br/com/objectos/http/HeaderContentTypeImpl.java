/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package br.com.objectos.http;

import br.com.objectos.http.media.ApplicationType;
import br.com.objectos.http.media.ImageType;
import br.com.objectos.http.media.MediaType;
import br.com.objectos.http.media.MediaTypeVisitor;
import br.com.objectos.http.media.TextType;
import br.com.objectos.http.media.TopLevel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import objectos.lang.Check;
import objectos.lang.Equals;
import objectos.lang.HashCode;

final class HeaderContentTypeImpl
    extends AbstractHeader<Header.ContentType> implements Header.ContentType {

  private static final Map<String, Charset> CHARSET_MAP = createCharsetMap();

  private static final Map<String, Map<String, MediaType>> TOP_LEVEL_MAP = createTopLevelMap();

  private StringBuilder builder = new StringBuilder();

  private boolean parseError;

  private char quoteChar;

  private Charset resultCharset;

  private MediaType resultMediaType;

  private Map<String, MediaType> resultTopLevel;

  private State state = State.TOP_LEVEL;

  private static Map<String, Charset> createCharsetMap() {
    Map<String, Charset> map;
    map = new HashMap<>();

    map.put("utf8", StandardCharsets.UTF_8);
    map.put("utf-8", StandardCharsets.UTF_8);

    return map;
  }

  private static Map<String, MediaType>
      createSubtypeMap(Class<? extends Enum<? extends MediaType>> subtype) {
    Map<String, MediaType> map;
    map = new HashMap<>();

    Enum<? extends MediaType>[] enumConstants;
    enumConstants = subtype.getEnumConstants();

    for (Enum<? extends MediaType> constant : enumConstants) {
      MediaType mediaType;
      mediaType = (MediaType) constant;

      String simpleName;
      simpleName = mediaType.getSimpleName();

      map.put(simpleName, mediaType);
    }

    return map;
  }

  private static Map<String, Map<String, MediaType>> createTopLevelMap() {
    Map<String, Map<String, MediaType>> map;
    map = new HashMap<>();

    // standard
    for (TopLevel topLevel : TopLevel.values()) {
      String simpleName;
      simpleName = topLevel.getSimpleName();

      Class<? extends Enum<? extends MediaType>> subtype;
      subtype = topLevel.getSubtypeEnumClass();

      Map<String, MediaType> subtypeMap;
      subtypeMap = createSubtypeMap(subtype);

      map.put(simpleName, subtypeMap);
    }

    // non-standard
    Map<String, MediaType> textMap;
    textMap = map.get("text");

    textMap.put("json", ApplicationType.JSON);

    return map;
  }

  @Override
  public final void acceptContentTypeVisitor(ContentTypeVisitor visitor) {
    Check.state(!isMalformed(), "this contentType is malformed");

    ThisMediaTypeVisitor mediaTypeVisitor;
    mediaTypeVisitor = new ThisMediaTypeVisitor(visitor);

    resultMediaType.acceptMediaTypeVisitor(mediaTypeVisitor);
  }

  @Override
  public final void acceptRequestVisitor(RequestVisitor visitor) {
    visitor.visitRequestHeader(this);
  }

  @Override
  public final void acceptResponseVisitor(ResponseVisitor visitor) {
    visitor.visitResponseHeader(this);
  }

  @Override
  public final void consume(char c) {
    state = execute(c);
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof HeaderContentTypeImpl)) {
      return false;
    }

    HeaderContentTypeImpl that;
    that = (HeaderContentTypeImpl) obj;

    return resultMediaType.equals(that.resultMediaType)
        && Equals.of(resultCharset, that.resultCharset);
  }

  public final Charset getCharset() {
    return resultCharset;
  }

  @Override
  public final String getHeaderName() {
    return "Content-Type";
  }

  public final MediaType getMediaType() {
    return resultMediaType;
  }

  @Override
  public final int hashCode() {
    return HashCode.of(resultMediaType, resultCharset);
  }

  @Override
  public final boolean isMalformed() {
    return parseError;
  }

  @Override
  public final boolean shouldConsume() {
    return state != null;
  }

  @Override
  final void clear() {
    builder = null;

    resultTopLevel = null;

    state = null;
  }

  @Override
  final void toStringValue(StringBuilder result) {
    result.append(resultMediaType);

    if (resultCharset != null) {
      result.append("; charset=");

      String charsetName;
      charsetName = resultCharset.name();

      result.append(charsetName.toLowerCase());
    }
  }

  private void appendLower(char c) {
    char lower;
    lower = Character.toLowerCase(c);

    builder.append(lower);
  }

  private String buildString() {
    String result;
    result = builder.toString();

    builder.setLength(0);

    return result;
  }

  private State execute(char c) {
    switch (state) {
      case CHARSET:
        return executeCharset(c);
      case CHARSET_NO_QUOTES:
        return executeCharsetNoQuotes(c);
      case CHARSET_WITH_QUOTES:
        return executeCharsetWithQuotes(c);
      case CR:
        return executeCr(c);
      case EOL:
        return executeEol(c);
      case PARAMETER:
        return executeParameter(c);
      case SUB_TYPE:
        return executeSubType(c);
      case TOP_LEVEL:
        return executeTopLevel(c);
      default:
        throw new UnsupportedOperationException("Implement me @ " + state.name());
    }
  }

  private State executeCharset(char c) {
    if (c == '\"') {
      quoteChar = c;

      return State.CHARSET_WITH_QUOTES;
    }

    if (Character.isLetter(c)) {
      appendLower(c);

      return State.CHARSET_NO_QUOTES;
    }

    return toParseError(c);
  }

  private State executeCharsetDone(char c) {
    String charsetName;
    charsetName = buildString();

    resultCharset = CHARSET_MAP.get(charsetName);

    if (resultCharset != null) {
      return toResult(c);
    }

    return toParseError(c);
  }

  private State executeCharsetNoQuotes(char c) {
    if (Http.isTokenChar(c)) {
      appendLower(c);

      return state;
    }

    return executeCharsetDone(c);
  }

  private State executeCharsetWithQuotes(char c) {
    if (c == quoteChar) {
      return executeCharsetDone(c);
    }

    if (Http.isTokenChar(c)) {
      appendLower(c);

      return state;
    }

    return toParseError(c);
  }

  private State executeCr(char c) {
    if (c == Http.LF) {
      return null;
    }

    return toParseError(c);
  }

  private State executeEol(char c) {
    if (c == Http.CR) {
      return State.CR;
    }

    return state;
  }

  private State executeParameter(char c) {
    if (Character.isLetter(c)) {
      appendLower(c);

      return State.PARAMETER;
    }

    if (c == ' ') {
      return State.PARAMETER;
    }

    String parameterName;
    parameterName = null;

    if (c == '=') {
      parameterName = buildString();
    }

    if ("charset".equals(parameterName)) {
      return State.CHARSET;
    }

    return toParseError(c);
  }

  private State executeSubType(char c) {
    if (Character.isLetter(c)) {
      appendLower(c);

      return State.SUB_TYPE;
    }

    if (c == ';') {
      return executeSubType0(State.PARAMETER);
    }

    if (c == Http.CR) {
      return executeSubType0(State.CR);
    }

    return toParseError(c);
  }

  private State executeSubType0(State onSuccess) {
    String subTypeName;
    subTypeName = buildString();

    MediaType mediaType;
    mediaType = resultTopLevel.get(subTypeName);

    if (mediaType == null) {
      return State.PARSE_ERROR;
    }

    resultMediaType = mediaType;

    return onSuccess;
  }

  private State executeTopLevel(char c) {
    if (Character.isLetter(c)) {
      appendLower(c);

      return State.TOP_LEVEL;
    }

    if (c == '/') {
      String topLevelName;
      topLevelName = buildString();

      Map<String, MediaType> topLevel;
      topLevel = TOP_LEVEL_MAP.get(topLevelName);

      if (topLevel == null) {
        return State.PARSE_ERROR;
      }

      resultTopLevel = topLevel;

      return State.SUB_TYPE;
    }

    return toParseError(c);
  }

  private State toParseError(char c) {
    parseError = true;

    return toResult(c);
  }

  private State toResult(char c) {
    if (c == Http.CR) {
      return State.CR;
    }

    return State.EOL;
  }

  private enum State {

    CHARSET,

    CHARSET_NO_QUOTES,

    CHARSET_WITH_QUOTES,

    CR,

    EOL,

    PARAMETER,

    PARSE_ERROR,

    SUB_TYPE,

    TOP_LEVEL;

  }

  private class ThisMediaTypeVisitor implements MediaTypeVisitor {

    private final ContentTypeVisitor delegate;

    public ThisMediaTypeVisitor(ContentTypeVisitor delegate) {
      this.delegate = delegate;
    }

    @Override
    public final void visitApplicationType(ApplicationType type) {
      if (resultCharset != null) {
        delegate.visitApplicationType(type, resultCharset);
      } else {
        delegate.visitApplicationType(type);
      }
    }

    @Override
    public final void visitImageType(ImageType type) {
      delegate.visitImageType(type);
    }

    @Override
    public final void visitTextType(TextType type) {
      if (resultCharset != null) {
        delegate.visitTextType(type, resultCharset);
      } else {
        delegate.visitTextType(type);
      }
    }

  };

}