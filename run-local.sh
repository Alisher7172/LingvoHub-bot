#!/usr/bin/env bash
# Run LingvoHub locally using application-local.yml (see application-local.example.yml)
cd "$(dirname "$0")" || exit 1

if [ ! -f application-local.yml ]; then
  if [ -f src/main/resources/application-local.yml ]; then
    cp src/main/resources/application-local.yml application-local.yml
  else
    cp src/main/resources/application-local.example.yml application-local.yml
  fi
  echo "Created application-local.yml. Add your bot token, username, and admin IDs, then run this script again."
  exit 1
fi

mvn spring-boot:run -Dspring-boot.run.profiles=local
