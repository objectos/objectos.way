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
import br.com.objectos.http.media.TextType;
import java.nio.charset.Charset;

/**
 * A header field.
 */
public interface Header {

  String getHeaderName();

  String getHeaderValue();

  interface Accept extends RequestHeader {}

  interface ContentLength extends RequestHeader, ResponseHeader {
    int getLength();
  }

  interface ContentType extends RequestHeader, ResponseHeader {
    void acceptContentTypeVisitor(ContentTypeVisitor visitor);
  }

  interface ContentTypeVisitor {
    void visitApplicationType(ApplicationType type);

    void visitApplicationType(ApplicationType type, Charset charset);

    void visitImageType(ImageType type);

    void visitTextType(TextType type);

    void visitTextType(TextType type, Charset charset);
  }

  interface Cookie extends RequestHeader {

    String getCookieName();

    String getCookieValue();

    Cookie withCookieValue(String newValue);

  }

  interface Host extends RequestHeader {}

  interface Location extends ResponseHeader {}

  interface Server extends ResponseHeader {}

  interface SetCookie extends ResponseHeader {

    String getCookieName();

    String getCookieValue();

  }

  interface Unknown extends RequestHeader, ResponseHeader {}

}
