@echo off
rem Build script to create SimpleText JDBC driver

del \jdk1_1\jdbc\SimpleText\*.class
javac -d \jdk1_1 CommonValue.java
call b Define
call b Object
call b Column
call b Table
call b InputStream
call b IDriver
call b IConnection
call b IStatement
call b ResultSetMetaData
call b ResultSet
call b Statement
call b PreparedStatement
call b DatabaseMetaData
call b Connection
call b Driver
