# AVFrame数据模型与API

## 1. 数据模型

 AVFrame与AVPacket类似，同样是通过一个引用指向真正的frame数据。

## 2. 常用API

| 函数原型                                              | 说明                                     |
| ----------------------------------------------------- | ---------------------------------------- |
| `AVFrame *av_frame_alloc(void);`                      | 分配AVFrame                              |
| `void av_frame_free(AVFrame **frame);`                | 释放AVFrame                              |
| `int av_frame_ref(AVFrame *dst, const AVFrame *src);` | 增加引用计数                             |
| `void av_frame_unref(AVFrame *frame);`                | 减少引用计数                             |
| `void av_frame_move_ref(AVFrame *dst, AVFrame *src);` | 转移引用计数                             |
| `int av_frame_get_buffer(AVFrame *frame, int align);` | 根据AVFrame分配内存                      |
| `AVFrame *av_frame_clone(const AVFrame *src);`        | 等于 `av_frame_alloc() + av_frame_ref()` |

## 3. examples

### av_frame_get_buffer

```C++
AVFrame* frame = av_frame_alloc();
// 1024 * (16/8) * 1 = 2048 byte
frame->nb_samples = 1024;	// 采样点数
frame->format = AV_SAMPLE_FMT_S16;	// 每个采样点16bit (AV_SAMPLE_FMT_S16/AV_SAMPLE_FMT_S16P)
frame->channel_layout = AV_CH_LAYOUT_MONO;	// (AV_CH_LAYOUT_MONO/AV_CH_LAYOUT_STEREO)

int ret = av_frame_get_buffer(frame, 0);	// 根据设置分配buffer内存 ref:1

ret = av_frame_make_writable(frame);	// 允许写入

av_frame_unref(frame);	// ref:0
av_frame_free(&frame);
```

FFmpeg 中定义两种音频声道布局类型，分别代表：

- `AV_CH_LAYOUT_MONO`: 单声道布局，只有一个声道。（size* 1）
- `AV_CH_LAYOUT_STEREO`: 立体声布局，有两个声道。（size* 2）

FFmpeg 中定义两种采样格式，其中：

- `AV_SAMPLE_FMT_S16` 表示音频样本以16位有符号整型的形式存储，所有声道的样本都存储在同一个连续的缓冲区内

  - （即在同一缓冲区交错存储）

  - ```C++
    frame->buf[0]->size;	// 4096
    ```

- `AV_SAMPLE_FMT_S16P` 表示每个声道的样本存储在不同的缓冲区中，它是一种平面音频格式，每个声道的数据是分开存储的

  - （即在两个缓冲区分别存储）

  - ```C++
    frame->buf[0]->size;	// 2048
    frame->buf[1]->size;	// 2048
    ```



