# ULV 微型燃气轮机（ULV Micro Gas Turbine）设计规格

> 状态：**已实现（v1.0，2026-09-09）** · 优先级：**P2** · 类型：单方块 ULV 发电机 + 流体燃料表
>
> 变更记录：v1.0 立项并实现（所有者 2026-09-09 指示「两条都立项」，与 [ulv-polarizer.md](ulv-polarizer.md) 同批）。

## 定位

上游燃气轮机注册于 `LV..HV`（`registerSimpleGenerator`，调研核实 7.5.3），**无任何 ULV 条目**——与构件线同构的层级空白。本机是常设微电家族的第三台：**温差 = 环境热、红石 = 固体燃料、燃气 = 流体燃料**，与上游 LV 的温差/燃气/内燃三分结构同构。

- 玩家故事：石油线跑起来之后，蒸馏塔的含硫气体、裂化的甲烷、钻井抽到的天然气终于有了归宿——第一口"油气田自己发电"。
- GSE 联动边界：本机消费的是流体，不与 A1 裁砍的"蒸汽变现"（蒸汽轮机）竞争语义——A1 的理由是 LV 蒸汽轮机可被廉价跳造，而这里 LV 燃气轮机的升级收益是功率与自动化燃料面，不存在跳造问题。

## 上游现状与空白（核实 7.5.3）

- `GAS_TURBINE` 注册层级 `LV..HV`，无 tier 0 条目。
- 燃料表 `GAS_TURBINE_FUELS` 上游现成，全档通用：天然气 8 mB→5t、含硫气体 32 mB→25t、甲烷 2 mB→7t、含硫石脑油 4 mB→5t（EUt 均为 -V[LV]=-32）。
- 本模组石油线现存三个死端：蒸馏塔含硫气体（60 mB/批无去向）、裂化甲烷（500 mB/批无去向）、钻井天然气矿脉（模组内零消费）。本机一次闭合。

## 注册与命名

| 项目 | 资源 ID | 中文名 | 英文名 |
| --- | --- | --- | --- |
| 发电机 | `gregulvexpansion:ulv_gas_turbine` | 超低压微型燃气轮机 | ULV Micro Gas Turbine |

- 配方类型 `gregulvexpansion:ulv_gas_turbine`（GENERATOR 组，EUt 为负），Java：`GULVRecipeTypes.ULV_GAS_TURBINE_FUELS`；机器类 `ULVGasTurbineMachine`（`SimpleGeneratorMachine` 子类）。

## 数值草案

| 燃料 | 上游（@ -32） | ULV 版（@ -8，总 EU 不变、时长 ×4） |
| --- | --- | ---: |
| 天然气 | 8 mB / 5t | 8 mB / 20t → 160 EU |
| 含硫气体 | 32 mB / 25t | 32 mB / 100t → 800 EU |
| 甲烷 | 2 mB / 7t | 2 mB / 28t → 224 EU |
| 含硫石脑油 | 4 mB / 5t | 4 mB / 20t → 160 EU |

- 输出口径：**8 EU/t · 1 A 恒定**，与红石/温差发电机一致；堆台数是唯一扩容方式。
- 流体罐 **4,000 mB 定容**（上游 `genericGeneratorTankSizeFunction` 在 tier 0 会算出负值，故自定义定容）。
- **白名单封闭**：首批仅上述四种——全部来自本模组石油线/钻井。乙烯不上表（聚合叙事优先，烧料 vs 塑料是玩家取舍，不上白名单即不做引导）；木煤气/煤气等留待 GSE 侧联动扩展。扩表必须先改本文档。

## 合成草案（上游样式 + tier-0 组件解析）

| 机器 | 图案（=上游 GAS_TURBINE LV） | 材料 |
| --- | --- | --- |
| 燃气轮机 | `CRC/RMR/EWE` | 锡转子 ×3 + 猫须探测器 ×2 + 超低压电动马达 ×2 + 红合金单线 ×1 + ULV 机械方块 ×1 |

- 全部材料工作台/蒸汽可达（转子/探测器/马达均零电力链），门槛自检通过。

## 与既有内容的边界

- 不做 ULV 蒸汽轮机（A1 负面清单）；不做 ULV 内燃机（LV 语义）。
- 燃料白名单终身不含乙烯/苯/液化气等化工上游燃料——它们属于 LV+ 产业链的取舍题。
- 数值原则同全线：总能量与上游一致，慢速释放；LV 燃气轮机的收益是功率与燃料面，不是省料。

## 实现要点（API 映射）

- `ULVGasTurbineMachine extends SimpleGeneratorMachine`（同红石发电机骨架），`recipeModifier(SimpleGeneratorMachine::recipeModifier)`，物品/流体输出位上限 0。
- 燃料配方经 `addRecipes` 运行时注入（GT 配方图，同红石燃料表手法）。
- 模型 `workableTieredHullModel(block/generators/ulv_gas_turbine)`，贴图沿用模组 ULV 调色板。

## 开放问题

无——数值待实测（重点：4,000 mB 罐容与 100t/批含硫气体的喂料节奏是否需要鼓风机位）。
