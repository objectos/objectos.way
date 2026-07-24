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
package objectox.html.rec;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HtmlBytesTest {

  @DataProvider
  public Object[][] consumeProvider() {
    return new Object[][] {
        {HtmlBytes.STARTTAG8, HtmlBytes.xSTARTTAG8},
        {HtmlBytes.ATTRIBUTE88, HtmlBytes.xATTRIBUTE88},
        {HtmlBytes.ATTRIBUTE816, HtmlBytes.xATTRIBUTE816},
        {HtmlBytes.ATTRIBUTE824, HtmlBytes.xATTRIBUTE824},
        {HtmlBytes.BOOLEAN8, HtmlBytes.xBOOLEAN8},
        {HtmlBytes.TEXT8, HtmlBytes.xTEXT8},
        {HtmlBytes.TEXT16, HtmlBytes.xTEXT16},
        {HtmlBytes.TEXT24, HtmlBytes.xTEXT24},
        {HtmlBytes.RAW8, HtmlBytes.xRAW8},
        {HtmlBytes.RAW16, HtmlBytes.xRAW16},
        {HtmlBytes.RAW24, HtmlBytes.xRAW24},
        {HtmlBytes.STARTFLAT, HtmlBytes.xSTARTFLAT},
        {HtmlBytes.STARTFRAG, HtmlBytes.xSTARTFRAG},
        {HtmlBytes.xSTARTTAG8, HtmlBytes.xSTARTTAG8},
        {HtmlBytes.xATTRIBUTE88, HtmlBytes.xATTRIBUTE88},
        {HtmlBytes.xATTRIBUTE816, HtmlBytes.xATTRIBUTE816},
        {HtmlBytes.xATTRIBUTE824, HtmlBytes.xATTRIBUTE824},
        {HtmlBytes.xBOOLEAN8, HtmlBytes.xBOOLEAN8},
        {HtmlBytes.xTEXT8, HtmlBytes.xTEXT8},
        {HtmlBytes.xTEXT16, HtmlBytes.xTEXT16},
        {HtmlBytes.xTEXT24, HtmlBytes.xTEXT24},
        {HtmlBytes.xRAW8, HtmlBytes.xRAW8},
        {HtmlBytes.xRAW16, HtmlBytes.xRAW16},
        {HtmlBytes.xRAW24, HtmlBytes.xRAW24},
        {HtmlBytes.xSTARTFLAT, HtmlBytes.xSTARTFLAT},
        {HtmlBytes.xSTARTFRAG, HtmlBytes.xSTARTFRAG}
    };
  }

  @Test(dataProvider = "consumeProvider")
  public void consume(byte source, byte expected) {
    final byte res;
    res = HtmlBytes.consume(source);

    assertEquals(res, expected);
  }

}
