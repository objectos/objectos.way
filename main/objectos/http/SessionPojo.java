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

import java.time.Instant;
import java.time.InstantSource;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class SessionPojo implements Session {

  static final int SESSION_LENGTH = 32;

  private Instant accessTime;

  private final HttpToken id;

  private final Lock lock = new ReentrantLock();

  SessionPojo(HttpToken id) {
    this.id = id;
  }

  public final Instant accessTime() {
    return accessTime;
  }

  public final HttpToken id() {
    return id;
  }

  @Override
  public final boolean isPresent() {
    return true;
  }

  public final void touch(InstantSource source) {
    lock.lock();
    try {
      accessTime = source.instant();
    } finally {
      lock.unlock();
    }
  }

}
