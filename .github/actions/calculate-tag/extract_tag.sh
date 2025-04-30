#!/bin/bash

github_ref=$1
draft_tag=$2

if [[ $github_ref == refs/tags/v* ]]; then
tag=${github_ref/refs\/tags\//}
echo "VERSION=$tag" >> "$GITHUB_OUTPUT"
else
tag=$draft_tag
echo "VERSION=$tag" >> "$GITHUB_OUTPUT"
fi
