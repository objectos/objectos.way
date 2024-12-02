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

  private final String action;

  private final List<WebFormField> fields;

  WebForm(WebFormConfig config) {
    action = config.action;

    fields = config.fields();
  }

  private WebForm(String action, List<WebFormField> fields) {
    this.action = action;

    this.fields = fields;
  }

  @Override
  public final boolean isValid() {
    return true;
  }

  @Override
  public final String action() {
    return action;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final List<Web.Form.Field> fields() {
    List<?> list;
    list = fields;

    return (List<Web.Form.Field>) list;
  }

  @Override
  public final Web.Form parse(Http.Exchange http) {
    Web.FormData data;
    data = Web.FormData.parse(http);

    int size;
    size = fields.size();

    WebFormField[] parsedFields;
    parsedFields = new WebFormField[size];

    for (int idx = 0; idx < size; idx++) {
      WebFormField field;
      field = fields.get(idx);

      parsedFields[idx] = field.parse(data);
    }

    return new WebForm(
        action,

        new UtilUnmodifiableList<>(parsedFields)
    );
  }

}