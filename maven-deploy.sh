#!/usr/bin/env bash

declare username=$1
declare api_key=$2

declare -a array=(
                "adal"
                "adal-utils"
                "adal-accounts"
                "adal-adapters"
                "adal-analytics"
                "adal-bus"
                "adal-fragments"
                "adal-managers"
                "adal-network")

# get length of an array
array_length=${#array[@]}

declare build_failed=false
for (( i=0; i<${array_length}; i++ ));
do
    OUTPUT="$(gradle "${array[$i]}":clean "${array[$i]}":build "${array[$i]}":bintrayUpload -PbintrayUser="$username" -PbintrayKey="$api_key" -PdryRun=false -Pskippasswordprompts)"
    if [[ ${OUTPUT} == *"BUILD FAILED"* ]]; then
        build_failed=true
    fi
done

#Update the ReadMe and commit
if ${build_failed} ; then
    echo 'Error Building or uploading a module'
else
    VERSION="$(gradle version | head -n 1)"

    echo 'All modules uploaded. Updating README...'
    sed -i '1s/.*/:libVersion: '${VERSION}'/' README.adoc
    git add README.adoc
    git commit -m "Deployed new version("${VERSION}") to Bintray"
    git push
fi