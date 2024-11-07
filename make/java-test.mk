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
# test execution options
#

## test runtime dependencies
# TEST_RUNTIME_DEPS := 

## test runtime resolution files
ifdef TEST_RUNTIME_DEPS
TEST_RUNTIME_RESOLUTION_FILES := $(call to-resolution-files,$(TEST_RUNTIME_DEPS))
endif

## test runtime module-path
TEST_RUNTIME_PATH := $(WORK)/test-runtime-path

## test main class
ifndef TEST_MAIN
$(error The required variable TEST_MAIN was not defined)
endif

## test runtime output path
TEST_RUNTIME_OUTPUT := $(WORK)/test-output

## test execution marker
TEST_RUN_MARKER = $(TEST_RUNTIME_OUTPUT)/index.html

## test arg files
#TEST_ARGFILES

## test java command
TEST_JAVAX := $(JAVA)
ifdef TEST_JVM_OPTS
TEST_JAVAX += $(TEST_JVM_OPTS)
endif
ifdef TEST_ARGFILES
TEST_JAVAX += $(foreach argfile,$(TEST_ARGFILES),@$(argfile))
endif
ifdef TEST_ADD_EXPORTS
TEST_JAVAX += $(foreach export,$(TEST_ADD_EXPORTS),--add-exports $(export))
endif
ifdef TEST_ADD_OPENS
TEST_JAVAX += $(foreach opens,$(TEST_ADD_OPENS),--add-opens $(opens))
endif
ifdef TEST_ADD_READS
TEST_JAVAX += $(foreach read,$(TEST_ADD_READS),--add-reads $(read))
endif
ifeq ($(ENABLE_PREVIEW),1)
TEST_JAVAX += --enable-preview
endif

ifeq ($(TEST_RUNTIME_MODE),class-path)

## test delimiter
TEST_PATH_DELIMITER := $(CLASS_PATH_SEPARATOR)

## test java command
TEST_JAVAX += --class-path @$(TEST_RUNTIME_PATH)
TEST_JAVAX += $(TEST_MAIN)
TEST_JAVAX += $(TEST_RUNTIME_OUTPUT)

## test execution requirements
TEST_RUNTIME_REQS := $(TEST_RUNTIME_CLASS_PATH)
TEST_RUNTIME_REQS += $(TEST_COMPILE_MARKER)

else

## test delimiter
TEST_PATH_DELIMITER := $(MODULE_PATH_SEPARATOR)

## test module
ifndef TEST_MODULE
ifdef ARTIFACT_ID
TEST_MODULE := $(ARTIFACT_ID)
else
$(error The required variable TEST_MODULE was not defined)
endif
endif

## test java command
TEST_JAVAX += --module-path @$(TEST_RUNTIME_PATH)
ifdef TEST_ADD_MODULES
TEST_JAVAX += $(foreach mod,$(TEST_ADD_MODULES),--add-modules $(mod))
endif
TEST_JAVAX += --patch-module $(TEST_MODULE)=$(TEST_CLASS_OUTPUT)
TEST_JAVAX += --module $(TEST_MODULE)/$(TEST_MAIN)
TEST_JAVAX += $(TEST_RUNTIME_OUTPUT)

endif

## test execution requirements
TEST_RUNTIME_REQS := $(TEST_RUNTIME_PATH)
TEST_RUNTIME_REQS += $(TEST_COMPILE_MARKER)

#
# test execution targets
#

.PHONY: test
test: $(TEST_RUNTIME_REQS)
	$(TEST_JAVAX)

.PHONY: test@clean
test@clean:
	rm -f $(TEST_RUN_MARKER) $(TEST_RUNTIME_PATH)
	
.PHONY: re-test
re-test: test@clean test

$(TEST_RUNTIME_PATH): $(TEST_RUNTIME_RESOLUTION_FILES) | $(WORK)
	echo $(CLASS_OUTPUT) > $@.tmp
ifdef COMPILE_RESOLUTION_FILES
	cat $(COMPILE_RESOLUTION_FILES) | sort -u >> $@.tmp
endif
	echo $(TEST_CLASS_OUTPUT) >> $@.tmp
ifdef TEST_COMPILE_RESOLUTION_FILES
	cat $(TEST_COMPILE_RESOLUTION_FILES) | sort -u >> $@.tmp
endif
ifdef TEST_RUNTIME_RESOLUTION_FILES
	cat $(TEST_RUNTIME_RESOLUTION_FILES) | sort -u >> $@.tmp
endif
	cat $@.tmp | paste --delimiter='$(TEST_PATH_DELIMITER)' --serial > $@

$(TEST_RUN_MARKER): $(TEST_RUNTIME_REQS)
	$(TEST_JAVAX)
	touch $@
