#!/bin/bash
# Compila y ejecuta A-Store (Linux / Mac).
#   ./ejecutar.sh          -> inicia la página en http://localhost:8080
#   ./ejecutar.sh demo     -> demostración en consola
set -e
cd "$(dirname "$0")"
rm -rf out && mkdir out
javac -encoding UTF-8 -sourcepath src -d out src/astore/Main.java
java -Dfile.encoding=UTF-8 -cp out astore.Main "$@"
