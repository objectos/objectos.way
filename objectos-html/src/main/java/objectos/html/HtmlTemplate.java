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
package objectos.html;

import java.util.Objects;
import objectos.html.internal.HtmlTemplateApi;
import objectos.html.internal.InternalHtmlTemplate;
import objectos.html.internal.NoOp;
import objectos.html.internal.Raw;
import objectos.html.internal.Validate;
import objectos.html.spi.Marker;
import objectos.html.spi.Renderer;
import objectos.html.tmpl.AnyElementValue;
import objectos.html.tmpl.AttributeOrElement;
import objectos.html.tmpl.CustomAttributeName;
import objectos.html.tmpl.ElementName;
import objectos.html.tmpl.Lambda;
import objectos.html.tmpl.NonVoidElementValue;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.html.tmpl.StandardTextElement;
import objectos.html.tmpl.Value;
import objectos.lang.Check;

/**
 * TODO
 *
 * @since 0.5.0
 */
public abstract class HtmlTemplate extends InternalHtmlTemplate implements NonVoidElementValue {

  public final AttributeOrElement clipPath(String text) {
    return addAttributeOrElement(AttributeOrElement.CLIPPATH, text);
  }

  public final void doctype() {
    api().addDoctype();
  }

  public final AttributeOrElement label(String text) {
    return addAttributeOrElement(AttributeOrElement.LABEL, text);
  }

  @Override
  public final void mark(Marker dsl) {}

  public final AnyElementValue noop() {
    return NoOp.INSTANCE;
  }

  @Override
  public final void render(Renderer renderer) {
    if (renderer instanceof HtmlTemplateApi dsl) {
      dsl.addTemplate(this);
    }
  }

  public final StandardTextElement t(String text) {
    api().addText(text);

    return StandardTextElement.INSTANCE;
  }

  public final StandardTextElement t(String... values) {
    Check.notNull(values, "values == null");
    switch (values.length) {
      case 0:
        return t("");
      default:
        StringBuilder sb = new StringBuilder();
        sb.append(Check.notNull(values[0], "values[0] == null"));
        for (int i = 1; i < values.length; i++) {
          sb.append(' ');
          String value = values[i];
          if (value == null) {
            throw new NullPointerException("values[" + i + "] == null");
          }
          sb.append(value);
        }

        return t(sb.toString());
    }
  }

  public final StandardTextElement t(
      String t1,
      String t2) {
    Check.notNull(t1, "t1 == null");
    Check.notNull(t2, "t2 == null");
    return t(
      new StringBuilder(t1)
          .append(' ')
          .append(t2)
          .toString()
    );
  }

  public final StandardTextElement t(
      String t1,
      String t2,
      String t3) {
    Check.notNull(t1, "t1 == null");
    Check.notNull(t2, "t2 == null");
    Check.notNull(t3, "t3 == null");
    return t(
      new StringBuilder(t1)
          .append(' ')
          .append(t2)
          .append(' ')
          .append(t3)
          .toString()
    );
  }

  public final StandardTextElement t(
      String t1,
      String t2,
      String t3,
      String t4) {
    Check.notNull(t1, "t1 == null");
    Check.notNull(t2, "t2 == null");
    Check.notNull(t3, "t3 == null");
    Check.notNull(t4, "t4 == null");
    return t(
      new StringBuilder(t1)
          .append(' ')
          .append(t2)
          .append(' ')
          .append(t3)
          .append(' ')
          .append(t4)
          .toString()
    );
  }

  public final StandardTextElement t(
      String t1,
      String t2,
      String t3,
      String t4,
      String t5) {
    Check.notNull(t1, "t1 == null");
    Check.notNull(t2, "t2 == null");
    Check.notNull(t3, "t3 == null");
    Check.notNull(t4, "t4 == null");
    Check.notNull(t5, "t5 == null");
    return t(
      new StringBuilder(t1)
          .append(' ')
          .append(t2)
          .append(' ')
          .append(t3)
          .append(' ')
          .append(t4)
          .append(' ')
          .append(t5)
          .toString()
    );
  }

  public final StandardTextElement t(
      String t1,
      String t2,
      String t3,
      String t4,
      String t5,
      String t6) {
    Check.notNull(t1, "t1 == null");
    Check.notNull(t2, "t2 == null");
    Check.notNull(t3, "t3 == null");
    Check.notNull(t4, "t4 == null");
    Check.notNull(t5, "t5 == null");
    Check.notNull(t6, "t6 == null");
    return t(
      new StringBuilder(t1)
          .append(' ')
          .append(t2)
          .append(' ')
          .append(t3)
          .append(' ')
          .append(t4)
          .append(' ')
          .append(t5)
          .append(' ')
          .append(t6)
          .toString()
    );
  }

  public final StandardTextElement t(
      String t1,
      String t2,
      String t3,
      String t4,
      String t5,
      String t6,
      String t7) {
    Check.notNull(t1, "t1 == null");
    Check.notNull(t2, "t2 == null");
    Check.notNull(t3, "t3 == null");
    Check.notNull(t4, "t4 == null");
    Check.notNull(t5, "t5 == null");
    Check.notNull(t6, "t6 == null");
    Check.notNull(t7, "t7 == null");
    return t(
      new StringBuilder(t1)
          .append(' ')
          .append(t2)
          .append(' ')
          .append(t3)
          .append(' ')
          .append(t4)
          .append(' ')
          .append(t5)
          .append(' ')
          .append(t6)
          .append(' ')
          .append(t7)
          .toString()
    );
  }

  public final StandardTextElement t(
      String t1,
      String t2,
      String t3,
      String t4,
      String t5,
      String t6,
      String t7,
      String t8) {
    Check.notNull(t1, "t1 == null");
    Check.notNull(t2, "t2 == null");
    Check.notNull(t3, "t3 == null");
    Check.notNull(t4, "t4 == null");
    Check.notNull(t5, "t5 == null");
    Check.notNull(t6, "t6 == null");
    Check.notNull(t7, "t7 == null");
    Check.notNull(t8, "t8 == null");
    return t(
      new StringBuilder(t1)
          .append(' ')
          .append(t2)
          .append(' ')
          .append(t3)
          .append(' ')
          .append(t4)
          .append(' ')
          .append(t5)
          .append(' ')
          .append(t6)
          .append(' ')
          .append(t7)
          .append(' ')
          .append(t8)
          .toString()
    );
  }

  public final StandardTextElement t(
      String t1,
      String t2,
      String t3,
      String t4,
      String t5,
      String t6,
      String t7,
      String t8,
      String t9) {
    Check.notNull(t1, "t1 == null");
    Check.notNull(t2, "t2 == null");
    Check.notNull(t3, "t3 == null");
    Check.notNull(t4, "t4 == null");
    Check.notNull(t5, "t5 == null");
    Check.notNull(t6, "t6 == null");
    Check.notNull(t7, "t7 == null");
    Check.notNull(t8, "t8 == null");
    Check.notNull(t9, "t9 == null");
    return t(
      new StringBuilder(t1)
          .append(' ')
          .append(t2)
          .append(' ')
          .append(t3)
          .append(' ')
          .append(t4)
          .append(' ')
          .append(t5)
          .append(' ')
          .append(t6)
          .append(' ')
          .append(t7)
          .append(' ')
          .append(t8)
          .append(' ')
          .append(t9)
          .toString()
    );
  }

  public final AttributeOrElement title(String text) {
    return addAttributeOrElement(AttributeOrElement.TITLE, text);
  }

  @Override
  public final String toString() {
    var sink = new HtmlSink();

    var out = new StringBuilder();

    sink.toStringBuilder(this, out);

    return out.toString();
  }

  @Override
  protected final <N extends StandardAttributeName> N addStandardAttribute(N name) {
    api().addAttribute(name);

    return name;
  }

  @Override
  protected final <N extends StandardAttributeName> N addStandardAttribute(N name, String value) {
    api().addAttribute(name, value);

    return name;
  }

  @Override
  protected final ElementName addStandardElement(StandardElementName element, String text) {
    api().addElement(element, text);

    return element;
  }

  @Override
  protected final ElementName addStandardElement(StandardElementName element, Value[] values) {
    Objects.requireNonNull(element, "element == null");

    api().addElement(element, values);

    return element;
  }

  @Override
  protected abstract void definition();

  protected final Lambda f(Lambda lambda) {
    api().addLambda(lambda);

    return lambda;
  }

  protected final void pathName(String path) {
    Validate.pathName(path.toString()); // path implicit null-check

    api().pathName(path);
  }

  protected CustomAttributeName.PathTo pathTo(String path) {
    Validate.pathName(path.toString()); // path implicit null-check

    var name = CustomAttributeName.PATH_TO;

    api().addAttribute(name, path);

    return name;
  }

  protected final NonVoidElementValue raw(String text) {
    api().addRaw(text);

    return Raw.INSTANCE;
  }

  private AttributeOrElement addAttributeOrElement(AttributeOrElement value, String text) {
    api().addAttributeOrElement(value, text);

    return value;
  }

}
