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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import objectos.way.Css.Layer;

record CssConfiguration(

    String base,

    Iterable<? extends Class<?>> classesToScan,

    Iterable<? extends Path> directoriesToScan,

    Iterable<? extends Class<?>> jarFilesToScan,

    Note.Sink noteSink,

    Set<? extends Css.Layer> skipLayer,

    Map<String, String> keywords,

    Iterable<? extends Map<String, Css.ThemeEntry>> themeEntries,

    Iterable<? extends Map.Entry<String, String>> themeQueries,

    Map<String, CssVariant> variants

) implements Css.Configuration {

  @Override
  public final String contentType() {
    return "text/css; charset=utf-8";
  }

  @Override
  public final Charset charset() {
    return StandardCharsets.UTF_8;
  }

  @Override
  public final void writeTo(Appendable out) throws IOException {
    Objects.requireNonNull(out, "out == null");

    final CssEngine engine;
    engine = new CssEngine(this);

    engine.execute();

    engine.generate(out);
  }

  final boolean contains(Layer value) {
    return skipLayer.contains(value);
  }

  final String keyword(String name) {
    return keywords.get(name);
  }

  final CssVariant variant(String name) {
    return variants.get(name);
  }

  final CssVariant variant(String name, CssVariant value) {
    return variants.put(name, value);
  }

}