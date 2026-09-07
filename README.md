# Greg ULV Expansion

基于 Minecraft 1.20.1 Forge 与 GregTech CEu Modern 的超低压（ULV）时代扩展附属模组。

> **首批内容已实现（待实测调优）**：工程、配方与资源均已落盘；手感数值（摇动节奏、产出速率等）仍以设计文档标注的区间为准实测微调。全部设计规格见 [`docs/design/`](docs/design/)。
>
> **⚠ 本项目大量使用 AI 辅助开发**：代码、设计文档、语言条目与部分贴图资源均在 AI 协助下完成，全部产出经人工审核后合入。AI 生成的数值、结构规则与接口约定一律以 `docs/design/` 中标注为"已定案"的章节为准，未标注的内容不代表最终设计。欢迎审阅源码与反馈问题。

## 项目定位

围绕 GregTech CEu Modern 电压体系的起点——**ULV（Ultra Low Voltage，超低压，8 EU/t）**——扩展从蒸汽时代跨入电力时代之间的过渡玩法。一句话玩家故事：**你刚摸到电，还没有电网**。

首批内容（零电起步链：猫须探测器 → 手摇发电机 → 电动马达 → 微电网）：

- **猫须探测器**：方铅矿提纯 + 红合金线，ULV 元件线起点；
- **手摇发电机**：摇 10 秒 ≈ 跑 1 分钟，曲柄可拆装（兼容 AE2 曲柄）；
- **ULV 构件线**：电动马达 + 传送带（2 件/t）/ 电动泵（16 mB/t）覆盖板；
- **原型电解槽**：水电解与 PbO₂ 阳极氧化，ULV 电化学第一课；
- **铅酸电池线**：单格电池 4k / 电池组 16k / 铅酸蓄电墙 24k EU，填平 1k → 80k 断档；
- **铅室法制酸装置**：无电多方块，蒸汽产硫酸（1850 年代工业复刻）；
- **温差发电机**：贴热产电 1–5 EU/t 免维护保底；
- **ULV 线材轧机 / 切割机**：上游机器的配方子集下沉，微电网有活干。

姊妹项目：[Greg Steam Expansion](https://github.com/MinazukiGear/gregsteamexpansion)（蒸汽时代扩展）。本项目与其同源同构，工程骨架、构建脚本与开发流程大量沿用该项目。

## 开发环境

| 组件 | 版本 |
| --- | --- |
| Minecraft | 1.20.1 |
| Forge | 47.4.10 |
| Java | 17 |
| GregTech CEu Modern | 7.5.3（必需前置） |
| Gradle | 8.8（项目 Wrapper） |

EMI、Jade、JECh（拼音搜索）、精妙背包/存储、Modern UI、GTM Things（连同其必需的 AE2 和 AE2 的前置 GuideME）仅作为开发客户端测试工具由 Gradle 运行时加载，不是本模组前置，也不会打包进发布 JAR。

## 开始开发

```powershell
.\gradlew.bat genIntellijRuns   # 生成 IDEA 运行配置（JDK 17）
.\gradlew.bat runClient         # 启动开发客户端
.\gradlew.bat build -x test     # 构建发布 JAR（build/libs/）
.\gradlew.bat runData           # 重新生成数据（资源/配方/语言）
```

若系统默认 Java 不是 17，先设置 `$env:JAVA_HOME` 指向 JDK 17。

## 项目信息

- Mod ID：`gregulvexpansion`
- 入口类：`com.hoshino.gregulvexpansion.GregULVExpansion`
- 当前版本：`0.1.0`（未发布）
- 许可：代码与功能性资源 LGPL-3.0（`LICENSE.txt`）；`textures/` 图像素材 CC BY-NC-SA 4.0，禁止商用（`LICENSE-ASSETS.txt`）
- 提交约定：Conventional Commits（`feat:`/`fix:`/`docs:` 等前缀），描述正文使用中文

## 致谢

工程骨架与开发流程沿用姊妹项目 [Greg Steam Expansion](https://github.com/MinazukiGear/gregsteamexpansion)。其余设计参考将随内容定案补充。

## 已知上游问题

开发客户端同时加载 GTCEu 内嵌 LDLib 与 EMI 时可能遇到 Mixin 初始化竞态（`MixinTargetAlreadyLoadedException` / `EmiPlugin was loaded too early`），参见 [GregTechCEu/GregTech#2917](https://github.com/GregTechCEu/GregTech/issues/2917)；不影响未安装 EMI 的环境。
