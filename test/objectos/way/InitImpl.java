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
package objectos.way;

final class InitImpl extends Init {

  private final int index;

  private final String value;

  public InitImpl(int index, String value) {
    this.index = index;
    this.value = value;
  }

  static int size() {
    return Values.size();
  }

  public static InitImpl get(int index) {
    return Values.get(index);
  }

  @Override
  public final int index() { return index; }

  @Override
  public final String value() { return value; }

  private static class Values {

    static InitImpl[] INSTANCE;

    static {
      INSTANCE = InitBuilder.BUILDER.buildValues();

      InitBuilder.BUILDER = null;
    }

    public static int size() {
      return INSTANCE.length;
    }

    public static InitImpl get(int index) {
      return INSTANCE[index];
    }

  }

}