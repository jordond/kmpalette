#!/bin/bash

github_ref=$1
draft_tag=$2

if [[ $github_ref == refs/tags/v* ]]; then
    tag=${github_ref/refs\/tags\//}
    tag="${tag:1}"
    echo "VERSION=$tag" >> "$GITHUB_OUTPUT"
else
    tag=$draft_tag
    tag="${tag:1}"
    echo "VERSION=$tag" >> "$GITHUB_OUTPUT"
fi
