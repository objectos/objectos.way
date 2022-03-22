/*
 * Copyright (C) 2021-2022 Objectos Software LTDA.
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
package br.com.objectos.logging;

public class CodeGenerator {

  public static void main(String[] args) {
    System.out.println(Template.VALUE);

    System.out.println(
        Template.replace(
            "DEBUG", "ERROR",
            "debug", "error"
        )
    );

    System.out.println(
        Template.replace(
            "DEBUG", "INFO",
            "debug", "info"
        )
    );

    System.out.println(
        Template.replace(
            "DEBUG", "TRACE",
            "debug", "trace"
        )
    );

    System.out.println(
        Template.replace(
            "DEBUG", "WARN",
            "debug", "warn"
        )
    );
  }

}