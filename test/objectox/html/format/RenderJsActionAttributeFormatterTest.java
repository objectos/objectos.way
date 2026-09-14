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
package objectox.html.format;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import objectos.html.format.QuotationMarkStyle;
import objectos.html.stream.RenderJsActionAttribute;
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.attr.AttributeName;
import objectox.html.stream.RenderJsActionAttributeEvent;
import org.testng.annotations.Test;

public class RenderJsActionAttributeFormatterTest {

  private String format(RenderJsActionAttributeFormatter subject) throws IOException {
    final JsAction action;
    action = Js.noop();

    final RenderJsActionAttribute attr;
    attr = new RenderJsActionAttributeEvent(AttributeName.ONCLICK, action);

    final Output out;
    out = OutputY.create();

    subject.format(attr, out);

    return out.toString();
  }

  private String format(RenderJsActionAttributeFormatter subject, String text) throws IOException {
    final JsAction action;
    action = Js.noop();

    final RenderJsActionAttribute attr;
    attr = new RenderJsActionAttributeEvent(AttributeName.ONCLICK, action);

    final Output out;
    out = OutputY.create(opts -> {
      opts.text = text;
    });

    subject.format(attr, out);

    return out.toString();
  }

  @Test(description = "single quotation mark wraps the JS action value")
  public void format01() throws IOException {
    final QuotationMarkStyle quotationMark;
    quotationMark = QuotationMarkStyle.single();

    final RenderJsActionAttributeFormatter subject;
    subject = new RenderJsActionAttributeFormatter(quotationMark);

    assertEquals(
        format(subject),

        " onclick='way.on(event,[\"NO\"])'"
    );
  }

  @Test(description = "double quotation mark wraps the JS action value when configured")
  public void format02() throws IOException {
    final QuotationMarkStyle quotationMark;
    quotationMark = QuotationMarkStyle.doubleMark();

    final RenderJsActionAttributeFormatter subject;
    subject = new RenderJsActionAttributeFormatter(quotationMark);

    assertEquals(
        format(subject),

        " onclick=\"way.on(event,[&quot;NO&quot;])\""
    );
  }

  @Test(description = "writes the leading space even when not at start of line")
  public void format03() throws IOException {
    final QuotationMarkStyle quotationMark;
    quotationMark = QuotationMarkStyle.single();

    final RenderJsActionAttributeFormatter subject;
    subject = new RenderJsActionAttributeFormatter(quotationMark);

    assertEquals(
        format(subject, "text"),

        "text onclick='way.on(event,[\"NO\"])'"
    );
  }

}
