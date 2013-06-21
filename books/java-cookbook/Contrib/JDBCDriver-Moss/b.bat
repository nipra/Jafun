@echo off

rem Build script to build a single SimpleText class

echo Building SimpleText%1.java
javac -d \jdk1_1 SimpleText%1.java
