# 群峦农艺
## 这是一个让 Let's Do 系列模组 兼容 群峦 的模组
### 模组特性
1. 食物都有群峦营养值。
2. 能使用群峦的物品制作食物。
3. 将所有食物方块修改为带保质期的方块。
4. 狗粮、猫粮、马饲料、鸡饲料能用到对应的群峦生物上。
5. 能使用宠物碗给狗、猫喂食。
6. 肥料袋能施肥 3 * 3 * 3 的范围。
7. 沃土能施肥 9 * 9 * 9 的范围。
8. 给肥沃耕地施肥会有额外加成。
9. 犁车可以开垦耕地。
10. 火炉可以作为群峦热源。
11. 添加了袋装方块。（可以使用KubeJS注册袋装方块）
12. 保质期方块均可放地窖延长保质期。

## 注册袋装食物方块
### 实例
1. 在 ```/kubejs/startup_scripts/reg.js``` 创建方块
```JavaScript
StartupEvents.registry('block', event => {
    event.create('red_apple_bag', 'tfc_farm_charm:bag')
})
```
2. 添加方块模型（跳过）
3. 在 ```/kubejs/server_scripts/bagData.js```定义物品箱装数据
```JavaScript
ServerEvents.highPriorityData(event => {
    event.addJson('kubejs:tfc_farm_charm/bag/example.json', {
        ingredient: {
            item: "tfc:food/red_apple"
        },
        block: 'kubejs:red_apple_bag'
    })
})
```

---

# TFC Farm Charm
## This mod let Let's Do series mod compat to TFC.
### Mod Trait
1. Foods have TFC nutrients.
2. Using TFC world items to crafting Let's Do series foods.
3. Modify the food block to decaying block.
4. DogFood, CatFood, HorseFodder, ChickenFeed can be used on corresponding mobs.
5. Can use PetBowl to feed dogs and cats.
6. Compost can fertilize within a range of 3 * 3 * 3.
7. Fertilized soil can be fertilized within a range of 9 * 9 * 9.
8. Fertilizing on Fertilized Farmland will result in additional nutrients bonuses.
9. Plowing carts can cultivate farmland.
10. Stove is TFC heat source.
11. Added bag block. (Bag block can be added using KubeJS)
12. The decaying block can extend it food life in the cellar.

## Bag block registry
### Example
1. In ```/kubejs/startup_scripts/reg.js``` to registry the block.
```JavaScript
StartupEvents.registry('block', event => {
    event.create('red_apple_bag', 'tfc_farm_charm:bag')
})
```
2. Add block model. (skip)
3. In ```/kubejs/server_scripts/bagData.js``` define data for bag block
```JavaScript
ServerEvents.highPriorityData(event => {
    event.addJson('kubejs:tfc_farm_charm/bag/example.json', {
        ingredient: {
            item: "tfc:food/red_apple"
        },
        block: 'kubejs:red_apple_bag'
    })
})
```