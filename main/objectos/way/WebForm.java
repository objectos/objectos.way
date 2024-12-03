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

import java.util.List;

final class WebForm implements Web.Form, Web.FormSpec {

  private final boolean valid;

  private final String action;

  private final List<WebFormField> fields;

  WebForm(WebFormConfig config) {
    valid = true;

    action = config.action;

    fields = config.fields();
  }

  private WebForm(boolean valid, String action, List<WebFormField> fields) {
    this.valid = valid;

    this.action = action;

    this.fields = fields;
  }

  @Override
  public final boolean isValid() {
    return valid;
  }

  @Override
  public final String action() {
    return action;
  }

  @Override
  public final List<? extends Web.Form.Field> fields() {
    return fields;
  }

  @Override
  public final Web.Form parse(Http.Exchange http) {
    boolean valid;
    valid = true;

    Web.FormData data;
    data = Web.FormData.parse(http);

    int size;
    size = fields.size();

    WebFormField[] parsedFields;
    parsedFields = new WebFormField[size];

    for (int idx = 0; idx < size; idx++) {
      WebFormField field;
      field = fields.get(idx);

      WebFormField parsed;
      parsed = field.parse(data);

      valid = valid && parsed.isValid();

      parsedFields[idx] = parsed;
    }

    return new WebForm(
        valid,

        action,

        new UtilUnmodifiableList<>(parsedFields)
    );
  }

}