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
# test options
#

## test base dir
TEST = $(MODULE)/test

## test source path
TEST_SOURCE_PATH = $(TEST)

## test source files 
TEST_SOURCES = $(shell find ${TEST_SOURCE_PATH} -type f -name '*.java' -print)

## test source files modified since last compilation
TEST_MODIFIED_SOURCES :=

## test class output path
TEST_CLASS_OUTPUT = $(WORK)/test

## test compiled classes
TEST_CLASSES = $(TEST_SOURCES:$(TEST_SOURCE_PATH)/%.java=$(TEST_CLASS_OUTPUT)/%.class)

## test compile-time dependencies
# TEST_COMPILE_DEPS =

## test compile-time class path
TEST_COMPILE_CLASS_PATH = $(call class-path,$(TEST_COMPILE_DEPS)) 

## test javac command
TEST_JAVACX = $(JAVAC)
TEST_JAVACX += -d $(TEST_CLASS_OUTPUT)
TEST_JAVACX += -Xlint:all
TEST_JAVACX += --class-path $(CLASS_OUTPUT)$(CLASS_PATH_SEPARATOR)$(TEST_COMPILE_CLASS_PATH)
ifdef ENABLE_PREVIEW
TEST_JAVACX += --enable-preview
endif
TEST_JAVACX += --release $(JAVA_RELEASE)
TEST_JAVACX += $(TEST_MODIFIED_SOURCES)

## test runtime dependencies
# TEST_RUNTIME_DEPS =

## test runtime module-path
TEST_RUNTIME_MODULE_PATH = $(call module-path,$(TEST_RUNTIME_DEPS))

## test runtime output path
TEST_RUNTIME_OUTPUT = $(WORK)/test-output

## test main class
ifndef TEST_MAIN
TEST_MAIN = $(MODULE).RunTests
endif

## test java command
TEST_JAVAX = $(JAVA)
TEST_JAVAX += --module-path $(CLASS_OUTPUT)$(MODULE_PATH_SEPARATOR)$(TEST_RUNTIME_MODULE_PATH)
TEST_JAVAX += --add-modules org.testng
TEST_JAVAX += --add-reads $(MODULE)=org.testng
ifdef TEST_JAVAX_EXPORTS
TEST_JAVAX += $(foreach pkg,$(TEST_JAVAX_EXPORTS),--add-exports $(MODULE)/$(pkg)=org.testng)
endif
ifdef ENABLE_PREVIEW
TEST_JAVAX += --enable-preview
endif
TEST_JAVAX += --patch-module $(MODULE)=$(TEST_CLASS_OUTPUT)
TEST_JAVAX += --module $(MODULE)/$(TEST_MAIN)
TEST_JAVAX += $(TEST_RUNTIME_OUTPUT)

#
# test target
#
.PHONY: test
test: $(ARTIFACT) $(TEST_COMPILE_DEPS) $(TEST_CLASSES) $(TEST_RUNTIME_DEPS) 
	if [ -n "$(TEST_MODIFIED_SOURCES)" ]; then \
		$(TEST_JAVACX); \
	fi
	$(TEST_JAVAX)

$(TEST_CLASSES): $(TEST_CLASS_OUTPUT)/%.class: $(TEST_SOURCE_PATH)/%.java
	$(eval TEST_MODIFIED_SOURCES += $$<)
