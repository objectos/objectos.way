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
/*

  @startuml

  ' config

  hide empty members
  ' left to right direction
  skinparam genericDisplay old
  ' skinparam monochrome true
  skinparam shadowing false
  ' skinparam style strictuml

  class Mysql {
    + {static} MysqlFacade\lcreateMysqlFacade(\lClient client,\lServer server,\lIoExecutor ioWorker)
    + {static} Server\lcreateServer(\lClient client,\lServer server,\lIoExecutor ioWorker)
  }

  interface MysqlFacade extends StateMachine {
    + void acceptResultVisitor(\lResultVisitor visitor)
    + void setExecuteFullBackup(\lLoginPath path,\lDirectory target)
    + void setExecuteIncrementalBackup(\lLoginPath path,\lDirectory target)
    + void setExecuteStatement(\lLoginPath path,\lString... statements)
    + void setRestoreFullBackup(\lLoginPath path,\lRegularFile file)
    + void setRestoreIncrementalBackup(\lLoginPath path,\lRegularFile file)
  }

  interface Server extends Service {
    + boolean isAlive()
    + boolean isReadyForConnections(\lRegularFile errorLog,\llong offset)
  }

  interface ResultVisitor {
    + void visitError(\lExecutionException exception)
    + void visitError(\lIOException exception)
    + void visitError(\lTimeoutException exception)
    + void visitExecuteFullBackup(\lRegularFile f)
    + void visitExecuteIncrementalBackup(\lUnmodifiableList<RegularFile> f)
    + void visitExecuteStatement(\lUnmodifiableList<String> r)
    + void visitRestoreFullBackup()
    + void visitRestoreIncrementalBackup()
  }

  @enduml

*/
package objectos.mysql;