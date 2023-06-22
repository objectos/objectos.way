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

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

final class SelectorThread extends Thread {

  final ServerSocketChannel channel;

  private final SelectionKey acceptKey;

  private final SelectorThreadAdapter adapter;

  private Throwable error;

  private final Selector selector;

  SelectorThread(SelectorThreadAdapter adapter,
                 ServerSocketChannel channel,
                 Selector selector,
                 SelectionKey acceptKey) {
    super("http-sel");

    this.adapter = adapter;

    this.channel = channel;

    this.selector = selector;

    this.acceptKey = acceptKey;
  }

  public static SelectorThread create(
      SelectorThreadAdapter adapter, SocketAddress address)
      throws IOException {
    var channel = ServerSocketChannel.open();

    channel.bind(address);

    channel.configureBlocking(false);

    Selector selector;
    selector = null;

    SelectionKey acceptKey;
    acceptKey = null;

    try {
      selector = Selector.open();

      acceptKey = channel.register(selector, SelectionKey.OP_ACCEPT);
    } catch (IOException e) {
      try {
        channel.close();
      } catch (IOException sup) {
        e.addSuppressed(sup);
      }

      throw e;
    }

    return new SelectorThread(adapter, channel, selector, acceptKey);
  }

  @Override
  public final void run() {
    while (!isInterrupted()) {
      int updateCount;
      updateCount = 0;

      try {
        updateCount = selector.select();

      } catch (IOException e) {
        error = e;

        break;
      }

      if (updateCount == 0) {
        continue;
      }

      if (updateCount != 1) {
        throw new UnsupportedOperationException("Implement me");
      }

      Set<SelectionKey> selectedKeys;
      selectedKeys = selector.selectedKeys();

      if (!selectedKeys.contains(acceptKey)) {
        throw new UnsupportedOperationException("Implement me");
      }

      try {
        SocketChannel socketChannel;
        socketChannel = channel.accept();

        adapter.acceptSocketChannel(socketChannel);
      } catch (IOException e) {
        error = e;

        break;
      } finally {
        selectedKeys.remove(acceptKey);
      }
    }

    if (selector.isOpen()) {
      error = close(error, selector);
    }

    if (channel.isOpen()) {
      error = close(error, channel);
    }
  }

  final boolean isOpen() {
    return channel.isOpen();
  }

  private Throwable close(Throwable original, AutoCloseable c) {
    Throwable rethrow = original;

    if (c != null) {
      try {
        c.close();
      } catch (Exception e) {
        if (rethrow == null) {
          rethrow = e;
        } else {
          rethrow.addSuppressed(e);
        }
      }
    }

    return rethrow;
  }

}