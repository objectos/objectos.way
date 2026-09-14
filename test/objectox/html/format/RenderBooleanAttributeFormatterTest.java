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
import objectos.html.format.BooleanAttributeValueStyle;
import objectos.html.format.QuotationMarkStyle;
import objectos.html.stream.RenderBooleanAttribute;
import objectox.html.attr.AttributeName;
import objectox.html.stream.RenderBooleanAttributeEvent;
import org.testng.annotations.Test;

public class RenderBooleanAttributeFormatterTest {

  private String format(RenderBooleanAttributeFormatter subject) throws IOException {
    final RenderBooleanAttribute attr;
    attr = new RenderBooleanAttributeEvent(AttributeName.CHECKED);

    final Output out;
    out = OutputY.create();

    subject.format(attr, out);

    return out.toString();
  }

  private String format(RenderBooleanAttributeFormatter subject, String text) throws IOException {
    final RenderBooleanAttribute attr;
    attr = new RenderBooleanAttributeEvent(AttributeName.CHECKED);

    final Output out;
    out = OutputY.create(opts -> {
      opts.text = text;
    });

    subject.format(attr, out);

    return out.toString();
  }

  @Test(description = "noValue style emits the attribute name without a value")
  public void valueStyle01() throws IOException {
    final RenderBooleanAttributeFormatter subject;
    subject = new RenderBooleanAttributeFormatter(
        BooleanAttributeValueStyle.noValue(),

        QuotationMarkStyle.doubleMark()
    );

    assertEquals(
        format(subject),

        " checked"
    );
  }

  @Test(description = "emptyString style emits an empty value wrapped in quotation marks")
  public void valueStyle02() throws IOException {
    final RenderBooleanAttributeFormatter subject;
    subject = new RenderBooleanAttributeFormatter(
        BooleanAttributeValueStyle.emptyString(),

        QuotationMarkStyle.doubleMark()
    );

    assertEquals(
        format(subject),

        " checked=\"\""
    );
  }

  @Test(description = "emptyString style emits single quotation marks when configured")
  public void valueStyle03() throws IOException {
    final RenderBooleanAttributeFormatter subject;
    subject = new RenderBooleanAttributeFormatter(
        BooleanAttributeValueStyle.emptyString(),

        QuotationMarkStyle.single()
    );

    assertEquals(
        format(subject),

        " checked=''"
    );
  }

  @Test(description = "attributeName style emits the attribute name as the value")
  public void valueStyle04() throws IOException {
    final RenderBooleanAttributeFormatter subject;
    subject = new RenderBooleanAttributeFormatter(
        BooleanAttributeValueStyle.attributeName(),

        QuotationMarkStyle.doubleMark()
    );

    assertEquals(
        format(subject),

        " checked=\"checked\""
    );
  }

  @Test(description = "attributeName style emits single quotation marks when configured")
  public void valueStyle05() throws IOException {
    final RenderBooleanAttributeFormatter subject;
    subject = new RenderBooleanAttributeFormatter(
        BooleanAttributeValueStyle.attributeName(),

        QuotationMarkStyle.single()
    );

    assertEquals(
        format(subject),

        " checked='checked'"
    );
  }

  @Test(description = "writes the leading space even when not at start of line")
  public void format01() throws IOException {
    final RenderBooleanAttributeFormatter subject;
    subject = new RenderBooleanAttributeFormatter(
        BooleanAttributeValueStyle.attributeName(),

        QuotationMarkStyle.doubleMark()
    );

    assertEquals(
        format(subject, "text"),

        "text checked=\"checked\""
    );
  }

}
