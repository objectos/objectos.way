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
package objectos.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import objectos.fs.Directory;
import objectos.fs.LocalFs;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.NoteSink;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;

final class IncrementalBackup extends AbstractClientJob<UnmodifiableList<RegularFile>> {

  private static final byte _COPY = 0;

  private static final byte _FIRST_LOG = 1;

  private static final byte _NEXT_LOG = 2;

  private static final byte _PURGE = 3;

  private static final Note1<String> EBINARY_LOG;

  private static final Note1<Exception> EFAILED;

  private static final Note1<String> ELOGBIN_BASENAME;

  private static final Note2<LoginPath, String> ESTART;

  private static final Note1<Long> ESUCCESS;

  static {
    Class<?> source;
    source = IncrementalBackup.class;

    EBINARY_LOG = Note1.debug(source, "Binary log");

    EFAILED = Note1.error(source, "Failed");

    ELOGBIN_BASENAME = Note1.debug(source, "Logbin basename");

    ESTART = Note2.info(source, "Start");

    ESUCCESS = Note1.info(source, "Success");
  }

  private static final byte IO_CLOSE = 0;

  private static final byte IO_COPY = 1;

  private static final byte IO_OPEN = 2;

  private String basename;

  private UnmodifiableList<String> binaryLogs;

  private int binaryLogsIndex;

  private String binlogPathName;

  private String binlogSimpleName;

  private final byte[] byteArray;

  private volatile boolean copyMore;

  private InputStream inputStream;

  private final NoteSink logger;

  private final LoginPath loginPath;

  private GZIPOutputStream outputStream;

  private String prefix;

  private final GrowableList<RegularFile> result = new GrowableList<>();

  private long startTime;

  private final Directory targetDirectory;

  private RegularFile targetFile;

  private ResolvedPath targetPath;

  IncrementalBackup(Client worker,
                    LoginPath loginPath,
                    Directory targetDirectory) {
    super(worker);

    byteArray = worker.getByteArray();

    logger = worker.getLogger();

    this.loginPath = loginPath;

    this.targetDirectory = targetDirectory;
  }

  @Override
  final byte execute(byte state) {
    switch (state) {
      case _COPY:
        return executeCopy();
      case _FIRST_LOG:
        return executeFirstLog();
      case _NEXT_LOG:
        return executeNextLog();
      case _PURGE:
        return executePurge();
      default:
        throw new UnsupportedOperationException("Implement me: state=" + state);
    }
  }

  @Override
  final void executeIoTask(byte task) throws IOException {
    switch (task) {
      case IO_CLOSE:
        ioClose();
        break;
      case IO_COPY:
        ioCopy();
        break;
      case IO_OPEN:
        ioOpen();
        break;
      default:
        throw new UnsupportedOperationException("Implement me: task=" + task);
    }
  }

  @Override
  final byte executeStart() {
    startTime = System.currentTimeMillis();

    logger.send(ESTART, loginPath, targetDirectory.getPath());

    result.clear();

    return toExecuteStatement(
        _FIRST_LOG,

        "SELECT @@log_bin_basename;",

        "SHOW BINARY LOGS;",

        "FLUSH BINARY LOGS;"
    );
  }

  @Override
  final UnmodifiableList<RegularFile> getResultImpl(
                                                    IOException exception, UnmodifiableList<String> stderr, UnmodifiableList<String> stdout)
                                                                                                                                             throws IOException {
    if (exception == null) {
      long totalTime;
      totalTime = System.currentTimeMillis() - startTime;

      logger.send(ESUCCESS, totalTime);

      return result.toUnmodifiableList();
    }

    else {
      logger.send(EFAILED, exception);

      throw exception;
    }
  }

  private byte executeCopy() {
    if (copyMore) {
      return toIo(IO_COPY, _COPY);
    }

    else {
      result.add(targetFile);

      logger.send(EBINARY_LOG, binlogPathName);

      return toIo(IO_CLOSE, _NEXT_LOG);
    }
  }

  private byte executeFirstLog() {
    UnmodifiableList<String> executeStatementResult;
    executeStatementResult = getSubTaskResult();

    if (executeStatementResult.isEmpty()) {
      // log error

      return toFinally();
    }

    basename = executeStatementResult.get(0);

    logger.send(ELOGBIN_BASENAME, basename);

    binaryLogs = executeStatementResult;

    binaryLogsIndex = 1;

    prefix = createBackupPrefix();

    return executeNextLog();
  }

  private byte executeNextLog() {
    if (binaryLogsIndex < binaryLogs.size()) {
      String row;
      row = binaryLogs.get(binaryLogsIndex);

      binaryLogsIndex++;

      binlogSimpleName = getBinlogSimpleName(row);

      int dot;
      dot = binlogSimpleName.lastIndexOf('.');

      if (dot < 0) {
        throw new UnsupportedOperationException("Implement me");
      }

      String extension;
      extension = binlogSimpleName.substring(dot);

      binlogPathName = basename + extension;

      return toIo(IO_OPEN, _COPY);
    }

    else {
      return toExecuteStatement(
          _PURGE,

          "SHOW MASTER STATUS;"
      );
    }
  }

  private byte executePurge() {
    UnmodifiableList<String> masterStatus;
    masterStatus = getSubTaskResult();

    if (masterStatus.isEmpty()) {
      throw new UnsupportedOperationException("Implement me: masterStatus is empty");
    }

    String masterStatusRow0;
    masterStatusRow0 = masterStatus.get(0);

    String currentBinLog;
    currentBinLog = getBinlogSimpleName(masterStatusRow0);

    return toExecuteStatement(
        toFinally(),

        Mysql.sql("PURGE BINARY LOGS TO '", currentBinLog, "'")
    );
  }

  private String getBinlogSimpleName(String row) {
    StringBuilder currentBinLogBuilder;
    currentBinLogBuilder = new StringBuilder();

    outer: for (char c : row.toCharArray()) {
      switch (c) {
        case '\t':
          break outer;
        default:
          currentBinLogBuilder.append(c);
      }
    }

    return currentBinLogBuilder.toString();
  }

  private void ioClose() throws IOException {
    closeTwo(inputStream, outputStream);
  }

  private void ioCopy() throws IOException {
    int count;
    count = inputStream.read(byteArray);

    if (count > 0) {
      outputStream.write(byteArray, 0, count);

      copyMore = true;
    } else {
      targetFile = targetPath.toRegularFile();

      copyMore = false;
    }
  }

  private void ioOpen() throws IOException {
    targetPath = targetDirectory.resolve(prefix + '-' + binlogSimpleName + ".gz");

    OutputStream out;
    out = targetPath.openOutputStream();

    outputStream = new GZIPOutputStream(out);

    RegularFile regularFile;
    regularFile = LocalFs.getRegularFile(binlogPathName);

    inputStream = regularFile.openInputStream();

    copyMore = false;

    ioCopy();
  }

  private byte toExecuteStatement(byte onReady, String... statements) {
    return toSubTask(
        new ExecuteStatement(client, loginPath, statements, Mysql.skipColumnNames()),

        onReady
    );
  }

}
