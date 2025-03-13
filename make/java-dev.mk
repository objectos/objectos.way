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
# dev execution options
#

## dev module name
ifndef MODULE
ifdef ARTIFACT_ID
MODULE := $(ARTIFACT_ID)
else
$(error The required variable MODULE was not defined)
endif
endif

## dev main class
ifndef DEV_MAIN
$(error The required variable DEV_MAIN was not defined)
endif

## dev dependencies
ifdef DEV_DEPS
DEV_RESOLUTION_FILES := $(call to-resolution-files,$(DEV_DEPS))
endif

## dev module-path
DEV_MODULE_PATH := $(WORK)/dev-module-path

## dev jvm options
#DEV_JVM_OPTS

## dev app args
#DEV_APP_ARGS

## dev java command
DEV_JAVAX := $(JAVA)
ifdef DEV_JVM_OPTS
DEV_JAVAX += $(DEV_JVM_OPTS)
endif
DEV_JAVAX += --module-path @$(DEV_MODULE_PATH)
ifdef DEV_ADD_EXPORTS
DEV_JAVAX += $(foreach export,$(DEV_ADD_EXPORTS),--add-exports $(export))
endif
ifdef DEV_ADD_OPENS
DEV_JAVAX += $(foreach opens,$(DEV_ADD_OPENS),--add-opens $(opens))
endif
ifdef DEV_ADD_READS
DEV_JAVAX += $(foreach read,$(DEV_ADD_READS),--add-reads $(read))
endif
ifeq ($(ENABLE_PREVIEW),1)
DEV_JAVAX += --enable-preview
endif
DEV_JAVAX += --module $(MODULE)/$(DEV_MAIN)
ifdef DEV_APP_ARGS
DEV_JAVAX += $(DEV_APP_ARGS)
endif

.PHONY: dev
dev: $(DEV_MODULE_PATH)
	$(DEV_JAVAX)

.PHONY: dev@clean
dev@clean:
	rm -f $(DEV_MODULE_PATH)

.PHONY: re-dev
re-dev: dev@clean dev
	
$(DEV_MODULE_PATH): $(COMPILE_MARKER) $(DEV_RESOLUTION_FILES)
	echo $(CLASS_OUTPUT) > $@.tmp
ifdef COMPILE_RESOLUTION_FILES
	cat $(COMPILE_RESOLUTION_FILES) >> $@.tmp
endif
ifdef DEV_RESOLUTION_FILES
	cat $(DEV_RESOLUTION_FILES) >> $@.tmp
endif
	cat $@.tmp | paste --delimiter='$(MODULE_PATH_SEPARATOR)' --serial > $@

