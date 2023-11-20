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
NOTES_BASE = objectos.notes.base

## module
NOTES_BASE_MODULE = $(NOTES_BASE)

## module version
NOTES_BASE_VERSION = $(VERSION)

## javac --release option
NOTES_BASE_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
NOTES_BASE_ENABLE_PREVIEW = 0

## compile deps
NOTES_BASE_COMPILE_DEPS = $(call module-gav,$(NOTES))

## jar name
NOTES_BASE_JAR_NAME = $(NOTES_BASE)

## test compile deps
NOTES_BASE_TEST_COMPILE_DEPS  = $(NOTES_BASE_COMPILE_DEPS)
NOTES_BASE_TEST_COMPILE_DEPS += $(call module-gav,$(NOTES_BASE))
NOTES_BASE_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
NOTES_BASE_TEST_RUNTIME_DEPS  = $(NOTES_BASE_TEST_COMPILE_DEPS)
NOTES_BASE_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## install coordinates
NOTES_BASE_GROUP_ID = $(GROUP_ID)
NOTES_BASE_ARTIFACT_ID = $(NOTES_BASE_MODULE)

## copyright years for javadoc
NOTES_BASE_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# NOTES_BASE_JAVADOC_SNIPPET_PATH := NOTES_BASE_TEST

## pom description
NOTES_BASE_DESCRIPTION = Base classes to write note sink implementations

#
# objectos.notes targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),NOTES_BASE_,notes.base@)))

.PHONY: notes.base@jar
notes.base@jar: $(NOTES_BASE_JAR_FILE)

.PHONY: notes.base@test
notes.base@test: $(NOTES_BASE_TEST_RUN_MARKER)

.PHONY: notes.base@install
notes.base@install: $(NOTES_BASE_INSTALL)

.PHONY: notes.base@source-jar
notes.base@source-jar: $(NOTES_BASE_SOURCE_JAR_FILE)

.PHONY: notes.base@javadoc
notes.base@javadoc: $(NOTES_BASE_JAVADOC_JAR_FILE)

.PHONY: notes.base@pom
notes.base@pom: $(NOTES_BASE_POM_FILE)

.PHONY: notes.base@ossrh-prepare
notes.base@ossrh-prepare: $(NOTES_BASE_OSSRH_PREPARE)
