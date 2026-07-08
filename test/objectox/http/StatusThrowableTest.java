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
package objectox.http;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import objectos.http.Status;
import objectos.lang.Throwables;
import org.testng.annotations.Test;

public class StatusThrowableTest {

  private final IOException exception = Throwables.trimStackTrace(new IOException(), 1);

  @Test
  public void toTestableText() {
    final StatusThrowable subject;
    subject = new StatusThrowable(Status.INTERNAL_SERVER_ERROR, exception);

    assertEquals(
        subject.toTestableText(),

        """
        500 Internal Server Error
        java.io.IOException
        	at objectos.way/objectox.http.StatusThrowableTest.<init>(StatusThrowableTest.java:27)
        """
    );
  }

}
