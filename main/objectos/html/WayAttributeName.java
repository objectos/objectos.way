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
package objectos.html;

final class WayAttributeName extends AttributeName {

  private final int index;

  private final String name;

  private final boolean booleanAttribute;

  private final boolean singleQuoted;

  private final Class<?> type;

  public WayAttributeName(int index, String name, boolean booleanAttribute, boolean singleQuoted, Class<?> type) {
    this.index = index;
    this.name = name;
    this.booleanAttribute = booleanAttribute;
    this.singleQuoted = singleQuoted;
    this.type = type;
  }

  static int size() {
    return Values.size();
  }

  public static WayAttributeName get(int index) {
    return Values.get(index);
  }

  @Override
  public final int index() {
    return index;
  }

  @Override
  public final String name() {
    return name;
  }

  @Override
  public final boolean booleanAttribute() {
    return booleanAttribute;
  }

  @Override
  public final boolean singleQuoted() {
    return singleQuoted;
  }

  @Override
  final Class<?> type() {
    return type;
  }

  private static class Values {

    static WayAttributeName[] INSTANCE;

    static {
      INSTANCE = WayAttributeNameBuilder.build();
    }

    public static int size() { return INSTANCE.length; }

    public static WayAttributeName get(int index) { return INSTANCE[index]; }

  }

}