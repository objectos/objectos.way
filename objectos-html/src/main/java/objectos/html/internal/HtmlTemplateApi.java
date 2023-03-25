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
package objectos.html.internal;

import objectos.html.HtmlTemplate;
import objectos.html.spi.Marker;
import objectos.html.spi.Renderer;
import objectos.html.tmpl.AttributeName;
import objectos.html.tmpl.AttributeOrElement;
import objectos.html.tmpl.ElementName;
import objectos.html.tmpl.Lambda;
import objectos.html.tmpl.Value;

/**
 * TODO
 *
 * @since 0.5.3
 */
public interface HtmlTemplateApi extends Marker, Renderer {

  void addAttribute(AttributeName name);

  void addAttribute(AttributeName name, String value);

  void addAttributeOrElement(AttributeOrElement value, String text);

  void addDoctype();

  void addElement(ElementName name, String text);

  void addElement(ElementName name, Value... values);

  void addLambda(Lambda lambda);

  void addRaw(String text);

  void addTemplate(HtmlTemplate template);

  void addText(String text);

  void pathName(String path);

}