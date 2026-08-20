#!/bin/bash

export JAVA_HOME="/usr/lib/jvm/java-21-temurin-jdk"
ANDROID_HOME="$HOME/Android/Sdk"
EMULATOR="$ANDROID_HOME/emulator/emulator"
ADB="$ANDROID_HOME/platform-tools/adb"

cd ~/CodeProjects

if ! "$ADB" devices | grep -q "emulator-"; then
    echo "🚀 No emulator running, starting one..."
    "$EMULATOR" -avd pixel6_aosp -no-snapshot-load &
    "$ADB" wait-for-device
    while [ "$("$ADB" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" != "1" ]; do
        sleep 1
    done
    echo "✅ Emulator ready"
fi

./gradlew installDebug && "$ADB" shell am start -n org.foss.fermux/.main.MainActivity
