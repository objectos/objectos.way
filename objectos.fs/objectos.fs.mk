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
# objectos.fs
#

## module directory
FS = objectos.fs

## module
FS_MODULE = $(FS)

## module version
FS_GROUP_ID = $(GROUP_ID)
FS_ARTIFACT_ID = $(FS)
FS_VERSION = $(VERSION)

## javac --release option
FS_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
FS_ENABLE_PREVIEW = 0

## compile deps
FS_COMPILE_DEPS = $(call module-gav,$(CORE_IO))

## test compile deps
FS_TEST_COMPILE_DEPS  = $(FS_COMPILE_DEPS)
FS_TEST_COMPILE_DEPS += $(call module-gav,$(FS))
FS_TEST_COMPILE_DEPS += $(TESTNG)

## test resources
FS_TEST_RESOURCES := $(FS)/test-resources

## test runtime dependencies
FS_TEST_RUNTIME_DEPS  = $(FS_TEST_COMPILE_DEPS)
FS_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## test env
FS_TEST_RUNTIME_SYSPROPS = objectos.fs.OperatingSystem.family=LINUX

## copyright years for javadoc
FS_COPYRIGHT_YEARS := 2021-2023

## javadoc snippet path
# FS_JAVADOC_SNIPPET_PATH := FS_TEST

## pom description
FS_DESCRIPTION = A Java library for filesystem access.

#
# objectos.fs targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),FS_,fs@)))

.PHONY: fs@source-jar
fs@source-jar: $(FS_SOURCE_JAR_FILE)

.PHONY: fs@javadoc
fs@javadoc: $(FS_JAVADOC_JAR_FILE)

.PHONY: fs@ossrh-prepare
fs@ossrh-prepare: $(FS_OSSRH_PREPARE)
