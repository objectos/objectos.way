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

## requirements
## - OSSRH_GPG_KEY
## - OSSRH_GPG_PASSPHRASE
## - OSSRH_USERNAME
## - OSSRH_PASSWORD

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

# the OSSRH server to use
ifndef OSSRH_SERVER
OSSRH_SERVER := https://s01.oss.sonatype.org
endif

## gpg command
GPGX = gpg
GPGX += --armor
GPGX += --batch
GPGX += --default-key $(OSSRH_GPG_KEY)
GPGX += --detach-sign
GPGX += --passphrase $(OSSRH_GPG_PASSPHRASE)
GPGX += --pinentry-mode loopback
GPGX += --yes

## ossrh bundle contents
OSSRH_CONTENTS := $(JAR_FILE)
OSSRH_CONTENTS += $(JAVADOC_JAR_FILE)
OSSRH_CONTENTS += $(SOURCE_JAR_FILE)
OSSRH_CONTENTS += $(POM_FILE)

## ossrh sigs
OSSRH_SIGS := $(OSSRH_CONTENTS:%=%.asc)

## ossrh bundle jar file
OSSRH_BUNDLE_FILE := $(WORK)/$(ARTIFACT_ID)-$(VERSION)-bundle.jar

## ossrh bundle contents
OSSRH_BUNDLE_CONTENTS := $(OSSRH_CONTENTS)
OSSRH_BUNDLE_CONTENTS += $(OSSRH_SIGS)

## ossrh bundle jar command
OSSRH_JARX = $(JAR)
OSSRH_JARX += --create
OSSRH_JARX += --file $(OSSRH_BUNDLE_FILE)
OSSRH_JARX += $(foreach file,$(OSSRH_BUNDLE_CONTENTS), -C $(dir $(file)) $(notdir $(file)))

## ossrh cookies
OSSRH_COOKIES = $(WORK)/ossrh-cookies.txt 

## ossrh login curl command
OSSRH_LOGIN_CURLX := curl
OSSRH_LOGIN_CURLX += --cookie-jar $(OSSRH_COOKIES)
OSSRH_LOGIN_CURLX += --output /dev/null
OSSRH_LOGIN_CURLX += --request GET
OSSRH_LOGIN_CURLX += --silent
OSSRH_LOGIN_CURLX += --url $(OSSRH_SERVER)/service/local/authentication/login
OSSRH_LOGIN_CURLX += --user $(OSSRH_USERNAME):$(OSSRH_PASSWORD)

## ossrh response json
OSSRH_UPLOAD_JSON := $(WORK)/ossrh-upload.json

## ossrh upload curl command
OSSRH_UPLOAD_CURLX := curl
OSSRH_UPLOAD_CURLX += --cookie $(OSSRH_COOKIES)
OSSRH_UPLOAD_CURLX += --header 'Content-Type: multipart/form-data'
OSSRH_UPLOAD_CURLX += --form file=@$(OSSRH_BUNDLE_FILE)
OSSRH_UPLOAD_CURLX += --output $(OSSRH_UPLOAD_JSON)
OSSRH_UPLOAD_CURLX += --request POST
OSSRH_UPLOAD_CURLX += --url $(OSSRH_SERVER)/service/local/staging/bundle_upload

## ossrh repository id sed parsing
OSSRH_SEDX := sed
OSSRH_SEDX += --regexp-extended
OSSRH_SEDX += --silent
OSSRH_SEDX += 's/^.*repositories\/(.*)".*/\1/p'
OSSRH_SEDX += $(OSSRH_UPLOAD_JSON)

## repository id
OSSRH_REPOSITORY_ID = $(shell $(OSSRH_SEDX))

## ossrh release curl command
OSSRH_RELEASE_CURLX = curl
OSSRH_RELEASE_CURLX += --cookie $(OSSRH_COOKIES)
OSSRH_RELEASE_CURLX += --data '{"data":{"autoDropAfterRelease":true,"description":"","stagedRepositoryIds":["$(OSSRH_REPOSITORY_ID)"]}}'
OSSRH_RELEASE_CURLX += --header 'Content-Type: application/json'
OSSRH_RELEASE_CURLX += --request POST
OSSRH_RELEASE_CURLX += --url $(OSSRH_SERVER)/service/local/staging/bulk/promote

## ossrh marker
OSSRH_MARKER := $(WORK)/ossrh-marker

#
# ossrh bundle targets
#

.PHONY: ossrh
ossrh: $(OSSRH_MARKER)

.PHONY: ossrh@clean
ossrh@clean:
	rm -f $(OSSRH_MARKER) $(OSSRH_UPLOAD_JSON) $(OSSRH_BUNDLE_FILE) $(OSSRH_BUNDLE_CONTENTS) $(OSSRH_COOKIES) 

$(OSSRH_MARKER): $(OSSRH_UPLOAD_JSON)
	@for i in 1 2 3; do \
	  echo "Waiting before release..."; \
	  sleep 45; \
	  echo "Trying to release $(OSSRH_REPOSITORY_ID)"; \
	  $(OSSRH_RELEASE_CURLX); \
	  if [ $$? -eq 0 ]; then \
	    exit 0; \
	  fi \
	done
	touch $@

$(OSSRH_UPLOAD_JSON): $(OSSRH_BUNDLE_FILE)
	@$(OSSRH_LOGIN_CURLX)
	$(OSSRH_UPLOAD_CURLX)

$(OSSRH_BUNDLE_FILE): $(OSSRH_BUNDLE_CONTENTS)
	$(OSSRH_JARX)

%.asc: %
	@$(GPGX) $<
