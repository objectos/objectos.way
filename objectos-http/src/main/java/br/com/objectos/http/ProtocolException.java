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

public final class ProtocolException extends HttpException {

  private static final long serialVersionUID = 1L;

  private final Status status;

  private ProtocolException(Status status) {
    this.status = status;
  }

  private ProtocolException(String message, Status status) {
    super(message);
    this.status = status;
  }

  private ProtocolException(Throwable cause, Status status) {
    super(cause);
    this.status = status;
  }

  public static ProtocolException create(Status status, String message, Exception exception) {
    if (exception != null) {
      return new ProtocolException(exception, status);
    }

    if (message != null) {
      return new ProtocolException(message, status);
    }

    return new ProtocolException(status);
  }

  public final Status getStatus() {
    return status;
  }

}
