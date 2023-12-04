/*
 * Copyright (C) 2023 Objectos Software LTDA.
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

import static org.testng.Assert.assertEquals;

import java.time.Duration;
import org.testng.annotations.Test;

public class SetCookieTest {

  @Test
  public void nameValue() {
    SetCookie set;
    set = SetCookie.of("PHPSESSID", "298zf09hf012fh2");

    assertEquals(set.toString(), "PHPSESSID=298zf09hf012fh2");
  }

  @Test
  public void maxAge() {
    SetCookie set;
    set = SetCookie.of("name", "value");

    set.maxAge(Duration.ofMinutes(1));

    assertEquals(set.toString(), "name=value; Max-Age=60");
  }

  @Test
  public void path() {
    SetCookie set;
    set = SetCookie.of("name", "value");

    set.path("/foobar");

    assertEquals(set.toString(), "name=value; Path=/foobar");
  }

}