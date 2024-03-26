#
# Copyright (C) 2023-2024 Objectos Software LTDA.
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
# test compilation options
#

## test source directory
TEST := test

## test source files 
TEST_SOURCES := $(shell find ${TEST} -type f -name '*.java' -print)

## test class output path
TEST_CLASS_OUTPUT := $(WORK)/test

## test compile-time dependencies
# TEST_COMPILE_DEPS :=

## test compile-time resolution files
ifdef TEST_COMPILE_DEPS
TEST_COMPILE_RESOLUTION_FILES := $(call to-resolution-files,$(TEST_COMPILE_DEPS))
endif

## test compile-time class-path
TEST_COMPILE_CLASS_PATH := $(WORK)/test-compile-class-path

## test javac command
TEST_JAVACX := $(JAVAC)
TEST_JAVACX += -d $(TEST_CLASS_OUTPUT)
TEST_JAVACX += --class-path @$(TEST_COMPILE_CLASS_PATH)
ifeq ($(ENABLE_PREVIEW),1)
TEST_JAVACX += --enable-preview
endif
TEST_JAVACX += --release $(JAVA_RELEASE)
TEST_JAVACX += --source-path $(TEST)
TEST_JAVACX += $(TEST_SOURCES)

## test resources directory
# TEST_RESOURCES = $(BASEDIR)/test-resources

ifdef TEST_RESOURCES

## test resources "source"
TEST_RESOURCES_SRC = $(shell find ${TEST_RESOURCES} -type f -print)

## test resources "output"
TEST_RESOURCES_OUT = $(TEST_RESOURCES_SRC:$(TEST_RESOURCES)/%=$(TEST_CLASS_OUTPUT)/%)

## target to copy test resources
$(TEST_RESOURCES_OUT): $(TEST_CLASS_OUTPUT)/%: $(TEST_RESOURCES)/%
	@mkdir --parents $(@D)
	cp $< $@
	
endif

## test compilation marker
TEST_COMPILE_MARKER = $(WORK)/test-compile-marker

## test compilation requirements
TEST_COMPILE_REQS := $(COMPILE_MARKER)
TEST_COMPILE_REQS += $(TEST_COMPILE_CLASS_PATH)
TEST_COMPILE_REQS += $(TEST_SOURCES)
TEST_COMPILE_REQS += $(TEST_RESOURCES_OUT)

#
# test compilation targets
#

.PHONY: test-compile
test-compile: $(TEST_COMPILE_MARKER)

.PHONY: test-compile@clean
test-compile@clean:
	rm -rf $(TEST_CLASS_OUTPUT) $(TEST_COMPILE_CLASS_PATH) $(TEST_COMPILE_MARKER)

.PHONY: re-test-compile
re-test-compile: test-compile@clean test-compile

$(TEST_COMPILE_CLASS_PATH): $(TEST_COMPILE_RESOLUTION_FILES)
	echo $(CLASS_OUTPUT) > $@.tmp
ifdef COMPILE_RESOLUTION_FILES
	cat $(COMPILE_RESOLUTION_FILES) >> $@.tmp
endif
ifdef TEST_COMPILE_RESOLUTION_FILES
	cat $(TEST_COMPILE_RESOLUTION_FILES) >> $@.tmp
endif
	$(call uniq-resolution-files,$@.tmp) | paste --delimiter='$(CLASS_PATH_SEPARATOR)' --serial > $@

$(TEST_COMPILE_MARKER): $(TEST_COMPILE_REQS) 
	$(TEST_JAVACX)
	touch $@
