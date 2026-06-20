/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import objectos.http.HeaderValueOptions;

public final class HeaderValueBuilder implements HeaderValueOptions {

  private final StringBuilder stringBuilder = new StringBuilder();

  @Override
  public final void value(String value) {
    final HeaderValueValidator validator;
    validator = new HeaderValueValidator(value);

    validator.validate();

    if (!stringBuilder.isEmpty()) {
      stringBuilder.append(", ");
    }

    stringBuilder.append(value);
  }

  @Override
  public final void param(String name, String value) {
    checkParameterName(name);

    if (stringBuilder.isEmpty()) {
      throw new IllegalStateException("Cannot add a parameter: there's no current value");
    }

    stringBuilder.append(';');
    stringBuilder.append(' ');
    stringBuilder.append(name);
    stringBuilder.append('=');

    final HeaderParamValueFormatter formatter;
    formatter = new HeaderParamValueFormatter(value);

    final String formatted;
    formatted = formatter.format();

    stringBuilder.append(formatted);
  }

  @Override
  public final void param(String name, Charset charset, String value) {
    checkParameterName(name);

    if (charset != StandardCharsets.UTF_8) {
      throw new IllegalArgumentException("The UTF-8 charset MUST be used.");
    }

    Objects.requireNonNull(value, "value == null");

    stringBuilder.append(';');
    stringBuilder.append(' ');
    stringBuilder.append(name);
    stringBuilder.append('=');

    final Rfc8187Encoder encoder;
    encoder = new Rfc8187Encoder(value);

    final String encoded;
    encoded = encoder.encode();

    stringBuilder.append(encoded);
  }

  public final String build() {
    return stringBuilder.toString();
  }

  private void checkParameterName(String name) {
    final HeaderParamNameValidator validator;
    validator = new HeaderParamNameValidator(name);

    validator.validate();
  }

}