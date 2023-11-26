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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;
import objectos.core.io.Charsets;
import objectos.core.io.Read;
import objectos.core.io.Resource;
import objectos.fs.Directory;
import objectos.fs.JavaIoTmpdir;
import objectos.fs.LocalFs;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.fs.SimplePathNameVisitor;
import objectos.fs.zip.Unzip;
import objectos.fs.zip.Zip;
import org.testng.collections.Sets;

public class MysqlFs {

  final Directory backup;

  final Directory binlog;

  final Directory data;

  final ResolvedPath errorLogPath;

  final Directory etc;

  final ResolvedPath generalLogPath;

  final Directory log;

  final Directory root;

  final GlobalOption socketOption;

  final ResolvedPath socketPath;

  MysqlFs(Directory root) throws IOException {
    this.root = root;

    binlog = root.createDirectory("binlog");

    backup = root.createDirectory("backup");

    data = root.createDirectory("data");

    etc = root.createDirectory("etc");

    log = root.createDirectory("log");

    errorLogPath = root.resolve("mysqld.err");

    generalLogPath = root.resolve("mysqld.log");

    socketPath = root.resolve("mysqld.sock");

    socketOption = Mysql.socket(socketPath);
  }

  public final void dispose() {
    try {
      root.deleteContents();
    } catch (IOException e) {}

    try {
      root.delete();
    } catch (IOException e) {}
  }

  public final RegularFile getErrorLogFile() throws IOException {
    return errorLogPath.toRegularFile();
  }

  public final void printErrorLog() {
    printLog(errorLogPath);
  }

  public final void printGeneralLog() {
    printLog(generalLogPath);
  }

  public final RegularFile waitForErrorLogFile() throws IOException {
    while (!errorLogPath.exists()) {
      // noop
    }

    return errorLogPath.toRegularFile();
  }

  public final void zip() {
    try {
      Directory target;
      target = JavaIoTmpdir.get();

      if (backup.exists()) {
        Zip.zip(
            backup,

            target.resolve("backup.zip"),

            Zip.recursePaths(),

            "."
        );
      }

      Zip.zip(
          data,

          target.resolve("data.zip"),

          Zip.recursePaths(),

          "."
      );

      Zip.zip(
          etc,

          target.resolve("etc.zip"),

          Zip.recursePaths(),

          "."
      );
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  final ConfigurationFile getConfigurationFile(Charset charset) throws IOException {
    RegularFile file;
    file = etc.getRegularFile("my.cnf");

    return Mysql.configurationFile(file, charset);
  }

  final LoginPathFile getLoginPathFile() throws IOException {
    RegularFile file;
    file = etc.getRegularFile("login.cnf");

    return Mysql.loginPathFile(file);
  }

  final void restoreBackup(Release release) {
    restore(release, "backup.zip", backup);
  }

  final String restoreBackup(String fileName) throws IOException {
    RegularFile file;
    file = backup.getRegularFile(fileName);

    return Read.string(file, Charset.defaultCharset());
  }

  final void restoreData0(Release release) {
    restore(release, "data0.zip", data);
  }

  final void restoreData1(Release release) {
    restore(release, "data1.zip", data);
  }

  final void restoreEtc(Release release) {
    restore(release, "etc.zip", etc);

    try {
      RegularFile login;
      login = etc.getRegularFile("login.cnf");

      File file;
      file = login.toFile();

      Path path;
      path = file.toPath();

      Set<PosixFilePermission> p0600;
      p0600 = Sets.newHashSet();

      p0600.add(PosixFilePermission.OWNER_READ);

      p0600.add(PosixFilePermission.OWNER_WRITE);

      Files.setPosixFilePermissions(path, p0600);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  final int size(Directory directory) throws IOException {
    RegularFileCount count;
    count = new RegularFileCount();

    directory.visitContents(count);

    return count.count;
  }

  private void printLog(ResolvedPath log) {
    try {
      log.acceptPathNameVisitor(Printer.INSTANCE, null);
    } catch (IOException e) {
      throw new AssertionError("IOException", e);
    }
  }

  private void restore(Release release, String resourceName, Directory target) {
    String releaseName;
    releaseName = release.name();

    String prefix;
    prefix = releaseName.replace('_', '-');

    Resource resource;
    resource = Resource.getResource(prefix + '/' + resourceName);

    try {
      RegularFile zip;
      zip = LocalFs.getRegularFile(resource);

      Unzip.unzip(target, zip);
    } catch (IOException e) {
      throw new AssertionError("IOException", e);
    }
  }

  private static class Printer extends SimplePathNameVisitor<Void, Void> {
    static final Printer INSTANCE = new Printer();

    @Override
    public final Void visitRegularFile(RegularFile file, Void p) throws IOException {
      String s;
      s = Read.string(file, Charsets.utf8());

      System.out.println(s);

      return null;
    }
  }

}