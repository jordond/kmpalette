#!/bin/sh
rm -rf .gradle
./gradlew clean
rm -rf build
rm -rf demo/iosApp/iosApp.xcworkspace
rm -rf demo/iosApp/Pods
rm -rf demo/iosApp/iosApp.xcodeproj/project.xcworkspace
rm -rf demo/iosApp/iosApp.xcodeproj/xcuserdata 
