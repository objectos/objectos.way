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
package objectos.y;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Consumer;
import objectos.internal.Util;

public final class OutputStreamY extends OutputStream {

  public static final class Options {

    public IOException throwOnClose;

    public IOException throwOnWrite;

    private OutputStream build() {
      return new OutputStreamY(throwOnClose, throwOnWrite);
    }

  }

  private byte[] bytes = new byte[32];

  private int bytesIndex;

  private final IOException onClose;

  private final IOException onWrite;

  OutputStreamY(IOException onClose, IOException onWrite) {
    this.onClose = onClose;

    this.onWrite = onWrite;
  }

  public static OutputStream create() {
    return create(_ -> {});
  }

  public static OutputStream create(Consumer<? super Options> opts) {
    var options = new Options();

    opts.accept(options);

    return options.build();
  }

  @Override
  public final void close() throws IOException {
    if (onClose != null) {
      throw onClose;
    }
  }

  public final void reset() {
    bytesIndex = 0;
  }

  @Override
  public String toString() {
    return toString(StandardCharsets.US_ASCII);
  }

  public final String toString(Charset charset) {
    return new String(bytes, 0, bytesIndex, charset);
  }

  @Override
  public final void write(int b) throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void write(byte[] b, int off, int len) throws IOException {
    Objects.checkFromIndexSize(off, len, b.length);

    if (onWrite != null) {
      throw onWrite;
    }

    bytes = Util.growIfNecessary(bytes, bytesIndex + len);

    System.arraycopy(b, off, bytes, bytesIndex, len);

    bytesIndex += len;
  }

}
