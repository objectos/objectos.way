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
# objectos.http
#

## module directory
HTTP = objectos.http

## module
HTTP_MODULE = $(HTTP)

## module version
HTTP_GROUP_ID = $(GROUP_ID)
HTTP_ARTIFACT_ID = $(HTTP_MODULE)
HTTP_VERSION = $(VERSION)

## javac --release option
HTTP_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
HTTP_ENABLE_PREVIEW = 0

## compile deps
HTTP_COMPILE_DEPS := $(call module-gav,$(UTIL_MAP))

## test compile deps
HTTP_TEST_COMPILE_DEPS := $(HTTP_COMPILE_DEPS)
HTTP_TEST_COMPILE_DEPS += $(call module-gav,$(HTTP))
HTTP_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
HTTP_TEST_RUNTIME_DEPS  = $(HTTP_TEST_COMPILE_DEPS)
HTTP_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## copyright years for javadoc
HTTP_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# HTTP_JAVADOC_SNIPPET_PATH := HTTP_TEST

## pom description
HTTP_DESCRIPTION := HTTP related types

## test runtime exports
HTTP_TEST_JAVAX_EXPORTS := objectox.http

#
# objectos.http targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),HTTP_,http@)))

.PHONY: http@source-jar
http@source-jar: $(HTTP_SOURCE_JAR_FILE)

.PHONY: http@javadoc
http@javadoc: $(HTTP_JAVADOC_JAR_FILE)

.PHONY: http@ossrh-prepare
http@ossrh-prepare: $(HTTP_OSSRH_PREPARE)
