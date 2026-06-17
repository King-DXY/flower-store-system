@echo off
echo 正在杀掉所有占端口的 Java 进程...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8080 :8081 :8082" ^| findstr "LISTENING"') do (
    echo 杀掉进程 %%a
    taskkill /F /PID %%a 2>nul
)
echo 完成！现在端口 8080/8081/8082 都释放了
pause
