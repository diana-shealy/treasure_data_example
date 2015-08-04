#!/bin/bash
if [[ $# -eq 0 ]]; then
	echo "No arguments specified"
	exit 1
fi
TD_COMMAND="$1"
shift

if [[ "$TD_COMMAND" ==  "build" ]]; then
echo "Building..."
if [[ ! -d td-query/build ]]; then
    echo "Creating Build Directory"
    mkdir td-query/build
fi

javac  -classpath "td-query/src/:td-query/lib/commons-cli-1.3.1.jar:td-query/lib/td-client-0.5.9-SNAPSHOT-jar-with-dependencies.jar" -d td-query/build td-query/src/main/*.java
elif [[ "$TD_COMMAND" == "query" ]]; then
    echo "Running with $@"
    java -classpath "td-query/build/:td-query/lib/commons-cli-1.3.1.jar:td-query/lib/td-client-0.5.9-SNAPSHOT-jar-with-dependencies.jar:td-query/resources" main.Main "$@"
# else if clean build directory
elif [[ "$TD_COMMAND" == "clean" ]]; then
    echo "Cleaning Build Directory"
		
    if [[  -d td-query/build ]]; then
        rm -rf td-query/build
    fi    
else 
    echo "Unknown command specified"
fi
