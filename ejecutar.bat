@echo off
chcp 65001 > nul
cd /d "%~dp0"
if exist out rmdir /s /q out
mkdir out
javac -encoding UTF-8 -sourcepath src -d out src\astore\Main.java
if errorlevel 1 (
  echo Hubo errores al compilar.
  pause
  exit /b 1
)
java -Dfile.encoding=UTF-8 -cp out astore.Main %*
pause
