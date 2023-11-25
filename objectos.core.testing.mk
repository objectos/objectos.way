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
# objectos.core.testing
#

## module directory
CORE_TESTING = objectos.core.testing

## module
CORE_TESTING_MODULE = $(CORE_TESTING)

## module version
CORE_TESTING_GROUP_ID = $(GROUP_ID)
CORE_TESTING_ARTIFACT_ID = $(CORE_TESTING)
CORE_TESTING_VERSION = $(VERSION)

## javac --release option
CORE_TESTING_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
CORE_TESTING_ENABLE_PREVIEW = 0

## compile deps
CORE_TESTING_COMPILE_DEPS  = $(call module-gav,$(LANG_OBJECT))

## test compile deps
CORE_TESTING_TEST_COMPILE_DEPS  = $(CORE_TESTING_COMPILE_DEPS)
CORE_TESTING_TEST_COMPILE_DEPS += $(call module-gav,$(CORE_TESTING))
CORE_TESTING_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
CORE_TESTING_TEST_RUNTIME_DEPS  = $(CORE_TESTING_TEST_COMPILE_DEPS)
CORE_TESTING_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## copyright years for javadoc
CORE_TESTING_COPYRIGHT_YEARS := 2021-2023

## javadoc snippet path
# CORE_TESTING_JAVADOC_SNIPPET_PATH := CORE_TESTING_TEST

## pom description
CORE_TESTING_DESCRIPTION = $(CORE_TESTING)

#
# objectos.core.testing targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),CORE_TESTING_,core.testing@)))

.PHONY: core.testing@source-jar
core.testing@source-jar: $(CORE_TESTING_SOURCE_JAR_FILE)

.PHONY: core.testing@javadoc
core.testing@javadoc: $(CORE_TESTING_JAVADOC_JAR_FILE)

.PHONY: core.testing@ossrh-prepare
core.testing@ossrh-prepare: $(CORE_TESTING_OSSRH_PREPARE)
