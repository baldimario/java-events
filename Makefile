.DEFAULT_GOAL := help
SHELL := /bin/bash
DC := docker-compose -f ./stack/docker/docker-compose.y*ml

help:
	@echo -e "$$(grep -hE '^\S+:.*##' $(MAKEFILE_LIST) | sed -e 's/:.*##\s*/:/' -e 's/^\(.\+\):\(.*\)/\\x1b[36m\1\\x1b[m:\2/' | column -c2 -t -s :)"

build: ## build docker containers
	${DC} build ${SERVICE}

pull: ## pull docker containers
	${DC} pull ${SERVICE}

ps: ## List docker containers
	${DC} ps

up: ## Setup docker containers
	${DC} up -d ${SERVICE}

down: ## Tier down docker containers
	${DC} down ${SERVICE}

logs: ## Show logs for docker containers
	${DC} logs ${SERVICE}

tail: ## Tail logs for docker containers
	${DC} logs -f ${SERVICE}

shell: ## Run shell into docker container
	${DC} run --rm ${SERVICE} sh

compile: ## Compile project
	${DC} run --rm --no-deps ${SERVICE} sh -c "mvn clean compile assembly:single"