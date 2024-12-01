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

final class WebRelation implements Web.Relation {

  private final String name;

  private final List<? extends WebRelationAttribute> attributes;

  WebRelation(WebRelationConfig config) {
    name = config.name;

    attributes = config.attributes();
  }

  @Override
  public final String name() {
    return name;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final List<Web.Relation.Attribute> attributes() {
    List<?> list;
    list = attributes;

    return (List<Web.Relation.Attribute>) list;
  }

  final List<? extends WebFormField> toFields() {
    UtilList<WebFormField> fields;
    fields = new UtilList<>();

    for (WebRelationAttribute attr : attributes) {
      fields.add(
          switch (attr) {
            case WebRelationStringAttribute str -> new WebFormTextInput(str);
          }
      );
    }

    return fields.toUnmodifiableList();
  }

}