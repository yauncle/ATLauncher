#!/bin/bash
java -Xmx2G -XX:MaxPermSize=256M -jar %%SERVERJAR%% "$@"
read -n1 -r -p "Press any key to close..."
