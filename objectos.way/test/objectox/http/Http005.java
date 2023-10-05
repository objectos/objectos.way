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
package objectox.http;

import objectox.http.TestingInput.KeepAliveInput;

/**
 * Keep-Alive request
 */
public final class Http005 {

  public static final String INPUT01 = """
    GET /login HTTP/1.1
    Host: www.example.com

    """.replace("\n", "\r\n");

  public static final String INPUT02 = """
    GET /login.css HTTP/1.1
    Host: www.example.com

    """.replace("\n", "\r\n");

  public static final KeepAliveInput INPUT = new KeepAliveInput(
    INPUT01, INPUT02);

  public static final String BODY01 = """
    <!doctype html>
    <html>
    <head>
    <title>Login page</title>
    <link rel="stylesheet" type="text/css" href="login.css" />
    </head>
    <body>
    <!-- the actual body -->
    </body>
    </html>
    """;

  public static final String BODY02 = """
    * {
      box-sizing: border-box;
    }
    """;

  public static final String OUTPUT01 = """
    HTTP/1.1 200 OK<CRLF>
    Content-Type: text/html; charset=utf-8<CRLF>
    Content-Length: 171<CRLF>
    Date: Fri, 07 Jul 2023 14:11:45 GMT<CRLF>
    <CRLF>
    %s""".formatted(BODY01).replace("<CRLF>\n", "\r\n");

  public static final String OUTPUT02 = """
    HTTP/1.1 200 OK<CRLF>
    Content-Type: text/css; charset=utf-8<CRLF>
    Content-Length: 32<CRLF>
    Date: Fri, 07 Jul 2023 14:11:45 GMT<CRLF>
    <CRLF>
    %s""".formatted(BODY02).replace("<CRLF>\n", "\r\n");

  public static final String OUTPUT = OUTPUT01 + OUTPUT02;

  public static final Http005 INSTANCE = new Http005();

}