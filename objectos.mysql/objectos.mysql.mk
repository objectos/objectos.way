#
# Copyright (C) 2023 Objectos Software LTDA.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#
# objectos.mysql
#

## module directory
MYSQL = objectos.mysql

## module
MYSQL_MODULE = $(MYSQL)

## module version
MYSQL_GROUP_ID = $(GROUP_ID)
MYSQL_ARTIFACT_ID = $(MYSQL)
MYSQL_VERSION = $(VERSION)

## javac --release option
MYSQL_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
MYSQL_ENABLE_PREVIEW = 0

## compile deps
MYSQL_COMPILE_DEPS  = $(call module-gav,$(CONCURRENT))
MYSQL_COMPILE_DEPS += $(call module-gav,$(FS))
MYSQL_COMPILE_DEPS += $(call module-gav,$(NOTES))

## test compile deps
MYSQL_TEST_COMPILE_DEPS  = $(MYSQL_COMPILE_DEPS)
MYSQL_TEST_COMPILE_DEPS += $(call module-gav,$(MYSQL))
MYSQL_TEST_COMPILE_DEPS += $(call module-gav,$(CORE_TESTING))
MYSQL_TEST_COMPILE_DEPS += $(call module-gav,$(FS_TESTING))
MYSQL_TEST_COMPILE_DEPS += $(call module-gav,$(FS_ZIP))
MYSQL_TEST_COMPILE_DEPS += $(call module-gav,$(NOTES_CONSOLE))
MYSQL_TEST_COMPILE_DEPS += $(TESTNG)

## test resources
MYSQL_TEST_RESOURCES := $(MYSQL)/test-resources

## test runtime dependencies
MYSQL_TEST_RUNTIME_DEPS  = $(MYSQL_TEST_COMPILE_DEPS)
MYSQL_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## test env
MYSQL_TEST_RUNTIME_SYSPROPS  = objectos.mysql.release=MYSQL_5_6
MYSQL_TEST_RUNTIME_SYSPROPS += objectos.mysql56.baseDir=/opt/mysql-5.6
MYSQL_TEST_RUNTIME_SYSPROPS += objectos.mysql56.binaryDirClient=/opt/mysql-5.6/bin
MYSQL_TEST_RUNTIME_SYSPROPS += objectos.mysql56.binaryDirServer=/opt/mysql-5.6/bin
MYSQL_TEST_RUNTIME_SYSPROPS += objectos.mysql56.scriptsDir=/opt/mysql-5.6/scripts
MYSQL_TEST_RUNTIME_SYSPROPS += objectos.mysql57.baseDir=/opt/mysql-5.7
MYSQL_TEST_RUNTIME_SYSPROPS += objectos.mysql57.binaryDirClient=/opt/mysql-5.7/bin
MYSQL_TEST_RUNTIME_SYSPROPS += objectos.mysql57.binaryDirServer=/opt/mysql-5.7/bin
MYSQL_TEST_RUNTIME_SYSPROPS += objectos.mysql8.baseDir=/opt/mysql-8
MYSQL_TEST_RUNTIME_SYSPROPS += objectos.mysql8.binaryDirClient=/opt/mysql-8/bin
MYSQL_TEST_RUNTIME_SYSPROPS += objectos.mysql8.binaryDirServer=/opt/mysql-8/bin

## runtime modules
MYSQL_TEST_JAVAX_MODULES  = org.testng
MYSQL_TEST_JAVAX_MODULES += objectos.core.testing
MYSQL_TEST_JAVAX_MODULES += objectos.fs.testing
MYSQL_TEST_JAVAX_MODULES += objectos.fs.zip
MYSQL_TEST_JAVAX_MODULES += objectos.notes.console

## test runtime reads
MYSQL_TEST_JAVAX_READS  = objectos.core.testing
MYSQL_TEST_JAVAX_READS += objectos.fs.testing
MYSQL_TEST_JAVAX_READS += objectos.fs.zip
MYSQL_TEST_JAVAX_READS += objectos.notes.console

## copyright years for javadoc
MYSQL_COPYRIGHT_YEARS := 2021-2023

## javadoc snippet path
# MYSQL_JAVADOC_SNIPPET_PATH := MYSQL_TEST

## pom description
MYSQL_DESCRIPTION = Objectos Management for MySQL\: a set of Java utilities to manage a native MySQL installation.

#
# objectos.mysql targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),MYSQL_,mysql@)))

.PHONY: mysql@source-jar
mysql@source-jar: $(MYSQL_SOURCE_JAR_FILE)

.PHONY: mysql@javadoc
mysql@javadoc: $(MYSQL_JAVADOC_JAR_FILE)

.PHONY: mysql@ossrh-prepare
mysql@ossrh-prepare: $(MYSQL_OSSRH_PREPARE)
