#!/bin/bash

# Check if the directory is passed as an argument
if [ -z "$1" ]; then
    echo "Usage: $0 /path/to/directory"
    exit 1
fi

# Find and delete all .DS_Store files recursively from the given directory
find "$1" -name ".DS_Store" -type f -delete

echo "All .DS_Store files have been deleted from $1"
