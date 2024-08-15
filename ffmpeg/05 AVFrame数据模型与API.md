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

