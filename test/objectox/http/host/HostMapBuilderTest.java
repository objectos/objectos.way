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
package objectox.http.host;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import java.io.IOException;
import java.util.function.Consumer;
import objectos.http.HostOptions;
import objectos.lang.Stage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HostMapBuilderTest {

  @Test(description = "reject null")
  public void host01() {
    final HostMapBuilder subject;
    subject = new HostMapBuilder();

    try {
      subject.add(null);

      Assert.fail("It should have thrown");
    } catch (NullPointerException expected) {}
  }

  @Test(description = "accept")
  public void host02() throws IOException {
    final HostMapBuilder subject;
    subject = new HostMapBuilder();

    final Consumer<? super HostOptions> opts;
    opts = host -> {
      host.name("www.example.com");
    };

    subject.add(opts);

    final HostGlobals globals;
    globals = new HostGlobals() {
      @Override
      public final int port() { return 1234; }

      @Override
      public final Stage stage() {
        return Stage.DEV;
      }
    };

    final HostMap res;
    res = subject.build(globals);

    assertEquals(res.size(), 1);
    assertNotNull(res.get("www.example.com:1234"));
  }

  @Test(description = "reject host duplicate name")
  public void host03() {
    final HostMapBuilder subject;
    subject = new HostMapBuilder();

    try {
      subject.add(host -> {
        host.name("duplicate.example.com");
      });

      subject.add(host -> {
        host.name("duplicate.example.com");
      });

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "A host with the same name was already registered: duplicate.example.com");
    }
  }

}
