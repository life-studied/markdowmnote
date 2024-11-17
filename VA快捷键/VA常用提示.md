---
create: 2023-10-07
---
# VA常用提示

## 1.ih

```
/*
 Copyright yunyin. All rights reserved.
 
 @Author: yunyin
 @Date: $DATE$
 @FileName:$FILE_BASE$
 @version:1.0
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
      https://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
#ifndef $FILE_BASE_UPPER$_H__
#define $FILE_BASE_UPPER$_H__

$end$
#endif // $FILE_BASE_UPPER$_H__
```

## 2.class

```
class $Class_name$
{
public:
	$Class_name$();
	~$Class_name$();

	$end$
};

$Class_name$::$Class_name$()
{
}

$Class_name$::~$Class_name$()
{
}

```

## 3.delcons

```
private: 
$ClassName$() = delete;
$ClassName$(const $ClassName$&) = delete; 
$ClassName$& operator=(const $ClassName$&) = delete;
```

## 4.main

```
int main()
{
	$selected$$end$
	return 0;
}
```

## 5.singleClass

```
class $classname$
{
public:
	$classname$() = delete;
	$classname$(const $classname$&) = delete; 
	$classname$& operator=(const $classname$&) = delete;
	
	static $classname$& getInstance() 
	{
		static $classname$ instance;
		return instance;
	}
private:
	
};
```

