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
# objectos.core.io
#

## module directory
CORE_IO = objectos.core.io

## module
CORE_IO_MODULE = $(CORE_IO)

## module version
CORE_IO_GROUP_ID = $(GROUP_ID)
CORE_IO_ARTIFACT_ID = $(CORE_IO)
CORE_IO_VERSION = $(VERSION)

## javac --release option
CORE_IO_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
CORE_IO_ENABLE_PREVIEW = 0

## compile deps
CORE_IO_COMPILE_DEPS  = $(call module-gav,$(LANG_OBJECT))
CORE_IO_COMPILE_DEPS += $(call module-gav,$(UTIL_LIST))

## test compile deps
CORE_IO_TEST_COMPILE_DEPS  = $(CORE_IO_COMPILE_DEPS)
CORE_IO_TEST_COMPILE_DEPS += $(call module-gav,$(CORE_IO))
CORE_IO_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
CORE_IO_TEST_RUNTIME_DEPS  = $(CORE_IO_TEST_COMPILE_DEPS)
CORE_IO_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## copyright years for javadoc
CORE_IO_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# CORE_IO_JAVADOC_SNIPPET_PATH := CORE_IO_TEST

## pom description
CORE_IO_DESCRIPTION = Utilities for java.io.InputStream and java.io.OutputStream

#
# objectos.core.io targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),CORE_IO_,core.io@)))

.PHONY: core.io@source-jar
core.io@source-jar: $(CORE_IO_SOURCE_JAR_FILE)

.PHONY: core.io@javadoc
core.io@javadoc: $(CORE_IO_JAVADOC_JAR_FILE)

.PHONY: core.io@ossrh-prepare
core.io@ossrh-prepare: $(CORE_IO_OSSRH_PREPARE)

