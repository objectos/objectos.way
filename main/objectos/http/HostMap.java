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

sealed abstract class HostMap {

  private static final HostMap0 EMPTY = new HostMap0();

  HostMap() {}

  static HostMap of() {
    return EMPTY;
  }

  public abstract Host get(String hostValue);

  abstract HostMap add(String name, Host host);

  private static final class HostMap0 extends HostMap {

    @Override
    public final Host get(String hostValue) {
      return null;
    }

    @Override
    final HostMap add(String name, Host host) {
      return new HostMap1(name, host);
    }

  }

  private static final class HostMap1 extends HostMap {

    private final String name1;

    private final Host host1;

    HostMap1(String name1, Host host1) {
      this.name1 = name1;
      this.host1 = host1;
    }

    @Override
    public final Host get(String hostValue) {
      if (name1.equals(hostValue)) {
        return host1;
      }

      return null;
    }

    @Override
    final HostMap add(String name, Host host) {
      return new HostMap2(name1, host1, name, host);
    }

  }

  private static final class HostMap2 extends HostMap {

    private final String name1;

    private final Host host1;

    private final String name2;

    private final Host host2;

    HostMap2(String name1, Host host1, String name2, Host host2) {
      this.name1 = name1;

      this.host1 = host1;

      this.name2 = name2;

      this.host2 = host2;
    }

    @Override
    public final Host get(String hostValue) {
      if (name1.equals(hostValue)) {
        return host1;
      }

      if (name2.equals(hostValue)) {
        return host2;
      }

      return null;
    }

    @Override
    final HostMap add(String name, Host host) {
      final HostMapN hosts;
      hosts = new HostMapN();

      hosts.add(name1, host1);
      hosts.add(name2, host2);
      hosts.add(name, host);

      return hosts;
    }

  }

  private static final class HostMapN extends HostMap {

    private final Map<String, Host> map = new HashMap<>();

    @Override
    public final Host get(String hostValue) {
      return map.get(hostValue);
    }

    @Override
    final HostMap add(String name, Host host) {
      map.put(name, host);

      return this;
    }

  }

}
