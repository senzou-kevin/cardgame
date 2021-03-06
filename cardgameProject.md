# 项目报告
- [项目报告](#项目报告)
  - [项目简介:](#项目简介)
  - [时间:](#时间)
  - [成员以及担任角色:](#成员以及担任角色)
  - [用户故事](#用户故事)
  - [Sprint](#sprint)
    - [Sprint0](#sprint0)
    - [**Sprint1 **](#sprint1-)
    - [**sprint2**](#sprint2)
    - [**Sprint3：**](#sprint3)
    - [**Sprint4:**](#sprint4)
    - [**Sprint5**](#sprint5)
## 项目简介:

该游戏是一个策略卡牌游戏(炉石)。玩家可以将卡牌召唤到棋盘上和电脑进行人机对战。

## 时间:

2020年6月1号-2020年七月11号

根据时间我们将该项目分为项目初期(sprint0)以及开发和测试阶段(sprint1-sprint5).

一周为一个sprint。

## 成员以及担任角色:

总共5位成员。

1位成员担任产品负责人(Product Owner)

1位成员担任Scrum Master

3位成员(包括我)担任开发者(developer)

## 用户故事

1.作为人类玩家，我应该能够从我的卡牌中抽牌到我的手上。 在游戏开始时，我应该能够抽三张牌，然后在每回合结束时再抽一张牌。

2.作为人类玩家，如果我的手牌已满并且我抽了一张牌，那么那张牌就会被丢弃.

3作为人类玩家，在游戏开始时应该有20点生命值。

4.作为人类玩家，在每回合开始时，我应该获得等于回合数 + 1 的法力值

5.作为人类玩家，在每回合结束时，我应该失去所有未消耗的魔法值

6.作为人类玩家，如果轮到我并且我点击了一个可以移动但尚未移动的单位，则该单位可以移动到的图块应高亮显示。

7.作为人类玩家，如果轮到我并且我点击了一个可以攻击但还没有攻击的单位，那么我的单位可以攻击的范围内有敌方单位的格子应该高亮显示

8.作为人类玩家，如果我选择了一个可以攻击的单位并向相邻的敌方单位发出攻击命令，我选择的单位应该攻击。

9.作为人类玩家，如果我选择了一个可以移动和攻击的单位，并向一个有效的非相邻敌方单位发出攻击命令，我选择的单位应该移动到他们在射程内的格子然后攻击。

10.作为人类玩家，如果我选择了一个可以移动的单位，然后单击我的单位可以移动到的有效板块，那么该单位应该移动到该板块。

11.作为人类玩家，如果我选择了一个具有远程攻击的单位，并且可以对不相邻的敌方单位进行攻击并向其发出攻击命令，则我选择的单位应该进行攻击。

12.作为人类玩家，如果我有一个单位攻击，我方单位向目标单位攻击并且目标单位幸存下来,如果我方单位在目标单位的攻击范围内，那么我方单位会遭到反击。

13.作为人类玩家，如果我的一个单位生命值为 0，它应该被摧毁。

14作为人类玩家，如果我对敌方角色造成伤害，那么也应该对敌方玩家造成伤害。 同样，如果我的分身被治愈，我也应该被治愈。

15.作为人类玩家，如果我将敌方玩家的生命值降低到 0，我将赢得比赛。 或者，如果我的生命值为 0，我就会输掉比赛。

16.作为人类玩家，如果我召唤一个单位，那么一旦该单位被创建，任何召唤效果都应该被触发.

17.作为人类玩家，如果我的一个单位死亡，则应在该单位被移除之前触发任何死亡效果

18.作为人类玩家，如果我在棋盘上有一个带有 SpellThief 的单位并且对手使用了一个魔法，那么应该触发所述效果

19.作为人类玩家，如果我点击手中的单位卡并且我有足够的法力来使用它，那么我可以将该单位召唤到的图块应高亮显示

20作为人类玩家，如果我一旦选择了要玩的单位卡，如果我点击了一个有效的板块，该单位应该会被召唤到那里。

21.作为人类玩家，如果我点击手上的魔法卡卡并且我有足够的法力来使用它，那么我可以定位的有效图块应该高亮显示

22.作为人类玩家，如果我一旦选择了要使用的魔法卡，如果我点击了有效的目标图块，则该法术效果应该应用于目标图块上的单位。

23.作为人类玩家，如果我有一个单位可以治疗另一个单位，那么目标单位的生命值应该增加指定的数量（上限为单位起始生命值总量)

24.作为人类玩家，如果我有一个有挑衅的单位，那么相邻的单位就不能移动，只能用挑衅攻击范围内的单位。

25.作为人类玩家，如果我有一张带有空投的单位卡，我应该可以在棋盘上的任何空闲空间召唤相关单位

26.作为人类玩家，如果我有一张带有飞行的单位卡，那么在移动或移动攻击时它应该可以移动到任何空闲的格子上.

27.作为人类玩家，如果我有一张对一个单位造成伤害的魔法卡，那么在使用它时，它应该使目标单位的生命值减少指定的数量。

28.作为人类玩家，如果我有一个法术可以摧毁一个单位或以其他方式将该单位的生命值降低到 0，则该单位应该被移除。

29.作为人类玩家，如果轮到我并且我按下结束回合，回合结束效果应该触发并且我的回合应该结束。

## Sprint

### Sprint0

由于项目是放在GitLab上(由于没权限公开，因此我将项目放到github个人账户上)，因此对于没有接触过Git成员来说在这一周时间需要先熟悉使用git。我们将根据用户故事来实现功能并且制定出sprint计划大纲。将总时间分为5个sprint，每个sprint为一周。

详细的sprint对应的实现用户故事如下:

### **Sprint1 **

**主要是初始化人类的一些资源，比如生命值，魔法，卡牌等。**

| Sprint1                        |
| ------------------------------ |
| #1(抽卡)                       |
| #2(手牌满再抽就弃牌一张)       |
| #3(初始化生命值)               |
| #4(回合开始增加魔法)           |
| #5(回合结束失去所有魔法)       |
| #7(通过高亮显示可以攻击的范围) |
| #19(高亮显示可以召唤的地方)    |
| #20(召唤选择的卡牌到棋盘上)    |
| #测试找bug                     |

**问题:**当玩家召唤卡牌到棋盘，手牌中的卡不会消失。召唤完一张卡牌，所有的卡牌应该会向左移动一格，但是已召唤的卡牌不会消失，回合新抽的卡牌造成重影。该问题一直没解决，所以把该问题一直留到下一个sprint中。

**总结:** 在这个初始阶段，相对的功能难度不大，由于所有队员没有玩过类似的卡牌游戏，因此在此sprint期间需要花时间了解该游戏的规则。唯一的问题就是前端页面当卡牌召唤时候，该卡牌不会从手中消失。分析了后端逻辑后依然没能解决。



### **sprint2**

**主要是移动+卡牌高亮+使用魔法卡**

| Sprint2                             |
| ----------------------------------- |
| #6(点击单位并高亮显示)              |
| #10(移动单位)                       |
| #21(点击魔法卡会有效图块会高亮显示) |
| #22(使用魔法卡)                     |
| #27(使用攻击性魔法卡会造成对方扣血) |
| #29(点击endTurn结束玩家回合)        |
| #测试找bug                          |

**解决sprint1遗留问题**:通过和老师沟通发现是前端老师给的代码有问题(我们仅负责使用java实现后端逻辑)，因此替换了新的前端代码。

**问题**:当一个单位从A点移动到B点时，如果该线路上有单位，它不会绕行，而是直接从该单位穿过。

**问题解决**:使用Astart 算法解决该问题。

**问题**:主角卡移动到最下面一行时候，点击自己的手牌会报错

**问题解决:** 以主角卡为中心获取召唤范围时候，范围的y轴边间设置错误。

**总结**:使用idea开发时，发现JDK版本没统一，因此在此阶段统一使用JDK12.开发过程中解决了移动时的问题以及移动到棋盘最下面格子再点击手牌报错的问题。通过和老师沟通解决卡牌不消失的问题。



### **Sprint3：**

**攻击+输赢判定+死亡**

| spint3                    |
| ------------------------- |
| #8(邻近单位攻击)          |
| #9(移动+攻击)             |
| #11(特殊单位可以远程攻击) |
| #12(反击)                 |
| #13(生命0死亡)            |
| #14(分身扣血/治愈)        |
| #15(游戏判定输赢)         |
| #测试bug                  |

**问题**:在实现移动+攻击时所需的时间不够，因此需要将该故事顺延到sprint4

**总结:** 其中一个故事没能在该sprint中完成，因此不得不将该故事移到下一个sprint当中



### **Sprint4:**

**实现sprint3未完成的故事。实现卡牌的特殊效果。**

| sprint4                           |
| --------------------------------- |
| #16(召唤时并触发该卡牌的特殊效果) |
| #17(单位死亡前触发特殊效果)       |
| #18                               |
| #24(实现挑衅功能)                 |
| #25(全图召唤)                     |
| #26(全图飞行)                     |
| #28(被魔法卡摧毁死亡)             |
| #测试bug                          |

**问题**:总共20张卡牌可以抽，当卡牌抽完后会报错。

**问题解决:** 最开始设计抽牌逻辑时忽略了卡牌抽空的情况。

总结:召唤特殊效果不难实现，因此有时间去实现移动+攻击功能。



### **Sprint5**

**实现AI的功能以及测试**

| Sprint5  |
| -------- |
| #实现AI  |
| #测试bug |

**问题**:AI在起初设置的太简单,按照顺序执行功能如:抽卡，召唤，移动攻击，回合结束。

**问题解决:** 给AI的表现行为赋值，这样当AI濒临死亡时会选择逃跑而非进攻。

总结:AI相对来说不太智能，因为没有学习过AI相关的知识，只能通过代码简单实现一个AI.



