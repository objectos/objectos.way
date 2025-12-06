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
package objectos.way;

import java.util.Objects;
import java.util.function.Consumer;

final class ScriptPojo implements Script {

  private final ScriptWriter writer = new ScriptWriter();

  public static ScriptPojo create(Consumer<? super Script> script) {
    final ScriptPojo pojo;
    pojo = new ScriptPojo();

    script.accept(pojo);

    return pojo;
  }

  @Override
  public final Script.Element element() {
    return new ScriptElement(writer);
  }

  @Override
  public final Element elementById(Html.Id id) {
    final String _id;
    _id = id.attrValue();

    return new ScriptElement(writer, _id);
  }

  @Override
  public final Element elementById(StringQuery id) {
    Objects.requireNonNull(id, "id == null");

    return new ScriptElement(writer, id);
  }

  // actions

  @Override
  public final void delay(int ms, Callback callback) {
    Objects.requireNonNull(callback, "callback == null");

    writer.delay(ms, callback);
  }

  @Override
  public final void html(Html.Template template) {
    writer.html(template);
  }

  @Override
  public final void navigate() {
    writer.navigate();
  }

  @Override
  public final void pushState(String url) {
    Objects.requireNonNull(url, "url == null");

    writer.pushState(url);
  }

  @Override
  public final void replaceState(String url) {
    Objects.requireNonNull(url, "url == null");

    writer.replaceState(url);
  }

  @Override
  public final void request(Consumer<? super Script.RequestOptions> options) {
    final ScriptRequestOptions pojo;
    pojo = new ScriptRequestOptions();

    options.accept(pojo);

    if (pojo.url == null) {
      throw new IllegalArgumentException("URL was not set");
    }

    writer.request(pojo);
  }

  @Override
  public final void stopPropagation() {
    writer.stopPropagation();
  }

  final ScriptWriter unwrap() {
    return writer;
  }

}