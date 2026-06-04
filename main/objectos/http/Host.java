/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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

record Host(Handler handler, String name) {

  public final ResponsePojo handle(Request request) {
    final Result result;
    result = handler.handle(request);

    return switch (result) {
      case Content content -> of(content);

      case ContentProvider provider -> {
        final Content content;
        content = provider.toContent();

        if (content == null) {
          throw new IllegalArgumentException("%s provided a null `Content` instance".formatted(provider));
        }

        yield of(content);
      }

      case RedirectPojo(HttpStatus0 status, String location) -> {
        final ResponseBuilder builder;
        builder = new ResponseBuilder();

        builder.status(status);

        builder.date();

        builder.header(HttpHeaderName.CONTENT_LENGTH, "0");

        builder.header(HttpHeaderName.LOCATION, location);

        yield builder.build();
      }

      case ResponsePojo response -> response;

      default -> throw new UnsupportedOperationException("Implement me :: " + result);
    };
  }

  private ResponsePojo of(Content content) {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.status(HttpStatus.OK);

    builder.date();

    builder.send(content);

    return builder.build();
  }

  public final boolean test(String hostValue) {
    return name.equals(hostValue);
  }

}
