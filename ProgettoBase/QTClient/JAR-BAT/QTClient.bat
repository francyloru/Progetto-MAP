@echo off
title QTClient
echo QTClient started !!
echo.

set /p param1="IP: "
set /p param2="Port: "

echo.

java -jar QTClient.jar %param1% %param2%

echo.
echo QTClient closed.
pause