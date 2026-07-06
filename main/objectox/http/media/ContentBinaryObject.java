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
package objectox.http.media;

import java.io.IOException;
import java.io.OutputStream;
import objectos.dev.Testable;
import objectos.http.Content;
import objectos.http.MediaType;
import objectos.lang.BinaryObject;

public record ContentBinaryObject(MediaType contentType, BinaryObject contents) implements Content {

  @Override
  public final void binaryTo(OutputStream out) throws IOException {
    contents.binaryTo(out);
  }

  public final String toTestableText() {
    final String fullType;
    fullType = contentType.fullType();

    final String v;
    v = contents instanceof Testable t
        ? t.toTestableText()
        : contents.toString();

    return fullType + "\n" + v;
  }

}
