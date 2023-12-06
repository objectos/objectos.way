/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

import java.io.IOException;
import java.nio.charset.Charset;
import objectos.core.io.OutputStreamSource;
import objectos.core.io.Write;
import objectos.lang.object.ToString;

/**
 * A Git blob object resulting from a <em>read blob</em> operation.
 *
 * @since 1
 */
public final class Blob extends GitObject {

  private final byte[] contents;

  Blob(byte[] contents, ObjectId objectId) {
    super(objectId);

    this.contents = contents;
  }

  @Override
  public final void formatToString(StringBuilder sb, int depth) {
    ToString.format(
        sb, depth, this,
        "", objectId
    );
  }

  /**
   * Returns the contents of this blob as a string encoded with the specified
   * charset.
   *
   * @param charset
   *        the charset to use for encoding
   *
   * @return a new string with the blob's contents
   */
  public final String toString(Charset charset) {
    return new String(contents, charset);
  }

  final byte[] getBytes() {
    return contents;
  }

  final void writeTo(OutputStreamSource source) throws IOException {
    Write.byteArray(source, contents);
  }

}