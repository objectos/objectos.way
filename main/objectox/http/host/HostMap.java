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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public sealed abstract class HostMap {

  HostMap() {}

  public static HostMap of(Collection<? extends Host> values) {
    return switch (values.size()) {
      case 0 -> new HostMap0();

      case 1 -> {
        final Iterator<? extends Host> it;
        it = values.iterator();

        final Host host1;
        host1 = it.next();

        yield new HostMap1(host1);
      }

      case 2 -> {
        final Iterator<? extends Host> it;
        it = values.iterator();

        final Host host1;
        host1 = it.next();

        final Host host2;
        host2 = it.next();

        yield new HostMap2(host1, host2);
      }

      default -> {
        final Map<String, Host> map;
        map = values.stream().collect(Collectors.toUnmodifiableMap(Host::name, Function.identity()));

        yield new HostMapN(map);
      }
    };
  }

  public abstract Host get(String hostValue);

  private static final class HostMap0 extends HostMap {

    @Override
    public final Host get(String hostValue) {
      return null;
    }

  }

  private static final class HostMap1 extends HostMap {

    private final Host host1;

    HostMap1(Host host1) {
      this.host1 = host1;
    }

    @Override
    public final Host get(String hostValue) {
      if (host1.test(hostValue)) {
        return host1;
      }

      return null;
    }

  }

  private static final class HostMap2 extends HostMap {

    private final Host host1;

    private final Host host2;

    HostMap2(Host host1, Host host2) {
      this.host1 = host1;

      this.host2 = host2;
    }

    @Override
    public final Host get(String hostValue) {
      if (host1.test(hostValue)) {
        return host1;
      }

      if (host2.test(hostValue)) {
        return host2;
      }

      return null;
    }

  }

  private static final class HostMapN extends HostMap {

    private final Map<String, Host> map;

    HostMapN(Map<String, Host> map) {
      this.map = map;
    }

    @Override
    public final Host get(String hostValue) {
      return map.get(hostValue);
    }

  }

}
