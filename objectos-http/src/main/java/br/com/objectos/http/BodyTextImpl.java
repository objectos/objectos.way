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

import java.nio.charset.Charset;

final class BodyTextImpl implements Body.Text {

  private final Charset charset;

  private final String value;

  BodyTextImpl(Charset charset, String value) {
    this.charset = charset;
    this.value = value;
  }

  @Override
  public final void acceptBodyVisitor(BodyVisitor visitor) {
    visitor.visitBody(this);
  }

  @Override
  public final void acceptRequestVisitor(RequestVisitor visitor) {
    visitor.visitRequestBody(this);
  }

  @Override
  public final void acceptResponseVisitor(ResponseVisitor visitor) {
    visitor.visitResponseBody(this);
  }

  @Override
  public final Charset getCharset() {
    return charset;
  }

  @Override
  public final String getContents() {
    return value;
  }

  @Override
  public final String toString() {
    return value;
  }

}