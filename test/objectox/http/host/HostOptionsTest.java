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

import java.util.function.Consumer;
import objectos.http.HostOptions;
import org.testng.annotations.Test;

public class HostOptionsTest {

  private Host create(Consumer<? super HostOptions> opts) {
    final HostBuilder builder;
    builder = new HostBuilder(new HostGlobals() {
      @Override
      public int port() {
        return 8080;
      }
    });

    opts.accept(builder);

    return builder.build();
  }

  @Test
  public void defaults() {
    final Host host;
    host = create(_ -> {});

    assertEquals(host.name(), "localhost:8080");
  }

  @Test
  public void name() {
    final Host host;
    host = create(opts -> {
      opts.name("www.example.com");
    });

    assertEquals(host.name(), "www.example.com:8080");
  }

}
