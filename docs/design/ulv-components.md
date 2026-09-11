# ULV 基础构件线（ULV Component Line）设计规格

> 状态：**已实现（v0.7，2026-09-09）** · 优先级：**P0（最高优先级，项目所有者 2026-09-07 指定）** · 类型：构件物品 + 覆盖板（cover）
>
> 变更记录：
> - v0.7（2026-09-09，所有者指示）：**流体调节器提批进首批**（原候选池 C13 余量收窄为发射器/传感器）——cover 物品，PUMP_SCALING tier 0 = 16 mB/t 可调精度，上游泵族渲染器复用；上游调节器为装配机专属（泵 + 电路 ×2），工作台配方按其材料清单自设计（泵 + 探测器 ×2 + 玻璃 ×3）。另补录石油线重燃料脱硫（见 primitive-distillation-tower.md）。
> - v0.6（2026-09-09，所有者指示）：**马达配方整体对齐上游 LV 电动马达（铁变体）图案 `CWR/WMW/RWC`**——红合金单线 ×2（线缆基准）+ 铜单线 ×4（铜绕组）+ 铁杆 ×2 + 磁化铁杆 ×1；上游马达本就无电路件，探测器不再入马达（D14/D15 语义不变：探测器仍是手摇机、电解槽、机械臂与全部机器的电路入口）。
> - v0.5.1（2026-09-09，所有者指示）：**钢的下沉基准改为锻铁**——活塞（锻铁板 ×3/锻铁杆 ×2）与机械臂（锻铁杆 ×2）材料更新；锻铁 = 熔炉烧铁粒（煤火零电力），门槛自检不破。活塞齿轮槽取**小青铜齿轮**（锻铁无小齿轮物品形态，青铜与切割机锯片同族）；并修正图案计数：活塞红合金线 ×2、机械臂马达 ×2（图案 MRM 两处马达，与上游装配机配方一致）。
> - v0.5（2026-09-09，所有者立项指示）：**马达配方追加磁化铁杆 ×1**（对齐上游马达「磁杆 M 槽」语义；铁板 2→1 平衡成本，D15 探测器槽保留）。磁化铁杆双来源：工作台红石粉 ×4 手工磁化（上游 `iron_magnetic_stick` 原配方，零电力兜底）/ **ULV 极化机**电力磁化（见 [ulv-polarizer.md](ulv-polarizer.md)）。
> - v0.4（2026-09-09，所有者指示）：**电动活塞与机械臂提批进首批**（机械臂原候选池 C13）；上游构件清单补正——电动活塞亦注册于 `LV..UV`（F12 原清单漏列）。活塞为纯合成构件（上游活塞全层级无 cover 形态），机械臂以 cover 形态加入。
> - v0.3 依据首轮裁决——传送带/泵**确认公式值**（C5）；**马达前置范围扩大**：温差/红石发电机与全部用电机器统一以马达为公共前置，手摇发电机例外（D15）；开放问题 3、4 关闭。

## 定位

上游电动构件——电动马达、传送带模块、电动泵、**电动活塞**、机械臂、发射器、传感器、流体调节器——最低注册于 LV（调研 F12，活塞为 v0.4 补正），ULV 层没有任何电动构件：**微电网连一根传送带都接不起来**。本线补齐 ULV 层的基础构件。

- 超低压电动马达：本模组全部 ULV 机器**与温差/红石发电机**的合成前置（D15 已裁决：凡带电动部件的设备统一以马达为公共前置；手摇发电机例外，保持零电门槛），内部需求闭环的核心件。
- 超低压传送带模块 / 超低压电动泵 / 超低压机械臂：以覆盖板（cover）形态提供玩家第一套电力物流。
- 超低压电动活塞：纯合成构件（上游活塞全层级即无 cover 形态），机械臂与后续往复机构的动力前置。
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
| 构件物品 | `gregulvexpansion:ulv_electric_piston` | 超低压电动活塞 | ULV Electric Piston |
| cover 物品 | `gregulvexpansion:ulv_robot_arm` | 超低压机械臂 | ULV Robot Arm |
| cover 物品 | `gregulvexpansion:ulv_fluid_regulator` | 超低压流体调节器 | ULV Fluid Regulator |

- 覆盖板定义 ID 沿用上游层级命名规则：`gregulvexpansion:conveyor.ulv` / `pump.ulv` / `robot_arm.ulv`。
- Java：`GULVItems.ULV_ELECTRIC_MOTOR / ULV_CONVEYOR_MODULE / ULV_ELECTRIC_PUMP / ULV_ELECTRIC_PISTON / ULV_ROBOT_ARM / ULV_FLUID_REGULATOR`；新注册中心 `GULVCovers`。
- 构件物品不带 ElectricStats（与上游构件一致，速率由 cover 层级闭包决定，见上）。
- 标签：传送带/泵/机械臂/活塞/流体调节器分别加入上游全层级标签 `conveyor_modules` / `electric_pumps` / `robot_arms` / `electric_pistons` / `fluid_regulators`。

## 数值草案

| 构件 | ULV 数值（公式代入 tier 0） | 对照 LV（上游公式值） |
| --- | ---: | ---: |
| 传送带 cover | 2 件/t | 8 件/t |
| 电动泵 cover | 16 mB/t | 64 mB/t |
| 机械臂 cover | 2 件/周期（`CONVEYOR_SCALING(0) = 2×4^0`，与传送带同式） | 8 件/周期 |
| 流体调节器 cover | 16 mB/t（`PUMP_SCALING(0)`，与泵同式；传输量可调） | 64 mB/t |
| 供能方式 | cover 标准语义：从所贴机器/容器的能量缓存取电 | 同 |
| 电动马达 / 电动活塞 | 纯合成构件，无运行时能耗语义 | — |

## 合成草案（全部工作台/蒸汽机可达，P0 门槛）

| 构件 | 材料 |
| --- | --- |
| 超低压电动马达 | 红合金单线 ×2 + 铜单线 ×4 + 铁杆 ×2 + 磁化铁杆 ×1（工作台，v0.6 上游图案 `CWR/WMW/RWC`；磁化铁杆 = 铁杆 + 红石粉 ×4 工作台，或极化机电力磁化） |
| 超低压传送带模块 | 超低压电动马达 ×1 + 橡胶板 ×2 + 铁螺丝 ×2（工作台） |
| 超低压电动泵 | 超低压电动马达 ×1 + 铁板 ×2 + 玻璃 ×2（工作台） |
| 超低压电动活塞 | 超低压电动马达 ×1 + 锻铁板 ×3 + 红合金单线 ×2 + 锻铁杆 ×2 + 小青铜齿轮 ×1（工作台） |
| 超低压机械臂 | 红合金单线 ×3 + 锻铁杆 ×2 + 超低压电动马达 ×2 + 超低压电动活塞 ×1 + 猫须探测器 ×1（工作台） |
| 超低压流体调节器 | 超低压电动泵 ×1 + 猫须探测器 ×2 + 玻璃 ×3（工作台，v0.7 自设计——上游为装配机专属配方，按其材料清单下沉） |

- 活塞/机械臂镜像上游 LV 工作台图案（`PPP/CRR/CMG`、`CCC/MRM/PXR`）下沉：钢→锻铁、锡线缆→红合金线、LV 电路→猫须探测器（D14 tier-0 电路件）；活塞小齿轮因锻铁无该形态取青铜（`GENERATE_SMALL_GEAR` 旗标核实，2026-09-09）。
- 小铁齿轮为上游自带工作台配方（铁杆 ×2 + 铁板 ×1 + 锤/锉工具动作，PartsRecipeHandler），零电力可达。
- 门槛自检：全部材料不需要任何电力机器（红合金线 = 铜+红石蒸汽合金炉，猫须探测器见 [ulv-circuit-line.md](ulv-circuit-line.md)），与手摇发电机构成「探测器 → 手摇 → 马达 → 活塞 → 机械臂」的零电力起步链。

## 与既有内容的边界

- 发射器、传感器的 ULV 版不进首批，进候选池（[next-machine-candidates.md](next-machine-candidates.md) C13，v0.7 起余量收窄为这两类）；传送带/泵/机械臂/流体调节器构成「第一套物品+流体自动化」。
- ULV 构件终身不出现在上游 LV+ 机器的配方或替代配方中。
- cover 功能集与上游对齐（机械臂保留传送带/泵同源的过滤、正反转与三种传输模式，不新增任何 UI 或智能）。

## 实现要点（API 映射）

- `GregULVExpansionAddon#registerCovers()` 中调用 `GULVCovers.init()`：注册四个 CoverDefinition（传送带/泵/机械臂复用上游行为类传 tier 0；马达与活塞为纯物品，无 cover）。
- cover 物品用 `ComponentItem` + `CoverPlaceBehavior(definition)`（与上游 `CONVEYOR_MODULE_LV` / `ROBOT_ARM_LV` 同构，调研 B1）。
- 渲染：复用上游 cover 渲染器 Supplier 并按 tier 区分外观（传送带/臂 `IOCoverRenderer` 常量贴图组，泵 `PUMP_LIKE_COVER_RENDERER`），F1 已闭合。
