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
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import objectos.util.UnmodifiableList;

final class TestableSocketChannel extends SocketChannel {

  private byte[] requestBytes;

  private int requestIndex = -1;

  protected TestableSocketChannel() {
    super(null);
  }

  @Override
  public SocketChannel bind(SocketAddress local) throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  public final void clear() {
    requestBytes = null;

    requestIndex = -1;
  }

  @Override
  public boolean connect(SocketAddress remote) throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public boolean finishConnect() throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public SocketAddress getLocalAddress() throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public <T> T getOption(SocketOption<T> name) throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public SocketAddress getRemoteAddress() throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public boolean isConnected() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public boolean isConnectionPending() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final int read(ByteBuffer dst) throws IOException {
    int remaining;
    remaining = dst.remaining();

    int bytesToPut;
    bytesToPut = Math.min(requestBytes.length - requestIndex, remaining);

    if (bytesToPut == 0) {
      return -1;
    }

    dst.put(requestBytes, requestIndex, bytesToPut);

    requestIndex += bytesToPut;

    return bytesToPut;
  }

  @Override
  public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public <T> SocketChannel setOption(SocketOption<T> name, T value) throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  public final void setRequest(UnmodifiableList<String> request) {
    String text;
    text = request.join(Http.CRLF);

    requestBytes = text.getBytes(StandardCharsets.UTF_8);

    requestIndex = 0;
  }

  @Override
  public SocketChannel shutdownInput() throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public SocketChannel shutdownOutput() throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public Socket socket() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public Set<SocketOption<?>> supportedOptions() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public int write(ByteBuffer src) throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  protected void implCloseSelectableChannel() throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  protected void implConfigureBlocking(boolean block) throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

}