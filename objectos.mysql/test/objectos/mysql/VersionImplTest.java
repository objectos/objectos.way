/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.mysql;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class VersionImplTest {

  @Test
  public void parseLine() {
    testParseLine(
        "mysqldump  Ver 10.13 Distrib 5.6.50, for linux-glibc2.12 (x86_64)",
        Release.MYSQL_5_6,
        "5.6.50"
    );
    testParseLine(
        "/opt/mysql-5.6/bin/mysql  Ver 14.14 Distrib 5.6.50, for linux-glibc2.12 (x86_64) using  EditLine wrapper",
        Release.MYSQL_5_6,
        "5.6.50"
    );
    testParseLine(
        "/opt/mysql-5.6/bin/mysqld  Ver 5.6.50 for linux-glibc2.12 on x86_64 (MySQL Community Server (GPL))",
        Release.MYSQL_5_6,
        "5.6.50"
    );
  }

  private void testParseLine(String line, Release release, String toString) {
    VersionImpl result;
    result = VersionImpl.parseLine(line);

    assertEquals(result.getRelease(), release);

    assertEquals(result.toString(), toString);
  }

}
