@echo off
cls
git status
git add .
git commit -m %1
git push