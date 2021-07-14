#!/bin/bash
# THIS SCRIPT FILE UPDATES LOCAL FILES WITH NEW VERSION VARIABLES
# AND CREATES A NEW DRAFT RELEASE ON GITHUB

# NOTE: Hardcoded JVM path ln:25
# REQUIRES: gh, lua

function user_confirmation() {
  read -p "did the previous script work as intended? (y/n) " -r
  echo # (optional) move to a new line
  if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    [[ "$0" == "$BASH_SOURCE" ]] && exit 1 || return 1 # handle exits from shell or function but don't exit interactive shell
  fi
}

# Update files to new version
lua updateVersion.lua $1
user_confirmation

# create new release
gh release create $1 -t "GoICasterCompanion v$1" -p -d -R rvrx/GoICasterCompanion
user_confirmation

# Generate DMG
./gradlew createDmg -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/jdk1.8.0_281.jdk/Contents/Home/
user_confirmation

# Upload DMG
gh release upload $1 build/distributions/GoICC\ v$1.dmg
