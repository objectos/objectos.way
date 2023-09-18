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
