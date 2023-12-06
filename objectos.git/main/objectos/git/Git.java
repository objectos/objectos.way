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
package objectos.git;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import objectos.concurrent.CpuArray;
import objectos.concurrent.CpuTask;
import objectos.concurrent.CpuWorker;
import objectos.lang.object.Check;

final class Git {

  /*

  @startuml

  ' config

  skinparam shadowing false

  ' actors

  actor :CI Server: as CI

  ' usecases

  usecase Copy as "Copy & transform"

  note bottom of Copy : Copies a commit from a source\nrepository to a target repository.\nAllows filtering and content\ntransformations.

  usecase Materialize as "Materialize a repository"

  note bottom of Materialize : Similar to a 'git clone --depth 1'\nbut skips the creation of the '.git' dir

  ' rels

  CI --> Copy
  CI --> Materialize

  @enduml

   */

  public static final RefName MASTER = RefName.MASTER;

  static final int DEFAULT_BUFFER_SIZE = 4096;

  static final char LF = '\n';

  static final byte NULL = 0x0;

  static final String SHA1 = "SHA-1";

  static final char SP = ' ';

  static final byte UTF8__lineFeed = 0x0A;

  static final byte UTF8__space = 0x20;

  private static final Random RANDOM = new SecureRandom();

  private Git() {}

  static int checkBufferSize(int size) {
    Check.argument(size >= 64, "bufferSize minimum value is 64 bytes");

    return size;
  }

  static Throwable close(Throwable primary, AutoCloseable closeable) {
    Throwable result;
    result = primary;

    if (closeable != null) {
      try {
        closeable.close();
      } catch (Throwable e) {
        if (result != null) {
          result.addSuppressed(e);
        } else {
          result = e;
        }
      }
    }

    return result;
  }

  static boolean isHexDigit(char c) {
    switch (c) {
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':

      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f':

      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
        return true;
      default:
        return false;
    }
  }

  static boolean matches(ByteBuffer byteBuffer, byte[] prefix) {
    int remaining;
    remaining = byteBuffer.remaining();

    int length;
    length = prefix.length;

    if (remaining < length) {
      return false;
    }

    byte[] array;
    array = byteBuffer.array();

    byte b;

    byte expected;

    int initialPosition;
    initialPosition = byteBuffer.position();

    int position;
    position = initialPosition;

    for (int i = 0; i < length; i++, position++) {
      b = array[position];

      expected = prefix[i];

      if (b != expected) {
        return false;
      }
    }

    byteBuffer.position(initialPosition + length);

    return true;
  }

  static <E> Deque<E> newArrayDeque(int capacity) {
    return new ArrayDeque<E>(capacity);
  }

  static IOException newNoResultException() {
    return new IOException("Expected to have a result but no result was produced");
  }

  static int parseInt(char c) {
    switch (c) {
      case '0':
        return 0;
      case '1':
        return 1;
      case '2':
        return 2;
      case '3':
        return 3;
      case '4':
        return 4;
      case '5':
        return 5;
      case '6':
        return 6;
      case '7':
        return 7;
      case '8':
        return 8;
      case '9':
        return 9;
      default:
        throw new NumberFormatException(c + " is not a digit");
    }
  }

  static int randomIndex(CpuArray array) {
    return RANDOM.nextInt(array.size());
  }

  static int randomIndex(Object[] a) {
    return RANDOM.nextInt(a.length);
  }

  static void submitTask(CpuArray cpuArray, CpuTask task) {
    int index;
    index = Git.randomIndex(cpuArray);

    outer: while (true) {
      for (int i = index, size = cpuArray.size(); i < size; i++) {
        CpuWorker worker;
        worker = cpuArray.get(i);

        if (worker.offer(task)) {
          break outer;
        }
      }

      for (int i = 0; i < index; i++) {
        CpuWorker worker;
        worker = cpuArray.get(i);

        if (worker.offer(task)) {
          break outer;
        }
      }
    }
  }

}
