@echo off
call setenv.bat
set WORKSPACE=%~dp0workspace
@echo Starting Eclipse with workspace "%WORKSPACE%"
%ECLIPSE%\eclipse.exe -data "%WORKSPACE%" -vm %JAVA_HOME%\bin\javaw.exe -vmargs -Xms256m -Xgc:gencon -Xns:64m -Dosgi.bundlefile.limit=100
