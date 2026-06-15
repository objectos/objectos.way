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
package objectox.http;

public record Version0(int major, int minor, boolean supported) implements Version {

  public static final Version0 HTTP_0_9 = of(0, 9);

  public static final Version0 HTTP_1_1 = new Version0(1, 1, true);

  public static Version0 of(int major, int minor) {
    return new Version0(major, minor, false);
  }

}
