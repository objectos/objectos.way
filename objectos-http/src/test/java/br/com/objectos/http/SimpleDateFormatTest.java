/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package br.com.objectos.http;

import static org.testng.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SimpleDateFormatTest {

  private SimpleDateFormat format;

  @BeforeClass
  public void _beforeClass() {
    format = new SimpleDateFormat("EEE, dd LLL yyyy HH:mm:ss zzz", Locale.US);

    TimeZone gmt;
    gmt = TimeZone.getTimeZone("GMT");

    format.setTimeZone(gmt);
  }

  @Test
  public void gmt() {
    Date date;
    date = new Date(1644404532278L);

    assertEquals(format.format(date), "Wed, 09 Feb 2022 11:02:12 GMT");
  }

}