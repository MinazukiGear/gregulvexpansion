# 设计文档

本目录存放 Greg ULV Expansion 的内容设计规格，约定沿用姊妹项目 Greg Steam Expansion：

- 每个机器/机制一个文件，文件名使用小写英文短横线命名（如 `ore-crushing.md`）。
- 章节或数值标注「已定案」后即为最终设计，代码与数据生成必须以其为准；未标注的内容仅代表当前思路，随时可能变动。
- 尚未定案的候选内容放在单独的清单文件（`next-machine-candidates.md`），不代表已确认会实现。

## 文档索引

| 文档 | 内容 | 状态 |
| --- | --- | --- |
| [00-overview.md](00-overview.md) | 总纲：定位、上游调研结论、设计原则、内容支柱、进度曲线、数值基准、命名与 API 规范 | 已裁决草案 v0.3（§4 状态列已标记实现进度） |
| [hand-crank-dynamo.md](hand-crank-dynamo.md) | 手摇发电机（第一桶 EU，P0；摇 10 秒 ≈ 跑 1 分钟） | 已实现 v0.3 |
| [ulv-components.md](ulv-components.md) | ULV 基础构件线：超低压电动马达/传送带模块/电动泵（P0 最高优先级） | 已实现 v0.3 |
| [ulv-circuit-line.md](ulv-circuit-line.md) | ULV 元件线：猫须探测器 + 替代配方 P2（禁用上游原配方，探测器为 ULV 电路唯一入口） | 已实现 v0.4.3 |
| [thermal-generator.md](thermal-generator.md) | 温差发电机（环境热免维护电，P1） | 已实现 v0.3.1 |
| [redstone-generator.md](redstone-generator.md) | 红石发电机（可堆叠燃料电，P2） | 已实现 v0.4 |
| [lead-acid-battery-line.md](lead-acid-battery-line.md) | 铅酸电池线（PbO₂ 新材料 + 电池物品 + 铅酸蓄电墙，P1） | 已实现 v0.3.1 |
| [lead-chamber-acid-plant.md](lead-chamber-acid-plant.md) | 铅室法制酸装置（无电多方块，P1） | 已实现 v0.3.1 |
| [primitive-electrolyzer.md](primitive-electrolyzer.md) | 原型电解槽（水电解 + PbO₂ 氧化，储能链前置，P1 首位） | 已实现 v0.3.1 |
| [ulv-basic-machines.md](ulv-basic-machines.md) | 超低压线材轧机 / 切割机 / 卷板机 / 车床 / 化学反应釜 / 流体固化器 / 流体提取机（配方子集下沉，P1；洗矿机已裁决砍除） | 已实现 v0.9 |
| [primitive-distillation-tower.md](primitive-distillation-tower.md) | 原始蒸馏塔 + 原始裂化机 + 超低压流体钻井机（石油线，无电多方块，含聚乙烯链） | 已实现 v1.2（待实测） |
| [open-questions.md](open-questions.md) | 开放问题裁决追踪表（34 项首轮裁决记录 + 待办调研） | 首轮裁决完成 |
| [next-machine-candidates.md](next-machine-candidates.md) | 后续内容候选总表与负面清单（含蒸汽轮机/洗矿机落选记录） | 候选 |

**首批范围已裁决（2026-09-07）**：上表 v0.3 各文档的结构、机器清单、命名与取舍均经项目所有者逐项确认（34 项开放问题全部裁决，记录见 [open-questions.md](open-questions.md)）；数值仍可在文档标注的区间内实测微调，结构性变更需新一轮裁决并更新总纲 §4。

上游事实断言（文中标注「调研 F*」者）基于对 GTCEu 7.5.3 源码的逐条核实（2026-09-07 两轮），上游版本升级时应复核。已知的两项设计决策记录：硒材料不做考虑（所有者指定）；锗路线因上游不可获取而冻结。
