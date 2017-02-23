#!/usr/bin/env bash

declare username=$1
declare api_key=$2
declare -a array=(
                "adal",
                "adal-accounts",
                "adal-adapters",
                "adal-analytics",
                "adal-bus",
                "adal-fragments",
                "adal-managers",
                "adal-networks",
                "adal-utils")

# get length of an array
array_length=${#array[@]}

for (( i=1; i<${array_length}+1; i++ ));
do
  gradle ${array[$i-1]}:clean ${array[$i-1]}:build ${array[$i-1]}:bintrayUpload -PbintrayUser="$username" -PbintrayKey="$api_key" -PdryRun=false -Pskippasswordprompts
done