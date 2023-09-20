/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.html;

class Iter06Spec extends HtmlSelfGen {

  static final Iter06Spec INSTANCE = new Iter06Spec();

  @Override
  protected final void definition() {
    rootElement()
        .attribute("accesskey")
        .attribute("class").as("cn", "className").classNameType()
        .attribute("contenteditable")
        .attribute("dir")
        .attribute("draggable")
        .attribute("hidden").booleanType()
        .attribute("id").idType()
        .attribute("lang")
        .attribute("spellcheck")
        .attribute("style")
        .attribute("tabindex")
        .attribute("title")
        .attribute("translate")
        .attributeEnd();

    CategorySpec metadata = category("metadata");
    CategorySpec flow = category("flow");

    element("html")
        .one(el("head"))
        .one(el("body"));

    element("head")
        .contentModel(metadata);

    element("meta")
        .attribute("name")
        .attribute("http-equiv")
        .attribute("content")
        .attribute("charset")
        .category(metadata)
        .noEndTag();

    element("title")
        .attribute("name")
        .category(metadata)
        .one(text());

    element("body")
        .contentModel(flow);

    element("div")
        .category(flow);
  }

}