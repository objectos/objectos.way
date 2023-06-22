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
package objectos.http;

import objectos.util.UnmodifiableList;

final class TestCase0001 {

  static final String DESCRIPTION = "GET / => 200 text/html";

  static final UnmodifiableList<String> REQUEST = UnmodifiableList.of(
    "GET / HTTP/1.1",
    "Host: localhost:7001",
    "Connection: keep-alive",
    "sec-ch-ua: \" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"",
    "sec-ch-ua-mobile: ?0",
    "sec-ch-ua-platform: \"Linux\"",
    "DNT: 1",
    "Upgrade-Insecure-Requests: 1",
    "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36",
    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
    "Sec-Fetch-Site: none",
    "Sec-Fetch-Mode: navigate",
    "Sec-Fetch-User: ?1",
    "Sec-Fetch-Dest: document",
    "Accept-Encoding: gzip, deflate, br",
    "Accept-Language: pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7",
    "",
    ""
  );

  static final UnmodifiableList<String> RESPONSE = UnmodifiableList.of(
    "HTTP/1.1 200 OK",
    "Server: nginx/1.20.1",
    "Date: Tue, 01 Feb 2022 11:50:15 GMT",
    "Content-Type: text/html",
    "Content-Length: 612",
    "Last-Modified: Mon, 31 Jan 2022 12:22:08 GMT",
    "Connection: keep-alive",
    "ETag: \"61f7d470-264\"",
    "Accept-Ranges: bytes"
  );

}