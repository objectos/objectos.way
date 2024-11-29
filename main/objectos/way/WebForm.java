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

final class WebForm implements Web.Form {

  private final String action;

  private final List<? extends WebFormField> fields;

  WebForm(WebFormConfig config) {
    action = config.action;

    fields = config.fields();
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
    return (List<Web.Form.Field>) fields;
  }

}