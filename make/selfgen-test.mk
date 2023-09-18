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
# selfgen-test options
#

## selfgen-test base dir
SELFGEN_TEST := $(SELFGEN_MODULE)/test

## selfgen-test source path
SELFGEN_TEST_SOURCE_PATH := $(SELFGEN_TEST)

## selfgen-test source files 
SELFGEN_TEST_SOURCES := $(shell find ${SELFGEN_TEST_SOURCE_PATH} -type f -name '*.java' -print)

## selfgen-test source files modified since last compilation
SELFGEN_TEST_MODIFIED_SOURCES :=

## selfgen-test class output path
SELFGEN_TEST_CLASS_OUTPUT := $(SELFGEN_WORK)/test

## selfgen-test compiled classes
SELFGEN_TEST_CLASSES := $(SELFGEN_TEST_SOURCES:$(SELFGEN_TEST_SOURCE_PATH)/%.java=$(SELFGEN_TEST_CLASS_OUTPUT)/%.class)

## selfgen-test compile-time dependencies
# SELFGEN_TEST_COMPILE_DEPS =

## selfgen-test compile-time class path
SELFGEN_TEST_COMPILE_CLASS_PATH := $(call class-path,$(SELFGEN_TEST_COMPILE_DEPS)) 

## selfgen-test javac command
SELFGEN_TEST_JAVACX = $(JAVAC)
SELFGEN_TEST_JAVACX += -d $(SELFGEN_TEST_CLASS_OUTPUT)
SELFGEN_TEST_JAVACX += -Xlint:all
SELFGEN_TEST_JAVACX += --class-path $(SELFGEN_CLASS_OUTPUT)$(CLASS_PATH_SEPARATOR)$(SELFGEN_TEST_COMPILE_CLASS_PATH)
ifdef SELFGEN_ENABLE_PREVIEW
SELFGEN_TEST_JAVACX += --enable-preview
endif
SELFGEN_TEST_JAVACX += --release $(SELFGEN_JAVAC_RELEASE)
SELFGEN_TEST_JAVACX += $(SELFGEN_TEST_MODIFIED_SOURCES)

## selfgen-test runtime dependencies
# SELFGEN_TEST_RUNTIME_DEPS =

## selfgen-test runtime module-path
SELFGEN_TEST_RUNTIME_MODULE_PATH := $(call module-path,$(SELFGEN_TEST_RUNTIME_DEPS))

## selfgen-test runtime output path
SELFGEN_TEST_RUNTIME_OUTPUT := $(SELFGEN_WORK)/test-output

## selfgen-test java command
SELFGEN_TEST_JAVAX = $(JAVA)
ifdef SELFGEN_TEST_JAVAX_EXPORTS
SELFGEN_TEST_JAVAX += $(foreach pkg,$(SELFGEN_TEST_JAVAX_EXPORTS),--add-exports $(SELFGEN_MODULE)/$(pkg)=org.testng)
endif
SELFGEN_TEST_JAVAX += --add-modules org.testng
SELFGEN_TEST_JAVAX += --add-reads $(SELFGEN_MODULE)=org.testng
ifdef SELFGEN_ENABLE_PREVIEW
SELFGEN_TEST_JAVAX += --enable-preview
endif
SELFGEN_TEST_JAVAX += --module-path $(SELFGEN_CLASS_OUTPUT)$(MODULE_PATH_SEPARATOR)$(SELFGEN_TEST_RUNTIME_MODULE_PATH)
SELFGEN_TEST_JAVAX += --patch-module $(SELFGEN_MODULE)=$(SELFGEN_TEST_CLASS_OUTPUT)
SELFGEN_TEST_JAVAX += --module $(SELFGEN_MODULE)/$(SELFGEN_TEST_MAIN)
SELFGEN_TEST_JAVAX += $(SELFGEN_TEST_RUNTIME_OUTPUT)

#
# selfgen-test target
#
.PHONY: selfgen-test
selfgen-test: $(SELFGEN_ARTIFACT) $(SELFGEN_TEST_COMPILE_DEPS) $(SELFGEN_TEST_CLASSES) $(SELFGEN_TEST_RUNTIME_DEPS) 
	if [ -n "$(SELFGEN_TEST_MODIFIED_SOURCES)" ]; then \
		$(SELFGEN_TEST_JAVACX); \
	fi
	$(SELFGEN_TEST_JAVAX)

$(SELFGEN_TEST_CLASSES): $(SELFGEN_TEST_CLASS_OUTPUT)/%.class: $(SELFGEN_TEST_SOURCE_PATH)/%.java
	$(eval SELFGEN_TEST_MODIFIED_SOURCES += $$<)
