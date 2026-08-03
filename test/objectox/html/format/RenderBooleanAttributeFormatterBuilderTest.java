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

import objectos.html.format.BooleanAttributeValueStyle;
import objectos.html.format.QuotationMarkStyle;
import org.testng.annotations.Test;

public class RenderBooleanAttributeFormatterBuilderTest {

  @Test(
      description = "reject null valueStyle",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null"
  )
  public void valueStyle01() {
    final RenderBooleanAttributeFormatterBuilder subject;
    subject = new RenderBooleanAttributeFormatterBuilder();

    subject.valueStyle(null);
  }

  @Test(description = "default valueStyle is NO_VALUE")
  public void valueStyle02() {
    final RenderBooleanAttributeFormatterBuilder subject;
    subject = new RenderBooleanAttributeFormatterBuilder();

    final RenderBooleanAttributeFormatter res;
    res = subject.build();

    assertEquals(res.valueStyle, BooleanAttributeValueOption.NO_VALUE);
  }

  @Test(description = "accept the specified valueStyle")
  public void valueStyle03() {
    final RenderBooleanAttributeFormatterBuilder subject;
    subject = new RenderBooleanAttributeFormatterBuilder();

    subject.valueStyle(BooleanAttributeValueStyle.attributeName());

    final RenderBooleanAttributeFormatter res;
    res = subject.build();

    assertEquals(res.valueStyle, BooleanAttributeValueOption.ATTRIBUTE_NAME);
  }

  @Test(
      description = "reject null quotationMark",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null"
  )
  public void quotationMark01() {
    final RenderBooleanAttributeFormatterBuilder subject;
    subject = new RenderBooleanAttributeFormatterBuilder();

    subject.quotationMarkStyle(null);
  }

  @Test(description = "default quotationMark is DOUBLE")
  public void quotationMark02() {
    final RenderBooleanAttributeFormatterBuilder subject;
    subject = new RenderBooleanAttributeFormatterBuilder();

    final RenderBooleanAttributeFormatter res;
    res = subject.build();

    assertEquals(res.quotationMarkStyle, QuotationMarkOption.DOUBLE);
  }

  @Test(description = "accept the specified quotationMark")
  public void quotationMark03() {
    final RenderBooleanAttributeFormatterBuilder subject;
    subject = new RenderBooleanAttributeFormatterBuilder();

    subject.quotationMarkStyle(QuotationMarkStyle.single());

    final RenderBooleanAttributeFormatter res;
    res = subject.build();

    assertEquals(res.quotationMarkStyle, QuotationMarkOption.SINGLE);
  }

}
