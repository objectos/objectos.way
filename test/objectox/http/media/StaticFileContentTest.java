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
package objectox.http.media;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import objectos.dev.Testable;
import objectos.http.MediaType;
import objectos.lang.BinaryObject;
import org.testng.annotations.Test;

public class StaticFileContentTest {

  @Test
  public void toTestableText01() {
    record Data(String value) implements BinaryObject, Testable {
      @Override
      public void binaryTo(OutputStream out) throws IOException {
        final byte[] bytes;
        bytes = value.getBytes(StandardCharsets.UTF_8);

        out.write(bytes);
      }

      @Override
      public final String toTestableText() {
        return value;
      }
    }

    final Data data;
    data = new Data("Am Testable");

    final ContentBinaryObject content;
    content = new ContentBinaryObject(MediaType.TEXT_PLAIN, data);

    final StaticFileContent subject;
    subject = new StaticFileContent(content);

    assertEquals(
        subject.toTestableText(),

        """
        [static file]
        text/plain; charset=utf-8
        Am Testable\
        """
    );
  }

}
