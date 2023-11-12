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
# objectos.core.object options
#

## code directory
CORE_OBJECT = objectos.core.object

## code module
CORE_OBJECT_MODULE = $(CORE_OBJECT)

## group id
CORE_OBJECT_GROUP_ID = $(GROUP_ID)

## artifact id
CORE_OBJECT_ARTIFACT_ID = $(CORE_OBJECT_MODULE)

## code module version
CORE_OBJECT_VERSION = $(VERSION)

## code javac --release option
CORE_OBJECT_JAVA_RELEASE = $(JAVA_RELEASE)

## code --enable-preview ?
CORE_OBJECT_ENABLE_PREVIEW = 1

## code jar name
CORE_OBJECT_JAR_NAME = $(CORE_OBJECT)

## code test compile deps
CORE_OBJECT_TEST_COMPILE_DEPS = $(CORE_OBJECT_JAR_FILE)
CORE_OBJECT_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## code test runtime dependencies
CORE_OBJECT_TEST_RUNTIME_DEPS = $(CORE_OBJECT_TEST_COMPILE_DEPS)
CORE_OBJECT_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
CORE_OBJECT_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
CORE_OBJECT_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## test runtime exports
CORE_OBJECT_TEST_JAVAX_EXPORTS := objectos.core.object.internal

#
# objectos.core.object targets
#

CORE_OBJECT_TASKS = COMPILE_TASK
CORE_OBJECT_TASKS += JAR_TASK
CORE_OBJECT_TASKS += TEST_COMPILE_TASK
CORE_OBJECT_TASKS += TEST_RUN_TASK
CORE_OBJECT_TASKS += INSTALL_TASK

$(foreach task,$(CORE_OBJECT_TASKS),$(eval $(call $(task),CORE_OBJECT_)))

.PHONY: core.object@clean
core.object@clean:
	rm -rf $(CORE_OBJECT_WORK)/*

.PHONY: core.object@compile
core.object@compile: $(CORE_OBJECT_COMPILE_MARKER)

.PHONY: core.object@jar
core.object@jar: $(CORE_OBJECT_JAR_FILE)

.PHONY: core.object@test
core.object@test: $(CORE_OBJECT_TEST_RUN_MARKER)

.PHONY: core.object@install
core.object@install: $(CORE_OBJECT_INSTALL)
