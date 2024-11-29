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
import java.util.Objects;
import java.util.function.Consumer;

final class WebRelationConfig implements Web.Relation.Config {

  String name = "Unnamed";

  UtilList<WebRelationAttribute> attributes = new UtilList<>();

  @Override
  public final void name(String value) {
    name = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void stringAttribute(Consumer<Web.Relation.StringAttribute.Config> config) {
    WebRelationStringAttributeConfig builder;
    builder = new WebRelationStringAttributeConfig();

    config.accept(builder);

    WebRelationStringAttribute attribute;
    attribute = builder.build();

    attributes.add(attribute);
  }

  final WebRelation build() {
    return new WebRelation(this);
  }

  final List<WebRelationAttribute> attributes() {
    return attributes.toUnmodifiableList();
  }

}