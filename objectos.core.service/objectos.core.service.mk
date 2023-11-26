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
# objectos.core.service
#

## module directory
CORE_SERVICE = objectos.core.service

## module
CORE_SERVICE_MODULE = $(CORE_SERVICE)

## module version
CORE_SERVICE_GROUP_ID = $(GROUP_ID)
CORE_SERVICE_ARTIFACT_ID = $(CORE_SERVICE)
CORE_SERVICE_VERSION = $(VERSION)

## javac --release option
CORE_SERVICE_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
CORE_SERVICE_ENABLE_PREVIEW = 0

## compile deps
CORE_SERVICE_COMPILE_DEPS  =

## test compile deps
CORE_SERVICE_TEST_COMPILE_DEPS  = $(CORE_SERVICE_COMPILE_DEPS)
CORE_SERVICE_TEST_COMPILE_DEPS += $(call module-gav,$(CORE_SERVICE))
CORE_SERVICE_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
CORE_SERVICE_TEST_RUNTIME_DEPS  = $(CORE_SERVICE_TEST_COMPILE_DEPS)
CORE_SERVICE_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## copyright years for javadoc
CORE_SERVICE_COPYRIGHT_YEARS := 2021-2023

## javadoc snippet path
# CORE_SERVICE_JAVADOC_SNIPPET_PATH := CORE_SERVICE_TEST

## pom description
CORE_SERVICE_DESCRIPTION = $(CORE_SERVICE)

#
# objectos.core.service targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),CORE_SERVICE_,core.service@)))

.PHONY: core.service@source-jar
core.service@source-jar: $(CORE_SERVICE_SOURCE_JAR_FILE)

.PHONY: core.service@javadoc
core.service@javadoc: $(CORE_SERVICE_JAVADOC_JAR_FILE)

.PHONY: core.service@ossrh-prepare
core.service@ossrh-prepare: $(CORE_SERVICE_OSSRH_PREPARE)
