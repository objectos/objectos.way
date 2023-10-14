/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectox.css;

import java.io.IOException;
import objectos.css.StyleSheet;
import objectos.css.StyleSheetWriter;

public final class CompiledStyleSheet implements StyleSheet {

  final byte[] main;

  final Object[] objects;

  public CompiledStyleSheet(byte[] main, Object[] objects) {
    this.main = main;

    this.objects = objects;
  }

  @Override
  public final String toString() {
    try {
      StringBuilder sb;
      sb = new StringBuilder();

      StyleSheetWriter writer;
      writer = StyleSheetWriter.of(sb);

      writer.write(this);

      return sb.toString();
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

}