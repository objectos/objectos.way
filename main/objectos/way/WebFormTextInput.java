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

import java.util.List;

final class WebFormTextInput extends WebFormField implements Web.Form.TextInput {

  private final int maxLength;

  private final MaxLengthFormatter maxLengthFormatter;

  private final String value;

  WebFormTextInput(WebFormTextInputConfig config) {
    super(config);

    maxLength = config.maxLength;

    maxLengthFormatter = config.maxLengthFormatter;

    value = "";
  }

  private WebFormTextInput(WebFormTextInput source, List<WebFormError> errors, String value) {
    super(source, errors);

    maxLength = source.maxLength;

    maxLengthFormatter = source.maxLengthFormatter;

    this.value = value;
  }

  @Override
  public final String type() {
    return "text";
  }

  @Override
  public final String value() {
    return value;
  }

  @Override
  public final void setValue(Sql.Transaction trx) {
    trx.param(value);
  }

  @Override
  final WebFormField parse(Http.Exchange data) {
    List<WebFormError> errors;
    errors = null;

    String value;
    value = data.formParam(name);

    if (value == null || value.isBlank()) {

      if (requiredMessage != null) {
        errors = WebFormError.addWithMessage(errors, requiredMessage);
      }

      value = "";

    }

    else {

      int actualLength;
      actualLength = value.length();

      if (actualLength > maxLength) {

        String msg;
        msg = maxLengthFormatter.format(maxLength, actualLength);

        errors = WebFormError.addWithMessage(errors, msg);

      }

    }

    return new WebFormTextInput(this, errors, value);
  }

}