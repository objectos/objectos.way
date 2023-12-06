/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ThrowableMessageBuilderTest {

  private ThrowableMessageBuilder badObjectMessage;

  @BeforeClass
  public void beforeClass() {
    badObjectMessage = new ThrowableMessageBuilder();
  }

  @Test
  public void test() throws InvalidObjectIdFormatException {
    badObjectMessage
        .clear()
        .append("Failed to inflate object data").nl()
        .nl()
        .append("No more bytes left to read.").nl()
        .nl()
        .keyValue("channelReadCount", 1234L).nl()
        .keyValue("channelReadLimit", 1220L).nl()
        .keyValue("        objectId", ObjectId.parse("808ee6fce27d119cfa2dbeacd176439e02e0924d"))
        .nl()
        .keyValue("           state", "_READ_OBJECT").nl()
        .nl();

    assertEquals(
      badObjectMessage.toString(),

      String.join(
        System.lineSeparator(),

        "Failed to inflate object data",
        "",
        "No more bytes left to read.",
        "",
        "channelReadCount=1234",
        "channelReadLimit=1220",
        "        objectId=ObjectId [",
        "  808ee6fce27d119cfa2dbeacd176439e02e0924d",
        "]",
        "           state=_READ_OBJECT",
        "",
        ""
      )
    );
  }

}