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
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import objectos.fs.PathName;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.fs.SimplePathNameVisitor;
import objectos.notes.Note1;
import objectos.notes.Note2;

/**
 * Resolves a Git reference.
 *
 * @since 1
 */
final class ResolveRef extends AbstractGitEngineTask {

  private static final Note2<Repository, RefName> ESTART;

  private static final Note1<MaybeObjectId> ESUCCESS;

  static {
    Class<?> source;
    source = ResolveRef.class;

    ESTART = Note2.debug(source, "Start");

    ESUCCESS = Note1.debug(source, "Success");
  }

  private static final byte _CLOSE = 1;

  private static final byte _DECODE = 2;

  private static final byte _LOOSE = 3;

  private static final byte _MAYBE_PACKED = 4;

  private static final byte _NULL_OBJECT = 5;

  private static final byte _PACKED = 6;

  private static final byte _PARSE_PACKED = 7;

  private static final byte IO_CLOSE = 1;

  private static final byte IO_READ = 2;

  private static final byte IO_RESOLVE_LOOSE = 3;

  private static final byte IO_RESOLVE_PACKED = 4;

  /*

  @startuml

  hide empty description
  skinparam shadowing false

  [*] --> IO : ioAction=resolveLoose\lioReady=MAYBE_LOOSE

  FINALLY --> [*]

  IO --> IO_WAIT

  IO_WAIT --> MAYBE_LOOSE
  IO_WAIT --> MAYBE_PACKED

  MAYBE_LOOSE --> OBJECT_ID
  MAYBE_LOOSE --> IO : ioAction=resolvePacked\lioReady=MAYBE_PACKED

  MAYBE_PACKED --> NULL
  MAYBE_PACKED --> PARSE

  OBJECT_ID --> FINALLY

  PARSE --> OBJECT_ID
  PARSE --> IO : ioAction=readPacked\lioReady=PARSE
  PARSE --> NULL

  NULL --> FINALLY

  @enduml

  */

  private FileChannel channel;

  private ByteBuffer channelBuffer;

  private long channelReadCount;

  private long channelReadLimit;

  private CharsetDecoder decoder;

  private CharBuffer decoderBuffer;

  private byte decodeReady;

  private PackedParser parser;

  private RefName ref;

  private Repository repository;

  private StringBuilder stringBuilder;

  ResolveRef(GitInjector injector) {
    super(injector);

  }

  public final void setInput(Repository repository, RefName ref) {
    checkSetInput();

    this.repository = repository;

    this.ref = ref;
  }

  @Override
  final byte errorState() {
    return _CLOSE;
  }

  @Override
  final byte execute(byte state) {
    switch (state) {
      case _CLOSE:
        return executeClose();
      case _DECODE:
        return executeDecode();
      case _LOOSE:
        return executeLoose();
      case _MAYBE_PACKED:
        return executeMaybePacked();
      case _NULL_OBJECT:
        return executeNullObject();
      case _PACKED:
        return executePacked();
      case _PARSE_PACKED:
        return executeParsePacked();
      default:
        return super.execute(state);
    }
  }

  @Override
  final byte executeFinally() {
    channel = null;

    channelBuffer = injector.putByteBuffer(channelBuffer);

    channelReadCount = 0;

    channelReadLimit = 0;

    decoder = injector.putDecoder(decoder);

    decoderBuffer = injector.putCharBuffer(decoderBuffer);

    decodeReady = 0;

    parser = null;

    ref = null;

    repository = null;

    stringBuilder = injector.putStringBuilder(stringBuilder);

    return super.executeFinally();
  }

  @Override
  final void executeIo(byte ioTask) throws GitStubException, IOException {
    switch (ioTask) {
      case IO_CLOSE:
        ioClose();
        break;
      case IO_READ:
        ioRead();
        break;
      case IO_RESOLVE_LOOSE:
        ioResolveLoose();
        break;
      case IO_RESOLVE_PACKED:
        ioResolvePacked();
        break;
      default:
        super.executeIo(ioTask);
        break;
    }
  }

  @Override
  final byte executeStart() {
    super.executeStart();

    channelBuffer = injector.getByteBuffer();

    Charset charset;
    charset = repository.getCharset();

    decoder = injector.getDecoder(charset);

    decoderBuffer = injector.getCharBuffer();

    stringBuilder = injector.getStringBuilder();

    send(ESTART, repository, ref);

    return toIo(
        IO_RESOLVE_LOOSE, toDecode(_LOOSE), _CLOSE
    );
  }

  final void openFile(RegularFile file) throws IOException {
    channel = file.openReadChannel();

    channel.position(0);

    channelBuffer.clear();

    channelReadCount = 0;

    channelReadLimit = channel.size();

    ioRead();
  }

  private byte executeClose() {
    if (channel != null) {
      return toIo(IO_CLOSE, _FINALLY, _FINALLY);
    } else {
      return _FINALLY;
    }
  }

  private byte executeDecode() {
    if (decoderBuffer.position() > 0) {
      decoderBuffer.compact();
    }

    int limit;
    limit = channelBuffer.limit();

    int position;
    position = channelBuffer.position();

    int preventOverflowLimit;
    preventOverflowLimit = position + decoderBuffer.remaining();

    boolean endOfInput;
    endOfInput = isChannelEof();

    if (preventOverflowLimit < limit) {
      channelBuffer.limit(preventOverflowLimit);

      endOfInput = false;
    }

    CoderResult coderResult;
    coderResult = decoder.decode(channelBuffer, decoderBuffer, endOfInput);

    channelBuffer.limit(limit);

    if (!coderResult.isUnderflow()) {
      return toStubException("codeResult != underflow");
    }

    decoderBuffer.flip();

    return decodeReady;
  }

  private byte executeLoose() {
    stringBuilder.append(decoderBuffer);

    if (!isChannelEof()) {
      return toIo(
          IO_READ, toDecode(_LOOSE), _CLOSE
      );
    }

    try {
      ObjectId objectId;
      objectId = ObjectId.parse(stringBuilder, 0);

      setResult(objectId);

      return _CLOSE;
    } catch (InvalidObjectIdFormatException e) {
      return toError(e);
    }
  }

  private byte executeMaybePacked() {
    return toIo(
        IO_RESOLVE_PACKED,

        toDecode(_PACKED),

        _CLOSE
    );
  }

  private byte executeNullObject() {
    setResult(EmptyObjectId.INSTANCE);

    return _FINALLY;
  }

  private byte executePacked() {
    parser = PackedParser.START;

    stringBuilder.setLength(0);

    return execute(_PARSE_PACKED);
  }

  private byte executeParsePacked() {
    boolean found;
    found = false;

    String id;
    id = "";

    IOException exception;
    exception = null;

    outer: //
    while (decoderBuffer.hasRemaining()) {
      char c;
      c = decoderBuffer.get();

      switch (parser) {
        case ERROR:
          exception = new IOException("Failed to parse packed-refs");

          break outer;
        case HEADER:
          if (c == Git.LF) {
            parser = PackedParser.ID;
          }

          break;
        case ID:
          if (c == Git.SP) {
            id = stringBuilder.toString();

            stringBuilder.setLength(0);

            parser = PackedParser.NAME;
          }

          else if (Git.isHexDigit(c)) {
            stringBuilder.append(c);
          }

          else {
            parser = PackedParser.ERROR;
          }

          break;
        case NAME:
          if (c == Git.LF) {
            String name;
            name = stringBuilder.toString();

            if (ref.matches(name)) {
              found = true;

              break outer;
            }

            else {
              stringBuilder.setLength(0);

              parser = PackedParser.START;
            }
          }

          else {
            stringBuilder.append(c);
          }

          break;
        case START:
          if (c == '#') {
            parser = PackedParser.HEADER;
          }

          else if (Git.isHexDigit(c)) {
            stringBuilder.append(c);

            parser = PackedParser.ID;
          }

          else {
            parser = PackedParser.ERROR;
          }

          break;
        default:
          throw new UnsupportedOperationException("Implement me @ " + parser);
      }
    }

    if (exception != null) {
      return toError(exception);
    }

    if (found) {
      try {
        ObjectId objectId;
        objectId = ObjectId.parse(id);

        setResult(objectId);

        return _CLOSE;
      } catch (InvalidObjectIdFormatException e) {
        return toError(e);
      }
    } else {
      return toIo(
          IO_READ,

          toDecode(_PARSE_PACKED),

          _CLOSE
      );
    }
  }

  private void ioClose() {
    close(channel);

    channel = null;
  }

  private void ioRead() throws IOException {
    if (channelBuffer.position() > 0) {
      channelBuffer.compact();
    }

    int thisReadCount;
    thisReadCount = channel.read(channelBuffer);

    if (thisReadCount > 0) {
      channelReadCount += thisReadCount;
    }

    channelBuffer.flip();
  }

  private void ioResolveLoose() throws IOException {
    ResolvedPath maybeFile;
    maybeFile = repository.resolveLoose(ref);

    maybeFile.acceptPathNameVisitor(ResolveLoose.INSTANCE, this);
  }

  private void ioResolvePacked() throws IOException {
    ResolvedPath maybeFile;
    maybeFile = repository.resolvePackedRefs();

    maybeFile.acceptPathNameVisitor(ResolvePacked.INSTANCE, this);
  }

  private boolean isChannelEof() {
    return channelReadCount >= channelReadLimit;
  }

  private void setResult(MaybeObjectId result) {
    send(ESUCCESS, result);

    super.setResult(result);
  }

  private byte toDecode(byte onReady) {
    decodeReady = onReady;

    return _DECODE;
  }

  private enum PackedParser {

    ERROR,

    HEADER,

    ID,

    NAME,

    START

  }

  private static class ResolveLoose extends SimplePathNameVisitor<Void, ResolveRef> {
    static final ResolveLoose INSTANCE = new ResolveLoose();

    @Override
    public final Void visitRegularFile(RegularFile file, ResolveRef p) throws IOException {
      p.openFile(file);

      return null;
    }

    @Override
    protected final Void defaultAction(PathName pathName, ResolveRef p) throws IOException {
      p.ioReady(_MAYBE_PACKED);

      return null;
    }
  }

  private static class ResolvePacked extends SimplePathNameVisitor<Void, ResolveRef> {
    static final ResolvePacked INSTANCE = new ResolvePacked();

    @Override
    public final Void visitRegularFile(RegularFile file, ResolveRef p) throws IOException {
      p.openFile(file);

      return null;
    }

    @Override
    protected final Void defaultAction(PathName pathName, ResolveRef p) throws IOException {
      p.ioReady(_NULL_OBJECT);

      return null;
    }
  }

}