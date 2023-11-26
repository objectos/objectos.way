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
# objectos.concurrent
#

## module directory
CONCURRENT = objectos.concurrent

## module
CONCURRENT_MODULE = $(CONCURRENT)

## module version
CONCURRENT_GROUP_ID = $(GROUP_ID)
CONCURRENT_ARTIFACT_ID = $(CONCURRENT)
CONCURRENT_VERSION = $(VERSION)

## javac --release option
CONCURRENT_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
CONCURRENT_ENABLE_PREVIEW = 0

## compile deps
CONCURRENT_COMPILE_DEPS  = $(call module-gav,$(CORE_SERVICE))
CONCURRENT_COMPILE_DEPS += $(call module-gav,$(NOTES))
CONCURRENT_COMPILE_DEPS += $(call module-gav,$(UTIL_LIST))

## test compile deps
CONCURRENT_TEST_COMPILE_DEPS  = $(CONCURRENT_COMPILE_DEPS)
CONCURRENT_TEST_COMPILE_DEPS += $(call module-gav,$(CONCURRENT))
CONCURRENT_TEST_COMPILE_DEPS += $(call module-gav,$(FS_TESTING))
CONCURRENT_TEST_COMPILE_DEPS += $(call module-gav,$(NOTES_CONSOLE))
CONCURRENT_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
CONCURRENT_TEST_RUNTIME_DEPS  = $(CONCURRENT_TEST_COMPILE_DEPS)
CONCURRENT_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## runtime modules
CONCURRENT_TEST_JAVAX_MODULES  = org.testng
CONCURRENT_TEST_JAVAX_MODULES += objectos.fs
CONCURRENT_TEST_JAVAX_MODULES += objectos.fs.testing
CONCURRENT_TEST_JAVAX_MODULES += objectos.notes.console

## test runtime reads
CONCURRENT_TEST_JAVAX_READS  = objectos.fs
CONCURRENT_TEST_JAVAX_READS += objectos.fs.testing
CONCURRENT_TEST_JAVAX_READS += objectos.notes.console

## copyright years for javadoc
CONCURRENT_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# CONCURRENT_JAVADOC_SNIPPET_PATH := CONCURRENT_TEST

## pom description
CONCURRENT_DESCRIPTION = Utilities for java.io.InputStream and java.io.OutputStream

#
# objectos.concurrent targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),CONCURRENT_,concurrent@)))

.PHONY: concurrent@source-jar
concurrent@source-jar: $(CONCURRENT_SOURCE_JAR_FILE)

.PHONY: concurrent@javadoc
concurrent@javadoc: $(CONCURRENT_JAVADOC_JAR_FILE)

.PHONY: concurrent@ossrh-prepare
concurrent@ossrh-prepare: $(CONCURRENT_OSSRH_PREPARE)
