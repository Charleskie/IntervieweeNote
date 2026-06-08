# Spark 面试题清单

> 复习重点：Spark 面试常围绕“架构、RDD/DataFrame、DAG、Stage、Shuffle、Spark SQL、Join 优化、数据倾斜、内存管理、Streaming、性能调优”展开。

## 1. Spark 的整体架构是什么？

- [ ] Spark 应用由哪些核心组件组成？
  答案：Spark 应用由 Driver、Executor、Cluster Manager、Job、Stage、Task 组成。Driver 负责编写和调度作业、生成 DAG；Cluster Manager 负责资源分配；Executor 在 Worker 节点上执行 Task 并缓存数据。 Driver 是应用大脑，Driver 挂掉通常整个应用失败。 一个 Spark Application 通常由 Driver、Executor 和 Cluster Manager 组成。Driver 运行用户的 main 方法，负责生成逻辑计划或 RDD lineage，创建 SparkContext/SparkSession，向集群申请资源，把作业拆成 Job、Stage、Task 并调度执行。Executor 运行在 Worker 节点上，负责执行 Task、缓存数据、读写 Shuffle 数据，并把执行状态汇报给 Driver。Cluster Manager 可以是 Standalone、YARN、Kubernetes、Mesos 等，负责资源分配。 回答时可以补一句：Driver 是控制面，Executor 是执行面，Cluster Manager 是资源面。Driver 挂掉通常整个应用失败；Executor 挂掉时，Spark 可以通过 lineage 或 shuffle 重算恢复丢失分区。 Client 模式下 Driver 运行在提交任务的客户端机器上，适合调试，但客户端断开或资源不足会影响作业。Cluster 模式下 Driver 运行在集群内部，由集群管理器托管，更适合生产。YARN/K8s 上生产一般优先 Cluster 模式。 不是。每个 Executor cores 太大，会让单个 Executor 同时跑太多 Task，GC 压力、内存竞争和失败重试成本变高；太小又会增加 Executor 数量和调度开销。常见做法是结合机器核数、内存、任务类型和集群规范选择，比如每个 Executor 3-5 cores 是很多场景的经验起点，但最终要压测。

## 2. RDD、DataFrame、Dataset 有什么区别？

- [ ] 实际开发更推荐使用哪个？
  答案：RDD 是底层弹性分布式数据集，类型安全但优化能力弱；DataFrame 是带 schema 的分布式表，底层可被 Catalyst 优化；Dataset 结合类型安全和 SQL 优化，但 Java/Python 中体验不同。生产中大多推荐 DataFrame/Spark SQL。 DataFrame 本质上是 Dataset[Row]。 RDD 是 Spark 最底层的弹性分布式数据集，提供 map、filter、reduce 等函数式算子，类型灵活，但 Spark 不理解字段语义，优化空间有限。DataFrame 是带 schema 的分布式表，本质是 Dataset[Row]，Spark 可以基于 schema 做 Catalyst 优化。Dataset 在 Scala/Java 中提供类型安全，同时保留一定 SQL 优化能力，但 Python 没有强类型 Dataset 体验。 生产中优先使用 Spark SQL/DataFrame，因为能获得谓词下推、列裁剪、Join 重排、常量折叠、AQE、WholeStageCodegen 等优化。RDD 适合底层自定义逻辑、非结构化处理或需要精细控制分区和依赖的场景。 大多数结构化处理场景 DataFrame 更快，因为优化器能理解 schema 和表达式。但如果 DataFrame 中大量使用黑盒 UDF，或者逻辑非常底层，优化器看不懂，优势会下降。性能不是 API 名字决定的，而是执行计划、数据量、Shuffle、Join 和代码生成决定的。 Dataset 的类型安全主要在 Scala/Java 中体现，但编码器、对象序列化和类型转换有额外成本。很多数仓和 ETL 场景更关注 SQL 表达能力和优化器，DataFrame/Spark SQL 更通用。

## 3. Spark 的懒执行是什么意思？

- [ ] Transformation 和 Action 有什么区别？
  答案：Transformation 只记录计算逻辑，不立即执行，如 map/filter/join；Action 触发真正执行，如 count/collect/save。Spark 会在 Action 时生成 DAG 并调度任务。 懒执行让 Spark 有机会做 DAG 优化和窄依赖流水线合并。

## 4. Spark 的 Job、Stage、Task 是什么？

- [ ] 一个 Action 会产生几个 Job？
  答案：一个 Action 通常触发一个 Job；Job 会按 Shuffle 边界切分成多个 Stage；Stage 内每个 Partition 对应一个 Task。Task 是最小执行单元。 多个 Action 会触发多次 Job，若中间结果复用应 cache/persist。 Spark 的 Transformation 是懒执行，只记录计算逻辑，不马上执行。Action 才会触发 Job，比如 `count`、`collect`、`save`。Spark 根据依赖关系生成 DAG，遇到宽依赖也就是 Shuffle 边界时切分 Stage。Stage 内部是可以流水线执行的一组窄依赖算子，每个分区对应一个 Task，Task 是实际运行的最小单位。 可以这样回答：一个 Action 通常触发一个 Job，一个 Job 按 Shuffle 切成多个 Stage，一个 Stage 按分区数生成多个 Task。 每个 Action 都可能触发一个 Job。如果代码中对同一个中间 DataFrame 连续 `count`、`show`、`write`，可能重复计算多次。对于复用成本高的中间结果，可以 cache/persist，避免重复执行上游逻辑。 主要由 Shuffle 边界决定。窄依赖可以合并在同一个 Stage 中流水线执行；宽依赖需要等待上游 map 输出完成，再进行 shuffle read，因此会切 Stage。SQL 中的 Join、聚合、去重、窗口等都可能产生 Shuffle。

## 5. 窄依赖和宽依赖是什么？

- [ ] 为什么宽依赖会产生 Shuffle？
  答案：窄依赖是父 RDD 的一个分区只被少量子分区依赖，可流水线执行；宽依赖是父分区数据会被多个子分区依赖，需要跨节点重分区，产生 Shuffle。 常见宽依赖有 groupByKey、reduceByKey、join、distinct、repartition。

## 6. Shuffle 的过程是什么？

- [ ] Shuffle 为什么是 Spark 性能瓶颈？
  答案：Shuffle 会把 map 端输出按 key 分区、落盘、传输到 reduce 端，再聚合或排序。它涉及磁盘 IO、网络 IO、序列化、内存缓冲和大量小文件/连接，因此成本高。 优化方向是减少 Shuffle、减少数据量、提升并行度、治理倾斜。 Shuffle 会让数据跨节点重分布，过程包括 map 端按分区器写 shuffle 文件，可能排序、聚合、落盘；reduce 端从多个 Executor 拉取属于自己的数据，再进行聚合、排序或 Join。它涉及网络 IO、磁盘 IO、序列化、内存缓冲、文件管理和大量连接，是 Spark 性能瓶颈之一。 优化思路是：减少 Shuffle 次数，减少 Shuffle 数据量，提升 Shuffle 并行度，治理 Shuffle 倾斜。代码上能用 `reduceByKey` 不用 `groupByKey`，先 filter 再 join，先聚合再 join，合理选择 join 策略。参数上调 `spark.sql.shuffle.partitions` 或启用 AQE 自动合并分区。数据上处理热点 key 和小文件。 `groupByKey` 会把同一个 key 的所有 value 拉到 reduce 端，没有 map 端预聚合，网络数据量和 reduce 端内存压力都很大。`reduceByKey`、`aggregateByKey` 可以先在 map 端局部聚合，减少传输数据量。SQL 里类似地，要尽量让聚合尽早发生。 Shuffle、Sort、Aggregation 需要内存缓冲。如果 Execution Memory 不足，Spark 会把中间数据 spill 到磁盘。Spill 能保证任务继续执行，但磁盘 IO 会让性能显著下降。Spark UI 中如果看到大量 memory spill/disk spill，就要检查分区大小、聚合方式、倾斜和内存配置。

## 7. `reduceByKey` 和 `groupByKey` 的区别是什么？

- [ ] 为什么更推荐 `reduceByKey`？
  答案：`reduceByKey` 会在 map 端先做局部聚合，减少 Shuffle 数据量；`groupByKey` 会把同 key 的所有值都拉到 reduce 端，网络和内存压力大。 能聚合就不要直接 groupByKey。

## 8. Spark SQL 的 Catalyst 优化器是什么？

- [ ] Spark SQL 为什么通常比手写 RDD 更快？
  答案：Catalyst 会把 SQL/DataFrame 转成逻辑计划，再做谓词下推、列裁剪、常量折叠、Join 重排等优化，最后生成物理计划。RDD 缺少结构化 schema，优化空间小。 Spark SQL 还有 Tungsten、WholeStageCodegen 等执行优化。 Catalyst 是 Spark SQL 优化器。SQL 或 DataFrame 先生成 unresolved logical plan，经过 analyzer 绑定表、字段和函数，形成 analyzed logical plan；optimizer 做谓词下推、列裁剪、常量折叠、表达式简化、Join 重排等规则优化；planner 生成多个物理计划，基于 cost 或规则选择最终计划。 Tungsten 更偏执行层优化，包括二进制内存格式、堆外内存管理、减少 Java 对象开销、提高 CPU cache 友好性。WholeStageCodegen 会把多个算子融合生成 Java 代码，减少虚函数调用和对象创建。 内置函数对 Catalyst 是透明的，优化器能理解表达式语义，可以做列裁剪、谓词下推、常量折叠和代码生成。普通 UDF 是黑盒，优化器不知道里面做了什么，可能阻断优化。Python UDF 还涉及 JVM 和 Python 进程间序列化，成本更高。 可以用 `explain` 或 `explain("formatted")` 查看逻辑计划和物理计划，重点看是否有 BroadcastHashJoin、SortMergeJoin、Exchange、Filter 下推、FileScan 读了哪些列、是否发生 AdaptiveSparkPlan。`Exchange` 通常代表 Shuffle 边界，是优化重点。

## 9. Tungsten 和 WholeStageCodegen 是什么？

- [ ] 它们解决什么问题？
  答案：Tungsten 优化内存管理、二进制数据格式和 CPU cache 友好性；WholeStageCodegen 把多个算子生成 Java 代码合并执行，减少虚函数调用和对象创建。 复杂 UDF 可能打断代码生成，影响性能。

## 10. Spark Join 有哪些类型？

- [ ] Broadcast Hash Join、Sort Merge Join、Shuffle Hash Join 有什么区别？
  答案：Broadcast Hash Join 把小表广播到各 Executor，避免大表 Shuffle；Sort Merge Join 双方按 key Shuffle 并排序后合并，适合大表 Join；Shuffle Hash Join 也会 Shuffle，但用哈希表匹配，适合一侧相对小且内存足够。 Spark 默认大表 Join 常用 Sort Merge Join，小表可通过 broadcast 优化。 Broadcast Hash Join 会把小表广播到每个 Executor，大表不需要 Shuffle，适合一张表很小且能放进内存的场景。Sort Merge Join 会让两边按 Join key Shuffle 并排序，再归并匹配，适合大表 Join，是 Spark 大表 Join 常见策略。Shuffle Hash Join 会两边 Shuffle 后在 reduce 端构建哈希表，适合一侧相对较小且内存足够的场景。Broadcast Nested Loop Join 常出现在非等值 Join 或缺少合适条件时，通常成本较高。 由 `spark.sql.autoBroadcastJoinThreshold` 控制，默认值随版本可能不同，常见是 10MB 级别。实际能否广播还取决于统计信息是否准确、Driver 收集广播表是否成功、Executor 内存是否足够。可以用 `broadcast(df)` hint 强制广播，但强制前要确认小表真实大小。 可能表统计信息缺失或不准，Spark 估算它不小；可能超过广播阈值；可能 join 类型不支持广播；可能禁用了自动广播；也可能 AQE 运行时才调整。解决方式是分析表统计、查看执行计划、设置阈值或使用 hint。

## 11. 广播变量和 Broadcast Join 有什么区别？

- [ ] 它们分别适合什么场景？
  答案：广播变量是 Spark Core 中把只读小对象分发到 Executor，减少任务重复传输；Broadcast Join 是 Spark SQL 物理 Join 策略，把小表广播到各节点做本地 Join。 广播对象不能太大，否则会造成 Driver/Executor 内存压力。

## 12. 数据倾斜是什么？如何判断？

- [ ] Spark 任务某些 Task 特别慢是什么原因？
  答案：数据倾斜是某些 key 或分区数据量远大于其他分区，导致少数 Task 执行很慢。可通过 Spark UI 看 Stage 中 Task 的数据量、运行时间、Shuffle read/write 是否明显不均衡。 倾斜常出现在 groupBy、join、distinct、窗口聚合等场景。

## 13. 数据倾斜如何解决？

- [ ] Join 倾斜和聚合倾斜分别怎么处理？
  答案：聚合倾斜可用两阶段聚合、key 加盐、提高并行度；Join 倾斜可用广播小表、倾斜 key 单独处理、大表加盐小表扩容、AQE skew join。还可以提前过滤无效数据。 加盐会改变 key，需要二次聚合或扩展小表匹配。 数据倾斜是少数 key 或分区的数据量远大于其他分区，导致少数 Task 运行特别慢，整个 Stage 被拖住。诊断时先看 Spark UI：同一个 Stage 中 Task duration 是否长尾明显，Shuffle Read/Write 是否少数 Task 特别大，spill 是否集中在少数 Task。再通过数据分布 SQL 看热点 key，比如 `group by key order by count desc`。 治理方式要按场景选。聚合倾斜可以两阶段聚合、key 加盐、提高并行度。Join 倾斜可以广播小表；热点 key 单独拆出来处理；大表加盐、小表扩容复制后 Join；开启 AQE skew join；提前过滤无效数据；调整业务分区字段。 假设大表某个 key 太热，可以给大表的 key 拼接随机盐，比如 `key_0` 到 `key_9`，把热点拆到多个 reduce 分区。小表对应 key 需要复制 10 份，分别拼接同样的盐，保证还能匹配。Join 后如果需要恢复原 key，再去掉盐。这个方案能缓解热点 Join，但会放大小表数据量。 不能完全。AQE 可以在运行时发现倾斜分区并拆分，自动优化部分 Sort Merge Join 的倾斜问题，但它依赖 Spark 版本、配置、运行时统计和查询形态。严重业务热点仍然需要数据治理、加盐、单独处理等手段。

## 14. Cache 和 Persist 有什么区别？

- [ ] 什么时候应该缓存？
  答案：`cache()` 是 `persist(MEMORY_ONLY)` 的简写；`persist` 可选择内存、磁盘、序列化等存储级别。多次复用、计算代价高、迭代算法适合缓存。 缓存不是越多越好，会占用内存，缓存后要适时 unpersist。

## 15. Checkpoint 和 Cache 的区别是什么？

- [ ] 为什么长血缘需要 Checkpoint？
  答案：Cache 是缓存中间结果，血缘仍保留；Checkpoint 会把数据可靠写入外部存储并截断血缘，适合迭代计算、长 lineage、容错恢复。 Checkpoint 通常要设置可靠存储路径，如 HDFS。 Cache 是 `persist(MEMORY_ONLY)` 的简写，适合中间结果会被多次复用且能放入内存的场景。Persist 可以选择不同存储级别，比如内存、磁盘、序列化、副本。Checkpoint 会把数据写到可靠存储并截断 lineage，适合迭代计算、血缘很长、恢复成本高的场景。 缓存不是越多越好。缓存会占用 Storage Memory，挤压 Execution Memory，可能导致 Shuffle spill 或驱逐。缓存后最好触发一次 Action 物化，使用完及时 `unpersist`。 Checkpoint 会触发计算并把结果写到外部存储。如果不 cache，后续 action 可能导致上游逻辑重复计算。常见做法是对要 checkpoint 的 RDD/DataFrame 先 persist，再 checkpoint，再触发 action，减少重复计算成本。 缓存的是当时计算得到的结果，不会自动跟随源表变化。后续读取可能命中缓存，得到旧数据。需要根据场景 `unpersist`、刷新表缓存或重新计算。

## 16. Spark 的容错机制是什么？

- [ ] RDD 丢失分区后如何恢复？
  答案：RDD 通过 lineage 记录依赖关系。某个分区丢失时，Spark 可以根据血缘重新计算该分区。宽依赖 Shuffle 输出丢失时，可能需要重算上游 map 任务。 血缘太长会导致恢复成本高，所以有 Checkpoint。

## 17. Spark 内存模型是什么？

- [ ] Execution Memory 和 Storage Memory 分别做什么？
  答案：Spark 统一内存管理中，Execution Memory 用于 Shuffle、Join、Sort、Aggregation 等计算；Storage Memory 用于缓存 RDD/DataFrame 和广播变量。两者可在一定范围内动态借用。 内存不足可能导致 spill 到磁盘，显著降低性能。

## 18. Spark 常见 OOM 原因有哪些？

- [ ] Executor OOM 怎么排查？
  答案：常见原因包括数据倾斜、大量 collect 到 Driver、广播变量过大、缓存过多、单分区数据过大、Shuffle 聚合内存不足、UDF 创建大量对象。排查 Spark UI、Executor 日志、GC 日志、Task 数据量。 Driver OOM 和 Executor OOM 原因不同，`collect` 常导致 Driver OOM。 Driver OOM 常见原因是 `collect`、`toPandas`、`show` 太大、广播表在 Driver 端构建过大、维护过多任务元数据、动态分区/小文件过多导致 driver 端压力大。Executor OOM 常见原因是单分区数据太大、数据倾斜、Shuffle 聚合内存不足、广播变量过大、缓存太多、UDF 创建大量对象、外部库内存泄漏。 排查时先看报错发生在哪个进程：Driver 日志还是 Executor 日志；看 Spark UI 中失败 Task 的 input size、shuffle read、spill、GC time；看是否只有少数 Task 失败，如果是通常是倾斜或单分区过大；如果大量 Executor 同时 OOM，可能是资源配置或整体数据量问题。 `collect` 会把所有分区数据拉到 Driver 内存。分布式数据一旦集中到单进程，Driver 很容易 OOM。生产中只应对小结果使用 collect，大结果应写到分布式存储，或者使用 `take`、`limit`、采样等方式。 Executor 除了 JVM 堆，还需要堆外内存、Python worker 内存、Netty、JVM 元空间、线程栈等，这部分由 memory overhead 覆盖。在 YARN/K8s 中，如果 overhead 太小，容器可能被直接 kill，即使 Java 堆没满。PySpark、RocksDB、Arrow、复杂 Shuffle 场景尤其要关注 overhead。

## 19. `coalesce` 和 `repartition` 的区别是什么？

- [ ] 什么时候用哪个？
  答案：`repartition` 会触发 Shuffle，可增加或减少分区，数据更均匀；`coalesce` 默认不 Shuffle，适合减少分区但可能导致不均衡。 写文件前常用 coalesce/repartition 控制小文件数量。

## 20. Spark 小文件问题怎么解决？

- [ ] 小文件为什么影响性能？
  答案：小文件会增加元数据压力、任务调度开销和读取开销。解决方式包括写前合并分区、控制输出文件数、使用 compact 作业、表格式合并文件能力、合理设置分区字段。 不能盲目合成超大文件，通常结合 HDFS block 或下游读取并行度。 小文件会增加 NameNode/元数据服务压力，增加文件打开成本，降低读取吞吐，并导致 Spark 生成大量小 Task，调度开销上升。在湖仓表里，小文件还会拖慢元数据规划和查询。 解决方式包括：写出前控制分区数，使用 `coalesce` 或 `repartition`；合理设置 `spark.sql.files.maxRecordsPerFile`；避免按高基数字段分区；定期 compact；使用 Delta/Hudi/Iceberg 的文件合并能力；流式写入时设置合理 trigger 和批大小；上游 Kafka 小批次写入要避免过碎。 通常不好。一个文件会让写入变成单 Task，吞吐低且容易 OOM；下游读取也缺少并行度。文件大小应该结合 HDFS block、对象存储、查询引擎和表格式建议，一般追求适中大文件，而不是单文件。 如果按用户 ID、订单 ID 这类高基数字段分区，每个分区目录数据很少，就会产生海量小文件和目录。分区字段应选择查询过滤常用、基数适中、数据量相对均衡的字段，比如日期、小时、地区等。

## 21. AQE 是什么？

- [ ] Spark AQE 能优化哪些问题？
  答案：AQE 是自适应查询执行，会在运行时根据统计信息调整执行计划。常见能力包括动态合并 Shuffle 分区、自动切换 Join 策略、优化倾斜 Join。 AQE 依赖运行时统计，通常用于 Spark SQL/DataFrame。 AQE 是 Adaptive Query Execution，自适应查询执行。Spark 在运行时拿到 Shuffle 统计信息后，可以动态调整物理计划。常见优化包括：合并过小的 Shuffle 分区，减少小 Task；把 Sort Merge Join 动态改成 Broadcast Join；识别并拆分倾斜分区，优化 skew join。 AQE 让 Spark SQL 从“编译期估算”变成“运行时根据真实数据调整”，对统计信息不准、数据倾斜和分区设置不合理的场景很有帮助。 仍然需要合理设置初始值。AQE 可以合并过小分区，但初始分区太少会导致单分区过大，后面不一定能完全补救。常见思路是初始 shuffle 分区设得略高，让 AQE 在运行时合并小分区。 可能未启用配置；查询不是 Spark SQL/DataFrame 形态；物理计划不满足优化条件；统计信息不足；Join 类型不支持；数据规模未达到倾斜阈值；或者使用了某些 UDF/自定义逻辑使优化器无法判断。要通过 `explain` 和 Spark UI 确认最终计划。

## 22. Spark Streaming 和 Structured Streaming 的区别是什么？

- [ ] 现在更推荐哪种？
  答案：Spark Streaming 基于 DStream 微批，API 较老；Structured Streaming 基于 DataFrame/Dataset 和 Catalyst，提供事件时间、窗口、状态和端到端语义，更推荐使用。 Structured Streaming 默认也是微批，也支持部分连续处理能力但使用范围有限。

## 23. Structured Streaming 如何保证 Exactly Once？

- [ ] 端到端 Exactly Once 需要什么条件？
  答案：Structured Streaming 通过 checkpoint 保存 offset 和状态，Source 需要可重放，Sink 需要幂等或事务能力。文件 Sink、Delta/Hudi/Iceberg 等表格式更容易实现一致写入。 写普通外部系统时，通常还要业务幂等。 Structured Streaming 通过 checkpoint 保存 source offset、状态和 commit log。故障恢复时，从 checkpoint 中记录的 offset 和状态继续执行。Source 需要可重放，比如 Kafka；Sink 需要支持幂等或事务。文件 Sink、Delta/Hudi/Iceberg 这类表格式通常更容易提供一致提交；写普通 JDBC、Redis、HTTP 接口时，需要业务幂等。 微批模式下，每个 batch 有 batchId。使用 `foreachBatch` 写外部系统时，可以用 batchId 加业务主键做幂等，避免失败重试导致重复写。 不能随便删除。Checkpoint 保存了流作业的 offset 和状态，删除后作业可能从最新或最早 offset 重新开始，导致丢数据或重复消费。只有在明确要重跑、改逻辑不兼容、或者有完整补偿方案时才处理 checkpoint。 Append 模式只输出新增结果，适合不会更新的结果；Update 模式输出更新过的行，适合状态聚合的中间更新；Complete 模式每次输出完整结果表，适合小规模聚合结果。选择模式要看查询是否有聚合、是否允许更新、Sink 是否支持更新语义。

## 24. Spark SQL 中 UDF 有什么问题？

- [ ] 为什么 UDF 可能影响性能？
  答案：普通 UDF 对优化器不透明，可能阻止谓词下推、列裁剪和代码生成；Python UDF 还涉及 JVM/Python 进程序列化通信，开销更大。 能用内置函数就用内置函数，必要时考虑 Pandas UDF 或 Scala/Java UDF。

## 25. Spark 作业性能调优步骤是什么？

- [ ] 一个 Spark 作业很慢，你怎么优化？
  答案：先看 Spark UI 定位慢 Stage 和慢 Task，再分析是否 Shuffle 大、倾斜、分区不合理、Join 策略不佳、缓存缺失、GC/OOM、输入小文件或外部系统慢。然后针对性做分区调整、广播 Join、预聚合、过滤下推、缓存、AQE、资源参数优化。 调优顺序是先定位瓶颈，再改 SQL/代码，再调资源参数。 第一步看 Spark UI，定位慢 Job、慢 Stage、慢 Task。重点看 Task 时间分布、Shuffle Read/Write、Input Size、Spill、GC Time、Executor CPU 和失败重试。第二步判断瓶颈类型：是 IO 慢、Shuffle 大、数据倾斜、Join 策略错误、分区不合理、缓存缺失、UDF 慢、GC/OOM，还是外部系统慢。第三步针对性优化代码和 SQL：过滤下推、列裁剪、减少 Shuffle、广播小表、预聚合、处理倾斜、替换 UDF、合理缓存。第四步再调资源参数：Executor 数、cores、memory、shuffle partitions、broadcast threshold、AQE。第五步压测验证和灰度上线。 加资源只能解决部分容量问题，不能解决错误的 Join、严重倾斜、全量 collect、UDF 黑盒、下游慢等问题。先定位瓶颈可以避免盲目扩大成本。比如一个 Stage 只有一个 Task 慢，加再多 Executor 也没用，应该查倾斜或单分区过大。 Jobs 页面看整体耗时；Stages 页面看慢 Stage、Task 分布、Shuffle 和 Spill；SQL 页面看 SQL 执行计划和算子耗时；Executors 页面看 GC、内存、失败、输入输出；Storage 页面看缓存是否生效。面试中能说出这些页面，通常会显得更有实操经验。

## 26. Spark 常见参数有哪些？

- [ ] 生产中常调哪些参数？
  答案：常见参数有 `spark.executor.instances`、`spark.executor.cores`、`spark.executor.memory`、`spark.driver.memory`、`spark.sql.shuffle.partitions`、`spark.default.parallelism`、`spark.sql.autoBroadcastJoinThreshold`、`spark.sql.adaptive.enabled`。 参数要结合集群资源和作业特征，不能只靠增大内存解决所有问题。

## 27. Spark 和 Flink 的区别是什么？

- [ ] 流处理场景怎么选？
  答案：Spark 强在批处理、SQL 生态、湖仓分析和统一离线计算；Flink 强在低延迟有状态流处理、事件时间、复杂窗口和 Exactly Once 流式语义。实时低延迟优先 Flink，离线/交互分析和湖仓 ETL 常用 Spark。 技术选型还要看团队经验、生态、稳定性和已有平台。 Spark 的优势在批处理、Spark SQL、离线 ETL、湖仓生态、机器学习和统一分析。它的生态成熟，和 Hive、Iceberg、Delta、Hudi、YARN/K8s 等集成广。Flink 的优势在低延迟流处理、事件时间、Watermark、有状态计算、复杂窗口、CEP 和端到端 Exactly Once 流式语义。 如果是 T+1 离线数仓、小时级 ETL、交互式分析、大规模 SQL，Spark 通常更合适。如果是毫秒到秒级实时风控、实时指标、复杂乱序事件处理、长状态流计算，Flink 更合适。很多公司是 Spark 做离线和湖仓，Flink 做实时，二者共存。 部分场景可以，比如 Kafka 到湖仓的准实时 ETL、秒级/分钟级指标、简单状态聚合。但如果业务要求很低延迟、复杂 Watermark、CEP、大状态精细控制、复杂流式 Join，Flink 通常更自然。选型要看 SLA、状态复杂度、团队经验和现有平台。 Spark SQL 生态成熟，批处理吞吐强，和 Hive Metastore、Iceberg、Delta、Hudi、Parquet、ORC 等生态集成好。湖仓中大量任务是批量扫描、Join、聚合、Compaction 和历史回溯，Spark 的执行模型和优化器非常适合这些工作负载。
