# Flink 面试题清单

> 复习重点：Flink 面试常围绕“有状态流处理、事件时间、Watermark、Checkpoint、Exactly Once、窗口、反压、状态后端、Flink SQL、性能调优”展开。

## 1. Flink 的核心架构是什么？

- [ ] Flink 集群由哪些组件组成？
  答案：Flink 主要由 Client、JobManager、TaskManager 组成。Client 负责提交作业；JobManager 负责作业调度、Checkpoint 协调、故障恢复；TaskManager 负责执行具体 Task，并通过 Slot 提供资源。 新版本中 JobManager 内部角色可拆为 Dispatcher、ResourceManager、JobMaster。

## 2. Flink 和 Spark Streaming 的区别是什么？

- [ ] 为什么说 Flink 是真正的流处理？
  答案：Flink 以事件流为核心，逐条处理数据，天然支持低延迟、有状态计算、事件时间和 Exactly Once。传统 Spark Streaming 是微批模型，Structured Streaming 虽然抽象成流，但底层很多场景仍以微批执行为主。 选择 Flink 通常是因为低延迟、状态一致性、复杂事件时间和持续流处理能力。 Flink 的核心模型是持续事件流，数据到来后可以逐条进入算子处理，天然支持低延迟、有状态计算、事件时间、Watermark、窗口和 Exactly Once 状态一致性。Spark Streaming 早期 DStream 是微批模型，把一小段时间内的数据收集成一个 batch 再执行，延迟下限通常受 batch interval 限制。Structured Streaming 在 API 上抽象成无界表，但很多执行模式仍然是微批。 面试时可以这样总结：Flink 更适合低延迟、复杂事件时间、有状态实时计算；Spark 更适合批处理、湖仓 SQL、离线 ETL 和统一分析场景。两者不是谁完全替代谁，而是侧重点不同。 不是。微批在吞吐、容错、生态集成上很成熟，尤其适合秒级或分钟级实时指标、湖仓增量处理。问题是当业务要求毫秒级延迟、复杂乱序处理、长生命周期状态和精细事件时间语义时，Flink 的持续流模型更自然。 可以。Flink 现在强调批流统一，DataStream 和 Table/SQL 都支持批流场景。批处理可以被视为有界流，流处理是无界流。区别在于执行优化、资源调度和状态生命周期会根据有界/无界输入做不同处理。

## 3. DataStream、DataSet、Table/SQL 有什么区别？

- [ ] Flink 常用 API 如何选择？
  答案：DataStream 面向流处理底层 API，灵活度高；DataSet 是早期批处理 API，逐渐被 Table/SQL 和 DataStream 批流统一取代；Table/SQL 声明式更强，适合数仓和实时指标。 现代 Flink 推荐使用 DataStream + Table/SQL，批流统一是重要方向。

## 4. Flink 的并行度和 Slot 是什么关系？

- [ ] Slot 是 CPU 吗？
  答案：Slot 是 TaskManager 上的资源划分单位，不等同于 CPU 核。并行度决定同一算子的并行 SubTask 数，Slot 提供这些 SubTask 运行资源。不同算子的 SubTask 可通过 Slot Sharing 共享 Slot。 设置并行度要看吞吐、状态大小、Source 分区数和下游承载能力。

## 5. 什么是 Operator Chain？

- [ ] Flink 为什么要做算子链合并？
  答案：Operator Chain 会把可以串联的算子合并到同一个 Task 线程中执行，减少线程切换、网络传输和序列化开销，提高性能。 `keyBy`、shuffle、rebalance 等会打断 chain，因为发生了数据重分区。

## 6. Flink 的时间语义有哪些？

- [ ] Processing Time、Event Time、Ingestion Time 有什么区别？
  答案：Processing Time 是机器处理时间，延迟低但结果受系统影响；Event Time 是事件真实发生时间，能处理乱序，是最常用的准确语义；Ingestion Time 是进入 Flink 的时间，介于两者之间。 实时数仓、用户行为分析通常用 Event Time。

## 7. Watermark 是什么？

- [ ] Watermark 如何处理乱序数据？
  答案：Watermark 是事件时间进度标记，表示系统认为不会再来早于该时间的数据。窗口会在 Watermark 超过窗口结束时间时触发计算。允许乱序时通常设置 `maxOutOfOrderness`。 Watermark 太激进会丢迟到数据，太保守会增加延迟。 Event Time 是事件真实发生时间，通常来自业务字段，比如订单创建时间、日志打点时间。Processing Time 是 Flink 机器处理这条数据的时间。实时数仓和用户行为分析通常选择 Event Time，因为数据可能乱序、延迟到达，如果用 Processing Time，窗口结果会受网络、Kafka 堆积、作业重启影响。 Watermark 是事件时间进度的声明，可以理解为 Flink 认为“时间已经推进到某个点，早于这个点的数据大概率不会再来了”。事件时间窗口通常在 Watermark 超过窗口结束时间时触发计算。例如 10:00-10:05 的窗口，Watermark 到达 10:05 后窗口触发。 常见方式是从事件时间字段中提取 timestamp，然后按策略生成 Watermark。比如允许最大乱序 5 秒，那么当前见过的最大事件时间是 10:05:20 时，可以发出 10:05:15 的 Watermark。这样可以容忍 5 秒内乱序。Watermark 可以在 Source 端生成，也可以在后续算子指定，但生产中更推荐尽早在 Source 附近生成，语义更清晰。 Watermark 太激进，也就是允许乱序时间太短，窗口会过早关闭，迟到数据增多，实时结果容易不准。Watermark 太保守，也就是允许乱序时间太长，窗口迟迟不触发，延迟变高，状态保留时间变长。它本质是在准确性、延迟和状态成本之间取舍。

## 8. 窗口有哪些类型？

- [ ] 滚动窗口、滑动窗口、会话窗口有什么区别？
  答案：滚动窗口固定大小且不重叠；滑动窗口固定大小但按滑动步长重叠；会话窗口按事件间隔切分，超过超时时间就关闭会话。 窗口还可按时间窗口和计数窗口划分，生产中最常用事件时间窗口。

## 9. Late Data 迟到数据如何处理？

- [ ] 窗口关闭后又来了旧数据怎么办？
  答案：可通过 allowedLateness 允许窗口延迟关闭，也可通过 side output 把迟到数据输出到侧输出流，后续做补偿或离线修正。 实时指标要明确迟到容忍时间和补偿机制。 迟到数据分两类：一类是在 Watermark 之后但窗口还允许迟到范围内到达，可以通过 `allowedLateness` 让窗口延迟清理，并对结果做更新；另一类是超过允许迟到范围的数据，可以通过 side output 输出到侧输出流，进入补偿链路、离线修正或单独告警。 比如做实时 GMV 大屏，允许 1 分钟乱序，窗口先快速出结果；迟到 10 分钟内的数据可以更新明细宽表和指标表；超过 10 分钟的数据进入迟到数据 Topic，夜间离线任务重算修正。 无限等待会导致窗口状态无法清理，状态持续膨胀，Checkpoint 变慢，最终影响作业稳定性。实时计算必须定义数据完整性的边界，例如“实时结果允许 5 分钟内修正，最终口径由 T+1 离线重算保证”。 下游必须支持更新语义。Append-only Sink 只能追加，不能修正旧结果；如果窗口结果会更新，最好写 Upsert Kafka、支持主键更新的数据库，或者湖仓表格式。否则可能出现同一个窗口多条结果，下游不知道哪条是最终值。

## 10. State 是什么？

- [ ] Flink 为什么强调有状态计算？
  答案：State 是算子在处理流数据时保存的中间状态，比如计数、聚合、去重、窗口内容、维表缓存。Flink 的状态和 Checkpoint 结合，能在故障后恢复一致计算。 状态分为 Keyed State 和 Operator State。

## 11. Keyed State 和 Operator State 的区别是什么？

- [ ] 分别适合什么场景？
  答案：Keyed State 绑定到 key，只能在 `keyBy` 后使用，适合按用户、订单等维度维护状态；Operator State 绑定到算子并行实例，适合 Source offset、广播配置等。 Keyed State 在 rescale 时可按 key group 重新分布。

## 12. Flink 常见状态后端有哪些？

- [ ] HashMapStateBackend 和 RocksDBStateBackend 有什么区别？
  答案：HashMapStateBackend 状态在 JVM 堆内，访问快但容量受内存限制；RocksDBStateBackend 状态在本地 RocksDB，支持超大状态但访问和序列化成本更高。 大状态、长窗口、海量 key 场景通常考虑 RocksDB。 HashMapStateBackend 把状态对象放在 JVM 堆内，访问速度快，实现简单，适合状态量较小、延迟敏感的场景。但状态受堆内存限制，状态大时容易造成 GC 压力。 RocksDBStateBackend 把状态存储在本地 RocksDB，数据主要在堆外和磁盘，适合大状态、海量 key、长窗口、去重和复杂 Join 场景。缺点是读写要经过序列化和 RocksDB，本地磁盘 IO、压缩、compaction 都会影响性能。 不是。RocksDB 主要降低 JVM 堆压力，但仍然会使用堆外内存、block cache、write buffer、索引和本地磁盘。如果 managed memory、容器内存、RocksDB 内存参数配置不合理，仍可能 OOM 或被容器 kill。还要关注 checkpoint 上传带宽和本地磁盘空间。 State TTL 用于给状态设置过期时间，避免长期不访问的 key 一直占用状态。适合去重、维表缓存、会话状态等场景。但 TTL 不是业务正确性的万能解，设置太短可能导致重复数据被当成新数据，设置太长又会增加状态。TTL 还要注意清理策略和过期状态是否对查询可见。

## 13. Checkpoint 的原理是什么？

- [ ] Flink 如何做分布式一致快照？
  答案：Flink 使用 Chandy-Lamport 思想，通过 Checkpoint Barrier 在数据流中传播。Source 注入 Barrier，各算子收到所有上游 Barrier 后对本地状态做快照，并把快照持久化到外部存储。 Checkpoint 是容错机制，Savepoint 更偏手动运维和版本升级。 Flink Checkpoint 基于分布式一致性快照思想。Checkpoint Coordinator 在 JobManager 中触发 checkpoint，Source 往数据流中注入 checkpoint barrier。Barrier 随数据一起向下游流动。算子收到来自所有上游的同一轮 barrier 后，对自己的状态做快照，并继续向下游发送 barrier。所有算子快照完成后，这一轮 checkpoint 才算成功。 如果发生故障，Flink 会从最近一次成功 checkpoint 恢复状态，Source 也恢复到 checkpoint 中记录的位置，例如 Kafka offset，然后重新消费后续数据，从而保证内部状态一致。 如果一个算子有多个上游，同一轮 checkpoint 的 barrier 到达时间可能不同。为了让快照对应同一个逻辑时间点，算子会阻塞已经收到 barrier 的输入通道，继续等待其他通道 barrier 到达，这就是 barrier alignment。它能保证一致性，但在反压或上游速度不均时可能拉长 checkpoint 时间。 Unaligned Checkpoint 会把通道中的 in-flight 数据也一起纳入快照，减少等待 barrier 对齐的时间，适合严重反压场景。但它会增加 checkpoint 数据量和恢复成本，不是所有场景都应该开启。面试中可以说：先定位反压原因，unaligned checkpoint 是缓解 checkpoint 超时的手段，不是替代性能治理。

## 14. Exactly Once 是怎么实现的？

- [ ] Flink 的端到端 Exactly Once 需要哪些条件？
  答案：Flink 内部通过 Checkpoint 保证状态一致；Source 需要可重放，比如 Kafka offset；Sink 需要支持事务或幂等提交，比如 Kafka 事务、两阶段提交、幂等写数据库。 只说 Flink Exactly Once 不够，端到端还要看外部系统。 Flink 内部 Exactly Once 依赖 checkpoint：状态变更和输入位置一起被快照，故障恢复后从一致点继续处理。端到端 Exactly Once 还需要 Source 和 Sink 配合。Source 必须可重放，比如 Kafka offset 可以回退到 checkpoint 记录的位置；Sink 必须支持事务、幂等或两阶段提交。 以 Kafka 到 Kafka 为例，Flink 消费 Kafka 时把 offset 放入 checkpoint；写 Kafka 时开启事务 Sink，某一轮 checkpoint 成功后提交对应事务。如果作业失败，未提交事务会被 abort，恢复后从 checkpoint offset 重放，最终下游只看到提交成功的数据。 MySQL 不天然参与 Flink checkpoint 协议。常见做法是幂等写，比如用业务主键 `insert on duplicate key update`，重复写不会改变最终结果；或者使用两阶段提交 Sink，但实现复杂且要处理事务超时、连接恢复、悬挂事务等问题。实际生产中更常说“通过 Flink checkpoint + MySQL 幂等写，实现最终结果精确一次”。 不一定。端到端语义取决于 Sink 的提交协议。两阶段提交 Sink 会在 checkpoint 前预提交，checkpoint 成功后正式提交；如果 Sink 只是普通写外部系统，checkpoint 成功只能说明 Flink 状态成功，不代表外部副作用和状态完全一致。

## 15. Savepoint 和 Checkpoint 的区别是什么？

- [ ] 为什么升级作业常用 Savepoint？
  答案：Checkpoint 是自动容错快照，生命周期由 Flink 管理；Savepoint 是用户手动触发的状态快照，适合升级、迁移、修改并行度和回滚。 修改算子 UID 可能导致状态无法恢复，因此生产作业要显式设置 UID。 Checkpoint 是 Flink 自动触发的容错快照，主要用于失败恢复，生命周期由 Flink 管理。Savepoint 是用户主动触发的、面向运维的状态快照，常用于版本升级、作业迁移、修改并行度、回滚。 生产升级常见流程是：先给关键算子设置稳定 UID；停止作业并触发 savepoint；用新代码从 savepoint 恢复；观察指标；如果异常，再用旧版本从 savepoint 或上一个可靠状态恢复。 Flink 恢复状态时需要把 savepoint 中的状态映射回新作业图中的算子。如果没有显式 UID，Flink 会根据作业图自动生成，代码稍微调整就可能变化，导致找不到原状态。生产作业中对有状态算子显式设置 UID，是为了升级和恢复时状态可识别。 Keyed State 会按 key group 重新分配，通常支持 rescale；Operator State 需要看状态分发策略，比如 list state 可以重新分配，union state 会广播给所有并行实例。并行度变化前要确认状态类型和 connector 支持情况。

## 16. Flink 反压是什么？怎么排查？

- [ ] BackPressure 常见原因有哪些？
  答案：反压是下游处理慢导致上游发送受阻。常见原因有 Sink 慢、数据倾斜、外部系统慢、状态访问慢、GC、网络瓶颈。可看 Flink Web UI 的 BackPressure、busy/idle 指标、Task 吞吐和下游延迟。 先定位瓶颈算子，再决定扩容、优化逻辑、异步 IO、批量写或治理倾斜。 反压是下游消费能力不足，导致上游发送被阻塞。排查要先看 Flink Web UI 的 backpressure、busy、idle、吞吐和 checkpoint 指标，定位是哪个算子最慢。通常最下游 Sink 慢会向上游传播，但也可能是中间算子状态访问慢、数据倾斜或外部维表查询慢。 排查步骤：第一，看拓扑中哪个节点 busy 高、backpressured 高；第二，看该节点输入输出速率是否匹配；第三，看 Task 级别是否只有少数 subtask 慢，如果是，怀疑数据倾斜；第四，看外部系统延迟，比如 Kafka、MySQL、Redis、ES；第五，看 GC、RocksDB IO、checkpoint duration 和 alignment time。 不一定。如果瓶颈是 CPU 计算，加并行度可能有用；如果瓶颈是下游数据库写入能力，加并行度会制造更多并发请求，让下游更慢；如果是热点 key 倾斜，加普通并行度也不一定解决，因为热点 key 仍然落到一个 subtask。要先定位瓶颈再处理。 可以做批量写、异步 IO、连接池调优、幂等 upsert、缓冲队列、限流、拆分 Sink、提升外部系统吞吐，必要时把直接写外部系统改成先写 Kafka，再由专门服务削峰写入。实时链路中 Sink 通常是最容易引发反压的地方。

## 17. Flink 数据倾斜如何处理？

- [ ] 某些 key 特别热怎么办？
  答案：可通过 key 加盐打散、两阶段聚合、本地预聚合、热点 key 单独处理、动态分流、调整并行度等方式处理。若是 Source 分区倾斜，也要从上游数据分布治理。 加盐会改变 key 粒度，最终结果需要二次聚合还原。 数据倾斜表现为同一个算子的某些 subtask 处理数据量、busy time、状态大小、checkpoint 大小明显高于其他 subtask。常见原因是 key 分布不均，少数热点用户、店铺、商品、城市带来大量数据。 处理方式包括：key 加盐，把热点 key 拆成多个子 key；两阶段聚合，先按加盐 key 局部聚合，再去盐做全局聚合；热点 key 单独识别后走独立分支；提高并行度；Source 层面治理分区倾斜；对维表 Join 使用缓存或异步 IO。 加盐会改变 key 的分组粒度，所以必须有二次聚合还原。如果业务要求同一个 key 内严格有序，加盐可能破坏顺序。对于窗口聚合，加盐后还要保证窗口边界一致。面试中不能只说“加盐”，要补一句“加盐后需要二阶段合并”。 可以把热点 key 识别出来单独处理，比如给热点商家单独一条链路或独立资源；对聚合类指标做局部预聚合；对无强一致要求的实时展示做分片统计后异步合并。超级热点往往是业务问题，需要业务规则和技术方案一起处理。

## 18. Flink 如何与 Kafka 集成？

- [ ] Kafka Source/Sink 如何保证一致性？
  答案：Flink 从 Kafka 读取时把 offset 纳入 Checkpoint，故障恢复后从快照 offset 继续消费；写 Kafka 时可使用事务 Sink，在 Checkpoint 成功后提交事务，实现端到端 Exactly Once。 Kafka 事务超时时间要大于 Checkpoint 间隔和最大恢复时间。 Flink 消费 Kafka 时，会把 Kafka offset 作为 Source 状态纳入 checkpoint。Checkpoint 成功后，这个 offset 才代表和 Flink 状态一致的消费进度。故障恢复时，Flink 从 checkpoint 中的 offset 重新消费，而不是简单依赖 Kafka 自动提交。 写 Kafka 时，如果使用支持事务的 Kafka Sink，Flink 会把每个 checkpoint 周期内的写入放入事务，checkpoint 成功后提交事务，失败则 abort。消费者设置 `read_committed` 后，只能看到提交事务的数据。 Flink 作业自身的一致性依赖 checkpoint 中保存的 offset，不依赖 Kafka consumer 自动提交。可以出于外部监控 lag 的需要提交 offset 到 Kafka，但恢复语义仍以 Flink checkpoint 为准。面试中要强调“offset 是 Flink 状态的一部分”。 Kafka 事务超时时间要大于 checkpoint 间隔、checkpoint 最大持续时间以及故障恢复可能耗时，否则事务可能在 Flink 提交前被 Kafka 超时 abort，影响 Exactly Once。生产中要结合 checkpoint 配置、作业恢复时间和 Kafka broker 的 `transaction.max.timeout.ms`。

## 19. Flink SQL 的动态表是什么？

- [ ] 为什么流可以当表查询？
  答案：Flink SQL 把流抽象成不断变化的动态表，输入流不断更新表，查询结果也会以 changelog 流输出，包含 insert/update/delete 语义。 Upsert Kafka、Retract Stream、Append Stream 都是动态表输出模式的体现。

## 20. Flink Join 有哪些常见类型？

- [ ] 流式 Join 要注意什么？
  答案：常见有 regular join、interval join、temporal join、lookup join、window join。流式 Join 往往需要维护状态，要注意状态膨胀、TTL、乱序和维表更新语义。 维表 Join 常用 lookup join + 缓存，但要考虑缓存过期和外部系统压力。 流式 Join 面对的是无界数据。如果 regular join 没有时间约束，Flink 需要长期保存两边流的历史数据，以便未来可能匹配，这会导致状态无限增长。Interval Join、Window Join、Temporal Join 等通过时间范围或版本语义限制状态保留范围，更适合生产。 维表 Join 常见做法是 lookup join，通过外部数据库或缓存查询维度信息；如果维表更新频繁，可以用 temporal table join 保留按时间版本的维表语义。 如果两边流的关联本身有明确时间范围，比如订单创建后 30 分钟内支付，用 interval join 更合适，因为状态可以按时间清理。如果是两个事实流长期任意匹配，regular join 状态成本很高，需要非常谨慎。能给 Join 加时间边界就尽量加。 Lookup Join 依赖外部系统，外部系统慢会造成反压；缓存过期策略会影响维度准确性；高 QPS 可能把数据库打挂；异步 lookup 要处理超时、重试和乱序返回。生产中通常要加缓存、限流、异步 IO 和降级策略。

## 21. Flink CEP 是什么？

- [ ] CEP 适合解决什么问题？
  答案：CEP 是复杂事件处理，用模式匹配识别事件序列，比如登录失败多次、支付超时、风控行为链路。Flink CEP 支持模式定义、时间约束、贪婪/非贪婪匹配等。 CEP 本质上会维护状态，模式复杂时要关注状态大小和超时清理。

## 22. Flink 性能调优有哪些方向？

- [ ] 一个 Flink 作业延迟高怎么优化？
  答案：先定位瓶颈算子，再从并行度、Slot、Operator Chain、序列化、状态后端、RocksDB 参数、Checkpoint 间隔、异步 IO、批量 Sink、数据倾斜、GC 和外部系统吞吐等方面优化。 调优要基于指标，不要盲目加并行度；下游慢时加上游并行度可能让堆积更严重。 调优先看指标，不先猜参数。第一步确认目标，是降低端到端延迟、提高吞吐、降低 checkpoint 时间，还是减少资源成本。第二步定位瓶颈，通过 Web UI、metrics、日志找到慢算子和慢 subtask。第三步分类处理：CPU 计算瓶颈就优化逻辑和提高并行度；状态瓶颈就优化 state 结构、TTL、RocksDB 参数和本地磁盘；Sink 瓶颈就批量/异步/限流；倾斜就做热点治理；checkpoint 慢就看状态大小、alignment、外部存储带宽。 常见调优点包括：合理设置并行度和 slot sharing；避免不必要的 shuffle；开启 operator chain；选择合适序列化；大状态使用 RocksDB；设置增量 checkpoint；控制 checkpoint 间隔和超时；使用异步 IO 访问外部系统；Sink 批量写；避免单条数据同步远程调用。 先看是状态太大、alignment time 太长，还是外部存储写入慢。状态太大就清理无用状态、设置 TTL、优化 key 粒度；alignment 太长通常说明反压或上游不均衡；外部存储慢就检查 HDFS/S3 带宽、文件数和网络。RocksDB 场景可以开启增量 checkpoint，但也要关注恢复时的文件管理成本。 低延迟要减少批量等待和外部阻塞。Watermark 不能过于保守，窗口大小和触发器要合理；Source/Sink 要降低批量等待；避免复杂同步 IO；控制状态访问成本；资源上保证 CPU 和网络不排队。低延迟和高吞吐经常冲突，需要结合 SLA 做取舍。
