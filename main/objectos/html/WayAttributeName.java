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

import java.util.ArrayList;
import java.util.List;

final class WayAttributeName extends AttributeName {

  static class Builder {

    private final List<WayAttributeName> standardValues = new ArrayList<>();

    private int index;

    public final WayAttributeName create(String name, boolean booleanAttribute) {
      WayAttributeName result;
      result = new WayAttributeName(index++, name, booleanAttribute);

      standardValues.add(result);

      return result;
    }

    public final WayAttributeName[] buildValues() {
      return standardValues.toArray(WayAttributeName[]::new);
    }

  }

  private final int index;

  private final String name;

  private final boolean booleanAttribute;

  public WayAttributeName(int index, String name, boolean booleanAttribute) {
    this.index = index;
    this.name = name;
    this.booleanAttribute = booleanAttribute;
  }

  private static WayAttributeName[] VALUES;

  static void set(Builder builder) {
    VALUES = builder.buildValues();
  }

  static int size() {
    return VALUES.length;
  }

  public static WayAttributeName get(int index) {
    return VALUES[index];
  }

  @Override
  public final int index() { return index; }

  @Override
  public final String name() { return name; }

  @Override
  public final boolean booleanAttribute() { return booleanAttribute; }

}