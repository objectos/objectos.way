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
NOTES = objectos.notes

## module
NOTES_MODULE = $(NOTES)

## module coordinates
NOTES_GROUP_ID = $(GROUP_ID)
NOTES_ARTIFACT_ID = $(NOTES)
NOTES_VERSION = $(VERSION)

## javac --release option
NOTES_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
NOTES_ENABLE_PREVIEW = 0

## compile deps
NOTES_COMPILE_DEPS = $(call module-gav,$(LANG_OBJECT))

## test compile deps
NOTES_TEST_COMPILE_DEPS  = $(NOTES_COMPILE_DEPS)
NOTES_TEST_COMPILE_DEPS += $(call module-gav,$(NOTES))
NOTES_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
NOTES_TEST_RUNTIME_DEPS  = $(NOTES_TEST_COMPILE_DEPS)
NOTES_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## test runtime exports
NOTES_TEST_JAVAX_EXPORTS := objectos.notes.internal

## copyright years for javadoc
NOTES_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# NOTES_JAVADOC_SNIPPET_PATH := NOTES_TEST

## pom description
NOTES_DESCRIPTION = Type-safe note sink API

#
# objectos.notes targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),NOTES_,notes@)))

.PHONY: notes@source-jar
notes@source-jar: $(NOTES_SOURCE_JAR_FILE)

.PHONY: notes@javadoc
notes@javadoc: $(NOTES_JAVADOC_JAR_FILE)

.PHONY: notes@ossrh-prepare
notes@ossrh-prepare: $(NOTES_OSSRH_PREPARE)

