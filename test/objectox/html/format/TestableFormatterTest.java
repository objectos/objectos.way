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

import objectos.html.stream.EndStartTagKind;
import objectos.html.stream.Event;
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.stream.BeginStartTagEvent;
import objectox.html.stream.EmitAttributeEvent;
import objectox.html.stream.EmitBooleanAttributeEvent;
import objectox.html.stream.EmitDoctypeEvent;
import objectox.html.stream.EmitEndTagEvent;
import objectox.html.stream.EmitJsActionAttributeEvent;
import objectox.html.stream.EmitRawEvent;
import objectox.html.stream.EmitTestableCellEvent;
import objectox.html.stream.EmitTestableHeadingEvent;
import objectox.html.stream.EmitTextEvent;
import objectox.html.stream.EndDocumentEvent;
import objectox.html.stream.EndStartTagEvent;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestableFormatterTest {

  @DataProvider
  public Object[][] testCase01Provider() {
    final JsAction action;
    action = Js.noop();

    return new Object[][] {
        {evts(new BeginStartTagEvent(0, ElementName.DIV)), ""},
        {evts(new EndStartTagEvent(0, EndStartTagKind.ELEMENT)), ""},
        {evts(new EndDocumentEvent(false)), ""},
        {evts(new EndDocumentEvent(true)), ""},
        {evts(new EmitAttributeEvent(0, AttributeName.ID, "foo")), ""},
        {evts(new EmitAttributeEvent(0, AttributeName.ID, "a\"b&c<d>e")), ""},
        {evts(new EmitBooleanAttributeEvent(0, AttributeName.AUTOFOCUS)), ""},
        {evts(new EmitDoctypeEvent(0)), ""},
        {evts(new EmitEndTagEvent(0, ElementName.DIV)), ""},
        {evts(new EmitJsActionAttributeEvent(0, AttributeName.ONCLICK, action)), ""},
        {evts(new EmitRawEvent(0, "<br/>")), ""},
        {evts(new EmitTextEvent(0, "<xml></xml>&copy;")), ""},
        {evts(new EmitTextEvent(0, "a\nb")), ""},
        {
            evts(
                new BeginStartTagEvent(0, ElementName.DIV),
                new EmitAttributeEvent(0, AttributeName.ID, "foo"),
                new EmitBooleanAttributeEvent(0, AttributeName.AUTOFOCUS),
                new EmitJsActionAttributeEvent(0, AttributeName.ONCLICK, action),
                new EndStartTagEvent(0, EndStartTagKind.ELEMENT),
                new EmitTextEvent(0, "Hello"),
                new EmitEndTagEvent(0, ElementName.DIV),
                new EndDocumentEvent(false)
            ),
            ""
        },
        {
            evts(
                new EmitAttributeEvent(0, AttributeName.ID, "foo"),
                new EmitBooleanAttributeEvent(0, AttributeName.AUTOFOCUS),
                new EmitJsActionAttributeEvent(0, AttributeName.ONCLICK, action)
            ),
            ""
        },
        {
            evts(
                new EmitDoctypeEvent(0),
                new EmitTextEvent(0, "<xml></xml>&copy;"),
                new EmitRawEvent(0, "<br/>"),
                new EmitEndTagEvent(0, ElementName.DIV),
                new EndDocumentEvent(true)
            ),
            ""
        }
    };
  }

  @Test(dataProvider = "testCase01Provider")
  public void testCase01(Event[] events, String expected) {
    test(events, expected);
  }

  @DataProvider
  public Object[][] testCase02Provider() {
    return new Object[][] {
        {
            evts(new EmitTestableHeadingEvent(0, 1, "Heading 1")),
            """
            # Heading 1
            """
        },
        {
            evts(new EmitTestableHeadingEvent(0, 2, "Heading 2")),
            """
            ## Heading 2
            """
        },
        {
            evts(new EmitTestableHeadingEvent(0, 3, "Heading 3")),
            """
            ### Heading 3
            """
        },
        {
            evts(new EmitTestableHeadingEvent(0, 4, "Heading 4")),
            """
            #### Heading 4
            """
        },
        {
            evts(new EmitTestableHeadingEvent(0, 5, "Heading 5")),
            """
            ##### Heading 5
            """
        },
        {
            evts(new EmitTestableHeadingEvent(0, 6, "Heading 6")),
            """
            ###### Heading 6
            """
        },
        {
            evts(
                new EmitTestableHeadingEvent(0, 2, "Heading 2"),
                new EmitTestableHeadingEvent(0, 3, "Heading 3")
            ),
            """
            ## Heading 2

            ### Heading 3
            """
        },
        {
            evts(
                new EmitTestableHeadingEvent(0, 1, "Heading 1"),
                new EmitTestableHeadingEvent(0, 2, "Heading 2"),
                new EmitTestableHeadingEvent(0, 3, "Heading 3")
            ),
            """
            # Heading 1

            ## Heading 2

            ### Heading 3
            """
        },
        {
            evts(
                new EmitTestableHeadingEvent(0, 2, "Heading 2"),
                new EndDocumentEvent(false)
            ),
            """
            ## Heading 2
            """
        },
        {
            evts(
                new EmitTestableHeadingEvent(0, 2, "Heading 2"),
                new EmitDoctypeEvent(0),
                new EmitTextEvent(0, "ignored"),
                new EmitRawEvent(0, "ignored"),
                new EmitTestableHeadingEvent(0, 3, "Heading 3")
            ),
            """
            ## Heading 2

            ### Heading 3
            """
        },
        {
            evts(
                new EmitTestableHeadingEvent(0, 2, "Heading 2"),
                new EmitTestableCellEvent(0, "1"),
                new EndDocumentEvent(false)
            ),
            """
            ## Heading 2

            | 1 |
            """
        },
        {
            evts(
                new EmitTestableHeadingEvent(0, 2, "Heading 2"),
                new EmitDoctypeEvent(0),
                new EmitTextEvent(0, "ignored"),
                new EmitRawEvent(0, "ignored"),
                new EmitTestableCellEvent(0, "1"),
                new EndDocumentEvent(false)
            ),
            """
            ## Heading 2

            | 1 |
            """
        },
        {
            evts(
                new EmitDoctypeEvent(0),
                new EmitTextEvent(0, "ignored"),
                new EmitRawEvent(0, "ignored"),
                new EmitTestableHeadingEvent(0, 2, "Heading 2")
            ),
            """
            ## Heading 2
            """
        },
        {
            evts(
                new EmitTestableHeadingEvent(0, 2, "Heading 2"),
                new EmitTextEvent(0, "ignored"),
                new EndDocumentEvent(false)
            ),
            """
            ## Heading 2
            """
        }
    };
  }

  @Test(dataProvider = "testCase02Provider")
  public void testCase02(Event[] events, String expected) {
    test(events, expected);
  }

  @DataProvider
  public Object[][] testCase03Provider() {
    return new Object[][] {
        {
            evts(
                new EmitTestableCellEvent(0, "1"),
                new EndDocumentEvent(false)
            ),
            """
            | 1 |
            """
        },
        {
            evts(
                new EmitTestableCellEvent(0, "1"),
                new EndDocumentEvent(true)
            ),
            """
            | 1 |
            """
        },
        {
            evts(
                new EmitTestableCellEvent(0, "1"),
                new EmitTestableCellEvent(0, "2"),
                new EndDocumentEvent(false)
            ),
            """
            | 1 | 2 |
            """
        },
        {
            evts(
                new EmitTestableCellEvent(0, "1"),
                new EmitTestableCellEvent(0, "2"),
                new EmitTestableCellEvent(0, "3"),
                new EndDocumentEvent(false)
            ),
            """
            | 1 | 2 | 3 |
            """
        },
        {
            evts(
                new EmitTestableCellEvent(0, "1"),
                new EmitTextEvent(0, "ignored"),
                new EmitRawEvent(0, "ignored"),
                new EmitTestableCellEvent(0, "2"),
                new EndDocumentEvent(false)
            ),
            """
            | 1 | 2 |
            """
        },
        {
            evts(
                new EmitTestableCellEvent(0, "1"),
                new EmitTestableHeadingEvent(0, 2, "Heading 2"),
                new EmitTestableCellEvent(0, "2"),
                new EndDocumentEvent(false)
            ),
            """
            | 1 |

            ## Heading 2

            | 2 |
            """
        },
        {
            evts(
                new EmitTestableCellEvent(0, "1"),
                new EmitTestableHeadingEvent(0, 2, "Heading 2"),
                new EmitTestableHeadingEvent(0, 3, "Heading 3"),
                new EmitTestableCellEvent(0, "2"),
                new EndDocumentEvent(false)
            ),
            """
            | 1 |

            ## Heading 2

            ### Heading 3

            | 2 |
            """
        },
        {
            evts(
                new EmitTestableHeadingEvent(0, 2, "Heading 2"),
                new EmitTestableCellEvent(0, "1"),
                new EmitTestableCellEvent(0, "2"),
                new EndDocumentEvent(false)
            ),
            """
            ## Heading 2

            | 1 | 2 |
            """
        },
        {
            evts(
                new EmitTestableCellEvent(0, "1 "),
                new EndDocumentEvent(false)
            ),
            """
            | 1  |
            """
        }
    };
  }

  @Test(dataProvider = "testCase03Provider")
  public void testCase03(Event[] events, String expected) {
    test(events, expected);
  }

  private Event[] evts(Event... events) {
    return events;
  }

  private void test(Event[] events, String expected) {
    final TestableFormatter subject;
    subject = new TestableFormatter();

    final Output out;
    out = OutputY.create();

    for (Event event : events) {
      subject.formatTo(event, out);
    }

    final String actual;
    actual = out.toString();

    assertEquals(actual, expected);
  }

}
