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

.PHONY: all
all: jar

.PHONY: clean
clean:
	rm -rf $(WORK)/*

# Delete the default suffixes
.SUFFIXES:

#
# Tool options
#

## java home
ifdef JAVA_HOME
JAVA_HOME_BIN := $(JAVA_HOME)/bin
else
JAVA_HOME_BIN :=
endif

## java common options
JAVA := $(JAVA_HOME_BIN)/java

## javac common options
JAVAC := $(JAVA_HOME_BIN)/javac
JAVAC += -g
JAVAC += -Xpkginfo:always

## jar common options
JAR := $(JAVA_HOME_BIN)/jar

## javadoc common options
JAVADOC := $(JAVA_HOME_BIN)/javadoc

## curl common options
CURL := curl
CURL += --fail

## gpg common options
GPG := gpg

## jq common options
JQ := jq

## sed common options
SED := sed

#
# Dependencies related options & functions
#

## local repository path
LOCAL_REPO_PATH := $(HOME)/.cache/objectos

## remote repository URL
REMOTE_REPO_URL := https://repo.maven.apache.org/maven2

## remote repository curl
REMOTE_REPO_CURLX = $(CURL)
REMOTE_REPO_CURLX += --create-dirs

## dependency function
## 
## syntax:
## $(call dependency,[GROUP_ID],[ARTIFACT_ID],[VERSION])
dot := .
solidus := /

dependency = $(LOCAL_REPO_PATH)/$(subst $(dot),$(solidus),$(1))/$(2)/$(3)/$(2)-$(3).jar

## class-path function
##
## syntax:
## $(call class-path,[list of deps])
ifeq ($(OS),Windows_NT)
CLASS_PATH_SEPARATOR := ;
else
CLASS_PATH_SEPARATOR := :
endif
empty :=
space := $(empty) $(empty)

class-path = $(subst $(space),$(CLASS_PATH_SEPARATOR),$(1))

## module-path function
##
## syntax:
## $(call module-path,[list of deps])
MODULE_PATH_SEPARATOR := :

module-path = $(subst $(space),$(MODULE_PATH_SEPARATOR),$(1))

#
# Gets the dependency from the remote repository
#
$(LOCAL_REPO_PATH)/%.jar:	
	$(REMOTE_REPO_CURLX) --output $@ $(@:$(LOCAL_REPO_PATH)/%.jar=$(REMOTE_REPO_URL)/%.jar)

#
# main artifact options
#

## main base dir
MAIN = $(MODULE)/main

## main source path
SOURCE_PATH = $(MAIN)

## main source files
SOURCES = $(shell find ${SOURCE_PATH} -type f -name '*.java' -print)

## main source files modified since last compilation
MODIFIED_SOURCES :=

## main work dir
WORK = $(MODULE)/work

## main class output path
CLASS_OUTPUT = $(WORK)/main

## META-INF
META_INF_DIR = $(CLASS_OUTPUT)/META-INF

## license 'artifact'
LICENSE_ARTIFACT = $(META_INF_DIR)/LICENSE

## main compiled classes
CLASSES = $(SOURCES:$(SOURCE_PATH)/%.java=$(CLASS_OUTPUT)/%.class)

## main compile-time dependencies
# COMPILE_DEPS = 

## main compile-time module-path
COMPILE_MODULE_PATH = $(call module-path,$(COMPILE_DEPS))
 
## main javac command
## - do no set the module-path for artifacts that have no compile-time deps
JAVACX = $(JAVAC)
JAVACX += -d $(CLASS_OUTPUT)
JAVACX += -Xlint:all
ifdef ENABLE_PREVIEW
JAVACX += --enable-preview
endif
ifneq ($(COMPILE_MODULE_PATH),)
JAVACX += --module-path $(COMPILE_MODULE_PATH)
endif
JAVACX += --module-version $(VERSION)
JAVACX += --release $(JAVA_RELEASE)
JAVACX += $(MODIFIED_SOURCES)

## main generated artifact
ARTIFACT = $(WORK)/$(ARTIFACT_ID)-$(VERSION).jar

## main jar command
JARX = $(JAR)
JARX += --create
JARX += --file $(ARTIFACT)
JARX += --module-version $(VERSION)
JARX += -C $(CLASS_OUTPUT)
JARX += .

#
# jar target
#
.PHONY: jar
jar: $(ARTIFACT)

$(ARTIFACT): $(COMPILE_DEPS) $(CLASSES) $(LICENSE_ARTIFACT)
	if [ -n "$(MODIFIED_SOURCES)" ]; then \
		$(JAVACX); \
	fi
	$(JARX)

$(CLASSES): $(CLASS_OUTPUT)/%.class: $(SOURCE_PATH)/%.java
	$(eval MODIFIED_SOURCES += $$<)

$(LICENSE_ARTIFACT): LICENSE
	mkdir -p $(META_INF_DIR)
	cp LICENSE $(META_INF_DIR)
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

## install location
INSTALL = $(call dependency,$(GROUP_ID),$(ARTIFACT_ID),$(VERSION))

#
# install target
#
.PHONY: install
install: $(INSTALL)

$(INSTALL): jar
	mkdir --parents $(@D)
	cp $(ARTIFACT) $@
	
## sources jar artifact
SOURCE_ARTIFACT = $(WORK)/$(ARTIFACT_ID)-$(VERSION)-sources.jar

## sources jar command
SOURCE_JARX = $(JAR)
SOURCE_JARX += --create
SOURCE_JARX += --file $(SOURCE_ARTIFACT)
SOURCE_JARX += -C $(SOURCE_PATH)
SOURCE_JARX += .

#
# source-jar target
#
.PHONY: source-jar
source-jar: $(SOURCE_ARTIFACT)

$(SOURCE_ARTIFACT): $(SOURCES)
	$(SOURCE_JARX)
	
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
#
# Provides the pom target:
#
# - generates a pom.xml suitable for deploying to a maven repository
# 
# Requirements:
#
# - you must provide the pom template $(MODULE)/pom.xml.tmpl

## pom source
POM_SOURCE = $(MODULE)/pom.xml.tmpl

## pom file
POM_ARTIFACT = $(WORK)/pom.xml

## pom external variables
# POM_VARIABLES = 

## ossrh pom sed command
POM_SEDX = $(SED)
POM_SEDX += $(foreach var,$(POM_VARIABLES),--expression='s/%%$(var)%%/$($(var))/g')
POM_SEDX += --expression='s/%%ARTIFACT_ID%%/$(ARTIFACT_ID)/g'
POM_SEDX += --expression='s/%%GROUP_ID%%/$(GROUP_ID)/g'
POM_SEDX += --expression='s/%%VERSION%%/$(VERSION)/g'
POM_SEDX += --expression='w $(POM_ARTIFACT)'
POM_SEDX += $(POM_SOURCE)

#
# Targets
#

.PHONY: pom
pom: $(POM_ARTIFACT)

$(POM_ARTIFACT): $(POM_SOURCE) Makefile
	$(POM_SEDX)

## include ossrh config
## - OSSRH_GPG_KEY
## - OSSRH_GPG_PASSPHRASE
## - OSSRH_USERNAME
## - OSSRH_PASSWORD
-include $(HOME)/.config/objectos/ossrh-config.mk

## gpg command
GPGX = $(GPG)
GPGX += --armor
GPGX += --batch
GPGX += --default-key $(OSSRH_GPG_KEY)
GPGX += --detach-sign
GPGX += --passphrase $(OSSRH_GPG_PASSPHRASE)
GPGX += --pinentry-mode loopback
GPGX += --yes

## ossrh artifact
OSSRH_ARTIFACT = $(WORK)/$(ARTIFACT_ID)-$(VERSION)-bundle.jar

## ossrh jars
OSSRH_JARS = $(ARTIFACT)
OSSRH_JARS += $(POM_ARTIFACT)
OSSRH_JARS += $(SOURCE_ARTIFACT)
OSSRH_JARS += $(JAVADOC_ARTIFACT)

## ossrh sigs
OSSRH_SIGS := $(OSSRH_JARS:%=%.asc)

## ossrh jar command
OSSRH_JARX = $(JAR)
OSSRH_JARX += --create
OSSRH_JARX += --file $(OSSRH_ARTIFACT)
OSSRH_JARX += $(OSSRH_JARS:$(WORK)/%=-C $(WORK) %)
OSSRH_JARX += $(OSSRH_SIGS:$(WORK)/%=-C $(WORK) %)

## cookies
OSSRH_COOKIES = $(WORK)/ossrh-cookies.txt 

## ossrh login curl command
OSSRH_LOGIN_CURLX = $(CURL)
OSSRH_LOGIN_CURLX += --cookie-jar $(OSSRH_COOKIES)
OSSRH_LOGIN_CURLX += --output /dev/null
OSSRH_LOGIN_CURLX += --request GET
OSSRH_LOGIN_CURLX += --silent
OSSRH_LOGIN_CURLX += --url https://oss.sonatype.org/service/local/authentication/login
OSSRH_LOGIN_CURLX += --user $(OSSRH_USERNAME):$(OSSRH_PASSWORD)

## ossrh response json
OSSRH_UPLOAD_JSON = $(WORK)/ossrh-upload.json

## ossrh upload curl command
OSSRH_UPLOAD_CURLX = $(CURL)
OSSRH_UPLOAD_CURLX += --cookie $(OSSRH_COOKIES)
OSSRH_UPLOAD_CURLX += --header 'Content-Type: multipart/form-data'
OSSRH_UPLOAD_CURLX += --form file=@$(OSSRH_ARTIFACT)
OSSRH_UPLOAD_CURLX += --output $(OSSRH_UPLOAD_JSON)
OSSRH_UPLOAD_CURLX += --request POST
OSSRH_UPLOAD_CURLX += --url https://oss.sonatype.org/service/local/staging/bundle_upload

## ossrh repository id sed parsing
OSSRH_SEDX = $(SED)
OSSRH_SEDX += --regexp-extended
OSSRH_SEDX += --silent
OSSRH_SEDX += 's/^.*repositories\/(.*)".*/\1/p'
OSSRH_SEDX += $(OSSRH_UPLOAD_JSON)

## repository id
OSSRH_REPOSITORY_ID = $(shell $(OSSRH_SEDX))

## ossrh release curl command
OSSRH_RELEASE_CURLX = $(CURL)
OSSRH_RELEASE_CURLX += --cookie $(OSSRH_COOKIES)
OSSRH_RELEASE_CURLX += --data '{"data":{"autoDropAfterRelease":true,"description":"","stagedRepositoryIds":["$(OSSRH_REPOSITORY_ID)"]}}'
OSSRH_RELEASE_CURLX += --header 'Content-Type: application/json'
OSSRH_RELEASE_CURLX += --request POST
OSSRH_RELEASE_CURLX += --url https://oss.sonatype.org/service/local/staging/bulk/promote

#
# ossrh target
#
.PHONY: ossrh
ossrh: $(OSSRH_UPLOAD_JSON)
	@for i in 1 2 3; do \
	  echo "Waiting before release..."; \
	  sleep 40; \
	  echo "Trying to release $(OSSRH_REPOSITORY_ID)"; \
	  $(OSSRH_RELEASE_CURLX); \
	  if [ $$? -eq 0 ]; then \
	    exit 0; \
	  fi \
	done

$(OSSRH_UPLOAD_JSON): $(OSSRH_ARTIFACT)
	@$(OSSRH_LOGIN_CURLX)
	$(OSSRH_UPLOAD_CURLX)

.PHONY: ossrh-bundle
ossrh-bundle: $(OSSRH_ARTIFACT)
 
$(OSSRH_ARTIFACT): $(OSSRH_SIGS)
	$(OSSRH_JARX)

%.asc: %
	@$(GPGX) $<

## include release config
## - GH_TOKEN
-include $(HOME)/.config/objectos/release-config.mk

## GitHub API
GH_API := https://api.github.com/repos/objectos/$(ARTIFACT_ID)

## GitHub milestone title
GH_MILESTONE_TITLE := v$(VERSION)
GH_TAG := $(GH_MILESTONE_TITLE)

## GitHub milestones curl command
GH_MILESTONE_CURLX := $(CURL)
GH_MILESTONE_CURLX += '$(GH_API)/milestones'

## GitHub milestone number parsing
GH_MILESTONE_JQX := $(JQ)
GH_MILESTONE_JQX += '.[] | select(.title == "$(GH_MILESTONE_TITLE)") | .number'

## GitHub milestone ID
GH_MILESTONE_ID = $(shell $(GH_MILESTONE_CURLX) | $(GH_MILESTONE_JQX))

## Issues from GitHub
GH_ISSUES_JSON := $(WORK)/github-issues.json

## GitHub issues curl command
GH_ISSUES_CURLX = $(CURL)
GH_ISSUES_CURLX += '$(GH_API)/issues?milestone=$(GH_MILESTONE_ID)&per_page=100&state=all'
GH_ISSUES_CURLX += >
GH_ISSUES_CURLX += $(GH_ISSUES_JSON)

##
GH_RELEASE_BODY := $(WORK)/github-release.md

## Filter and format issues
GH_ISSUES_JQX = $(JQ)
GH_ISSUES_JQX += --arg LABEL "$(LABEL)"
GH_ISSUES_JQX += --raw-output
GH_ISSUES_JQX += 'sort_by(.number) | [.[] | {number: .number, title: .title, label: .labels[] | select(.name) | .name}] | .[] | select(.label == $$LABEL) | "- \(.title) \#\(.number)"'
GH_ISSUES_JQX += $(GH_ISSUES_JSON)

gh_issues = $(let LABEL,$1,$(GH_ISSUES_JQX))

##
GH_RELEASE_JSON := $(WORK)/github-release.json

##
GH_RELEASE_JQX = $(JQ)
GH_RELEASE_JQX += --raw-input
GH_RELEASE_JQX += --slurp
GH_RELEASE_JQX += '. as $$body | {"tag_name":"$(GH_TAG)","name":"Release $(GH_TAG)","body":$$body,"draft":true,"prerelease":false,"generate_release_notes":false}'
GH_RELEASE_JQX += $(GH_RELEASE_BODY)

## 
GH_RELEASE_CURLX := $(CURL)
GH_RELEASE_CURLX += --data-binary "@$(GH_RELEASE_JSON)"
GH_RELEASE_CURLX += --header "Accept: application/vnd.github+json"
GH_RELEASE_CURLX += --header "Authorization: Bearer $(GH_TOKEN)"
GH_RELEASE_CURLX += --header "X-GitHub-Api-Version: 2022-11-28"
GH_RELEASE_CURLX += --location
GH_RELEASE_CURLX += --request POST
GH_RELEASE_CURLX +=  $(GH_API)/releases

#
# Targets
#

.PHONY: release
release: $(GH_RELEASE_JSON)
	@$(GH_RELEASE_CURLX)

$(GH_ISSUES_JSON):
	$(GH_ISSUES_CURLX)
	
$(GH_RELEASE_BODY): $(GH_ISSUES_JSON)
	echo '## Enhancements' > $(GH_RELEASE_BODY)
	echo '' >> $(GH_RELEASE_BODY)
	$(call gh_issues,enhancement) >> $(GH_RELEASE_BODY) 
	echo '' >> $(GH_RELEASE_BODY)
	echo '## Bug Fixes' >> $(GH_RELEASE_BODY)
	echo '' >> $(GH_RELEASE_BODY)
	$(call gh_issues,bug) >> $(GH_RELEASE_BODY) 
	echo '' >> $(GH_RELEASE_BODY)
	echo '## Documentation' >> $(GH_RELEASE_BODY)
	echo '' >> $(GH_RELEASE_BODY)
	$(call gh_issues,documentation) >> $(GH_RELEASE_BODY) 

$(GH_RELEASE_JSON): $(GH_RELEASE_BODY)
	$(GH_RELEASE_JQX) > $(GH_RELEASE_JSON) 
