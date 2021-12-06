# Java Web Test

this repository contains two applications

1- Assign
2- Assign API

Assign: is the web JSF prime faces project

IDE: Eclipse mars 2
Server Tomcat 8
Primefaces 8.0

Assign API: rest api
contains the following rest services:
- login
- get accounts
- get account statement

connects with database JDBC connection with microsoft access database

//NOTE:
before debloy you must add the database file location from

tomcat/conf/catalina.properties

DATABASE_URL=jdbc:ucanaccess://YOUR_DATABASE_FILE_LOCATION e.g E:/accountsdb.accdb
or you can set it hardcoded in java
AssignAPI-> src-> com->assign->utils-> Constant class -> DATABASE_URL variable
