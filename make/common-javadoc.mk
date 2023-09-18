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

## javadoc output path
JAVADOC_OUTPUT = $(WORK)/javadoc

## javadoc marker
JAVADOC_MARKER = $(JAVADOC_OUTPUT)/index.html

## javadoc command
JAVADOCX = $(JAVADOC)
JAVADOCX += -d $(JAVADOC_OUTPUT)
ifdef ENABLE_PREVIEW
JAVADOCX += --enable-preview
endif
JAVADOCX += --module $(MODULE)
ifneq ($(COMPILE_MODULE_PATH),)
JAVADOCX += --module-path $(COMPILE_MODULE_PATH)
endif
JAVADOCX += --module-source-path "./*/main"
JAVADOCX += --release $(JAVA_RELEASE)
JAVADOCX += --show-module-contents api
JAVADOCX += --show-packages exported
ifdef JAVADOC_SNIPPET_PATH
JAVADOCX += --snippet-path $(JAVADOC_SNIPPET_PATH)
endif 
JAVADOCX += -bottom 'Copyright &\#169; 2022&\#x2013;2023 <a href="https://www.objectos.com.br/">Objectos Software LTDA</a>. All rights reserved.'
JAVADOCX += -charset 'UTF-8'
JAVADOCX += -docencoding 'UTF-8'
JAVADOCX += -doctitle '$(GROUP_ID):$(ARTIFACT_ID) $(VERSION) API'
JAVADOCX += -encoding 'UTF-8'
JAVADOCX += -use
JAVADOCX += -version
JAVADOCX += -windowtitle '$(GROUP_ID):$(ARTIFACT_ID) $(VERSION) API'

## javadoc artifact
JAVADOC_ARTIFACT = $(WORK)/$(ARTIFACT_ID)-$(VERSION)-javadoc.jar

## javadoc jar command
JAVADOC_JARX = $(JAR)
JAVADOC_JARX += --create
JAVADOC_JARX += --file $(JAVADOC_ARTIFACT)
JAVADOC_JARX += -C $(JAVADOC_OUTPUT)
JAVADOC_JARX += .

#
# javadoc target
#
.PHONY: javadoc
javadoc: $(JAVADOC_ARTIFACT)
 
$(JAVADOC_ARTIFACT): $(JAVADOC_MARKER)
	$(JAVADOC_JARX)

$(JAVADOC_MARKER): $(SOURCES)
	$(JAVADOCX)