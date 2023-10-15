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
package objectox.css;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import objectox.lang.Check;

public class ConstantPool {

  static class Builder {

    private final String binaryName;

    byte[] bytes;

    int bytesIndex;

    int cpCount;

    public Builder(String binaryName) {
      this.binaryName = Check.notNull(binaryName, "binaryName == null");
    }

    public final ConstantPool build() throws IOException {
      throw new UnsupportedOperationException("Implement me");
    }

    final void loadResource() throws IOException {
      String resourceName;
      resourceName = binaryName.replace('.', '/');

      resourceName += ".class";

      ClassLoader loader;
      loader = ClassLoader.getSystemClassLoader();

      InputStream in;
      in = loader.getResourceAsStream(resourceName);

      if (in == null) {
        throw new FileNotFoundException("""
        Could not find .class resource for %s
        """.formatted(binaryName));
      }

      try (in; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        in.transferTo(out);

        bytes = out.toByteArray();
      }
    }

    final void verifyMagic() throws IOException {
      if (bytes.length < 4) {
        throw invalidMagic();
      }

      int magic;
      magic = readU4();

      if (magic != 0xCAFEBABE) {
        throw invalidMagic();
      }
    }

    final void parseConstantPoolCount() {
      // skip minor/major
      bytesIndex += 4;

      cpCount = readU2();
    }

    private int readU2() {
      byte b1;
      b1 = nextByte();

      byte b0;
      b0 = nextByte();

      return Bytes.intValue(b0, b1);
    }

    private int readU4() {
      byte b3;
      b3 = nextByte();

      byte b2;
      b2 = nextByte();

      byte b1;
      b1 = nextByte();

      byte b0;
      b0 = nextByte();

      return Bytes.intValue(b0, b1, b2, b3);
    }

    private byte nextByte() {
      return bytes[bytesIndex++];
    }

    private IOException invalidMagic() {
      return new InvalidConstantPoolException("Magic number not found");
    }

  }

}