# ffmpeg编译linux下so

使用`gcc -shared`会主动调用ld，并且传入系统共享库路径。而如果手动调用ld则需要手动添加路径-Lxxx。

```shell
export ECFLAG="-fPIC" 
./configure --prefix="$HOME/ffmpeg_build" --disable-shared --enable-static --enable-pic --enable-asm --enable-neon --disable-ffmpeg --disable-doc --extra-cflags="-fPIC "
make -j$(nproc)
make install 

gcc -shared -o libffmpeg.so -Wl,-soname,libffmpeg.so -Wl,-Bsymbolic -Wl,--whole-archive -Wl,--nostdlib -Wl,--no-undefined -Wl,--allow-multiple-definition -Wl,-Bsymbolic-functions \
    libavcodec.a \
    libavfilter.a \
    libswresample.a \
    libavformat.a \
    libavutil.a \
    libavdevice.a \
    libswscale.a \
    -Wl,--no-whole-archive \
    -lc -lm -lz -ldl
```

