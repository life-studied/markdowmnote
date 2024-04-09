# 星露谷操作bot

## 代码目录

​	由于在游戏中编写代码不方便，建议直接在代码保存目录进行更改，在游戏中load即可。

​	目录为：`C:\Users\10654\AppData\Roaming\StardewValley\Saves\云隐_370669801\strout.farmtronics\usrdisks\5936339997733495984`

## 机器人module

​	老版本中机器人为bot，在新版本中已经废弃，使用me来操作。

### 属性

#### 可读写

| Name               | Type         | Meaning                                                      |
| ------------------ | ------------ | ------------------------------------------------------------ |
| `name`             | string       | machine name, used to distinguish it from others in code, chat, etc. |
| `screenColor`      | color string | background color of the screen                               |
| `currentToolIndex` | integer      | index of the inventory item the bot is holding: 0 for the first item, 1 for the second, etc. |
| `statusColor`      | color string | color of the bot status light, e.g. "#FFFF00" for yellow     |

#### 只读

| Name        | Type     | Meaning                                                      |
| ----------- | -------- | ------------------------------------------------------------ |
| `isBot`     | integer  | true (1) if this machine is a bot; false (0) if it's the home computer |
| `owner`     | string   | name of the player who owns this bot or computer             |
| `position`  | map      | current tile position: `x`, `y`, and `area` (a `Location`)   |
| `facing`    | integer  | direction bot is facing: 0=north, 1=east, 2=south, 3=west    |
| `energy`    | integer  | how much power the bot has left (0-270)                      |
| `inventory` | list     | what item is in each slot of the bot's inventory             |
| `here`      | Location | shortcut for `me.position.area`                              |
| `ahead`     | map      | tile information for the spot directly in front of the robot |

### 函数

| Name                        | Effect                                                       |
| --------------------------- | ------------------------------------------------------------ |
| `forward`                   | moves bot forward 1 tile                                     |
| `left`                      | turns bot 90° to the left                                    |
| `right`                     | turns bot 90° to the right                                   |
| `select` *toolNameOrIndex*  | sets `currentToolIndex` to the corresponding item            |
| `placeItem`                 | place the selected item down ahead of the robot              |
| `takeItem` *n*              | take an item from slot *n* of the chest/bot ahead            |
| `swapItem` *idx1, idx2*     | swap the inventory items in slots *idx1* and *idx2*          |
| `harvest`                   | harvest the crop/product in front of the bot (returns true on success) |
| `useTool`                   | applies current tool/item to the tile ahead of the bot       |
| `clearAhead`                | select and apply appropriate tool to clear tile ahead of the bot (Note 2) |
| `clearAndMove` *distance=1* | clear and move forward a given number of tiles (Note 2)      |

## 参考资料

* [API Reference · JoeStrout/Farmtronics Wiki (github.com)](https://github.com/JoeStrout/Farmtronics/wiki/API-Reference)