@echo off
echo Starting Simple Health Card Application with MySQL...
echo.
echo Make sure MySQL is running on localhost:3306
echo Database: healthcard_db
echo Username: root
echo Password: root
echo.
mvn spring-boot:run
pause