---
create: 2024-11-16
---
# CMakePresets.json配置

```json
{
    "version": 3,
    "configurePresets": [
        {
            "name": "windows-msvc-base",
            "hidden": true,
            "generator": "Ninja",
            "binaryDir": "${sourceDir}/build/${presetName}",
            "installDir": "${sourceDir}/install/${presetName}",
            "cacheVariables": {
                "CMAKE_C_COMPILER": "cl.exe",
                "CMAKE_CXX_COMPILER": "cl.exe",
                "CMAKE_EXPORT_COMPILE_COMMANDS": "TRUE"
            },
            "condition": {
                "type": "equals",
                "lhs": "${hostSystemName}",
                "rhs": "Windows"
            }
        },
        {
            "name": "windows-gcc-base",
            "hidden": true,
            "generator": "Ninja",
            "binaryDir": "${sourceDir}/build/${presetName}",
            "installDir": "${sourceDir}/install/${presetName}",
            "cacheVariables": {
                "CMAKE_C_COMPILER": "gcc",
                "CMAKE_CXX_COMPILER": "g++",
                "CMAKE_EXPORT_COMPILE_COMMANDS": "TRUE"
            },
            "condition": {
                "type": "equals",
                "lhs": "${hostSystemName}",
                "rhs": "Windows"
            }
        },
        {
            "name": "windows-clang-base",
            "hidden": true,
            "generator": "Ninja",
            "binaryDir": "${sourceDir}/build/${presetName}",
            "installDir": "${sourceDir}/install/${presetName}",
            "cacheVariables": {
                "CMAKE_C_COMPILER": "clang",
                "CMAKE_CXX_COMPILER": "clang++",
                "CMAKE_EXPORT_COMPILE_COMMANDS": "TRUE"
            },
            "condition": {
                "type": "equals",
                "lhs": "${hostSystemName}",
                "rhs": "Windows"
            }
        },
        {
            "name": "linux-gcc-base",
            "hidden": true,
            "generator": "Ninja",
            "binaryDir": "${sourceDir}/build/${presetName}",
            "installDir": "${sourceDir}/install/${presetName}",
            "cacheVariables": {
                "CMAKE_C_COMPILER": "gcc",
                "CMAKE_CXX_COMPILER": "g++",
                "CMAKE_EXPORT_COMPILE_COMMANDS": "TRUE"
            },
            "condition": {
                "type": "equals",
                "lhs": "${hostSystemName}",
                "rhs": "Linux"
            }
        },
        {
            "name": "msvc-x64-debug",
            "displayName": "MSVC x64 Debug",
            "inherits": "windows-msvc-base",
            "architecture": {
                "value": "x64",
                "strategy": "external"
            },
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "Debug"
            }
        },
        {
            "name": "msvc-x64-release",
            "displayName": "MSVC x64 Release",
            "inherits": "msvc-x64-debug",
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "Release"
            }
        },
        {
            "name": "msvc-x64-minsizerel",
            "displayName": "MSVC x64 MinSizeRel",
            "inherits": "msvc-x64-debug",
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "MinSizeRel"
            }
        },
        {
            "name": "msvc-x64-relwithdebinfo",
            "displayName": "MSVC x64 RelWithDebInfo",
            "inherits": "msvc-x64-debug",
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "RelWithDebInfo"
            }
        },
        {
            "name": "msvc-x86-debug",
            "displayName": "MSVC x86 Debug",
            "inherits": "windows-msvc-base",
            "architecture": {
                "value": "x86",
                "strategy": "external"
            },
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "Debug"
            }
        },
        {
            "name": "msvc-x86-release",
            "displayName": "MSVC x86 Release",
            "inherits": "msvc-x86-debug",
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "Release"
            }
        },
        {
            "name": "msvc-x86-minsizerel",
            "displayName": "MSVC x86 MinSizeRel",
            "inherits": "msvc-x86-debug",
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "MinSizeRel"
            }
        },
        {
            "name": "msvc-x86-relwithdebinfo",
            "displayName": "MSVC x86 RelWithDebInfo",
            "inherits": "msvc-x86-debug",
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "RelWithDebInfo"
            }
        },
        {
            "name": "gcc-x64-debug-win",
            "displayName": "GCC x64 Debug Win",
            "inherits": "windows-gcc-base",
            "architecture": {
                "value": "x64",
                "strategy": "external"
            },
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "Debug"
            }
        },
        {
            "name": "gcc-x64-release-win",
            "displayName": "GCC x64 Release Win",
            "inherits": "gcc-x64-debug-win",
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "Release"
            }
        },
        {
            "name": "clang-x64-debug-win",
            "displayName": "Clang x64 Debug Win",
            "inherits": "windows-clang-base",
            "architecture": {
                "value": "x64",
                "strategy": "external"
            },
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "Debug"
            }
        },
        {
            "name": "clang-x64-release-win",
            "displayName": "Clang x64 Release Win",
            "inherits": "clang-x64-debug-win",
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "Release"
            }
        },
        {
            "name": "gcc-x64-debug-linux",
            "displayName": "GCC x64 Debug Linux",
            "inherits": "linux-gcc-base",
            "architecture": {
                "value": "x64",
                "strategy": "external"
            },
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "Debug"
            }
        },
        {
            "name": "gcc-x64-release-linux",
            "displayName": "GCC x64 Release Linux",
            "inherits": "gcc-x64-debug-linux",
            "cacheVariables": {
                "CMAKE_BUILD_TYPE": "Release"
            }
        }
    ]
}
```

