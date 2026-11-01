@echo off
set "LOCAL_MVN=C:\Users\gkavi\.m2\wrapper\dists\apache-maven-3.9.9-bin\33b4b2b4\apache-maven-3.9.9\bin\mvn.cmd"
if exist "%LOCAL_MVN%" (
    call "%LOCAL_MVN%" %*
) else (
    call mvn %*
)
