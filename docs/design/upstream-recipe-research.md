# 上游配方与 API 调研记录（第二轮调研，2026-09-07）

> 状态：**调研完成**（B4 硫副产实测除外，需进游戏）。本文件是纯调研产出，不含设计决策；结论供各设计文档的子集表与实现直接引用。
> 调研来源：本机 Gradle 缓存的 GTCEu 7.5.3 sources jar（`gtceu-1.20.1-7.5.3_mapped_official_1.20.1`），全部结论标注源码文件与行号。

## 一、F1：cover 渲染器按 tier 复用性 —— 结论：**可复用，无需简模占位**

源码依据：

- `GTCovers.registerTiered(...)`（GTCovers.java L135-144）为每个层级注册独立 `CoverDefinition`（ID 形如 `conveyor.ulv`），渲染器 supplier 签名是 `Int2ObjectFunction<ICoverRenderer>`，即**每个层级各自拿到一个渲染器实例，但实例内容由 supplier 决定**。
- 传送带：`tier -> new IOCoverRenderer("block/cover/conveyor", ...)`（L56-60）——**tier 参数被忽略**，全层级共用同一组贴图路径（普通/发光/反转发光）。
- 泵与流体调节器：`tier -> IOCoverRenderer.PUMP_LIKE_COVER_RENDERER`（L72、L75）——所有层级共用**同一个常量实例**。
- `IOCoverRenderer` 全类没有任何 tier 字段或参数（IOCoverRenderer.java L29-91），贴图叠加逻辑只看 `IIOCover.getIo()` 方向；`SimpleCoverRenderer` 同理。
- ULV 层级先例：`SOLAR_PANEL` 用 `ALL_TIERS_WITH_ULV` 注册（L112-113），ULV cover 是上游支持的场景。

对本模组的含义：

1. ULV 传送带/泵直接复用上游 `IOCoverRenderer` + 上游贴图即可，**F1 预案「不可复用则简模占位」不会触发**。
2. 注册时 ID 自然生成 `conveyor.ulv` / `pump.ulv`，与总纲 §7 的 cover ID 约定 `<name>.<vn>` 一致，无需特殊处理。
3. 若想给 ULV 构件做差异化外观（可选），只需在 supplier 里按 tier 换贴图路径，架构上零障碍。

## 二、B5：真空管 / NAND 芯片原配方完整清单（D14 移除目标）

### 2.1 配方 ID 与移除机制

- **机器配方 ID 规则**：`<namespace>:<配方类型注册名>/<builder 名>`（GTRecipeBuilder.java L1638-1640：`getId()` 返回 `recipeType.registryName.getPath() + "/" + id.getPath()`）。
- **工作台配方 ID 规则**：`VanillaRecipeHelper.addShapedRecipe` 用 `GTCEu.id(regName)`（VanillaRecipeHelper.java L235-237）。
- **移除机制**：`GTRecipes.recipeAddition()` 用统一 consumer 包裹全部配方生成（GTRecipes.java L41-45），凡 ID 命中 `RECIPE_FILTERS` 的配方（**含 GT 机器配方**）直接不落盘；`removeRecipes` 钩子往 `RECIPE_FILTERS` 加 ID（L117）。**D14 通过 `IGTAddon#removeRecipes` 实现确认可行。**

### 2.2 真空管（4 条）

| # | 配方 ID | 输入 | 产出 | EUt | 时长 |
| --- | --- | --- | --- | ---: | ---: |
| 1 | `gtceu:vacuum_tube`（工作台） | 钢螺栓 ×2 + 玻璃管 ×1 + 铜单线 ×3 | 1× 真空管 | — | — |
| 2 | `gtceu:assembler/vacuum_tube_plain` | 玻璃管 + 钢螺栓 + 铜单线 ×2，电路 1 | 2× | 7 | 120 |
| 3 | `gtceu:assembler/vacuum_tube_red_alloy` | 同上 + 红合金流体 18L | 3× | 7 | 40 |
| 4 | `gtceu:assembler/vacuum_tube_red_alloy_annealed` | 玻璃管 + 钢螺栓 + 退火铜单线 ×2 + 红合金 18L | 4× | 7 | 40 |

（CircuitRecipes.java L308-336）

### 2.3 NAND 芯片 ULV（2 条，电路装配机）

| # | 配方 ID | 输入 | 产出（普通/harder） | EUt | 时长 |
| --- | --- | --- | --- | ---: | ---: |
| 5 | `gtceu:circuit_assembler/nand_chip_ulv_good_board` | 好电路板 + 简单 SoC + 红合金螺栓 ×2 + 锡细线 ×2 | 8× / 4× | 30 | 300 |
| 6 | `gtceu:circuit_assembler/nand_chip_ulv_plastic_board` | 塑料电路板 + 简单 SoC + 红合金螺栓 ×2 + 锡细线 ×2 | 12× / 6× | 30 | 300 |

（CircuitRecipes.java L1079-1093；`outputAmount = harder ? 1 : 2`，L1002）

### 2.4 替代配方等价设计的材料池（P2 用）

玻璃管（上游自身有 3 种制法：合金炉/流固/压制，CircuitRecipes L338-355）、钢螺栓、铜/退火铜单线、红合金流体、好/塑料电路板、简单 SoC、红合金螺栓、锡细线。全部为 ULV 前可达的材料。

## 三、B2：上游电解配方清单（原型电解槽子集对标）

配方类型注册名 `electrolyzer`（GTRecipeTypes.java L243），来源 SeparationRecipes.java L324-365：

| 配方 ID | 输入 | 产出 | EUt | 时长 |
| --- | --- | --- | ---: | ---: |
| `water_electrolysis` | 水 1000 mB | 氢 2000 + 氧 1000 mB | 30 | 1500 |
| `distilled_water_electrolysis` | 蒸馏水 1000 mB | 氢 2000 + 氧 1000 mB | 30 | 1500 |
| `salt_water_electrolysis`（D11 后置，仅备案） | 盐水 1000 mB | NaOH 尘 ×3 + 氯 1000 + 氢 1000 | 30 | 720 |
| `sodium_bisulfate_electrolysis`（同上） | 亚硫酸氢钠尘 ×7 | 过硫酸钠 500 + 氢 1000 | 30 | 150 |

按总纲 §6.1 降档规则的水电解 ULV 版：**EUt 30 → 7（VA[ULV]），时长 1500 → 3000t**，输入输出不变。

**PbO 氧化对标**：上游不存在 PbO₂，也无任何 PbO 电解条目（与调研 F7 一致）——阳极氧化为纯自定义配方，无上游数值可对标。

## 四、轧机 / 切割机上游配方族（子集表数值列）

### 4.1 线材轧机（WireRecipeHandler.java）

上游轧机**吃锭/宝石/尘，不吃板**（L82-84：`ingot / gem / dust`）：

| 配方族 | 输入 → 产出 | EUt | 时长 |
| --- | --- | ---: | ---: |
| 单线 | 锭 ×1 → 单线 ×2（电路 1） | **7**（熔点 <2800）/ 30（≥2800） | 材料质量 |
| 多倍线 | 锭 ×n → 2x/4x/8x/16x 线 ×1 | 同上 | 质量 ×n |
| 细线 | 锭 ×1 → 细线 ×8（电路 3） | 同上 | 质量 ×3 |

板→线在上游**只有工作台配方**（板 + 线剪工具，"Xx"，L130-134），无机器配方。

**与设计数值的交叉验证**：红合金、铜、铁等常用线材熔点均 <2800，上游轧机配方 EUt 恰为 `VA[ULV]=7`——ULV 轧机子集配方只需「时长 ×2」即可守恒，EUt 无需换算，与 `ulv-basic-machines.md` 的换算规则自洽。

### 4.2 切割机（PartsRecipeHandler.java、MaterialRecipeHandler.java）

| 配方族 | 输入 → 产出 | EUt | 时长 |
| --- | --- | ---: | ---: |
| 杆→螺栓 | 杆 ×1 → 螺栓 ×4 | 4 | 质量 ×2 |
| 长杆→杆 | 长杆 ×1 → 杆 ×2 | 4 | 质量 |
| 板材块→板 | 块 ×1 → 板 ×(materialAmount/M) | 30 | 质量 ×8 |
| 晶圆切割（电路系，LV+ 语义） | wafer → 芯片 | 64-192 | 900 |

**注意**：上游**齿轮不走切割机**（齿轮 = 挤出机/合金炉，PartsRecipeHandler.java L201-214）。总纲 §5「切割机出齿轮」的表述与上游配方族不符，子集表收录齿轮时应改走「轧机/工作台」或修正总纲表述——此为实现前需对齐的一处措辞，不构成设计变更。

### 4.3 通用注意

- 杆→螺栓、长杆→杆的 EUt=4 **低于** ULV 换算基准 7，直接镜像时用上游原值即可（不超过 8 EU/t 上限）。
- 板材块→板 EUt=30 按降档换算为 7、时长 ×2（质量 ×16）。

## 五、遗留

| 项 | 状态 |
| --- | --- |
| B4 硫粉主世界副产积累速度实测 | **待办**（需进游戏实测，铅室产能校准前完成即可） |
| 34 项待办调研中其余 4 项 | 本文档已覆盖，open-questions.md 待办表已更新 |
