---
create: '2025-01-26'
modified: '2025-01-26'
---

# Python守护进程

5s检测一次进程情况，并记录日志到指定位置：`python D:\codeSpace\bad_code\test_python_protect_process\daemon.py --start D:\codeSpace\bad_code\test_python_protect_process\test.py --log_dir D:\codeSpace\bad_code\test_python_protect_process\logs`。

```python
import subprocess
import time
import argparse
import os
from datetime import datetime


def start_process(python_file, log_file):
    """启动目标Python进程，并将输出重定向到日志文件"""
    print(f"Starting python {python_file} and redirecting output to {log_file}...")
    with open(log_file, "w") as f:
        # 启动Python进程，将进程的标准输出和标准错误重定向到日志文件
        process = subprocess.Popen(["python", python_file], stdout=f, stderr=f)
    return process

def main():
    parser = argparse.ArgumentParser(description="守护进程脚本，用于启动和监控指定的Python脚本。")
    parser.add_argument("--start", required=True, type=str, help="指定要启动的Python脚本文件名，例如 --start a.py")
    parser.add_argument("--log_dir", required=True, type=str, help="指定日志文件存储的目录")
    args = parser.parse_args()
    if not args.start:
        print("Error: No script specified. Use --start <script_name> to specify the script to run.")
        return
    if not args.log_dir:
        print("Error: No log directory specified. Use --log_dir <log_directory> to specify the log directory.")
        return
    
    python_file = args.start
    log_dir = args.log_dir

    # 确保log目录存在
    if not os.path.exists(log_dir):
        os.makedirs(log_dir)
    
    while True:
        # 获取当前时间戳，拼接成日志文件名，防止日志文件发生重名覆盖
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        log_file = f"log_{timestamp}.txt"
        log_file = f"{log_dir}\\{log_file}"

        # 启动目标进程
        process = start_process(python_file, log_file)

        # 执行5s一次的守护逻辑
        while True:
            time.sleep(5)
            if process.poll() is not None:  # 如果进程已结束
                print(f"Process has exited with code {process.returncode}. Restarting...")
                break  # 退出当前循环，重新启动进程
            else:
                print(f"Process is running on {datetime.now().strftime('%Y%m%d_%H%M%S')}.") # 进程正常运行，打印当前时间戳

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("Daemon stopped by user.")
    except Exception as e:
        print(f"An error occurred: {e}")
    finally:
        print("Daemon stopped.")

print("Press Enter to exit...")
input()
```