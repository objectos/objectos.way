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

final class TestingMysql {

  /*
  
  @startmindmap
  
  *_ Test cases

  **:**Test case 09**
  ----
  IncrementalRestore use-case.
  ----
  # use test case 08
  # verify current checksum != inc0 checksum
  # inc0 restore
  # verify current checksum == inc0 checksum
  # verify current checksum != inc1 checksum
  # inc1 restore
  # verify current checksum == inc1 checksum;
  *** Client
  *** IncrementalRestore
  *** Mysql

  **:**Test case 08**
  ----
  FullRestore use-case.
  ----
  # use test case 07
  # verify current checksum != full checksum
  # full restore
  # verify current checksum == full checksum;
  *** Client
  *** FullRestore
  *** Mysql

  **:**Test case 07**
  ----
  IncrementalBackup use-case.
  ----
  # use test case 06
  # delete some data
  # insert some more data
  # incremental backup
  # assert file was created
  # delete some data
  # insert some more data
  # incremental backup
  # assert file was created
  # compute a checksum;
  *** Client
  *** IncrementalBackup
  *** Mysql

  **:**Test case 06**
  ----
  FullBackup use-case.
  ----
  # use test case 05
  # create databases
  # create random data
  # full backup
  # assert file was created
  # compute a checksum;
  *** Client
  *** FullBackup
  *** Mysql
  
  **:**Test case 05**
  ----
  ExecuteSql use-case.
  ----
  # use test case 04
  # set root password
  # create mysqldump user
  # update login path;
  *** Client
  *** ExecuteStatement
  *** Mysql
  *** SetLoginPath
  
  **:**Test case 04**
  ----
  SetLoginPath use-case.
  ----
  # use test case 03
  # create client configuration file
  # create client
  # set root login path w/ 'empty' password;
  *** Client
  *** Mysql
  *** SetLoginPath
  
  **:**Test case 03**
  ----
  Start the MysqlServer.
  ----
  # use test case 02
  # create server
  # start server
  # assert server is ready for connections;
  *** Mysql
  *** Server
  *** ServerInitialization
  
  **:**Test case 02**
  ----
  Initialize data directory.
  ----
  # use test case 01
  # assert data dir is empty
  # invoke mysql_install_db / mysqld --initialize
  # assert data dir is not empty;
  *** Mysql
  *** Server
  *** ProcessHandle
  
  **:**Test case 01**
  ----
  Create a configuration file.
  ----
  # create a configuration file
  # assert file lines;
  *** ConfigurationFile
  *** Mysql
  
  @endmindmap
  
  */

  private TestingMysql() {}

}
