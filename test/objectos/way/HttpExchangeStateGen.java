/*
 * Copyright (C) 2025 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless httpuired by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.way;

final class HttpExchangeStateGen {

  private int state = 0;

  public static void main(String[] args) {
    final HttpExchangeStateGen gen;
    gen = new HttpExchangeStateGen();

    gen.value("$START");

    gen.line();

    gen.value("$READ");
    gen.value("$READ_MAX_BUFFER");
    gen.value("$READ_EOF");

    gen.line();

    gen.value("$PARSE_METHOD");
    gen.value("$PARSE_PATH");
    gen.value("$PARSE_PATH_CONTENTS0");
    gen.value("$PARSE_PATH_CONTENTS1");
    gen.value("$PARSE_QUERY");
    gen.value("$PARSE_QUERY0");
    gen.value("$PARSE_QUERY1");
    gen.value("$PARSE_QUERY_VALUE");
    gen.value("$PARSE_QUERY_VALUE0");
    gen.value("$PARSE_QUERY_VALUE1");
    gen.value("$PARSE_VERSION_0_9");
    gen.value("$PARSE_VERSION_1_1");
    gen.value("$PARSE_VERSION_OTHERS");

    gen.line();

    gen.value("$PARSE_HEADER");
    gen.value("$PARSE_HEADER_NAME");
    gen.value("$PARSE_HEADER_VALUE");
    gen.value("$PARSE_HEADER_VALUE_CONTENTS");
    gen.value("$PARSE_HEADER_VALUE_CR");
    gen.value("$PARSE_HEADER_CR");

    gen.line();

    gen.value("$PARSE_BODY");
    gen.value("$PARSE_BODY_FIXED");
    gen.value("$PARSE_BODY_FIXED_ZERO");
    gen.value("$PARSE_BODY_FIXED_BUFFER");
    gen.value("$PARSE_BODY_FIXED_BUFFER_READ");
    gen.value("$PARSE_BODY_FIXED_BUFFER_SUCCESS");
    gen.value("$PARSE_BODY_FIXED_FILE");
    gen.value("$PARSE_BODY_FIXED_FILE_BUFFER");
    gen.value("$PARSE_BODY_FIXED_FILE_READ");
    gen.value("$PARSE_BODY_FIXED_FILE_CLOSE");

    gen.line();

    gen.value("$PARSE_APP_FORM");
    gen.value("$PARSE_APP_FORM0");
    gen.value("$PARSE_APP_FORM1");
    gen.value("$PARSE_APP_FORM_VALUE");
    gen.value("$PARSE_APP_FORM_VALUE0");
    gen.value("$PARSE_APP_FORM_VALUE1");

    gen.line();

    gen.value("$DECODE_PERC");
    gen.value("$DECODE_PERC1_LOW");
    gen.value("$DECODE_PERC2_1_LOW");
    gen.value("$DECODE_PERC2_2");
    gen.value("$DECODE_PERC2_2_HIGH");
    gen.value("$DECODE_PERC2_2_LOW");
    gen.value("$DECODE_PERC3_1_LOW");
    gen.value("$DECODE_PERC3_2");
    gen.value("$DECODE_PERC3_2_HIGH");
    gen.value("$DECODE_PERC3_2_LOW");
    gen.value("$DECODE_PERC3_3");
    gen.value("$DECODE_PERC3_3_HIGH");
    gen.value("$DECODE_PERC3_3_LOW");
    gen.value("$DECODE_PERC4_1_LOW");
    gen.value("$DECODE_PERC4_2");
    gen.value("$DECODE_PERC4_2_HIGH");
    gen.value("$DECODE_PERC4_2_LOW");
    gen.value("$DECODE_PERC4_3");
    gen.value("$DECODE_PERC4_3_HIGH");
    gen.value("$DECODE_PERC4_3_LOW");
    gen.value("$DECODE_PERC4_4");
    gen.value("$DECODE_PERC4_4_HIGH");
    gen.value("$DECODE_PERC4_4_LOW");

    gen.line();

    gen.value("$BAD_REQUEST");
    gen.value("$URI_TOO_LONG");
    gen.value("$REQUEST_HEADER_FIELDS_TOO_LARGE");
    gen.value("$NOT_IMPLEMENTED");
    gen.value("$HTTP_VERSION_NOT_SUPPORTED");

    gen.line();

    gen.value("$COMMIT");
    gen.value("$WRITE");

    gen.line();

    gen.value("$REQUEST");

    gen.line();

    gen.value("$RESPONSE_HEADERS");

    gen.line();

    gen.value("$ERROR");
  }

  private void line() {
    System.out.println();
  }

  private void value(String name) {
    System.out.printf("static final byte %s = %d;%n", name, state++);
  }

}