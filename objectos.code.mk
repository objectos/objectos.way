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
# objectos.code
#

## code directory
CODE = objectos.code

## code module
CODE_MODULE = $(CODE)

## code coordinates
CODE_GROUP_ID = $(GROUP_ID)
CODE_ARTIFACT_ID = $(CODE)
CODE_VERSION = $(VERSION)

## code javac --release option
CODE_JAVA_RELEASE = $(JAVA_RELEASE)

## code --enable-preview ?
CODE_ENABLE_PREVIEW = 1

## code jar name
CODE_JAR_NAME = $(CODE)

## code test compile deps
CODE_TEST_COMPILE_DEPS = $(CODE_JAR_FILE)
CODE_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## code test runtime dependencies
CODE_TEST_RUNTIME_DEPS = $(CODE_TEST_COMPILE_DEPS)
CODE_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
CODE_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
CODE_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## test runtime exports
CODE_TEST_JAVAX_EXPORTS := objectos.code.internal

#
# objectos.code targets
#

CODE_TASKS  = CLEAN_TASK
CODE_TASKS += COMPILE_TASK
CODE_TASKS += JAR_TASK
CODE_TASKS += TEST_COMPILE_TASK
CODE_TASKS += TEST_RUN_TASK
CODE_TASKS += INSTALL_TASK

$(foreach task,$(CODE_TASKS),$(eval $(call $(task),CODE_,code@)))

.PHONY: code@jar
code@jar: $(CODE_JAR_FILE)

.PHONY: code@test
code@test: $(CODE_TEST_RUN_MARKER)
