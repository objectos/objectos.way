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
package objectox.html.markup;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HtmlBytesTest {

  @DataProvider
  public Object[][] testableOnlyProvider() {
    return new Object[][] {
        {HtmlBytes.THEADING8, HtmlBytes.oTHEADING8},
        {HtmlBytes.THEADING16, HtmlBytes.oTHEADING16},
        {HtmlBytes.THEADING24, HtmlBytes.oTHEADING24},
        {HtmlBytes.TCELL8, HtmlBytes.oTCELL8},
        {HtmlBytes.TCELL16, HtmlBytes.oTCELL16},
        {HtmlBytes.TCELL24, HtmlBytes.oTCELL24},
        {HtmlBytes.TPROP8, HtmlBytes.oTPROP8},
        {HtmlBytes.TPROP16, HtmlBytes.oTPROP16},
        {HtmlBytes.TPROP24, HtmlBytes.oTPROP24},
        {HtmlBytes.TPROPKEY8, HtmlBytes.oTPROPKEY8},
        {HtmlBytes.TPROPKEY16, HtmlBytes.oTPROPKEY16},
        {HtmlBytes.TPROPKEY24, HtmlBytes.oTPROPKEY24},
        {HtmlBytes.TPROPVAL8, HtmlBytes.oTPROPVAL8},
        {HtmlBytes.TPROPVAL16, HtmlBytes.oTPROPVAL16},
        {HtmlBytes.TPROPVAL24, HtmlBytes.oTPROPVAL24},

        {HtmlBytes.oTHEADING8, HtmlBytes.oTHEADING8},
        {HtmlBytes.oTHEADING16, HtmlBytes.oTHEADING16},
        {HtmlBytes.oTHEADING24, HtmlBytes.oTHEADING24},
        {HtmlBytes.oTCELL8, HtmlBytes.oTCELL8},
        {HtmlBytes.oTCELL16, HtmlBytes.oTCELL16},
        {HtmlBytes.oTCELL24, HtmlBytes.oTCELL24},
        {HtmlBytes.oTPROP8, HtmlBytes.oTPROP8},
        {HtmlBytes.oTPROP16, HtmlBytes.oTPROP16},
        {HtmlBytes.oTPROP24, HtmlBytes.oTPROP24},
        {HtmlBytes.oTPROPKEY8, HtmlBytes.oTPROPKEY8},
        {HtmlBytes.oTPROPKEY16, HtmlBytes.oTPROPKEY16},
        {HtmlBytes.oTPROPKEY24, HtmlBytes.oTPROPKEY24},
        {HtmlBytes.oTPROPVAL8, HtmlBytes.oTPROPVAL8},
        {HtmlBytes.oTPROPVAL16, HtmlBytes.oTPROPVAL16},
        {HtmlBytes.oTPROPVAL24, HtmlBytes.oTPROPVAL24}
    };
  }

  @Test(dataProvider = "testableOnlyProvider")
  public void testableOnly(byte source, byte expected) {
    final byte res;
    res = HtmlBytes.testableOnly(source);

    assertEquals(res, expected);
  }

  @DataProvider
  public Object[][] testableOnlyInvalidProvider() {
    return new Object[][] {
        {HtmlBytes.DOCTYPE},

        {HtmlBytes.ELEMSTR8},
        {HtmlBytes.ELEMVOID8},

        {HtmlBytes.ATTRIBUTE88},
        {HtmlBytes.ATTRIBUTE816},
        {HtmlBytes.ATTRIBUTE824},
        {HtmlBytes.BOOLEAN8},
        {HtmlBytes.JSACTION88},
        {HtmlBytes.JSACTION816},
        {HtmlBytes.JSACTION824},

        {HtmlBytes.FRAGSTR},
        {HtmlBytes.FRAGEND},

        {HtmlBytes.TNEWLINE},

        {HtmlBytes.TEXT8},
        {HtmlBytes.TEXT16},
        {HtmlBytes.TEXT24},

        {HtmlBytes.RAW8},
        {HtmlBytes.RAW16},
        {HtmlBytes.RAW24},

        {HtmlBytes.VALUES8},
        {HtmlBytes.VALUES16},
        {HtmlBytes.VALUES24},

        {HtmlBytes.xELEMSTR8},
        {HtmlBytes.xELEMVOID8},

        {HtmlBytes.xATTRIBUTE88},
        {HtmlBytes.xATTRIBUTE816},
        {HtmlBytes.xATTRIBUTE824},
        {HtmlBytes.xBOOLEAN8},
        {HtmlBytes.xJSACTION88},
        {HtmlBytes.xJSACTION816},
        {HtmlBytes.xJSACTION824},

        {HtmlBytes.xFRAGSTR},

        {HtmlBytes.xTHEADING8},
        {HtmlBytes.xTHEADING16},
        {HtmlBytes.xTHEADING24},
        {HtmlBytes.xTCELL8},
        {HtmlBytes.xTCELL16},
        {HtmlBytes.xTCELL24},
        {HtmlBytes.xTPROP8},
        {HtmlBytes.xTPROP16},
        {HtmlBytes.xTPROP24},
        {HtmlBytes.xTPROPKEY8},
        {HtmlBytes.xTPROPKEY16},
        {HtmlBytes.xTPROPKEY24},
        {HtmlBytes.xTPROPVAL8},
        {HtmlBytes.xTPROPVAL16},
        {HtmlBytes.xTPROPVAL24},
        {HtmlBytes.xTNEWLINE},

        {HtmlBytes.xTEXT8},
        {HtmlBytes.xTEXT16},
        {HtmlBytes.xTEXT24},

        {HtmlBytes.xRAW8},
        {HtmlBytes.xRAW16},
        {HtmlBytes.xRAW24}
    };
  }

  @Test(dataProvider = "testableOnlyInvalidProvider")
  public void testableOnlyInvalid(byte source) {
    try {
      HtmlBytes.testableOnly(source);

      fail();
    } catch (IllegalArgumentException e) {
      final String msg;
      msg = e.getMessage();

      assertEquals(msg, "source=" + source);
    }
  }

}
