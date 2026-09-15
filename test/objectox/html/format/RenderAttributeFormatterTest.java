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
import objectos.html.stream.RenderAttribute;
import objectox.html.attr.AttributeName;
import objectox.html.stream.RenderAttributeEvent;
import org.testng.annotations.Test;

public class RenderAttributeFormatterTest {

  private String format(RenderAttributeFormatter subject) throws IOException {
    final RenderAttribute attr;
    attr = RenderAttributeEvent.of(0, AttributeName.ID, "value");

    final Output out;
    out = OutputY.create();

    subject.format(attr, out);

    return out.toString();
  }

  private String format(RenderAttributeFormatter subject, String text) throws IOException {
    return format(subject, "value", text);
  }

  private String format(RenderAttributeFormatter subject, String attrValue, String text) throws IOException {
    final RenderAttribute attr;
    attr = RenderAttributeEvent.of(0, AttributeName.ID, attrValue);

    final Output out;
    out = OutputY.create(opts -> {
      opts.text = text;
    });

    subject.format(attr, out);

    return out.toString();
  }

  @Test(description = "double quotation mark wraps the attribute value")
  public void quotationMark01() throws IOException {
    final QuotationMarkStyle quotationMark;
    quotationMark = QuotationMarkStyle.doubleMark();

    final RenderAttributeFormatter subject;
    subject = new RenderAttributeFormatter(quotationMark);

    assertEquals(
        format(subject),

        " id=\"value\""
    );
  }

  @Test(description = "single quotation mark wraps the attribute value")
  public void quotationMark02() throws IOException {
    final QuotationMarkStyle quotationMark;
    quotationMark = QuotationMarkStyle.single();

    final RenderAttributeFormatter subject;
    subject = new RenderAttributeFormatter(quotationMark);

    assertEquals(
        format(subject),

        " id='value'"
    );
  }

  @Test(description = "writes the leading space even when not at start of line")
  public void format01() throws IOException {
    final QuotationMarkStyle quotationMark;
    quotationMark = QuotationMarkStyle.doubleMark();

    final RenderAttributeFormatter subject;
    subject = new RenderAttributeFormatter(quotationMark);

    assertEquals(
        format(subject, "text"),

        "text id=\"value\""
    );
  }

  @Test(description = "double quotation mark escapes the attribute value")
  public void escape01() throws IOException {
    final QuotationMarkStyle quotationMark;
    quotationMark = QuotationMarkStyle.doubleMark();

    final RenderAttributeFormatter subject;
    subject = new RenderAttributeFormatter(quotationMark);

    assertEquals(
        format(subject, "A&B", ""),

        " id=\"A&amp;B\""
    );
  }

  @Test(description = "double quotation mark escapes double quotes in the attribute value")
  public void escape02() throws IOException {
    final QuotationMarkStyle quotationMark;
    quotationMark = QuotationMarkStyle.doubleMark();

    final RenderAttributeFormatter subject;
    subject = new RenderAttributeFormatter(quotationMark);

    assertEquals(
        format(subject, "A\"B", ""),

        " id=\"A&quot;B\""
    );
  }

  @Test(description = "single quotation mark escapes single quotes in the attribute value")
  public void escape03() throws IOException {
    final QuotationMarkStyle quotationMark;
    quotationMark = QuotationMarkStyle.single();

    final RenderAttributeFormatter subject;
    subject = new RenderAttributeFormatter(quotationMark);

    assertEquals(
        format(subject, "A'B", ""),

        " id='A&#39;B'"
    );
  }

}
