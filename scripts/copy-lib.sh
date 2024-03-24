#!/usr/bin/env bash

SCRIPT_PATH="$(cd -- "$(dirname "$0")" >/dev/null 2>&1 && pwd -P)"
cd "$SCRIPT_PATH"

if [ -z "$1" ]; then
    RUST_ENCRYPTION_DIR_PATH="../../encryption/"
    if [ ! -d "$RUST_ENCRYPTION_DIR_PATH" ]; then
        echo "Error: No argument provided and $RUST_ENCRYPTION_DIR_PATH not present (repo)"
        echo "Usage: ./copy-lib.sh [<RUST_ENCRYPTION_DIR_PATH>]"
        exit 1
    fi
else
    if [[ ! "$1" =~ ^/ ]]; then
        echo "Error: Provided argument is not an absolute path"
        echo "Usage: ./copy-lib.sh [<RUST_ENCRYPTION_DIR_PATH>]"
        exit 2
    fi
    RUST_ENCRYPTION_DIR_PATH="$1"
fi

cd "$RUST_ENCRYPTION_DIR_PATH"

if [ ! -f "./target/release/encrypt" ]; then
  if ! docker info >/dev/null 2>&1; then
    error "Docker is not running"
    exit 4
  fi
  ./build.sh
fi

cd "$SCRIPT_PATH"

function copy_file {
  local source_file="$1"
  local destination="$2"

  if [ -f "$source_file" ]; then
    cp "$source_file" "$destination"
  else
    echo "Error: File $source_file not found"
    exit 5
  fi
}

mkdir -p "../lib"
copy_file "$RUST_ENCRYPTION_DIR_PATH/target/release/encrypt" "../lib"