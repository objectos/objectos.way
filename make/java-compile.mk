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
# java compilation options
#

## source directory
ifndef MAIN
MAIN := main
endif

## source files
SOURCES := $(shell find ${MAIN} -type f -name '*.java' -print)

## source files modified since last compilation (dynamically evaluated)
DIRTY :=

## holds the list of files to be compiled 
COMPILE_SOURCES := $(WORK)/compile-sources

## class output path
CLASS_OUTPUT := $(WORK)/main

## compiled classes
CLASSES := $(SOURCES:$(MAIN)/%.java=$(CLASS_OUTPUT)/%.class)

## compile dependencies
# COMPILE_DEPS := 

ifdef COMPILE_DEPS
## compile resolution files
COMPILE_RESOLUTION_FILES := $(call to-resolution-files,$(COMPILE_DEPS))

## compile module-path (or class-path)
COMPILE_PATH := $(WORK)/compile-path
endif

## annotation processing
ifdef PROCESSING_DEPS
## compile proc resolution files
PROCESSING_RESOLUTION_FILES := $(call to-resolution-files,$(PROCESSING_DEPS))

## compile proc module-path (or class-path)
PROCESSING_PATH := $(WORK)/compile-proc-path

## annotation processing output
PROCESSING_OUTPUT := $(WORK)/main-generated-sources
endif

## common javac options
JAVACX  = $(JAVAC)
JAVACX += -d $(CLASS_OUTPUT)
JAVACX += -Xlint:none
JAVACX += -Xpkginfo:always
ifdef COMPILE_MAXERRS
JAVACX += -Xmaxerrs $(COMPILE_MAXERRS)
endif

ifeq ($(COMPILE_MODE),class-path)

## path delimiter
COMPILE_PATH_DELIMITER := $(CLASS_PATH_SEPARATOR)

ifdef COMPILE_PATH
## class-path
JAVACX += --class-path @$(COMPILE_PATH)
endif

else

## path delimiter
COMPILE_PATH_DELIMITER := $(MODULE_PATH_SEPARATOR)

ifdef COMPILE_PATH
## module-path
JAVACX += --module-path @$(COMPILE_PATH)
endif

ifdef PROCESSING_PATH
## processing module-path
JAVACX += --processor-module-path @$(PROCESSING_PATH)
JAVACX += -s $(PROCESSING_OUTPUT)
endif

## module version
ifndef VERSION
VERSION := 1.0.0-SNAPSHOT
endif
JAVACX += --module-version $(VERSION)

endif

## common javac trailing options
ifeq ($(ENABLE_PREVIEW),1)
JAVACX += --enable-preview
endif
ifdef JAVA_RELEASE
JAVACX += --release $(JAVA_RELEASE)
endif
JAVACX += --source-path $(MAIN)
JAVACX += @$(COMPILE_SOURCES)

## compilation marker
COMPILE_MARKER = $(WORK)/compile-marker

## compilation requirements
ifndef COMPILE_REQS
COMPILE_REQS :=
endif
COMPILE_REQS += $(CLASSES)
ifdef COMPILE_RESOLUTION_FILES
COMPILE_REQS += $(COMPILE_PATH)
endif
ifdef PROCESSING_RESOLUTION_FILES
COMPILE_REQS += $(PROCESSING_PATH)
COMPILE_REQS += | $(PROCESSING_OUTPUT)
endif

## resources
# RESOURCES :=

ifdef RESOURCES
## test resources "source"
RESOURCES_SRC = $(shell find ${RESOURCES} -type f -print)

## test resources "output"
RESOURCES_OUT = $(RESOURCES_SRC:$(RESOURCES)/%=$(CLASS_OUTPUT)/%)

## target to copy test resources
$(RESOURCES_OUT): $(CLASS_OUTPUT)/%: $(RESOURCES)/%
	@mkdir --parents $(@D)
	cp $< $@
	
COMPILE_REQS += $(RESOURCES_OUT)
endif

#
# compilation targets
#

.PHONY: compile
compile: $(COMPILE_MARKER)

.PHONY: compile@clean
compile@clean:
	rm -rf $(CLASS_OUTPUT) $(COMPILE_MARKER) $(COMPILE_PATH) $(PROCESSING_OUTPUT)

.PHONY: re-compile
re-compile: compile@clean compile

$(COMPILE_PATH): $(COMPILE_RESOLUTION_FILES)
	$(call uniq-resolution-files,$(COMPILE_RESOLUTION_FILES)) > $@.tmp
	cat $@.tmp | paste --delimiter='$(COMPILE_PATH_DELIMITER)' --serial > $@

$(PROCESSING_PATH): $(PROCESSING_RESOLUTION_FILES)
	$(call uniq-resolution-files,$(PROCESSING_RESOLUTION_FILES)) > $@.tmp
	cat $@.tmp | paste --delimiter='$(COMPILE_PATH_DELIMITER)' --serial > $@
	
$(PROCESSING_OUTPUT):
	mkdir --parents $@
	
$(CLASSES): $(CLASS_OUTPUT)/%.class: $(MAIN)/%.java
	$(eval DIRTY += $$<)

$(COMPILE_MARKER): $(COMPILE_REQS)
	$(file > $(COMPILE_SOURCES).tmp,$(strip $(DIRTY)))
	cat $(COMPILE_SOURCES).tmp | tr -d '\n' > $(COMPILE_SOURCES)
	if [ -s $(COMPILE_SOURCES) ]; then \
		$(JAVACX); \
	fi
	echo "$(CLASS_OUTPUT)" > $@
ifdef COMPILE_RESOLUTION_FILES
	$(call uniq-resolution-files,$(COMPILE_RESOLUTION_FILES)) >> $@
endif
