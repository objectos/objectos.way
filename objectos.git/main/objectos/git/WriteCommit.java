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
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.security.MessageDigest;
import java.util.zip.Deflater;
import objectos.core.io.Charsets;
import objectos.fs.Directory;
import objectos.fs.NotRegularFileException;
import objectos.fs.PathNameVisitor;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.notes.Note1;

final class WriteCommit extends AbstractGitEngineTask {

  private static final Note1<MutableCommit> ESTART;

  private static final Note1<ObjectId> ESUCCESS;

  static {
    Class<?> source;
    source = WriteCommit.class;

    ESTART = Note1.debug(source, "Start");

    ESUCCESS = Note1.debug(source, "Success");
  }

  static final byte _ASSEMBLE = 1;

  static final byte _CONTENTS = 2;

  static final byte _ENCODE = 3;

  private static final byte IO_WRITE_OBJECT = 1;

  private ByteArrayWriter body;

  private CharBuffer charBuffer;

  private MutableCommit commit;

  private ByteArrayWriter compressed;

  private ByteArrayWriter contents;

  private ByteBuffer dataBuffer;

  private Deflater deflater;

  private CharsetEncoder encoder;

  private MessageDigest messageDigest;

  private ObjectId objectId;

  private Repository repository;

  private StringBuilder stringBuilder;

  WriteCommit(GitInjector injector) {
    super(injector);
  }

  public final void setInput(Repository repository, MutableCommit commit) {
    checkSetInput();

    this.repository = repository;

    this.commit = commit;
  }

  @Override
  final byte execute(byte state) {
    switch (state) {
      case _ASSEMBLE:
        return executeAssemble();
      case _CONTENTS:
        return executeContents();
      case _ENCODE:
        return executeEncode();
      default:
        return super.execute(state);
    }
  }

  @Override
  final byte executeFinally() {
    body = injector.putByteArrayWriter(body);

    charBuffer = injector.putCharBuffer(charBuffer);

    compressed = injector.putByteArrayWriter(compressed);

    contents = injector.putByteArrayWriter(contents);

    dataBuffer = injector.putByteBuffer(dataBuffer);

    deflater = injector.putDeflater(deflater);

    encoder = injector.putEncoder(encoder);

    messageDigest = injector.putMessageDigest(messageDigest);

    stringBuilder = injector.putStringBuilder(stringBuilder);

    return super.executeFinally();
  }

  @Override
  final void executeIo(byte ioTask) throws GitStubException, IOException {
    switch (ioTask) {
      case IO_WRITE_OBJECT:
        ioWriteObject();
        break;
      default:
        super.executeIo(ioTask);
        break;
    }
  }

  @Override
  final byte executeStart() {
    super.executeStart();

    send(ESTART, commit);

    stringBuilder = injector.getStringBuilder();

    IllegalArgumentException maybe;
    maybe = commit.validate(stringBuilder);

    if (maybe != null) {
      return toError(maybe);
    } else {
      body = injector.getByteArrayWriter();

      charBuffer = injector.getCharBuffer();

      compressed = injector.getByteArrayWriter();

      contents = injector.getByteArrayWriter();

      dataBuffer = injector.getByteBuffer();

      deflater = injector.getDeflater();

      Charset charset;
      charset = repository.getCharset();

      encoder = injector.getEncoder(charset);

      messageDigest = injector.getMessageDigest(Git.SHA1);

      return _ASSEMBLE;
    }
  }

  final void setResult() {
    setResult(objectId);

    send(ESUCCESS, objectId);
  }

  final void write(ResolvedPath notFound) throws IOException {
    notFound.createParents();

    compressed.writeTo(notFound);

    setResult();
  }

  private byte executeAssemble() {
    commit.acceptStringBuilder(stringBuilder);

    return _ENCODE;
  }

  private byte executeContents() {
    contents.write(ObjectKind.COMMIT.headerPrefix);

    int objectSize;
    objectSize = body.size();

    String objectSizeString;
    objectSizeString = Integer.toString(objectSize, 10);

    byte[] objectSizeBytes;
    objectSizeBytes = objectSizeString.getBytes(Charsets.utf8());

    contents.write(objectSizeBytes);

    contents.write(Git.NULL);

    contents.write(body);

    objectId = contents.computeObjectId(messageDigest);

    contents.deflate(deflater, compressed);

    return toIo(IO_WRITE_OBJECT, _FINALLY, _FINALLY);
  }

  private byte executeEncode() {
    String s;
    s = stringBuilder.toString();

    char[] charArray;
    charArray = s.toCharArray();

    int encodeOffset;
    encodeOffset = 0;

    int encodeRemaining;
    encodeRemaining = charArray.length;

    while (encodeRemaining > 0) {
      int thisEncodeLength;
      thisEncodeLength = Math.min(charBuffer.remaining(), encodeRemaining);

      charBuffer.put(charArray, encodeOffset, thisEncodeLength);

      encodeOffset += thisEncodeLength;

      encodeRemaining -= thisEncodeLength;

      boolean endOfInput;
      endOfInput = encodeRemaining == 0;

      charBuffer.flip();

      CoderResult coderResult;
      coderResult = encoder.encode(charBuffer, dataBuffer, endOfInput);

      if (!coderResult.isUnderflow()) {
        return toStubException("coderResult is error: " + coderResult);
      }

      charBuffer.compact();

      dataBuffer.flip();

      body.write(dataBuffer);

      dataBuffer.compact();
    }

    return _CONTENTS;
  }

  private void ioWriteObject() throws IOException {
    ResolvedPath loose;
    loose = repository.resolveLooseObject(objectId);

    loose.acceptPathNameVisitor(Resolver.INSTANCE, this);
  }

  private enum Resolver implements PathNameVisitor<Void, WriteCommit> {
    INSTANCE;

    @Override
    public final Void visitDirectory(Directory directory, WriteCommit p) throws IOException {
      throw new NotRegularFileException(directory);
    }

    @Override
    public final Void visitNotFound(ResolvedPath notFound, WriteCommit p) throws IOException {
      p.write(notFound);

      return null;
    }

    @Override
    public final Void visitRegularFile(RegularFile file, WriteCommit p) throws IOException {
      p.setResult();

      return null;
    }
  }

}