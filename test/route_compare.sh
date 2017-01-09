#!/bin/bash

echo "$1"
echo "$2"

while true; do
    # read line from each file
    read -r a
    read -r b <&3

    echo "a-> $a"
    echo "b-> $b"

    # check if done reading both files
    if [ -z "$a" && -z "$b" ]; then
        echo "PASSED"
        exit
    fi

    # check if done reading one of the files
    if [ -z "$a" ]; then
        echo "FAILED: Second file is longer"
        exit
    fi
    if [ -z "$b" ]; then
        echo "FAILED: First file is longer"
        exit
    fi

    if [ "$a" != "$b" ]; then
        exit
    fi
done < "$1" 3<"$2"