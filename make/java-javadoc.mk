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
# javadoc task
#

ifndef COMPILE_MARKER
$(error Required java-compile.mk was not included)
endif

## javadoc command
JAVADOC := $(JAVA_HOME_BIN)/javadoc

## javadoc output path
JAVADOC_OUTPUT := $(WORK)/javadoc

## javadoc marker
JAVADOC_MARKER := $(JAVADOC_OUTPUT)/index.html

## javadoc module
ifndef JAVADOC_MODULE
ifdef ARTIFACT_ID
JAVADOC_MODULE := $(ARTIFACT_ID)
else
$(error The required variable JAVADOC_MODULE was not defined)
endif
endif

## javadoc command
JAVADOCX := $(JAVADOC)
JAVADOCX += -d $(JAVADOC_OUTPUT)
ifeq ($(ENABLE_PREVIEW),1)
JAVADOCX += --enable-preview
endif
JAVADOCX += --module $(JAVADOC_MODULE)
ifneq ($(COMPILE_PATH),)
JAVADOCX += --module-path @$(COMPILE_PATH)
endif
JAVADOCX += --source-path $(MAIN)
JAVADOCX += --release $(JAVA_RELEASE)
JAVADOCX += --show-module-contents api
JAVADOCX += --show-packages exported
ifdef JAVADOC_SNIPPET_PATH
JAVADOCX += --snippet-path $(JAVADOC_SNIPPET_PATH)
endif 
JAVADOCX += -bottom 'Copyright &\#169; $(COPYRIGHT_YEARS) <a href="https://www.objectos.com.br/">Objectos Software LTDA</a>. All rights reserved.'
JAVADOCX += -charset 'UTF-8'
JAVADOCX += -docencoding 'UTF-8'
JAVADOCX += -doctitle '$(GROUP_ID):$(ARTIFACT_ID) $(VERSION) API'
JAVADOCX += -encoding 'UTF-8'
JAVADOCX += -use
JAVADOCX += -version
JAVADOCX += -windowtitle '$(GROUP_ID):$(ARTIFACT_ID) $(VERSION) API'
JAVADOCX += -Xmaxerrs 65536
JAVADOCX += -Xmaxwarns 65536

## javadoc jar file
JAVADOC_JAR_FILE_NAME := $(ARTIFACT_ID)-$(VERSION)-javadoc.jar

## javadoc jar file
JAVADOC_JAR_FILE = $(WORK)/$(JAVADOC_JAR_FILE_NAME)

## javadoc jar command
JAVADOC_JARX := $(JAR)
JAVADOC_JARX += --create
JAVADOC_JARX += --file $(JAVADOC_JAR_FILE)
JAVADOC_JARX += -C $(JAVADOC_OUTPUT)
JAVADOC_JARX += .

#
# javadoc targets
#

.PHONY: javadoc
javadoc: $(JAVADOC_MARKER)

.PHONY: javadoc@clean
javadoc@clean:
	rm -rf $(JAVADOC_OUTPUT)

.PHONY: re-javadoc
re-javadoc: javadoc@clean javadoc

$(JAVADOC_MARKER): $(SOURCES)
	$(JAVADOCX)

$(JAVADOC_JAR_FILE): $(JAVADOC_MARKER)
	$(JAVADOC_JARX)
	