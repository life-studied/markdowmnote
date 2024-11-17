---
create: 2023-07-31
---
# 21 Redis

## import

```go
import "github.com/redis/go-redis/v9"
```

## 1. 使用Redis String

```go
// Package main -----------------------------
// @file      : redis.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/8/2 12:22
// -------------------------------------------
package main

import (
	"context"
	"fmt"
	"github.com/redis/go-redis/v9"
	"time"
)

func main() {
	client := redis.NewClient(&redis.Options{
		Addr:     "172.16.136.8:6379", //ip:port
		Password: "123456",
		DB:       0, //默认创建0-15个DB，此处使用0号DB
	})
	ctx := context.TODO()

	useRedisString(ctx, client)
}

func useRedisString(ctx context.Context, client *redis.Client) {
	key := "name"
	value := "yunyin"
	err := client.Set(ctx, key, value, 2*time.Second).Err() //设置过期时间为2s，如果为0则是永不过期
	checkRedisErr(err)

	v2, err := client.Get(ctx, key).Result()
	checkRedisErr(err)
	fmt.Println(v2)

	client.Del(ctx, key)
}

func checkRedisErr(err error) {
	if err == nil {
		return
	}

	fmt.Println("ERR:", err)

}

```

## 2. 使用Redis List

```go
// Package main -----------------------------
// @file      : redis.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/8/2 12:22
// -------------------------------------------
package main

import (
	"context"
	"fmt"
	"github.com/redis/go-redis/v9"
	"time"
)

func main() {
	client := redis.NewClient(&redis.Options{
		Addr:     "172.16.136.8:6379", //ip:port
		Password: "123456",
		DB:       0, //默认创建0-15个DB，此处使用0号DB
	})
	ctx := context.TODO()

	useRedisList(ctx, client)
}

func useRedisList(ctx context.Context, client *redis.Client) {
	key := "ids"
	values := []interface{}{1, 2, 3, "hello", "你好"}
	//使用...将切片转换成一个不定长参数
	err := client.RPush(ctx, key, values...).Err() //在key对应的list的尾部追加元素（list不存在则先创建一个空list）
	checkRedisErr(err)

	//读取list的一个区间，从[0,-1]，即全部元素
	v2, err := client.LRange(ctx, key, 0, -1).Result()
	checkRedisErr(err)
	fmt.Println(v2)

	client.Del(ctx, key)
}

func checkRedisErr(err error) {
	if err == nil {
		return
	}

	fmt.Println("ERR:", err)

}

```

## 3. 使用Redis Hash

​	主要用途：存一个结构体数据。

```go
// Package main -----------------------------
// @file      : redis.go
// @author    : Yunyin
// @contact   : yunyin_jayyi@qq.com
// @time      : 2023/8/2 12:22
// -------------------------------------------
package main

import (
	"context"
	"fmt"
	"github.com/redis/go-redis/v9"
	"time"
)

func main() {
	client := redis.NewClient(&redis.Options{
		Addr:     "172.16.136.8:6379", //ip:port
		Password: "123456",
		DB:       0, //默认创建0-15个DB，此处使用0号DB
	})
	ctx := context.TODO()

	useRedisString(ctx, client)
}

func userRedisHash(ctx context.Context, client *redis.Client) {
	//写入一个Hash，前两个参数是ctx和key，后续是不定长的键值对
	err := client.HSet(ctx, "学生ID1", "Name", "Jay", "Age", 18, "Height", 180.4).Err()
	checkRedisErr(err)

	err = client.HSet(ctx, "学生ID2", "Name", "John", "Age", 20, "Height", 173.5).Err()
	checkRedisErr(err)

	age, err := client.HGet(ctx, "学生ID1", "Age").Result() //获取key中对应字段的val
	checkRedisErr(err)
	fmt.Println(age)

	//获取所有字段，返回一个map，里面包含键值对
	for field, value := range client.HGetAll(ctx, "学生ID2").Val() {
		fmt.Println(field, value)
	}

	client.Del(ctx, "学生ID1")
	client.Del(ctx, "学生ID2")
}

func checkRedisErr(err error) {
	if err == nil {
		return
	}

	fmt.Println("ERR:", err)

}

```



