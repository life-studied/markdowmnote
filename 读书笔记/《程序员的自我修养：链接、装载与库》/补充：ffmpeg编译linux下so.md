---
create: 2024-11-12
modified: '2025-03-14'
---

# ffmpeg编译linux下so

使用`gcc -shared`会主动调用ld，并且传入系统共享库路径。而如果手动调用ld则需要手动添加路径-Lxxx。

```shell
export ECFLAG="-fPIC" 
./configure --prefix="$HOME/ffmpeg_build" --disable-shared --enable-static --enable-pic --disable-ffmpeg --disable-doc --extra-cflags="-fPIC" --extra-ldflags="-fPIC"
make -j$(nproc)
make install 

gcc -shared -o libffmpeg.so -Wl,-soname,libffmpeg.so -Wl,-Bsymbolic \
	-Wl,--whole-archive \
	-Wl,--no-undefined \
	-Wl,--allow-multiple-definition -Wl,-Bsymbolic-functions \
	-Wl,--start-group \
    libavcodec.a \
    libavfilter.a \
    libswresample.a \
    libavformat.a \
    libavutil.a \
    libavdevice.a \
    libswscale.a \
    -Wl,--end-group \
    -Wl,--no-whole-archive \
    -lc -lm -ldl
    
    
gcc -shared -o libffmpeg.so -Wl,-soname,libffmpeg.so \ 
	-Wl,--start-group \
    libavcodec.a \
    libavfilter.a \
    libswresample.a \
    libavformat.a \
    libavutil.a \
    libavdevice.a \
    libswscale.a \
    -Wl,--end-group
```