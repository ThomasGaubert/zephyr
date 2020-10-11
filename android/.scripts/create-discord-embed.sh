#!/bin/bash

# Load arguments
while [ $# -gt 0 ] ; do
  case $1 in
    -b | --build-metadata) BUILD_METADATA="$2" ;;
    -v | --version-number) VERSION_NUMBER="$2" ;;
    -b | --build-number) BUILD_NUMBER="$2" ;;
    -c | --changelog-path) CHANGELOG_PATH="$2" ;;
  esac
  shift
done

# Check for required arguments
if [ -z "$BUILD_METADATA" ]; then
  echo "Missing required parameter: --build-metadata"
  exit 1
fi

if [ -z "$CHANGELOG_PATH" ]; then
  echo "Missing required parameter: --changelog-path"
  exit 1
fi

# Parse version info from build metadata
VERSION_NUMBER=$(cat "$BUILD_METADATA" | \
    python -c "import json,sys;obj=json.load(sys.stdin)['elements'][0];print(obj['versionName']);")

BUILD_NUMBER=$(cat "$BUILD_METADATA" | \
    python -c "import json,sys;obj=json.load(sys.stdin)['elements'][0];print(obj['versionCode']);")

VERSION_NUMBER="$VERSION_NUMBER ($BUILD_NUMBER)"

# Get changelog and timestamp
CHANGELOG=$(awk '{printf "%s\\n", $0}' $CHANGELOG_PATH)
TIMESTAMP=$(date -u +"%Y-%m-%dT%H:%M:%SZ")

# Create embed
EMBED_FORMAT='{"embeds":[{"title":"Android Beta %s released", "description": "**Changelog**\\n%s","url":"https://install.appcenter.ms/users/thomasgaubert/apps/zephyr/distribution_groups/beta","color":"4054148","footer":{"text":"Build %s"},"timestamp":"%s"}]}'
EMBED=$(printf "$EMBED_FORMAT" "$VERSION_NUMBER" "$CHANGELOG" "$BUILD_NUMBER" "$TIMESTAMP")

# Output
echo "$EMBED"