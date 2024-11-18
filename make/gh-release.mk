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
#  GitHub release
#

ifndef WORK
$(error Required common-clean.mk was not included)
endif

## GitHub API
ifndef GH_API
GH_API = https://api.github.com/repos/objectos/$(ARTIFACT_ID)
endif

## GitHub curl command
GH_CURL = curl
GH_CURL += --header "Accept: application/vnd.github+json"
GH_CURL += --header "Authorization: Bearer $(GH_TOKEN)"
GH_CURL += --header "X-GitHub-Api-Version: 2022-11-28"

## GitHub milestone title
GH_MILESTONE_TITLE = v$(VERSION)

## GitHub milestones curl command
GH_MILESTONE_CURLX = $(GH_CURL)
GH_MILESTONE_CURLX += '$(GH_API)/milestones'

## GitHub milestone number parsing
GH_MILESTONE_JQX = jq
GH_MILESTONE_JQX += '.[] | select(.title == "$(GH_MILESTONE_TITLE)") | .number'

## GitHub milestone ID
GH_MILESTONE_ID = $(shell $(GH_MILESTONE_CURLX) | $(GH_MILESTONE_JQX))

## Issues from GitHub
GH_ISSUES_JSON = $(WORK)/gh-issues.json

## GitHub issues curl command
GH_ISSUES_CURLX = $(GH_CURL)
GH_ISSUES_CURLX += '$(GH_API)/issues?milestone=$(GH_MILESTONE_ID)&per_page=100&state=all'
GH_ISSUES_CURLX += >
GH_ISSUES_CURLX += $(GH_ISSUES_JSON)

##
GH_RELEASE_BODY = $(WORK)/gh-release.md

## Filter and format issues
GH_ISSUES_JQX = jq
GH_ISSUES_JQX += --arg LABEL "$(LABEL)"
GH_ISSUES_JQX += --raw-output
GH_ISSUES_JQX += 'sort_by(.number) | [.[] | {number: .number, title: .title, label: .labels[] | select(.name) | .name}] | .[] | select(.label == $$LABEL) | "- \(.title) \#\(.number)"'
GH_ISSUES_JQX += $(GH_ISSUES_JSON)

gh_issues = $(let LABEL,$1,$(GH_ISSUES_JQX))

##
GH_RELEASE_JSON := $(WORK)/gh-release.json

## git tag name to be generated
GH_TAG := $(GH_MILESTONE_TITLE)

##
GH_RELEASE_JQX = jq
GH_RELEASE_JQX += --raw-input
GH_RELEASE_JQX += --slurp
GH_RELEASE_JQX += '. as $$body | {"tag_name":"$(GH_TAG)","name":"Release $(GH_TAG)","body":$$body,"draft":true,"prerelease":false,"generate_release_notes":false}'
GH_RELEASE_JQX += $(GH_RELEASE_BODY)

## 
GH_RELEASE_CURLX = $(GH_CURL)
GH_RELEASE_CURLX += --data-binary "@$(GH_RELEASE_JSON)"
GH_RELEASE_CURLX += --location
GH_RELEASE_CURLX += --request POST
GH_RELEASE_CURLX +=  $(GH_API)/releases

##
GH_RELEASE_MARKER = $(WORK)/gh-release-marker 

#
# GitHub release targets
#

.PHONY: gh-release
gh-release: $(GH_RELEASE_MARKER)

.PHONY: gh-release@clean
gh-release@clean:
	rm -f $(GH_RELEASE_MARKER) $(GH_RELEASE_JSON) $(GH_RELEASE_BODY) $(GH_ISSUES_JSON)

$(GH_RELEASE_MARKER): $(GH_RELEASE_JSON)
	@$(GH_RELEASE_CURLX)
	touch $@

$(GH_RELEASE_JSON): $(GH_RELEASE_BODY)
	$(GH_RELEASE_JQX) > $(GH_RELEASE_JSON) 

$(GH_ISSUES_JSON):
	$(GH_ISSUES_CURLX)

$(GH_RELEASE_BODY): $(GH_ISSUES_JSON)
	echo '## New features' > $(GH_RELEASE_BODY)
	echo '' >> $(GH_RELEASE_BODY)
	$(call gh_issues,t:feature) >> $(GH_RELEASE_BODY) 
	echo '' >> $(GH_RELEASE_BODY)
	echo '## Enhancements' >> $(GH_RELEASE_BODY)
	echo '' >> $(GH_RELEASE_BODY)
	$(call gh_issues,t:enhancement) >> $(GH_RELEASE_BODY) 
	echo '' >> $(GH_RELEASE_BODY)
	echo '## Bug Fixes' >> $(GH_RELEASE_BODY)
	echo '' >> $(GH_RELEASE_BODY)
	$(call gh_issues,t:defect) >> $(GH_RELEASE_BODY) 
	echo '' >> $(GH_RELEASE_BODY)
	echo '## Documentation' >> $(GH_RELEASE_BODY)
	echo '' >> $(GH_RELEASE_BODY)
	$(call gh_issues,t:documentation) >> $(GH_RELEASE_BODY) 
	echo '' >> $(GH_RELEASE_BODY)
	echo '## Work' >> $(GH_RELEASE_BODY)
	echo '' >> $(GH_RELEASE_BODY)
	$(call gh_issues,t:work) >> $(GH_RELEASE_BODY) 
