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

abstract class AbstractHeader<T extends Header> implements Header, HeaderParser<T> {

  String headerValue;

  AbstractHeader() {}

  @SuppressWarnings("unchecked")
  @Override
  public final T build() {
    clear();

    return (T) this;
  }

  @Override
  public String getHeaderValue() {
    if (headerValue == null) {
      StringBuilder b;
      b = new StringBuilder();

      toStringValue(b);

      headerValue = b.toString();
    }

    return headerValue;
  }

  @Override
  public final String toString() {
    StringBuilder result;
    result = new StringBuilder();

    String headerName;
    headerName = getHeaderName();

    result.append(headerName);

    result.append(':');

    result.append(' ');

    toStringValue(result);

    return result.toString();
  }

  abstract void clear();

  abstract void toStringValue(StringBuilder result);

}
