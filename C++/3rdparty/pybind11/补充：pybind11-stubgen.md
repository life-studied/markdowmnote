---
create: '2025-01-05'
modified: '2025-04-26'
---

# pybind11-stubgen

## example（Windows-powershell）

```powershell
$env:PYTHONPATH = "$env:PYTHONPATH;$pwd"	# 将.pyd/.so的生成目录加入到PYTHONPATH中
											# pybind11-stubgen才能查找到对应的module
											# 此处是$pwd，更换成你自己的目录
pybind11-stubgen -o ./typings --ignore-all-errors example

# pybind11-stubgen -o ./typings example # if no capsule
```

## usage

```shell
pybind11-stubgen [-h]
                 [-o OUTPUT_DIR]
                 [--root-suffix ROOT_SUFFIX]
                 [--ignore-invalid-expressions REGEX]
                 [--ignore-invalid-identifiers REGEX]
                 [--ignore-unresolved-names REGEX]
                 [--ignore-all-errors]
                 [--enum-class-locations REGEX:LOC]
                 [--numpy-array-wrap-with-annotated|
                  --numpy-array-use-type-var|
                  --numpy-array-remove-parameters]
                 [--print-invalid-expressions-as-is]
                 [--print-safe-value-reprs REGEX]
                 [--exit-code]
                 [--stub-extension EXT]
                 MODULE_NAME
```

* [sizmailov/pybind11-stubgen: Generate stubs for python modules](https://github.com/sizmailov/pybind11-stubgen)