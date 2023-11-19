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
# objectos.notes options
#

## module directory
NOTES_FILE = objectos.notes.file

## module
NOTES_FILE_MODULE = $(NOTES_FILE)

## module version
NOTES_FILE_VERSION = $(VERSION)

## javac --release option
NOTES_FILE_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
NOTES_FILE_ENABLE_PREVIEW = 0

## compile deps
NOTES_FILE_COMPILE_DEPS = $(call module-gav,$(NOTES_BASE))

## jar name
NOTES_FILE_JAR_NAME = $(NOTES_FILE)

## test compile deps
NOTES_FILE_TEST_COMPILE_DEPS = $(NOTES_FILE_COMPILE_DEPS)
NOTES_FILE_TEST_COMPILE_DEPS += $(NOTES_FILE_JAR_FILE)
NOTES_FILE_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test runtime dependencies
NOTES_FILE_TEST_RUNTIME_DEPS = $(NOTES_FILE_TEST_COMPILE_DEPS)
NOTES_FILE_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
NOTES_FILE_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
NOTES_FILE_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## install coordinates
NOTES_FILE_GROUP_ID = $(GROUP_ID)
NOTES_FILE_ARTIFACT_ID = $(NOTES_FILE_MODULE)

## copyright years for javadoc
NOTES_FILE_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# NOTES_FILE_JAVADOC_SNIPPET_PATH := NOTES_FILE_TEST

## pom description
NOTES_FILE_DESCRIPTION = NoteSink implementation that writes out notes to a regular file.

#
# objectos.notes targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),NOTES_FILE_,notes.file@)))

.PHONY: notes.file@jar
notes.file@jar: $(NOTES_FILE_JAR_FILE)

.PHONY: notes.file@test
notes.file@test: $(NOTES_FILE_TEST_RUN_MARKER)

.PHONY: notes.file@install
notes.file@install: $(NOTES_FILE_INSTALL)

.PHONY: notes.file@source-jar
notes.file@source-jar: $(NOTES_FILE_SOURCE_JAR_FILE)

.PHONY: notes.file@javadoc
notes.file@javadoc: $(NOTES_FILE_JAVADOC_JAR_FILE)

.PHONY: notes.file@pom
notes.file@pom: $(NOTES_FILE_POM_FILE)

.PHONY: notes.file@ossrh-prepare
notes.file@ossrh-prepare: $(NOTES_FILE_OSSRH_PREPARE)
