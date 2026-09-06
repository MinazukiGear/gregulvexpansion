# 设计文档

本目录存放 Greg ULV Expansion 的内容设计规格，约定沿用姊妹项目 Greg Steam Expansion：

- 每个机器/机制一个文件，文件名使用小写英文短横线命名（如 `ore-crushing.md`）。
- 章节或数值标注「已定案」后即为最终设计，代码与数据生成必须以其为准；未标注的内容仅代表当前思路，随时可能变动。
- 尚未定案的候选内容放在单独的清单文件（`next-machine-candidates.md`），不代表已确认会实现。

## 文档索引

| 文档 | 内容 | 状态 |
| --- | --- | --- |
| [00-overview.md](00-overview.md) | 总纲：定位、上游调研结论、设计原则、内容支柱、进度曲线、数值基准、命名与 API 规范 | 草案 v0.2 |
| [hand-crank-dynamo.md](hand-crank-dynamo.md) | 手摇发电机（第一桶 EU，P0） | 草案 v0.1 |
| [ulv-components.md](ulv-components.md) | ULV 基础构件线：超低压电动马达/传送带模块/电动泵（P0 最高优先级） | 草案 v0.2 |
| [ulv-circuit-line.md](ulv-circuit-line.md) | ULV 元件线：猫须探测器 + ULV 电路替代配方（探测器 P0 / 替代配方 P3） | 草案 v0.2 |
| [thermal-generator.md](thermal-generator.md) | 温差发电机（环境热免维护电，P1） | 草案 v0.2 |
| [redstone-generator.md](redstone-generator.md) | 红石发电机（可堆叠燃料电，P2） | 草案 v0.2 |
| [small-steam-turbine.md](small-steam-turbine.md) | 小型蒸汽轮机（蒸汽变现 + 姊妹项目联动，P1） | 草案 v0.2 |
| [lead-acid-battery-line.md](lead-acid-battery-line.md) | 铅酸电池线（PbO₂ 新材料 + 电池物品 + 蓄电池箱，P1） | 草案 v0.1 |
| [lead-chamber-acid-plant.md](lead-chamber-acid-plant.md) | 铅室法制酸装置（无电多方块，P1） | 草案 v0.2 |
| [primitive-electrolyzer.md](primitive-electrolyzer.md) | 原型电解槽（水电解等配方子集，P1） | 草案 v0.2 |
| [ulv-basic-machines.md](ulv-basic-machines.md) | 超低压洗矿机 / 线材轧机 / 切割机（配方子集下沉，P1） | 草案 v0.2 |
| [next-machine-candidates.md](next-machine-candidates.md) | 后续内容候选总表与负面清单 | 候选 |
| [open-questions.md](open-questions.md) | 开放问题裁决追踪表（34 项，按裁决优先级分类，附方案权衡与倾向） | 追踪 |

当前**没有任何已定案设计**：上述文档均为 2026-09-07 立项讨论产生的草案（v0.1 首批评审，v0.2 第二轮上游调研修订），数值、边界与命名都待项目所有者逐项确认；确认后的文档在此表与 [总纲 §4](00-overview.md) 中标注「已定案」。

上游事实断言（文中标注「调研 F*」者）基于对 GTCEu 7.5.3 源码的逐条核实（2026-09-07 两轮），上游版本升级时应复核。已知的两项设计决策记录：硒材料不做考虑（所有者指定）；锗路线因上游不可获取而冻结。
