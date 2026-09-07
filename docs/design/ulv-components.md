# ULV 基础构件线（ULV Component Line）设计规格

> 状态：**已实现（v0.3，2026-09-07）** · 优先级：**P0（最高优先级，项目所有者 2026-09-07 指定）** · 类型：构件物品 + 覆盖板（cover）
>
> 变更记录：v0.3 依据首轮裁决——传送带/泵**确认公式值**（C5）；**马达前置范围扩大**：温差/红石发电机与全部用电机器统一以马达为公共前置，手摇发电机例外（D15）；开放问题 3、4 关闭。

## 定位

上游全部电动构件——电动马达、传送带模块、电动泵、机械臂、发射器、传感器、流体调节器——最低注册于 LV（调研 F12），ULV 层没有任何电动构件：**微电网连一根传送带都接不起来**。本线补齐 ULV 层的基础构件。

- 超低压电动马达：本模组全部 ULV 机器**与温差/红石发电机**的合成前置（D15 已裁决：凡带电动部件的设备统一以马达为公共前置；手摇发电机例外，保持零电门槛），内部需求闭环的核心件。
- 超低压传送带模块 / 超低压电动泵：以覆盖板（cover）形态提供玩家第一套电力物流。
- 一句话玩家故事：摇出发第一度电、造出第一台马达之后，流水线终于能自己动起来——哪怕每秒只挪两件东西。

## 上游现状与空白（调研核实，2026-09-07 第二轮）

- 构件物品注册层级 `LV..UV`（highTier 开启时至 OpV），**无任何 tier 0 条目**；且构件物品本身无 ElectricStats（纯物品 + `CoverPlaceBehavior`），调研 B1。
- 覆盖板速率由层级闭包决定，与物品无关：传送带 `2 × 4^min(tier, LuV)` 件/t、泵 `64 × 4^min(tier−1, IV)` mB/t（调研 B2）。**tier 0 直接代入公式即得：传送带 2 件/t、泵 16 mB/t，恰为 LV 的一半**——行为类零修改，上游只是没注册条目。
- 附属注册路径已核实：`GTCovers.init()` 会调用 `IGTAddon#registerCovers()`（无参 default 方法，早于 `GTRegistries.COVERS.freeze()`），在其中调用 public 的 `GTCovers.register(id, behaviorProvider, rendererSupplier)` 注册自定义 `CoverDefinition`，tier 经闭包传入上游行为类（`ConveyorCover::new` 等）即可（调研 B2）。

## 注册与命名

| 项目 | 资源 ID | 中文名 | 英文名 |
| --- | --- | --- | --- |
| 构件物品 | `gregulvexpansion:ulv_electric_motor` | 超低压电动马达 | ULV Electric Motor |
| cover 物品 | `gregulvexpansion:ulv_conveyor_module` | 超低压传送带模块 | ULV Conveyor Module |
| cover 物品 | `gregulvexpansion:ulv_electric_pump` | 超低压电动泵 | ULV Electric Pump |

- 覆盖板定义 ID 沿用上游层级命名规则：`gregulvexpansion:conveyor.ulv` / `pump.ulv`。
- Java：`GULVItems.ULV_ELECTRIC_MOTOR / ULV_CONVEYOR_MODULE / ULV_ELECTRIC_PUMP`；新注册中心 `GULVCovers`。
- 构件物品不带 ElectricStats（与上游构件一致，速率由 cover 层级闭包决定，见上）。

## 数值草案

| 构件 | ULV 数值（公式代入 tier 0） | 对照 LV（上游公式值） |
| --- | ---: | ---: |
| 传送带 cover | 2 件/t | 8 件/t |
| 电动泵 cover | 16 mB/t | 64 mB/t |
| 供能方式 | cover 标准语义：从所贴机器/容器的能量缓存取电 | 同 |
| 电动马达 | 纯合成构件，无运行时能耗语义 | — |

## 合成草案（全部工作台/蒸汽机可达，P0 门槛）

| 构件 | 材料 |
| --- | --- |
| 超低压电动马达 | 红合金单线 ×4 + 铁杆 ×2 + 铁板 ×2 + 猫须探测器 ×1（工作台） |
| 超低压传送带模块 | 超低压电动马达 ×1 + 橡胶板 ×2 + 铁螺丝 ×2（工作台） |
| 超低压电动泵 | 超低压电动马达 ×1 + 铁板 ×2 + 玻璃 ×2（工作台） |

- 橡胶、螺丝、铁杆均为 GTCEu 蒸汽时代已有材料形态，具体配方以定案核对为准。
- 门槛自检：全部材料不需要任何电力机器（红合金线 = 铜+红石蒸汽合金炉，猫须探测器见 [ulv-circuit-line.md](ulv-circuit-line.md)），与手摇发电机构成「探测器 → 手摇 → 马达 → ULV 机器」的零电力起步链。

## 与既有内容的边界

- 机械臂、发射器、传感器、流体调节器的 ULV 版不进首批，进候选池（[next-machine-candidates.md](next-machine-candidates.md) C13）；传送带与泵是「第一套自动化」的最小集。
- ULV 构件终身不出现在上游 LV+ 机器的配方或替代配方中。
- cover 功能集与上游对齐（过滤、正反转等既有功能保留），不新增任何 UI 或智能。

## 实现要点（API 映射）

- `GregULVExpansionAddon#registerCovers()` 中调用 `GULVCovers.init()`：注册三个 CoverDefinition（传送带/泵复用上游行为类传 tier 0；马达为纯物品，无 cover）。
- cover 物品用 `ComponentItem` + `CoverPlaceBehavior(definition)`（与上游 `CONVEYOR_MODULE_LV` 同构，调研 B1）。
- 渲染：首批尝试复用上游 cover 渲染器 Supplier 并按 tier 区分外观；不可复用则占位为简单方块贴图（开放问题 2）。

## 开放问题

v0.3 首轮裁决后：C5（速率基准，确认公式值 ±50% 调参空间）、开放问题 3（马达兼作发电机前置，已裁决为「是」，手摇例外）、开放问题 4（核为伪问题）全部关闭。残余仅 F1：cover 渲染器能否按 tier 复用（实现期验证，不可复用则简模占位）。
