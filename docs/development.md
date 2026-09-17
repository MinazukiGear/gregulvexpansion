# 开发与代码结构

> 当前实现基线：Minecraft 1.20.1、Forge 47.4.10、GTCEu Modern 7.5.3、Java 17。本文记录 2026-09-17 完成代码精简后的结构，玩法规则仍以 `docs/design/` 为准。

## 初始化与注册

- `GregULVExpansion` 注册 Registrate 事件监听器、工作台配方数据生成器，以及 GTCEu 的配方类型和机器注册回调。
- `GregULVExpansionAddon` 按 GTCEu 生命周期注册覆盖板、物品、语言和运行时配方。
- `GULVRecipeTypes` 统一创建 14 种配方类型，并向 Forge 的配方类型、序列化器注册表及 GTCEu 配方类型注册表登记。
- 普通物品模型使用 Registrate `ItemBuilder` 自带的默认 `item/generated` 模型，不再维护重复的数据提供器。

注册顺序依赖 GTCEu 生命周期：覆盖板必须早于引用它们的物品，配方类型必须早于机器定义。调整入口时应同时执行数据生成和 GameTest，防止只在完整启动阶段出现空引用。

## 配方模块

`GULVRecipes` 是稳定的公开入口，具体配方按职责拆分为包内实现：

| 文件 | 职责 |
| --- | --- |
| `GULVCraftingRecipes` | 工作台配方与数据生成入口 |
| `GULVComponentRecipes` | ULV 构件装配与替代电路配方 |
| `GULVPrimitiveRecipes` | 电解、基础加工和铅室配方 |
| `GULVChemistryRecipes` | 化工、固化、提取、蒸馏和裂化配方 |
| `GULVFuelRecipes` | 红石发电机与燃气轮机燃料 |

工作台配方由 Registrate 数据生成；GTCEu 机器配方通过 `IGTAddon.addRecipes` 运行时加入。GTCEu 7.5.3 的机器配方不应改回 JSON 数据生成路径。

## 机器实现

- 无自定义行为的 ULV 加工机统一实例化 `ULVSimpleMachine`，由 `GULVMachines.registerUlvSimpleMachine` 配置 ID、配方类型与模型。
- 红石发电机和燃气轮机直接实例化 GTCEu `SimpleGeneratorMachine`，由 `registerUlvGenerator` 完成公共注册。
- 只有具有独立状态或工作逻辑的机器保留专用类，例如手摇发电机、温差发电机、铅酸蓄电墙与 ULV 流体钻井机。
- 结构方块通过 `GULVBlocks.registerStructureBlock` 生成相同的方块属性、方块模型和物品模型。
- ULV 物品与流体传输速率由 `GULVCovers` 集中定义，覆盖板行为和物品 Tooltip 共用同一数值来源。

新增简单机器前应优先扩展注册参数；只有需要持久化状态、交互覆写或独立 tick 逻辑时才增加机器子类。

## 构建与验证

主 `build.gradle` 负责插件、依赖、运行配置和打包。资源、发布 JAR 与 GameTest 日志守卫集中在 `gradle/verification.gradle`。

```powershell
$env:JAVA_HOME = 'C:\Program Files\Zulu\zulu-17' # 系统默认 Java 不是 17 时
.\gradlew.bat runData --offline
.\gradlew.bat runGameTestServer --offline
.\gradlew.bat build --offline
```

- `runData` 必须完成且不产生未解释的资源差异。
- `runGameTestServer` 必须发现测试，并出现全部必需测试通过的日志；任务会把摘要写入 `build/reports/gametest.txt`。
- `build` 会执行 JSON、工作台符号、语言键、模型引用、发布 JAR 元数据和缓存泄漏检查。
- Java 编译启用 `-Xlint:all,-classfile,-processing`，保留项目源码警告并过滤依赖 class 中的可选注解噪声。
- 发布 JAR 不得包含已删除包装类、Registrate `.cache` 或缺失的许可证与元数据文件。
