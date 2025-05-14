---
create: '2025-05-10'
modified: '2025-05-10'
---

# ffmpeg的main函数

```C
int main(int argc, char **argv)
{
    int i, ret; // 用于循环计数和存储函数返回值的变量
    BenchmarkTimeStamps ti; // 用于存储时间戳，用于性能基准测试

    init_dynload(); // 初始化动态加载功能，可能用于加载外部库或模块

    register_exit(ffmpeg_cleanup); // 注册程序退出时的清理函数，确保资源被正确释放

    setvbuf(stderr,NULL,_IONBF,0); /* win32 runtime needs this */
    // 设置标准错误输出（stderr）为无缓冲模式，确保错误信息能够及时输出

    av_log_set_flags(AV_LOG_SKIP_REPEATED); // 设置日志标志，跳过重复的日志信息
    parse_loglevel(argc, argv, options); // 解析命令行参数中的日志级别设置

    if(argc>1 && !strcmp(argv[1], "-d")){
        run_as_daemon=1; // 设置程序以守护进程模式运行
        av_log_set_callback(log_callback_null); // 设置日志回调为空函数，即不输出日志
        argc--; // 调整参数数量
        argv++; // 调整参数指针，跳过 "-d" 参数
    }

    #if CONFIG_AVDEVICE
    avdevice_register_all(); // 注册所有设备（如果编译时启用了 AVDEVICE 模块）
    #endif
    avformat_network_init(); // 初始化网络功能，用于处理网络相关的输入/输出

    show_banner(argc, argv, options); // 显示 FFmpeg 的欢迎信息和版本信息

    /* parse options and open all input/output files */
    ret = ffmpeg_parse_options(argc, argv); // 解析命令行参数，并打开所有输入/输出文件
    if (ret < 0)
        exit_program(1); // 如果解析失败，退出程序

    if (nb_output_files <= 0 && nb_input_files == 0) { // 检查是否有输入/输出文件
        show_usage(); // 显示使用说明
        av_log(NULL, AV_LOG_WARNING, "Use -h to get full help or, even better, run 'man %s'\n", program_name);
        exit_program(1); // 提示用户并退出程序
    }

    /* file converter / grab */
    if (nb_output_files <= 0) { // 确保至少有一个输出文件
        av_log(NULL, AV_LOG_FATAL, "At least one output file must be specified\n");
        exit_program(1);
    }

    for (i = 0; i < nb_output_files; i++) { // 遍历所有输出文件
        if (strcmp(output_files[i]->ctx->oformat->name, "rtp")) // 检查输出格式是否为 RTP
            want_sdp = 0; // 如果不是 RTP 格式，则设置 want_sdp 为 0
    }

    current_time = ti = get_benchmark_time_stamps(); // 获取当前时间戳
    if (transcode() < 0) // 执行转码操作
        exit_program(1); // 如果转码失败，退出程序
    if (do_benchmark) { // 如果启用了性能基准测试
        int64_t utime, stime, rtime; // 用于存储时间差
        current_time = get_benchmark_time_stamps(); // 再次获取当前时间戳
        utime = current_time.user_usec - ti.user_usec; // 计算用户态时间差
        stime = current_time.sys_usec  - ti.sys_usec; // 计算内核态时间差
        rtime = current_time.real_usec - ti.real_usec; // 计算实际时间差
        av_log(NULL, AV_LOG_INFO,
               "bench: utime=%0.3fs stime=%0.3fs rtime=%0.3fs\n",
               utime / 1000000.0, stime / 1000000.0, rtime / 1000000.0); // 输出性能基准测试结果
    }
    av_log(NULL, AV_LOG_DEBUG, "%"PRIu64" frames successfully decoded, %"PRIu64" decoding errors\n",
           decode_error_stat[0], decode_error_stat[1]); // 输出解码统计信息
    if ((decode_error_stat[0] + decode_error_stat[1]) * max_error_rate < decode_error_stat[1])
        exit_program(69); // 如果解码错误率超过阈值，退出程序

    exit_program(received_nb_signals ? 255 : main_return_code); // 根据信号接收情况或主返回码退出程序
    return main_return_code; // 返回主返回码
}
```

1. **初始化和设置**：
    - `init_dynload()`：初始化动态加载功能，可能用于加载外部库或模块。
    - `register_exit(ffmpeg_cleanup)`：注册程序退出时的清理函数，确保资源被正确释放。
    - `setvbuf(stderr,NULL,_IONBF,0)`：设置标准错误输出为无缓冲模式，确保错误信息能够及时输出。
    - `av_log_set_flags(AV_LOG_SKIP_REPEATED)`：设置日志标志，跳过重复的日志信息。
    - `parse_loglevel(argc, argv, options)`：解析命令行参数中的日志级别设置。
2. **守护进程模式**：
    - 如果命令行参数中包含 `-d`，则设置程序以守护进程模式运行，并禁用日志输出。
3. **设备和网络初始化**：
    - `avdevice_register_all()`：注册所有设备（如果编译时启用了 AVDEVICE 模块）。
    - `avformat_network_init()`：初始化网络功能，用于处理网络相关的输入/输出。
4. **显示欢迎信息**：
    - `show_banner(argc, argv, options)`：显示 FFmpeg 的欢迎信息和版本信息。
5. **解析命令行参数**：
    - `ffmpeg_parse_options(argc, argv)`：解析命令行参数，并打开所有输入/输出文件。如果解析失败或没有输入/输出文件，显示使用说明并退出程序。
6. **检查输出文件**：
    - 确保至少有一个输出文件，否则退出程序。
    - 遍历所有输出文件，检查输出格式是否为 RTP，如果不是，则设置 `want_sdp` 为 0。
7. **转码操作**：
    - `transcode()`：执行转码操作。如果转码失败，退出程序。
8. **性能基准测试**：
    - 如果启用了性能基准测试，计算并输出用户态、内核态和实际时间差。
9. **解码统计信息**：
    - 输出解码统计信息，包括成功解码的帧数和解码错误数。
    - 如果解码错误率超过阈值，退出程序。
10. **程序退出**：
    - 根据信号接收情况或主返回码退出程序。