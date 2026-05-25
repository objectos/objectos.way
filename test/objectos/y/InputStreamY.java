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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;

public final class InputStreamY extends InputStream {

  public static final class Options {

    public List<Object> data = List.of();

    public IOException throwOnClose;

    private Options() {}

    private InputStream build() {
      final List<InputStream> sources;
      sources = data.stream().map(this::convert).toList();

      final Enumeration<InputStream> enumeration;
      enumeration = Collections.enumeration(sources);

      final SequenceInputStream delegate;
      delegate = new SequenceInputStream(enumeration);

      return new InputStreamY(
          delegate,

          throwOnClose
      );
    }

    @SuppressWarnings("resource")
    private InputStream convert(Object o) {
      return switch (o) {
        case byte[] bytes -> new ByteArrayInputStream(bytes);

        case InputStream is -> is;

        case String s -> {
          final byte[] bytes;
          bytes = s.getBytes(StandardCharsets.UTF_8);

          yield new ByteArrayInputStream(bytes);
        }

        case IOException ioe -> new InputStream() {
          @Override
          public final int read() throws IOException {
            throw ioe;
          }
        };

        default -> throw new IllegalArgumentException("Unsupported type: " + o.getClass());
      };
    }

  }

  private final InputStream delegate;

  private final IOException throwOnClose;

  private InputStreamY(InputStream delegate, IOException throwOnClose) {
    this.delegate = delegate;

    this.throwOnClose = throwOnClose;
  }

  public static InputStream create(Consumer<? super Options> opts) {
    final Options options;
    options = new Options();

    opts.accept(options);

    return options.build();
  }

  public static InputStream of(Object... data) {
    return create(is -> {
      is.data = List.of(data);
    });
  }

  @Override
  public final void close() throws IOException {
    if (throwOnClose != null) {
      throw throwOnClose;
    }
  }

  @Override
  public final int read() throws IOException {
    return delegate.read();
  }

  @Override
  public final int read(byte[] b, int off, int len) throws IOException {
    return delegate.read(b, off, len);
  }

}
