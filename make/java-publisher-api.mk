#
# Copyright (C) 2023-2025 Objectos Software LTDA.
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

## requirements
## - PUBLISHER_API_GPG_KEY
## - PUBLISHER_API_GPG_PASSPHRASE
## - PUBLISHER_API_USERNAME
## - PUBLISHER_API_PASSWORD

ifndef JAR_FILE
$(error Required java-jar.mk was not included)
endif

ifndef JAVADOC_JAR_FILE
$(error Required java-javadoc.mk was not included)
endif

ifndef SOURCE_JAR_FILE
$(error Required java-source-jar.mk was not included)
endif

ifndef POM_FILE
$(error Required java-pom.mk was not included)
endif

# the api prefix to use
ifndef PUBLISHER_API
PUBLISHER_API := https://central.sonatype.com/api/v1/publisher
endif

## Publisher API bundle directory
PUBLISHER_API_BUNDLE := $(WORK)/$(ARTIFACT_ID)-$(VERSION)-bundle

## Publisher API target directory
PUBLISHER_API_TARGET := $(PUBLISHER_API_BUNDLE)/$(call mk-maven-layout,$(GROUP_ID),$(ARTIFACT_ID),$(VERSION))

## Publisher API bundle contents
PUBLISHER_API_JAR_FILE := $(PUBLISHER_API_TARGET)/$(JAR_FILE_NAME)
PUBLISHER_API_JAVADOC_JAR_FILE := $(PUBLISHER_API_TARGET)/$(JAVADOC_JAR_FILE_NAME)
PUBLISHER_API_SOURCE_JAR_FILE := $(PUBLISHER_API_TARGET)/$(SOURCE_JAR_FILE_NAME)
PUBLISHER_API_POM_FILE := $(PUBLISHER_API_TARGET)/$(POM_FILE_NAME)

PUBLISHER_API_CONTENTS := $(PUBLISHER_API_JAR_FILE)
PUBLISHER_API_CONTENTS += $(PUBLISHER_API_JAVADOC_JAR_FILE)
PUBLISHER_API_CONTENTS += $(PUBLISHER_API_SOURCE_JAR_FILE)
PUBLISHER_API_CONTENTS += $(PUBLISHER_API_POM_FILE)

## Publisher API sigs
PUBLISHER_API_ASC := $(PUBLISHER_API_CONTENTS:%=%.asc)
PUBLISHER_API_MD5 := $(PUBLISHER_API_CONTENTS:%=%.md5)
PUBLISHER_API_SHA1 := $(PUBLISHER_API_CONTENTS:%=%.sha1)

PUBLISHER_API_SIGS := $(PUBLISHER_API_ASC)
PUBLISHER_API_SIGS += $(PUBLISHER_API_MD5)
PUBLISHER_API_SIGS += $(PUBLISHER_API_SHA1)

## Publisher API bundle zip file
PUBLISHER_API_BUNDLE_FILE := $(WORK)/$(ARTIFACT_ID)-$(VERSION)-bundle.zip

## gpg command
PUBLISHER_API_GPGX := gpg
PUBLISHER_API_GPGX += --armor
PUBLISHER_API_GPGX += --batch
PUBLISHER_API_GPGX += --default-key $(PUBLISHER_API_GPG_KEY)
PUBLISHER_API_GPGX += --detach-sign
PUBLISHER_API_GPGX += --passphrase $(PUBLISHER_API_GPG_PASSPHRASE)
PUBLISHER_API_GPGX += --pinentry-mode loopback
PUBLISHER_API_GPGX += --yes

## token
PUBLISHER_API_TOKEN := $(shell echo -n "$(PUBLISHER_API_USERNAME):$(PUBLISHER_API_PASSWORD)" | base64)

## upload result file
PUBLISHER_API_UPLOAD_RESULT := $(WORK)/$(ARTIFACT_ID)-$(VERSION)-upload

## upload command
PUBLISHER_API_UPLOADX := curl
PUBLISHER_API_UPLOADX += --form bundle=@$(PUBLISHER_API_BUNDLE_FILE)
PUBLISHER_API_UPLOADX += --header 'Authorization: Bearer $(PUBLISHER_API_TOKEN)'
PUBLISHER_API_UPLOADX += --output $(PUBLISHER_API_UPLOAD_RESULT)
PUBLISHER_API_UPLOADX += --request POST
PUBLISHER_API_UPLOADX += $(PUBLISHER_API)/upload?publishingType=AUTOMATIC
#PUBLISHER_API_UPLOADX += $(PUBLISHER_API)/upload

## deployment id
PUBLISHER_API_DEPLOYMENT_ID = $(file < $(PUBLISHER_API_UPLOAD_RESULT))

## status json
PUBLISHER_API_STATUS_JSON := $(WORK)/$(ARTIFACT_ID)-$(VERSION)-status.json

## status command
PUBLISHER_API_STATUSX  = curl
PUBLISHER_API_STATUSX += --header 'Authorization: Bearer $(PUBLISHER_API_TOKEN)'
PUBLISHER_API_STATUSX += --output $(PUBLISHER_API_STATUS_JSON)
PUBLISHER_API_STATUSX += --request POST
PUBLISHER_API_STATUSX += $(PUBLISHER_API)/status?id=$(PUBLISHER_API_DEPLOYMENT_ID)

## delete command
PUBLISHER_API_DELETEX  = curl
PUBLISHER_API_DELETEX += --header 'Authorization: Bearer $(PUBLISHER_API_TOKEN)'
PUBLISHER_API_DELETEX += --request DELETE
PUBLISHER_API_DELETEX += $(PUBLISHER_API)/deployment/$(PUBLISHER_API_DEPLOYMENT_ID)

## Publisher API marker
PUBLISHER_API_MARKER := $(WORK)/publisher-api-marker

#
# Publisher API targets
#

.PHONY: publish
publish: $(PUBLISHER_API_MARKER)

.PHONY: publish@clean
publish@clean:
	rm -rf $(PUBLISHER_API_MARKER) $(PUBLISHER_API_BUNDLE) $(PUBLISHER_API_BUNDLE_FILE) $(PUBLISHER_API_UPLOAD_RESULT) $(PUBLISHER_API_STATUS_JSON) 

.PHONY: publish@status
publish@status:
	@$(PUBLISHER_API_STATUSX)

.PHONY: publish@delete
publish@delete:
	@$(PUBLISHER_API_DELETEX)

$(PUBLISHER_API_MARKER): $(PUBLISHER_API_UPLOAD_RESULT)
	touch $@

$(PUBLISHER_API_UPLOAD_RESULT): $(PUBLISHER_API_BUNDLE_FILE)
	@$(PUBLISHER_API_UPLOADX)

$(PUBLISHER_API_BUNDLE_FILE): $(PUBLISHER_API_CONTENTS) $(PUBLISHER_API_SIGS)
	(cd $(PUBLISHER_API_BUNDLE) && zip -r - .) > $@

$(PUBLISHER_API_JAR_FILE): $(JAR_FILE) | $(PUBLISHER_API_TARGET)
	cp $< $@

$(PUBLISHER_API_JAVADOC_JAR_FILE): $(JAVADOC_JAR_FILE) | $(PUBLISHER_API_TARGET)
	cp $< $@

$(PUBLISHER_API_SOURCE_JAR_FILE): $(SOURCE_JAR_FILE) | $(PUBLISHER_API_TARGET)
	cp $< $@

$(PUBLISHER_API_POM_FILE): $(POM_FILE) | $(PUBLISHER_API_TARGET)
	cp $< $@
	
$(PUBLISHER_API_TARGET):
	mkdir --parents $@

$(PUBLISHER_API_TARGET)/%.asc: $(PUBLISHER_API_TARGET)/%
	@$(PUBLISHER_API_GPGX) $<

$(PUBLISHER_API_TARGET)/%.md5: $(PUBLISHER_API_TARGET)/%
	md5sum $< | cut -d' ' -f1 | tr -d '\n' > $@

$(PUBLISHER_API_TARGET)/%.sha1: $(PUBLISHER_API_TARGET)/%
	sha1sum $< | cut -d' ' -f1 | tr -d '\n' > $@
