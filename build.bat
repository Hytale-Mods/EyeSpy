@echo off
echo Building EyeSpy...
call gradlew.bat clean shadowJar --no-daemon
if %ERRORLEVEL% EQU 0 (
    echo Build completed successfully!
    echo ShadowJar created and copied to location specified in .env
) else (
    echo Build failed with error code %ERRORLEVEL%
)
pause
