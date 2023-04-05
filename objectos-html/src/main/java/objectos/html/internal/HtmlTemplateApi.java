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

import objectos.html.spi.Marker;
import objectos.html.spi.Renderer;
import objectos.html.tmpl.AttributeName;
import objectos.html.tmpl.ElementName;
import objectos.html.tmpl.FragmentAction;
import objectos.html.tmpl.Instruction;

/**
 * TODO
 *
 * @since 0.5.3
 */
public abstract class HtmlTemplateApi implements Marker, Renderer {

  abstract void addAmbiguous(Ambiguous name, String text);

  abstract void addAttribute(AttributeName name);

  abstract void addAttribute(AttributeName name, String value);

  abstract void addDoctype();

  abstract void addElement(ElementName name, Instruction... contents);

  abstract void addElement(ElementName name, String text);

  abstract void addFragment(FragmentAction action);

  abstract void addRaw(String text);

  abstract void addTemplate(InternalHtmlTemplate template);

  abstract void addText(String text);

  abstract void pathName(String path);

}