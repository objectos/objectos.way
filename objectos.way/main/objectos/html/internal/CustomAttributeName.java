/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html.internal;

public enum CustomAttributeName implements AttributeName {

  DATA_WAY_CLICK(AttributeKind.STRING, "data-way-click"),

  DATA_WAY_SUBMIT(AttributeKind.STRING, "data-way-submit"),

  PATH_TO(AttributeKind.STRING, "href");

  private static final CustomAttributeName[] ARRAY = values();

  private final AttributeKind kind;

  private final String name;

  private CustomAttributeName(AttributeKind kind, String name) {
    this.kind = kind;
    this.name = name;
  }

  public static CustomAttributeName getByCode(int code) {
    int index = code - StandardAttributeName.size();

    return ARRAY[index];
  }

  @Override
  public final int getCode() {
    return StandardAttributeName.size() + ordinal();
  }

  @Override
  public final AttributeKind getKind() {
    return kind;
  }

  @Override
  public final String getName() {
    return name;
  }

}
