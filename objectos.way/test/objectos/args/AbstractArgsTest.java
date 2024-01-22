/*
 * Copyright (C) 2024 Objectos Software LTDA.
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
package objectos.args;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

public abstract class AbstractArgsTest {

  final String[] args(String... values) {
    return Arrays.copyOf(values, values.length);
  }

  final void assertMessage(CommandLineException exception, String expectedMessage) {
    try (PrintStream printer = new ThisStream()) {
      exception.printMessage(printer);

      assertEquals(printer.toString(), expectedMessage);
    }
  }

  final void parse(Option<?> option, String[] args) throws CommandLineException {
    CommandLine cli;
    cli = new CommandLine("Test", option);

    cli.parse(args);
  }

  private static class ThisStream extends PrintStream {

    public ThisStream() {
      super(new ByteArrayOutputStream());
    }

    @Override
    public final String toString() {
      ByteArrayOutputStream byteStream;
      byteStream = (ByteArrayOutputStream) out;

      byte[] bytes;
      bytes = byteStream.toByteArray();

      return new String(bytes);
    }

  }

}