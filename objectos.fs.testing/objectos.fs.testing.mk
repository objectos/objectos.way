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
# objectos.fs.testing
#

## module directory
FS_TESTING = objectos.fs.testing

## module
FS_TESTING_MODULE = $(FS_TESTING)

## module version
FS_TESTING_GROUP_ID = $(GROUP_ID)
FS_TESTING_ARTIFACT_ID = $(FS_TESTING)
FS_TESTING_VERSION = $(VERSION)

## javac --release option
FS_TESTING_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
FS_TESTING_ENABLE_PREVIEW = 0

## compile deps
FS_TESTING_COMPILE_DEPS  = $(call module-gav,$(CORE_TESTING))
FS_TESTING_COMPILE_DEPS += $(call module-gav,$(FS))

## test compile deps
FS_TESTING_TEST_COMPILE_DEPS  = $(FS_TESTING_COMPILE_DEPS)
FS_TESTING_TEST_COMPILE_DEPS += $(call module-gav,$(FS_TESTING))
FS_TESTING_TEST_COMPILE_DEPS += $(TESTNG)

## test resources
FS_TESTING_TEST_RESOURCES := $(FS_TESTING)/test-resources

## test runtime dependencies
FS_TESTING_TEST_RUNTIME_DEPS  = $(FS_TESTING_TEST_COMPILE_DEPS)
FS_TESTING_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## test env
FS_TESTING_TEST_RUNTIME_SYSPROPS = objectos.fs.testing.OperatingSystem.family=LINUX

## copyright years for javadoc
FS_TESTING_COPYRIGHT_YEARS := 2021-2023

## javadoc snippet path
# FS_TESTING_JAVADOC_SNIPPET_PATH := FS_TESTING_TEST

## pom description
FS_TESTING_DESCRIPTION = $(FS_TESTING)

#
# objectos.fs.testing targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),FS_TESTING_,fs.testing@)))

.PHONY: fs.testing@source-jar
fs.testing@source-jar: $(FS_TESTING_SOURCE_JAR_FILE)

.PHONY: fs.testing@javadoc
fs.testing@javadoc: $(FS_TESTING_JAVADOC_JAR_FILE)

.PHONY: fs.testing@ossrh-prepare
fs.testing@ossrh-prepare: $(FS_TESTING_OSSRH_PREPARE)
