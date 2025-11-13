/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

final class WebResources implements Web.Resources {

  record Notes(
      Note.Ref2<String, String> contentTypeRegistered,
      Note.Ref3<String, String, String> contentTypeIgnored,
      Note.Ref1<String> traversal
  ) {

    static Notes create() {
      final Class<?> s;
      s = Web.Resources.class;

      return new Notes(
          Note.Ref2.create(s, "CTR", Note.INFO),
          Note.Ref3.create(s, "CTI", Note.INFO),
          Note.Ref1.create(s, "Traversal detected", Note.ERROR)
      );
    }

  }

  private volatile WebResourcesKernel kernel;

  WebResources(WebResourcesKernel kernel) {
    this.kernel = kernel;
  }

  @Override
  public final void close() throws IOException {
    kernel.close();
  }

  @Override
  public final boolean deleteIfExists(String path) throws IOException {
    return kernel.deleteIfExists(path);
  }

  @Override
  public final void handle(Http.Exchange http) {
    kernel.handle(http);
  }

  @Override
  public final void reconfigure(Consumer<Options> config) throws IOException {
    final WebResourcesBuilder builder;
    builder = new WebResourcesBuilder();

    config.accept(builder);

    final WebResourcesKernel newKernel;
    newKernel = builder.build();

    final WebResourcesKernel previous;
    previous = kernel;

    try {
      kernel = newKernel;
    } finally {
      previous.close();
    }
  }

  @Override
  public final String toString() {
    return "Web.Resources[" + kernel.rootDirectory() + "]";
  }

  @Override
  public final void writeMedia(String path, Media media) throws IOException {
    kernel.writeMedia(path, media);
  }

  final Path rootDirectory() {
    return kernel.rootDirectory();
  }

}