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
package objectos.http;

import java.util.HashMap;
import java.util.Map;

sealed abstract class HttpHosts {

  private static final HttpHosts0 EMPTY = new HttpHosts0();

  HttpHosts() {}

  static HttpHosts of() {
    return EMPTY;
  }

  public abstract HttpHost6Pojo get(String hostValue);

  abstract HttpHosts add(String name, HttpHost6Pojo host);

  private static final class HttpHosts0 extends HttpHosts {

    @Override
    public final HttpHost6Pojo get(String hostValue) {
      return null;
    }

    @Override
    final HttpHosts add(String name, HttpHost6Pojo host) {
      return new HttpHosts1(name, host);
    }

  }

  private static final class HttpHosts1 extends HttpHosts {

    private final String name1;

    private final HttpHost6Pojo host1;

    HttpHosts1(String name1, HttpHost6Pojo host1) {
      this.name1 = name1;
      this.host1 = host1;
    }

    @Override
    public final HttpHost6Pojo get(String hostValue) {
      if (name1.equals(hostValue)) {
        return host1;
      }

      return null;
    }

    @Override
    final HttpHosts add(String name, HttpHost6Pojo host) {
      return new HttpHosts2(name1, host1, name, host);
    }

  }

  private static final class HttpHosts2 extends HttpHosts {

    private final String name1;

    private final HttpHost6Pojo host1;

    private final String name2;

    private final HttpHost6Pojo host2;

    HttpHosts2(String name1, HttpHost6Pojo host1, String name2, HttpHost6Pojo host2) {
      this.name1 = name1;

      this.host1 = host1;

      this.name2 = name2;

      this.host2 = host2;
    }

    @Override
    public final HttpHost6Pojo get(String hostValue) {
      if (name1.equals(hostValue)) {
        return host1;
      }

      if (name2.equals(hostValue)) {
        return host2;
      }

      return null;
    }

    @Override
    final HttpHosts add(String name, HttpHost6Pojo host) {
      final HttpHostsN hosts;
      hosts = new HttpHostsN();

      hosts.add(name1, host1);
      hosts.add(name2, host2);
      hosts.add(name, host);

      return hosts;
    }

  }

  private static final class HttpHostsN extends HttpHosts {

    private final Map<String, HttpHost6Pojo> map = new HashMap<>();

    @Override
    public final HttpHost6Pojo get(String hostValue) {
      return map.get(hostValue);
    }

    @Override
    final HttpHosts add(String name, HttpHost6Pojo host) {
      map.put(name, host);

      return this;
    }

  }

}
