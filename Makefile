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

# This file was generated. Do not edit!

#
# Objectos Way
#

GROUP_ID := br.com.objectos
ARTIFACT_ID := objectos.way
VERSION := 0.1.5.5-SNAPSHOT

## Deps versions

TESTNG_VERSION := 7.7.1
JCOMMANDER_VERSION := 1.82
SLF4J_VERSION := 1.7.36

JAVA_RELEASE := 21

#
# objectos.core.object options
#

## directory
CORE_OBJECT := objectos.core.object

## module
CORE_OBJECT_MODULE := $(CORE_OBJECT)

## module version
CORE_OBJECT_VERSION := $(VERSION)

## javac --release option
CORE_OBJECT_JAVA_RELEASE := $(JAVA_RELEASE)

## --enable-preview ?
CORE_OBJECT_ENABLE_PREVIEW := 0

## jar name
CORE_OBJECT_JAR_NAME := $(CORE_OBJECT)

## test compile deps
CORE_OBJECT_TEST_COMPILE_DEPS = $(CORE_OBJECT_JAR_FILE)
CORE_OBJECT_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test runtime dependencies
CORE_OBJECT_TEST_RUNTIME_DEPS = $(CODE_TEST_COMPILE_DEPS)
CORE_OBJECT_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
CORE_OBJECT_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
CORE_OBJECT_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

#
# objectos.way options
# 

## way directory
WAY := objectos.way

## way module
WAY_MODULE := $(WAY)

## way module version
WAY_VERSION := $(VERSION)

## way javac --release option
WAY_JAVA_RELEASE := 21

## way --enable-preview ?
WAY_ENABLE_PREVIEW := 0

## way jar name
WAY_JAR_NAME := $(WAY)

## way JS source
WAY_JS_SRC = $(WAY)/js/objectos.way.js

## way JS artifact
WAY_JS_ARTIFACT = $(WAY_CLASS_OUTPUT)/objectos/js/objectos.way.js

## way jar file reqs
WAY_JAR_FILE_REQS_MORE = $(WAY_JS_ARTIFACT)

## way test compile-time dependencies
WAY_TEST_COMPILE_DEPS = $(WAY_JAR_FILE)
WAY_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## way test runtime dependencies
WAY_TEST_RUNTIME_DEPS = $(WAY_TEST_COMPILE_DEPS)
WAY_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
WAY_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
WAY_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## way test runtime reads
WAY_TEST_JAVAX_READS := java.compiler

## way test runtime exports
WAY_TEST_JAVAX_EXPORTS := objectos.html.internal
WAY_TEST_JAVAX_EXPORTS += objectos.util
WAY_TEST_JAVAX_EXPORTS += objectox.css
WAY_TEST_JAVAX_EXPORTS += objectox.css.util
WAY_TEST_JAVAX_EXPORTS += objectox.http
WAY_TEST_JAVAX_EXPORTS += objectox.lang

## way install coordinates
WAY_GROUP_ID := $(GROUP_ID)
WAY_ARTIFACT_ID := $(ARTIFACT_ID)

## way copyright years for javadoc
WAY_COPYRIGHT_YEARS := 2022-2023

## way javadoc snippet path
WAY_JAVADOC_SNIPPET_PATH := WAY_TEST

# Delete the default suffixes
.SUFFIXES:

#
# Default target
#

.PHONY: all
all: jar

.PHONY: jar
jar: way@jar

#
# Defines the tools
#

## configures JAVA_HOME_BIN
ifdef JAVA_HOME
JAVA_HOME_BIN := $(JAVA_HOME)/bin
else
JAVA_HOME_BIN :=
endif

## java command
JAVA := $(JAVA_HOME_BIN)/java

## javac command
JAVAC := $(JAVA_HOME_BIN)/javac
JAVAC += -g

## jar command
JAR := $(JAVA_HOME_BIN)/jar

## javadoc command
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

## include ossrh config
## - OSSRH_GPG_KEY
## - OSSRH_GPG_PASSPHRASE
## - OSSRH_USERNAME
## - OSSRH_PASSWORD
-include $(HOME)/.config/objectos/ossrh-config.mk

## include GH config
## - GH_TOKEN
-include $(HOME)/.config/objectos/gh-config.mk

#
# compilation options
#

define COMPILE

## source directory
$(1)MAIN = $$($(1)MODULE)/main

## source files
$(1)SOURCES = $$(shell find $${$(1)MAIN} -type f -name '*.java' -print)

## source files modified since last compilation
$(1)DIRTY :=

## work dir
$(1)WORK = $$($(1)MODULE)/work

## class output path
$(1)CLASS_OUTPUT = $$($(1)WORK)/main

## compiled classes
$(1)CLASSES = $$($(1)SOURCES:$$($(1)MAIN)/%.java=$$($(1)CLASS_OUTPUT)/%.class)

## compile-time dependencies
# $(1)COMPILE_DEPS = 

## compile-time module-path
$(1)COMPILE_MODULE_PATH = $$(call module-path,$$($(1)COMPILE_DEPS))
 
## javac command
$(1)JAVACX = $$(JAVAC)
$(1)JAVACX += -d $$($(1)CLASS_OUTPUT)
$(1)JAVACX += -g
$(1)JAVACX += -Xlint:all
$(1)JAVACX += -Xpkginfo:always
ifeq ($$($(1)ENABLE_PREVIEW),1)
$(1)JAVACX += --enable-preview
endif
ifneq ($$($(1)COMPILE_MODULE_PATH),)
$(1)JAVACX += --module-path $$($(1)COMPILE_MODULE_PATH)
endif
$(1)JAVACX += --module-version $$($(1)VERSION)
$(1)JAVACX += --release $$($(1)JAVA_RELEASE)
$(1)JAVACX += $$($(1)DIRTY)

## resources
# $(1)RESOURCES =

## compilation marker
$(1)COMPILE_MARKER = $$($(1)WORK)/compile-marker

#
# compilation targets
#

$$($(1)COMPILE_MARKER): $$($(1)COMPILE_DEPS) $$($(1)CLASSES) $$($(1)RESOURCES)
	if [ -n "$$($(1)DIRTY)" ]; then \
		$$($(1)JAVACX); \
	fi
	touch $$@

$$($(1)CLASSES): $$($(1)CLASS_OUTPUT)/%.class: $$($(1)MAIN)/%.java
	$$(eval $(1)DIRTY += $$$$<)

endef
#
# objectos.way jar options
#

## objectos.way license 'artifact'
WAY_LICENSE = $(WAY_CLASS_OUTPUT)/META-INF/LICENSE

## objectos.way jar file path
WAY_JAR_FILE = $(WAY_WORK)/$(WAY_JAR_NAME)-$(WAY_VERSION).jar

## objectos.way jar command
WAY_JARX = $(JAR)
WAY_JARX += --create
WAY_JARX += --file $(WAY_JAR_FILE)
WAY_JARX += --module-version $(WAY_VERSION)
WAY_JARX += -C $(WAY_CLASS_OUTPUT)
WAY_JARX += .

## requirements of the WAY_JAR_FILE target
WAY_JAR_FILE_REQS = $(WAY_COMPILE_MARKER)
WAY_JAR_FILE_REQS += $(WAY_LICENSE)
ifdef WAY_JAR_FILE_REQS_MORE
WAY_JAR_FILE_REQS += $(WAY_JAR_FILE_REQS_MORE)
endif

#
# objectos.way jar targets
#

$(WAY_JAR_FILE): $(WAY_JAR_FILE_REQS)
	$(WAY_JARX)

$(WAY_LICENSE): LICENSE
	mkdir --parents $(@D)
	cp LICENSE $(@D)

#
# objectos.way test compilation options
#

## objectos.way test source directory
WAY_TEST = $(WAY_MODULE)/test

## objectos.way test source files 
WAY_TEST_SOURCES = $(shell find ${WAY_TEST} -type f -name '*.java' -print)

## objectos.way test source files modified since last compilation
WAY_TEST_DIRTY :=

## objectos.way test class output path
WAY_TEST_CLASS_OUTPUT = $(WAY_WORK)/test

## objectos.way test compiled classes
WAY_TEST_CLASSES = $(WAY_TEST_SOURCES:$(WAY_TEST)/%.java=$(WAY_TEST_CLASS_OUTPUT)/%.class)

## objectos.way test compile-time dependencies
# WAY_TEST_COMPILE_DEPS =

## objectos.way test javac command
WAY_TEST_JAVACX = $(JAVAC)
WAY_TEST_JAVACX += -d $(WAY_TEST_CLASS_OUTPUT)
WAY_TEST_JAVACX += -g
WAY_TEST_JAVACX += -Xlint:all
WAY_TEST_JAVACX += --class-path $(call class-path,$(WAY_TEST_COMPILE_DEPS))
ifeq ($(WAY_ENABLE_PREVIEW),1)
WAY_TEST_JAVACX += --enable-preview
endif
WAY_TEST_JAVACX += --release $(WAY_JAVA_RELEASE)
WAY_TEST_JAVACX += $(WAY_TEST_DIRTY)

## objectos.way test compilation marker
WAY_TEST_COMPILE_MARKER = $(WAY_WORK)/test-compile-marker

#
# objectos.way test compilation targets
#

$(WAY_TEST_COMPILE_MARKER): $(WAY_TEST_COMPILE_DEPS) $(WAY_TEST_CLASSES) 
	if [ -n "$(WAY_TEST_DIRTY)" ]; then \
		$(WAY_TEST_JAVACX); \
	fi
	touch $@

$(WAY_TEST_CLASSES): $(WAY_TEST_CLASS_OUTPUT)/%.class: $(WAY_TEST)/%.java
	$(eval WAY_TEST_DIRTY += $$<)

#
# objectos.way test execution options
#

## objectos.way test runtime dependencies
# WAY_TEST_RUNTIME_DEPS =

## objectos.way test main class
ifndef WAY_TEST_MAIN
WAY_TEST_MAIN = $(WAY_MODULE).RunTests
endif

## objectos.way test runtime output path
WAY_TEST_RUNTIME_OUTPUT = $(WAY_WORK)/test-output

## objectos.way test java command
WAY_TEST_JAVAX = $(JAVA)
WAY_TEST_JAVAX += --module-path $(call module-path,$(WAY_TEST_RUNTIME_DEPS))
WAY_TEST_JAVAX += --add-modules org.testng
WAY_TEST_JAVAX += --add-reads $(WAY_MODULE)=org.testng
ifdef WAY_TEST_JAVAX_READS
WAY_TEST_JAVAX += $(foreach mod,$(WAY_TEST_JAVAX_READS),--add-reads $(WAY_MODULE)=$(mod))
endif
ifdef WAY_TEST_JAVAX_EXPORTS
WAY_TEST_JAVAX += $(foreach pkg,$(WAY_TEST_JAVAX_EXPORTS),--add-exports $(WAY_MODULE)/$(pkg)=org.testng)
endif
ifeq ($(WAY_ENABLE_PREVIEW),1)
WAY_TEST_JAVAX += --enable-preview
endif
WAY_TEST_JAVAX += --patch-module $(WAY_MODULE)=$(WAY_TEST_CLASS_OUTPUT)
WAY_TEST_JAVAX += --module $(WAY_MODULE)/$(WAY_TEST_MAIN)
WAY_TEST_JAVAX += $(WAY_TEST_RUNTIME_OUTPUT)

## objectos.way test execution marker
WAY_TEST_RUN_MARKER = $(WAY_TEST_RUNTIME_OUTPUT)/index.html

#
# objectos.way test execution targets
#

$(WAY_TEST_RUN_MARKER): $(WAY_TEST_COMPILE_MARKER) 
	$(WAY_TEST_JAVAX)

#
# objectos.way install options
#

## objectos.way install location
WAY_INSTALL = $(call dependency,$(WAY_GROUP_ID),$(WAY_ARTIFACT_ID),$(WAY_VERSION))

#
# objectos.way install target
#

$(WAY_INSTALL): $(WAY_JAR_FILE)
	mkdir --parents $(@D)
	cp $< $@

#
# objectos.way source-jar options
#

## objectos.way source-jar file
WAY_SOURCE_JAR_FILE = $(WAY_WORK)/$(WAY_JAR_NAME)-$(WAY_VERSION)-sources.jar

## objectos.way source-jar command
WAY_SOURCE_JARX = $(JAR)
WAY_SOURCE_JARX += --create
WAY_SOURCE_JARX += --file $(WAY_SOURCE_JAR_FILE)
WAY_SOURCE_JARX += -C $(WAY_MAIN)
WAY_SOURCE_JARX += .

#
# objectos.way source-jar targets
#

$(WAY_SOURCE_JAR_FILE): $(WAY_SOURCES)
	$(WAY_SOURCE_JARX)
	
#
# objectos.way javadoc options
#

## objectos.way javadoc output path
WAY_JAVADOC_OUTPUT = $(WAY_WORK)/javadoc

## objectos.way javadoc marker
WAY_JAVADOC_MARKER = $(WAY_JAVADOC_OUTPUT)/index.html

## objectos.way javadoc command
WAY_JAVADOCX = $(JAVADOC)
WAY_JAVADOCX += -d $(WAY_JAVADOC_OUTPUT)
ifeq ($(WAY_ENABLE_PREVIEW),1)
WAY_JAVADOCX += --enable-preview
endif
WAY_JAVADOCX += --module $(WAY_MODULE)
ifneq ($(WAY_COMPILE_MODULE_PATH),)
WAY_JAVADOCX += --module-path $(WAY_COMPILE_MODULE_PATH)
endif
WAY_JAVADOCX += --module-source-path "./*/main"
WAY_JAVADOCX += --release $(WAY_JAVA_RELEASE)
WAY_JAVADOCX += --show-module-contents api
WAY_JAVADOCX += --show-packages exported
ifdef WAY_JAVADOC_SNIPPET_PATH
WAY_JAVADOCX += --snippet-path $($(WAY_JAVADOC_SNIPPET_PATH))
endif 
WAY_JAVADOCX += -bottom 'Copyright &\#169; $(WAY_COPYRIGHT_YEARS) <a href="https://www.objectos.com.br/">Objectos Software LTDA</a>. All rights reserved.'
WAY_JAVADOCX += -charset 'UTF-8'
WAY_JAVADOCX += -docencoding 'UTF-8'
WAY_JAVADOCX += -doctitle '$(WAY_GROUP_ID):$(WAY_ARTIFACT_ID) $(WAY_VERSION) API'
WAY_JAVADOCX += -encoding 'UTF-8'
WAY_JAVADOCX += -use
WAY_JAVADOCX += -version
WAY_JAVADOCX += -windowtitle '$(WAY_GROUP_ID):$(WAY_ARTIFACT_ID) $(WAY_VERSION) API'

## objectos.way javadoc jar file
WAY_JAVADOC_JAR_FILE = $(WAY_WORK)/$(WAY_ARTIFACT_ID)-$(WAY_VERSION)-javadoc.jar

## objectos.way javadoc jar command
WAY_JAVADOC_JARX = $(JAR)
WAY_JAVADOC_JARX += --create
WAY_JAVADOC_JARX += --file $(WAY_JAVADOC_JAR_FILE)
WAY_JAVADOC_JARX += -C $(WAY_JAVADOC_OUTPUT)
WAY_JAVADOC_JARX += .

#
# objectos.way javadoc targets
#

$(WAY_JAVADOC_JAR_FILE): $(WAY_JAVADOC_MARKER)
	$(WAY_JAVADOC_JARX)

$(WAY_JAVADOC_MARKER): $(WAY_SOURCES)
	$(WAY_JAVADOCX)

#
# Provides the pom target:
#
# - generates a pom.xml suitable for deploying to a maven repository
# 
# Requirements:
#
# - you must provide the pom template $(MODULE)/pom.xml.tmpl

## objectos.way pom source
WAY_POM_SOURCE = $(WAY_MODULE)/pom.xml.tmpl

## objectos.way pom file
WAY_POM_FILE = $(WAY_WORK)/pom.xml

## objectos.way pom external variables
# WAY_POM_VARIABLES = 

## objectos.way ossrh pom sed command
WAY_POM_SEDX = $(SED)
WAY_POM_SEDX += $(foreach var,$(POM_VARIABLES),--expression='s/@$(var)@/$($(var))/g')
WAY_POM_SEDX += --expression='s/@COPYRIGHT_YEARS@/$(WAY_COPYRIGHT_YEARS)/g'
WAY_POM_SEDX += --expression='s/@ARTIFACT_ID@/$(WAY_ARTIFACT_ID)/g'
WAY_POM_SEDX += --expression='s/@GROUP_ID@/$(WAY_GROUP_ID)/g'
WAY_POM_SEDX += --expression='s/@VERSION@/$(WAY_VERSION)/g'
WAY_POM_SEDX += --expression='w $(WAY_POM_FILE)'
WAY_POM_SEDX += $(WAY_POM_SOURCE)

#
# Targets
#

$(WAY_POM_FILE): $(WAY_POM_SOURCE) Makefile
	$(WAY_POM_SEDX)

## objectos.way gpg command
WAY_GPGX = $(GPG)
WAY_GPGX += --armor
WAY_GPGX += --batch
WAY_GPGX += --default-key $(OSSRH_GPG_KEY)
WAY_GPGX += --detach-sign
WAY_GPGX += --passphrase $(OSSRH_GPG_PASSPHRASE)
WAY_GPGX += --pinentry-mode loopback
WAY_GPGX += --yes

## objectos.way ossrh bundle jar file
WAY_OSSRH_BUNDLE = $(WAY_WORK)/$(WAY_ARTIFACT_ID)-$(WAY_VERSION)-bundle.jar

## objectos.way ossrh bundle contents
WAY_OSSRH_CONTENTS = $(WAY_POM_FILE)
WAY_OSSRH_CONTENTS += $(WAY_JAR_FILE)
WAY_OSSRH_CONTENTS += $(WAY_SOURCE_JAR_FILE)
WAY_OSSRH_CONTENTS += $(WAY_JAVADOC_JAR_FILE)

## objectos.way ossrh sigs
WAY_OSSRH_SIGS = $(WAY_OSSRH_CONTENTS:%=%.asc)

## objectos.way ossrh bundle jar command
WAY_OSSRH_JARX = $(JAR)
WAY_OSSRH_JARX += --create
WAY_OSSRH_JARX += --file $(WAY_OSSRH_BUNDLE)
WAY_OSSRH_JARX += $(WAY_OSSRH_CONTENTS:$(WAY_WORK)/%=-C $(WAY_WORK) %)
WAY_OSSRH_JARX += $(WAY_OSSRH_SIGS:$(WAY_WORK)/%=-C $(WAY_WORK) %)

#
# objectos.way ossrh bundle targets
#

$(WAY_OSSRH_BUNDLE): $(WAY_OSSRH_SIGS)
	$(WAY_OSSRH_JARX)

%.asc: %
	@$(WAY_GPGX) $<

## objectos.way ossrh cookies
WAY_OSSRH_COOKIES = $(WAY_WORK)/ossrh-cookies.txt 

## objectos.way ossrh login curl command
WAY_OSSRH_LOGIN_CURLX = $(CURL)
WAY_OSSRH_LOGIN_CURLX += --cookie-jar $(WAY_OSSRH_COOKIES)
WAY_OSSRH_LOGIN_CURLX += --output /dev/null
WAY_OSSRH_LOGIN_CURLX += --request GET
WAY_OSSRH_LOGIN_CURLX += --silent
WAY_OSSRH_LOGIN_CURLX += --url https://oss.sonatype.org/service/local/authentication/login
WAY_OSSRH_LOGIN_CURLX += --user $(OSSRH_USERNAME):$(OSSRH_PASSWORD)

## objectos.way ossrh response json
WAY_OSSRH_UPLOAD_JSON = $(WAY_WORK)/ossrh-upload.json

## objectos.way ossrh upload curl command
WAY_OSSRH_UPLOAD_CURLX = $(CURL)
WAY_OSSRH_UPLOAD_CURLX += --cookie $(WAY_OSSRH_COOKIES)
WAY_OSSRH_UPLOAD_CURLX += --header 'Content-Type: multipart/form-data'
WAY_OSSRH_UPLOAD_CURLX += --form file=@$(WAY_OSSRH_BUNDLE)
WAY_OSSRH_UPLOAD_CURLX += --output $(WAY_OSSRH_UPLOAD_JSON)
WAY_OSSRH_UPLOAD_CURLX += --request POST
WAY_OSSRH_UPLOAD_CURLX += --url https://oss.sonatype.org/service/local/staging/bundle_upload

## objectos.way ossrh repository id sed parsing
WAY_OSSRH_SEDX = $(SED)
WAY_OSSRH_SEDX += --regexp-extended
WAY_OSSRH_SEDX += --silent
WAY_OSSRH_SEDX += 's/^.*repositories\/(.*)".*/\1/p'
WAY_OSSRH_SEDX += $(WAY_OSSRH_UPLOAD_JSON)

## objectos.way repository id
WAY_OSSRH_REPOSITORY_ID = $(shell $(WAY_OSSRH_SEDX))

## objectos.way ossrh release curl command
WAY_OSSRH_RELEASE_CURLX = $(CURL)
WAY_OSSRH_RELEASE_CURLX += --cookie $(WAY_OSSRH_COOKIES)
WAY_OSSRH_RELEASE_CURLX += --data '{"data":{"autoDropAfterRelease":true,"description":"","stagedRepositoryIds":["$(WAY_OSSRH_REPOSITORY_ID)"]}}'
WAY_OSSRH_RELEASE_CURLX += --header 'Content-Type: application/json'
WAY_OSSRH_RELEASE_CURLX += --request POST
WAY_OSSRH_RELEASE_CURLX += --url https://oss.sonatype.org/service/local/staging/bulk/promote

## objectos.way ossrh marker
WAY_OSSRH_MARKER = $(WAY_WORK)/ossrh-marker

#
# objectos.way ossrh targets
#

$(WAY_OSSRH_MARKER): $(WAY_OSSRH_UPLOAD_JSON)
	@for i in 1 2 3; do \
	  echo "Waiting before release..."; \
	  sleep 45; \
	  echo "Trying to release $(WAY_OSSRH_REPOSITORY_ID)"; \
	  $(WAY_OSSRH_RELEASE_CURLX); \
	  if [ $$? -eq 0 ]; then \
	    exit 0; \
	  fi \
	done
	touch $@

$(WAY_OSSRH_UPLOAD_JSON): $(WAY_OSSRH_BUNDLE)
	@$(WAY_OSSRH_LOGIN_CURLX)
	$(WAY_OSSRH_UPLOAD_CURLX)

#
# objectos.way GitHub release
#

## objectos.way GitHub API
ifndef WAY_GH_API
WAY_GH_API = https://api.github.com/repos/objectos/$(WAY_ARTIFACT_ID)
endif

## objectos.way GitHub milestone title
WAY_GH_MILESTONE_TITLE = v$(WAY_VERSION)

## objectos.way GitHub milestones curl command
WAY_GH_MILESTONE_CURLX = $(CURL)
WAY_GH_MILESTONE_CURLX += '$(WAY_GH_API)/milestones'

## objectos.way GitHub milestone number parsing
WAY_GH_MILESTONE_JQX = $(JQ)
WAY_GH_MILESTONE_JQX += '.[] | select(.title == "$(WAY_GH_MILESTONE_TITLE)") | .number'

## objectos.way GitHub milestone ID
WAY_GH_MILESTONE_ID = $(shell $(WAY_GH_MILESTONE_CURLX) | $(WAY_GH_MILESTONE_JQX))

## objectos.way Issues from GitHub
WAY_GH_ISSUES_JSON = $(WAY_WORK)/gh-issues.json

## objectos.way GitHub issues curl command
WAY_GH_ISSUES_CURLX = $(CURL)
WAY_GH_ISSUES_CURLX += '$(WAY_GH_API)/issues?milestone=$(WAY_GH_MILESTONE_ID)&per_page=100&state=all'
WAY_GH_ISSUES_CURLX += >
WAY_GH_ISSUES_CURLX += $(WAY_GH_ISSUES_JSON)

##
WAY_GH_RELEASE_BODY = $(WAY_WORK)/gh-release.md

## objectos.way Filter and format issues
WAY_GH_ISSUES_JQX = $(JQ)
WAY_GH_ISSUES_JQX += --arg LABEL "$(LABEL)"
WAY_GH_ISSUES_JQX += --raw-output
WAY_GH_ISSUES_JQX += 'sort_by(.number) | [.[] | {number: .number, title: .title, label: .labels[] | select(.name) | .name}] | .[] | select(.label == $$LABEL) | "- \(.title) \#\(.number)"'
WAY_GH_ISSUES_JQX += $(WAY_GH_ISSUES_JSON)

WAY_gh_issues = $(let LABEL,$1,$(WAY_GH_ISSUES_JQX))

##
WAY_GH_RELEASE_JSON := $(WAY_WORK)/gh-release.json

## objectos.way git tag name to be generated
WAY_GH_TAG := $(WAY_GH_MILESTONE_TITLE)

##
WAY_GH_RELEASE_JQX = $(JQ)
WAY_GH_RELEASE_JQX += --raw-input
WAY_GH_RELEASE_JQX += --slurp
WAY_GH_RELEASE_JQX += '. as $$body | {"tag_name":"$(WAY_GH_TAG)","name":"Release $(WAY_GH_TAG)","body":$$body,"draft":true,"prerelease":false,"generate_release_notes":false}'
WAY_GH_RELEASE_JQX += $(WAY_GH_RELEASE_BODY)

## 
WAY_GH_RELEASE_CURLX = $(CURL)
WAY_GH_RELEASE_CURLX += --data-binary "@$(WAY_GH_RELEASE_JSON)"
WAY_GH_RELEASE_CURLX += --header "Accept: application/vnd.github+json"
WAY_GH_RELEASE_CURLX += --header "Authorization: Bearer $(GH_TOKEN)"
WAY_GH_RELEASE_CURLX += --header "X-GitHub-Api-Version: 2022-11-28"
WAY_GH_RELEASE_CURLX += --location
WAY_GH_RELEASE_CURLX += --request POST
WAY_GH_RELEASE_CURLX +=  $(WAY_GH_API)/releases

##
WAY_GH_RELEASE_MARKER = $(WAY_WORK)/gh-release-marker 

#
# objectos.way GitHub release targets
#

$(WAY_GH_RELEASE_MARKER): $(WAY_GH_RELEASE_JSON)
	@$(WAY_GH_RELEASE_CURLX)
	touch $@

$(WAY_GH_RELEASE_JSON): $(WAY_GH_RELEASE_BODY)
	$(WAY_GH_RELEASE_JQX) > $(WAY_GH_RELEASE_JSON) 

$(WAY_GH_ISSUES_JSON):
	$(WAY_GH_ISSUES_CURLX)

$(WAY_GH_RELEASE_BODY): $(WAY_GH_ISSUES_JSON)
	echo '## New features' > $(WAY_GH_RELEASE_BODY)
	echo '' >> $(WAY_GH_RELEASE_BODY)
	$(call WAY_gh_issues,t:feature) >> $(WAY_GH_RELEASE_BODY) 
	echo '' >> $(WAY_GH_RELEASE_BODY)
	echo '## Enhancements' >> $(WAY_GH_RELEASE_BODY)
	echo '' >> $(WAY_GH_RELEASE_BODY)
	$(call WAY_gh_issues,t:enhancement) >> $(WAY_GH_RELEASE_BODY) 
	echo '' >> $(WAY_GH_RELEASE_BODY)
	echo '## Bug Fixes' >> $(WAY_GH_RELEASE_BODY)
	echo '' >> $(WAY_GH_RELEASE_BODY)
	$(call WAY_gh_issues,t:defect) >> $(WAY_GH_RELEASE_BODY) 
	echo '' >> $(WAY_GH_RELEASE_BODY)
	echo '## Documentation' >> $(WAY_GH_RELEASE_BODY)
	echo '' >> $(WAY_GH_RELEASE_BODY)
	$(call WAY_gh_issues,t:documentation) >> $(WAY_GH_RELEASE_BODY) 
	echo '' >> $(WAY_GH_RELEASE_BODY)
	echo '## Work' >> $(WAY_GH_RELEASE_BODY)
	echo '' >> $(WAY_GH_RELEASE_BODY)
	$(call WAY_gh_issues,t:work) >> $(WAY_GH_RELEASE_BODY) 
	
#
# objectos.code options
#

## code directory
CODE = objectos.code

## code module
CODE_MODULE = $(CODE)

## code module version
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

CODE_PREFIX = CODE_

$(eval $(call COMPILE,$(CODE_PREFIX)))

.PHONY: code@clean
code@clean:
	echo "rm -rf $(CODE_WORK)/*"

.PHONY: code@compile
code@compile: $(CODE_COMPILE_MARKER)

.PHONY: code@jar
code@jar: $(CODE_JAR_FILE)

.PHONY: code@test
code@test: $(CODE_TEST_RUN_MARKER)

#
# objectos.selfgen options
#

## selfgen directory
SELFGEN := objectos.selfgen

## selfgen module
SELFGEN_MODULE := $(SELFGEN)

## selfgen module version
SELFGEN_VERSION := $(VERSION)

## selfgen javac --release option
SELFGEN_JAVA_RELEASE := 21

## selfgen --enable-preview ?
SELFGEN_ENABLE_PREVIEW := 1

## selfgen compile deps
SELFGEN_COMPILE_DEPS = $(CODE_JAR_FILE) 

## selfgen jar name
SELFGEN_JAR_NAME := $(SELFGEN)

## selfgen test compile deps
SELFGEN_TEST_COMPILE_DEPS = $(CODE_JAR_FILE)
SELFGEN_TEST_COMPILE_DEPS += $(SELFGEN_JAR_FILE)
SELFGEN_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## selfgen test runtime dependencies
SELFGEN_TEST_RUNTIME_DEPS = $(SELFGEN_TEST_COMPILE_DEPS)
SELFGEN_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
SELFGEN_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
SELFGEN_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## seflgen test runtime exports
SELFGEN_TEST_JAVAX_EXPORTS := objectos.selfgen.css
SELFGEN_TEST_JAVAX_EXPORTS += objectos.selfgen.html
SELFGEN_TEST_JAVAX_EXPORTS += selfgen.css.util

#
# objectos.code targets
#

#
# objectos.selfgen targets
#

SELFGEN_PREFIX = SELFGEN_

$(eval $(call COMPILE,$(SELFGEN_PREFIX)))

.PHONY: selfgen@clean
selfgen@clean:
	rm -rf $(SELFGEN_WORK)/*

.PHONY: selfgen@compile
selfgen@compile: $(SELFGEN_COMPILE_MARKER)

.PHONY: selfgen@jar
selfgen@jar: $(SELFGEN_JAR_FILE)

.PHONY: selfgen@test
selfgen@test: $(SELFGEN_TEST_RUN_MARKER)

## marker to indicate when selfgen was last run
SELFGEN_MARKER = $(WAY_WORK)/selfgen-marker

## selfgen runtime deps
SELFGEN_RUNTIME_DEPS = $(SELFGEN_JAR_FILE)
SELFGEN_RUNTIME_DEPS += $(SELFGEN_COMPILE_DEPS)

## selfgen java command
SELFGEN_JAVAX = $(JAVA)
SELFGEN_JAVAX += --module-path $(call module-path,$(SELFGEN_RUNTIME_DEPS))
ifeq ($(SELFGEN_ENABLE_PREVIEW), 1)
SELFGEN_JAVAX += --enable-preview
endif
SELFGEN_JAVAX += --module $(SELFGEN_MODULE)/$(SELFGEN_MODULE).Main
SELFGEN_JAVAX += $(WAY_MAIN)

.PHONY: selfgen
selfgen: $(SELFGEN_MARKER)

$(SELFGEN_MARKER): $(SELFGEN_JAR_FILE)
	$(SELFGEN_JAVAX)
	mkdir --parents $(@D)
	touch $(SELFGEN_MARKER)

#
# Targets section
#

.PHONY: clean
clean: code@clean selfgen@clean way@clean

.PHONY: test
test: code@test selfgen@test way@test

.PHONY: install
install: way@install

.PHONY: ossrh
ossrh: way@ossrh

.PHONY: gh-release
gh-release: way@gh-release

# maybe use eval for module targets?

#
# objectos.way targets
#

.PHONY: way@clean
way@clean:
	rm -r $(WAY_WORK)/*

$(WAY_JS_ARTIFACT): $(WAY_JS_SRC)
	mkdir --parents $(@D)
	cp $< $@

.PHONY: way@jar
way@jar: $(SELFGEN_MARKER) $(WAY_JAR_FILE)

.PHONY: way@test
way@test: $(WAY_TEST_RUN_MARKER)

.PHONY: way@install
way@install: $(WAY_INSTALL)

.PHONY: way@source-jar
way@source-jar: $(WAY_SOURCE_JAR_FILE)

.PHONY: way@javadoc way@clean-javadoc
way@javadoc: $(WAY_JAVADOC_JAR_FILE)

way@clean-javadoc:
	rm -r $(WAY_JAVADOC_OUTPUT)

.PHONY: way@pom
way@pom: $(WAY_POM_FILE)

.PHONY: way@ossrh way@ossrh-bundle
way@ossrh: $(WAY_OSSRH_MARKER)

way@ossrh-bundle: $(WAY_OSSRH_BUNDLE)

.PHONY: way@gh-release way@gh-release-body
way@gh-release: $(WAY_GH_RELEASE_MARKER)

way@gh-release-body: $(WAY_GH_RELEASE_BODY)

#
# Eclipse project targets
#

define ECLIPSE_CLASSPATH
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="src" output="work/main" path="main"/>
	<classpathentry kind="src" output="work/test" path="test">
		<attributes>
			<attribute name="test" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-21">
		<attributes>
			<attribute name="module" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="var" path="M2_REPO/org/testng/testng/7.7.1/testng-7.7.1.jar"/>
	<classpathentry kind="var" path="M2_REPO/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar"/>
	<classpathentry kind="var" path="M2_REPO/org/slf4j/slf4j-nop/1.7.36/slf4j-nop-1.7.36.jar"/>
	<classpathentry kind="var" path="M2_REPO/com/beust/jcommander/1.82/jcommander-1.82.jar"/>
	<classpathentry kind="output" path="eclipse-bin"/>
</classpath>
endef

define ECLIPSE_GITIGNORE
/eclipse-bin/
/work/
endef

define ECLIPSE_PROJECT
<?xml version="1.0" encoding="UTF-8"?>
<projectDescription>
	<name>objectos.way:$(ECLIPSE_MODULE_NAME)</name>
	<comment></comment>
	<projects>
	</projects>
	<buildSpec>
		<buildCommand>
			<name>org.eclipse.jdt.core.javabuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
	</buildSpec>
	<natures>
		<nature>org.eclipse.jdt.core.javanature</nature>
	</natures>
</projectDescription>
endef

define ECLIPSE_SETTINGS_CORE_RESOURCES
eclipse.preferences.version=1
encoding/<project>=UTF-8
endef

define ECLIPSE_SETTINGS_JDT_CORE
eclipse.preferences.version=1
org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode=enabled
org.eclipse.jdt.core.compiler.codegen.targetPlatform=21
org.eclipse.jdt.core.compiler.codegen.unusedLocal=preserve
org.eclipse.jdt.core.compiler.compliance=21
org.eclipse.jdt.core.compiler.debug.lineNumber=generate
org.eclipse.jdt.core.compiler.debug.localVariable=generate
org.eclipse.jdt.core.compiler.debug.sourceFile=generate
org.eclipse.jdt.core.compiler.problem.assertIdentifier=error
org.eclipse.jdt.core.compiler.problem.enablePreviewFeatures=disabled
org.eclipse.jdt.core.compiler.problem.enumIdentifier=error
org.eclipse.jdt.core.compiler.problem.reportPreviewFeatures=warning
org.eclipse.jdt.core.compiler.release=enabled
org.eclipse.jdt.core.compiler.source=21
endef

define ECLIPSE_SETTINGS_JDT_UI
eclipse.preferences.version=1
org.eclipse.jdt.ui.javadoc=true
org.eclipse.jdt.ui.text.custom_code_templates=<?xml version\="1.0" encoding\="UTF-8" standalone\="no"?><templates><template autoinsert\="false" context\="gettercomment_context" deleted\="false" description\="Comment for getter function" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.gettercomment" name\="gettercomment"/><template autoinsert\="false" context\="settercomment_context" deleted\="false" description\="Comment for setter function" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.settercomment" name\="settercomment"/><template autoinsert\="false" context\="constructorcomment_context" deleted\="false" description\="Comment for created constructors" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.constructorcomment" name\="constructorcomment"/><template autoinsert\="false" context\="filecomment_context" deleted\="false" description\="Comment for created Java files" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.filecomment" name\="filecomment">/*\n * Copyright (C) 2023 Objectos Software LTDA.\n *\n * Licensed under the Apache License, Version 2.0 (the "License");\n * you may not use this file except in compliance with the License.\n * You may obtain a copy of the License at\n *\n * http\://www.apache.org/licenses/LICENSE-2.0\n *\n * Unless required by applicable law or agreed to in writing, software\n * distributed under the License is distributed on an "AS IS" BASIS,\n * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n * See the License for the specific language governing permissions and\n * limitations under the License.\n */</template><template autoinsert\="false" context\="typecomment_context" deleted\="false" description\="Comment for created types" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.typecomment" name\="typecomment"/><template autoinsert\="true" context\="fieldcomment_context" deleted\="false" description\="Comment for fields" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.fieldcomment" name\="fieldcomment">/**\n * \n */</template><template autoinsert\="false" context\="methodcomment_context" deleted\="false" description\="Comment for non-overriding function" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.methodcomment" name\="methodcomment"/><template autoinsert\="true" context\="modulecomment_context" deleted\="false" description\="Comment for modules" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.modulecomment" name\="modulecomment">/**\n * ${tags}\n */</template><template autoinsert\="false" context\="overridecomment_context" deleted\="false" description\="Comment for overriding functions" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.overridecomment" name\="overridecomment"/><template autoinsert\="false" context\="delegatecomment_context" deleted\="false" description\="Comment for delegate methods" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.delegatecomment" name\="delegatecomment"/><template autoinsert\="true" context\="newtype_context" deleted\="false" description\="Newly created files" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.newtype" name\="newtype">${filecomment}\n${package_declaration}\n\n${typecomment}\n${type_declaration}</template><template autoinsert\="true" context\="classbody_context" deleted\="false" description\="Code in new class type bodies" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.classbody" name\="classbody">\n</template><template autoinsert\="true" context\="interfacebody_context" deleted\="false" description\="Code in new interface type bodies" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.interfacebody" name\="interfacebody">\n</template><template autoinsert\="true" context\="enumbody_context" deleted\="false" description\="Code in new enum type bodies" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.enumbody" name\="enumbody">\n</template><template autoinsert\="true" context\="recordbody_context" deleted\="false" description\="Code in new record type bodies" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.recordbody" name\="recordbody">\n</template><template autoinsert\="true" context\="annotationbody_context" deleted\="false" description\="Code in new annotation type bodies" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.annotationbody" name\="annotationbody">\n</template><template autoinsert\="true" context\="catchblock_context" deleted\="false" description\="Code in new catch blocks" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.catchblock" name\="catchblock">// ${todo} Auto-generated catch block\n${exception_var}.printStackTrace();</template><template autoinsert\="false" context\="methodbody_context" deleted\="false" description\="Code in created function stubs" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.methodbody" name\="methodbody">${body_statement}</template><template autoinsert\="false" context\="constructorbody_context" deleted\="false" description\="Code in created constructor stubs" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.constructorbody" name\="constructorbody">${body_statement}</template><template autoinsert\="true" context\="getterbody_context" deleted\="false" description\="Code in created getters" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.getterbody" name\="getterbody">return ${field};</template><template autoinsert\="true" context\="setterbody_context" deleted\="false" description\="Code in created setters" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.setterbody" name\="setterbody">$${field} \= $${param};</template></templates>
endef

.PHONY: eclipse-gen
eclipse-gen: export CLASSPATH = $(ECLIPSE_CLASSPATH)
eclipse-gen: export GITIGNORE = $(ECLIPSE_GITIGNORE)
eclipse-gen: export PROJECT = $(ECLIPSE_PROJECT)
eclipse-gen: export SETTINGS_CORE_RESOURCES = $(ECLIPSE_SETTINGS_CORE_RESOURCES)
eclipse-gen: export SETTINGS_JDT_CORE = $(ECLIPSE_SETTINGS_JDT_CORE)
eclipse-gen: export SETTINGS_JDT_UI = $(ECLIPSE_SETTINGS_JDT_UI)
eclipse-gen:
	mkdir --parents $(ECLIPSE_MODULE_NAME)/main
	mkdir --parents $(ECLIPSE_MODULE_NAME)/test
	echo "$$CLASSPATH" > $(ECLIPSE_MODULE_NAME)/.classpath
	echo "$$GITIGNORE" > $(ECLIPSE_MODULE_NAME)/.gitignore
	echo "$$PROJECT" > $(ECLIPSE_MODULE_NAME)/.project
	mkdir --parents $(ECLIPSE_MODULE_NAME)/.settings
	echo "$$SETTINGS_CORE_RESOURCES" > $(ECLIPSE_MODULE_NAME)/.settings/org.eclipse.core.resources.prefs
	echo "$$SETTINGS_JDT_CORE" > $(ECLIPSE_MODULE_NAME)/.settings/org.eclipse.jdt.core.prefs
	echo "$$SETTINGS_JDT_UI" > $(ECLIPSE_MODULE_NAME)/.settings/org.eclipse.jdt.ui.prefs
	