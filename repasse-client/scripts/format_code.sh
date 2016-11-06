#!/bin/bash
# you must install https://www.npmjs.com/package/js-beautify
# npm i -g js-beautify

set -e

find -regex ".*\.\(js\|json\|css\|html\)" \
    -not -path "./bower_components/*" \
    -not -path "./node_modules/*" \
    -not -path "./dist/*" \
    -not -path "./.publish/*" \
    -not -path "./.tmp/*" \
    | xargs js-beautify --quiet --replace