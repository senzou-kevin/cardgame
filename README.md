# cardgame

## 简介:

该游戏是一个策略卡牌游戏(炉石)。玩家可以将卡牌召唤到棋盘上和电脑进行人机对战。

## 上手指南:

使用IDEA导入项目

1.创建一个新的Run Configuration

2.选择sbt Task

3.在tasks输入框中输入run

4.点击应用。

如果使用eclipse 详见该官方文档:https://www.playframework.com/documentation/2.8.x/IDE

## 使用到的框架:

Play framework

## 前后端交互:

采用异步交互方式。数据存储在Json对象中

![前后端交互.jpg](https://i.loli.net/2021/09/04/j5v2oaEz7kUn69p.png)



## 数据库

没有使用数据库。所有卡牌相关信息存储在Json文件中，游戏启动会将json文件加载进内存。

![json存储数据.png](https://i.loli.net/2021/09/04/aFlSAB4t8pUhneg.png)



## 版权说明:

详情请参阅:LICENSE.md

## 鸣谢:

感谢与小组成员协同开发。