# Kafka 面试题清单

> 复习重点：Kafka 面试通常围绕“高吞吐、分区有序、消费组、可靠性、Exactly Once、Rebalance、性能调优、线上排查”展开。回答时最好结合业务场景讲取舍。

## 1. Kafka 的整体架构是什么？

- [ ] Kafka 由哪些核心组件组成？
  答案：Kafka 的核心组件包括 Producer、Broker、Topic、Partition、Replica、Consumer、Consumer Group、Controller。Producer 负责写消息，Broker 负责存储和服务请求，Topic 是逻辑主题，Partition 是物理并行和顺序单位，Replica 提供副本容灾，Consumer Group 用于分摊消费，Controller 负责分区 leader 选举和集群元数据管理。 Kafka 的有序性只保证单个 Partition 内有序，不保证整个 Topic 全局有序。

## 2. Topic 和 Partition 的关系是什么？

- [ ] 为什么 Kafka 要把 Topic 拆成多个 Partition？
  答案：Topic 是逻辑分类，Partition 是实际存储和并行处理单位。拆分 Partition 可以提升吞吐，因为多个分区可以分布在不同 Broker 上并行读写；也可以提升消费并行度，因为一个消费组内不同消费者可以消费不同分区。 Partition 数不是越多越好，太多会增加文件句柄、元数据、Controller 管理、Rebalance 和恢复成本。

## 3. Kafka 如何保证消息顺序？

- [ ] Kafka 能保证全局有序吗？
  答案：Kafka 只能保证同一个 Partition 内消息按追加顺序有序。要保证某个业务维度有序，需要把同一业务 key 的消息发送到同一个 Partition，比如订单维度用 orderId 作为 key。Topic 级别多个 Partition 之间没有全局顺序。 如果要强全局有序，只能使用一个 Partition，但会牺牲吞吐和扩展性。 Kafka 只保证单 Partition 内有序，不保证 Topic 全局有序。要保证某个业务维度有序，需要用稳定 key，例如 orderId、userId，让同一个 key 的消息进入同一个 Partition；消费者侧也要避免对同一 key 并发乱序处理。 可能破坏顺序的场景包括：增加 Partition 后 key 映射变化；Producer 使用不稳定分区策略；消费者把同一 Partition 的消息丢到多线程里并发处理但没有按 key 排队；失败消息跳过后补偿处理；重试队列和主队列并行消费。 通常按订单号分区，保证同一个订单内有序，不追求全局有序。吞吐通过多个订单分散到多个 Partition 实现。如果某些订单是超级热点，可以考虑业务拆分、热点单独队列或在业务层降低单 key 串行压力，但不能简单把同一个订单拆到多个 Partition，否则顺序语义会变复杂。 一种做法是单线程按 Partition 消费和处理，最简单但吞吐有限。另一种是按 key 分发到固定工作队列，同一个 key 永远进入同一个队列，队列内串行处理，不同 key 并行处理。提交 offset 时要注意不能因为后面的消息先处理完就提前提交，通常需要维护每个 Partition 已连续完成的最大 offset。

## 4. Producer 发送消息的流程是什么？

- [ ] 一条消息从 Producer 到 Broker 经历哪些步骤？
  答案：Producer 先序列化消息，根据 key 或分区策略选择 Partition，写入本地 RecordAccumulator 缓冲区，由 Sender 线程按 batch 发送到对应 Partition leader。Broker leader 写入本地日志，根据 ack 配置等待副本同步后返回响应。 Producer 的高吞吐来自批量发送、压缩、顺序写、异步 IO 和缓冲聚合。

## 5. Producer 的 `acks` 参数怎么理解？

- [ ] `acks=0/1/all` 有什么区别？
  答案：`acks=0` 表示发送后不等待 Broker 响应，吞吐高但可能丢消息；`acks=1` 表示 leader 写入成功即返回，leader 宕机且 follower 未同步时可能丢；`acks=all` 表示 ISR 中副本都确认后返回，可靠性最高，通常配合 `min.insync.replicas` 使用。 高可靠配置常见组合是 `acks=all`、`retries>0`、`enable.idempotence=true`、`min.insync.replicas>=2`、副本数至少 3。

## 6. ISR、LEO、HW 分别是什么？

- [ ] Kafka 副本同步里 ISR、LEO、HW 的含义是什么？
  答案：ISR 是与 leader 保持同步的副本集合；LEO 是某个副本日志末尾的下一条 offset；HW 是高水位线，表示消费者可见的最大已提交位置。只有 HW 之前的消息才认为对消费者可见。 Follower 落后超过阈值会被踢出 ISR，恢复同步后可重新加入。 ISR 是 In-Sync Replicas，表示与 leader 保持同步的副本集合；LEO 是 Log End Offset，表示某个副本下一条待写入消息的位置；HW 是 High Watermark，高水位线，表示已经被足够副本确认、对消费者可见的位置。Kafka 消费者只能读取 HW 之前的数据，这是为了避免 leader 宕机后未同步的数据被消费者读到又回滚。 可以用一句话串起来：Producer 写 leader，Follower 从 leader 拉取同步，各副本维护自己的 LEO，leader 根据 ISR 中副本同步进度推进 HW，Consumer 只能看到 HW 之前的数据。 Follower 如果长时间没有跟上 leader，会被移出 ISR。被移出 ISR 后，它不再参与 `acks=all` 的确认。等它追上 leader 后，可以重新加入 ISR。如果 ISR 缩小到低于 `min.insync.replicas`，高可靠 Producer 写入会失败，这是为了防止只写一个副本导致风险过高。 正常副本选举语义下，HW 之前的数据已经被 ISR 中副本确认，相对安全。但分布式系统没有绝对安全，仍依赖副本配置、选举策略、磁盘可靠性和运维操作。面试中可以说“在 Kafka 正常副本协议和合理配置下，HW 之前的数据被认为已提交”。

## 7. Kafka 如何保证消息不丢失？

- [ ] 生产端、Broker 端、消费端分别怎么保证不丢消息？
  答案：生产端使用 `acks=all`、重试、幂等生产者和回调检查；Broker 端配置多副本、`min.insync.replicas`、合理刷盘和监控 ISR；消费端处理成功后再提交 offset，避免先提交后处理失败。 没有任何分布式系统能在所有故障下“绝对不丢”，面试中要讲清配置和代价。 要从 Producer、Broker、Consumer 三端分别回答。 Producer 端：设置 `acks=all`，让 leader 等待 ISR 中足够副本确认；开启 `retries` 和合理的 `delivery.timeout.ms`，让临时网络抖动可重试；开启 `enable.idempotence=true`，避免重试导致重复写入；发送后检查 callback，不能忽略异常。 Broker 端：Topic 副本数通常至少 3；设置 `min.insync.replicas>=2`，避免只剩 leader 时仍然接受写入；关闭或谨慎使用不可靠 leader 选举，避免落后副本成为 leader 导致数据回退；监控 ISR 收缩、磁盘、网络和 Controller 状态。 Consumer 端：业务处理成功后再提交 offset；失败时重试、进入重试队列或死信队列；不要先提交 offset 再处理业务，否则处理失败后 Kafka 会认为消息已经消费，造成业务意义上的丢失。 不是绝对不丢。`acks=all` 表示 ISR 中满足条件的副本确认后才返回成功，但如果副本数配置太低、`min.insync.replicas=1`，或者发生极端故障，仍可能丢。可靠性要看组合配置：`replication.factor=3`、`min.insync.replicas=2`、Producer `acks=all`、幂等和重试、消费者处理成功后提交 offset。还要接受一个现实：可靠性越高，延迟和可用性成本越高。例如 ISR 不足时继续要求 `acks=all`，Producer 会写入失败，这是为了避免不安全写入。 分布式消息系统里，“不丢”和“不重”很难同时无条件保证。为了不丢，Producer 和 Consumer 往往会重试；一旦重试，就可能重复。Kafka 更容易提供 at-least-once，即至少处理一次。要做到业务上的 exactly-once，通常需要 Kafka 幂等生产、事务、Consumer offset 与结果提交一致，以及业务侧幂等约束一起配合。面试时要强调“端到端语义”，不要只说 Kafka 一个组件。

## 8. Kafka 为什么可能重复消费？如何解决？

- [ ] 重复消费的来源有哪些？
  答案：消费者处理完消息但提交 offset 前宕机，重启后会再次消费；生产者重试也可能导致重复写入，除非开启幂等。解决方式是业务幂等，比如唯一键、去重表、状态机、Redis 去重、幂等更新。 消息队列通常更容易做到 at-least-once，exactly-once 需要生产、存储、消费和业务侧共同配合。 重复消费主要有三类来源。第一，Consumer 处理完业务但还没提交 offset 就宕机，重启后会从旧 offset 再消费一次。第二，Consumer 提交 offset 成功与否不确定，比如网络超时，应用可能重试提交或重启后重复处理。第三，Producer 发送请求超时后重试，Broker 其实已经写入成功，未开启幂等时可能写入两条相同业务消息。 解决重复消费不能只依赖 MQ，要做业务幂等。常见方式有：数据库唯一键，比如订单号加事件类型作为唯一索引；状态机幂等，比如订单已支付后再次收到支付成功事件直接忽略；去重表，按 messageId 记录处理结果；Redis `SETNX` 做短期去重；下游接口提供幂等 token。 自动提交的风险是 offset 提交和业务处理不是一个原子操作。如果自动提交已经发生，但业务还没处理完或处理失败，重启后会跳过这些消息，造成丢处理。自动提交适合日志消费、监控统计这类允许少量误差的场景；订单、支付、库存这类场景建议手动提交，并且在业务成功后提交。 也会重复。手动提交只是把“丢处理”的风险降下来，但只要业务成功和 offset 提交之间发生故障，就会重复消费。所以手动提交通常实现的是 at-least-once，业务幂等仍然必要。

## 9. Kafka 幂等生产者是什么？

- [ ] `enable.idempotence=true` 能解决什么问题？
  答案：幂等生产者通过 ProducerId、Partition、Sequence Number 去重，保证单个 Producer 会话内、单个 Partition 上的重试不会产生重复消息。 幂等生产者主要解决生产端重试重复，不等于业务端消费幂等，也不等于跨会话、跨系统的全链路 exactly-once。

## 10. Kafka 事务消息是什么？

- [ ] Kafka 的事务能保证什么？
  答案：Kafka 事务允许 Producer 原子地写入多个 Partition，并可把消费 offset 和生产消息放进同一个事务，实现“消费-处理-生产”的 exactly-once 流式处理语义。 消费者需要设置 `isolation.level=read_committed` 才只读取已提交事务消息。 Kafka 的 Exactly Once 不是说任何业务系统接入 Kafka 后天然都只处理一次，而是 Kafka 在特定范围内提供精确一次语义。幂等生产者解决单 Producer 会话内、单 Partition 上重试重复写的问题；事务能力可以保证多个 Partition 的写入原子性，并且可以把消费 offset 和生产结果放到同一个事务里，适合“消费 Kafka A，处理后写 Kafka B”的流式链路。 要真正端到端 Exactly Once，需要满足几个条件：Source 可重放，Kafka 本身满足；计算状态能一致恢复，例如 Flink/Spark checkpoint；Sink 支持事务或幂等；业务外部副作用可控。如果 Sink 是普通 MySQL 更新或调用外部 HTTP 接口，就需要唯一键、幂等接口或事务外盒等方案。 幂等生产者解决的是“同一个 Producer 对同一个 Partition 重试发送导致重复”的问题，粒度较小。事务解决的是“一组写入要么都成功，要么都失败”的原子性问题，可以跨 Partition，也可以提交消费 offset。事务能力更强，但也会带来额外状态管理和性能成本。 消费者需要设置 `isolation.level=read_committed`，这样只读取已提交事务的数据。如果使用默认的 `read_uncommitted`，可能读到未提交事务消息，后续事务 abort 时就会出现语义问题。这个追问经常用来区分候选人是否真正理解 Kafka 事务。

## 11. Consumer Group 的工作机制是什么？

- [ ] 一个消费组内多个消费者如何分配 Partition？
  答案：同一消费组内，每个 Partition 同一时刻只能分配给一个消费者消费；一个消费者可消费多个 Partition。多个消费者共同分摊 Topic 的 Partition，实现水平扩展。 如果消费者数量大于 Partition 数，多出来的消费者会空闲。

## 12. Rebalance 是什么？为什么会发生？

- [ ] Rebalance 对业务有什么影响？
  答案：Rebalance 是消费组重新分配 Partition 的过程，通常由消费者加入/离开、订阅变化、Partition 变化、心跳超时触发。Rebalance 期间可能暂停消费，造成延迟抖动。 可通过静态成员、合理设置心跳和 poll 参数、Cooperative Rebalance、避免频繁扩缩容来降低影响。 Rebalance 是消费组重新分配 Partition 的过程。触发原因包括消费者加入、消费者宕机或心跳超时、订阅 Topic 变化、Topic Partition 数变化、`max.poll.interval.ms` 超时等。Rebalance 期间分区所有权会变化，部分消费者暂停拉取，可能造成延迟抖动；如果业务处理很慢，还可能频繁触发 Rebalance，形成消费堆积。 优化手段包括：合理设置 `session.timeout.ms`、`heartbeat.interval.ms`，避免短暂抖动就误判消费者死亡；合理设置 `max.poll.interval.ms`，让单批消息处理时间小于该值；控制 `max.poll.records`，避免一次拉太多处理不过来；使用静态成员 `group.instance.id` 减少重启导致的完全 Rebalance；使用 Cooperative Rebalance 减少全量分区撤销；部署时避免频繁滚动重启整个消费组。 `session.timeout.ms` 主要和心跳有关，消费者在这个时间内没有心跳，Coordinator 会认为它挂了。`max.poll.interval.ms` 关注的是应用调用 `poll()` 的间隔，如果业务处理太久导致迟迟不 poll，即使心跳线程还活着，也会被认为消费能力异常并触发 Rebalance。一个偏“活着没有”，一个偏“有没有继续消费”。 同一个消费组内，一个 Partition 同一时刻只能分配给一个消费者。如果 Topic 只有 10 个 Partition，一个消费组最多只有 10 个消费者能真正工作，第 11 个开始会空闲。要提升消费并行度，需要增加 Partition，或者在消费者内部做线程池处理，但内部并发又要处理 offset 提交、有序性和幂等问题。

## 13. Offset 是什么？如何提交？

- [ ] 自动提交和手动提交有什么区别？
  答案：Offset 表示消费进度。自动提交简单但可能处理失败仍提交导致丢数据；手动提交可在业务处理成功后提交，更可靠。提交方式有同步提交和异步提交。 最稳妥是“处理成功后提交 offset”，失败则重试或进入死信/补偿流程。

## 14. Kafka 为什么吞吐高？

- [ ] Kafka 高性能的关键原因有哪些？
  答案：顺序写磁盘、充分利用 OS Page Cache、批量发送、消息压缩、零拷贝 `sendfile`、分区并行、消费者拉模式、日志分段顺序追加。 磁盘顺序写不一定慢，随机写才慢；Kafka 通过顺序追加接近磁盘最大吞吐。 Kafka 高吞吐不是靠单点性能“硬撑”，而是多层设计叠加出来的。第一，Broker 端采用追加写日志，写入路径基本是顺序 IO，避免大量随机写；第二，Kafka 大量依赖操作系统 Page Cache，消息写入后通常先进入页缓存，后续消费者读取也可以直接命中页缓存；第三，Producer 端会按 Partition 做批量聚合，`batch.size` 和 `linger.ms` 让多条消息合并成批次发送，减少网络请求次数；第四，支持压缩，`lz4`、`snappy`、`zstd` 等压缩方式能减少网络和磁盘 IO；第五，Consumer 拉取数据时可以使用零拷贝，把磁盘页缓存中的数据直接发送到 socket，减少用户态/内核态复制；第六，Topic 拆成多个 Partition 后可以在多个 Broker、多个 Consumer 之间并行读写。 面试时可以按“写入路径、存储路径、读取路径、扩展路径”四层讲，这样比只背“顺序写、零拷贝”更完整。 不能简单说磁盘顺序写比内存快。更准确的说法是：磁盘顺序写可以接近磁盘自身的最大吞吐，而随机写会被寻址和机械/块设备调度拖慢。Kafka 通过顺序追加、批量、页缓存把磁盘 IO 做成高吞吐模式，但内存访问延迟仍然远低于磁盘。面试中要避免绝对化表述。 不一定。Kafka 依赖操作系统 Page Cache，Broker 写入日志后数据通常先在页缓存里，刷盘由操作系统和 Kafka 配置共同影响。Kafka 的可靠性更主要依赖多副本同步，而不是每条消息同步刷盘。强制频繁 fsync 会明显降低吞吐，生产上通常通过副本数、ISR、`acks=all`、`min.insync.replicas` 来保证可靠性。

## 15. Kafka 的零拷贝是什么？

- [ ] Kafka 如何减少数据拷贝？
  答案：消费者读取消息时，Kafka 可通过文件系统 Page Cache 和 `sendfile` 直接把文件数据从内核缓冲区发送到 socket，减少用户态和内核态拷贝，降低 CPU 开销。 零拷贝主要优化 Broker 到 Consumer 的数据传输路径。

## 16. Kafka 日志存储结构是什么？

- [ ] Segment、Index、Log 的关系是什么？
  答案：一个 Partition 对应一个日志目录，日志按 Segment 分段。每个 Segment 通常包含 `.log` 数据文件、`.index` offset 索引、`.timeindex` 时间索引，便于按 offset 或时间快速定位。 分段有利于删除过期数据、索引定位和故障恢复。

## 17. Kafka 的数据保留策略有哪些？

- [ ] `retention` 和 `compact` 有什么区别？
  答案：retention 按时间或大小保留消息，过期删除；log compaction 按 key 保留最新值，适合保存状态变更或最新配置。 compact 不保证只剩一条数据，它是后台异步压缩，旧值最终会被清理。

## 18. 如何选择 Partition 数量？

- [ ] Partition 数量规划要考虑哪些因素？
  答案：要结合吞吐目标、消费者并行度、Broker 数、单分区吞吐、未来扩展、Rebalance 成本和文件句柄成本。一般先按吞吐和并发估算，再压测验证。 后续增加 Partition 会影响 key 到 Partition 的映射，可能破坏业务有序性。 Partition 数要同时考虑吞吐、并行度、Broker 资源和未来扩展。一个常见估算方式是：先压测单 Partition 在当前消息大小和副本配置下的写入/读取吞吐，再用目标吞吐除以单分区吞吐得到基本分区数；同时考虑消费者并行度，因为同一消费组最大有效并行度不超过 Partition 数；再结合 Broker 数让 Partition 尽量均匀分布。 Partition 不是越多越好。过多 Partition 会增加 Broker 文件句柄、内存、Controller 元数据、Leader 选举成本、Rebalance 成本和故障恢复时间。对延迟敏感的消费组，过多 Partition 也可能导致 poll 管理和调度开销上升。 增加 Partition 会改变默认 key hash 到 Partition 的映射。比如原来同一个 orderId 一直落在 Partition 2，增加分区后可能落到 Partition 5，这会破坏“同一业务 key 分区有序”的假设。对强依赖 key 有序的业务，要谨慎增加 Partition，必要时使用自定义分区策略或按业务维度提前规划。 一个 Partition 可以保证 Topic 内全局有序，但吞吐、存储和消费并行度都受限，leader 所在 Broker 也容易成为瓶颈。只有在强全局顺序是硬要求、吞吐可接受时才这么做。

## 19. 消费堆积如何排查？

- [ ] Consumer Lag 很高时怎么处理？
  答案：先判断生产突增还是消费变慢；看消费者处理耗时、线程池、下游 DB/接口、Rebalance、GC、网络、分区分配是否均衡。处理方式包括扩消费者和分区、批量消费、优化业务逻辑、限流、临时扩容、跳过/隔离异常消息。 消费者数超过 Partition 数无效；扩容前要确认 Partition 数足够。 第一步先确认堆积范围：是所有 Topic 都堆积，还是某个 Topic；是所有 Partition 都堆积，还是少数 Partition；是所有消费者都慢，还是个别实例慢。 第二步看生产速度和消费速度：如果生产突然暴涨，消费能力没变，堆积属于容量问题；如果生产没变但消费下降，要重点排查消费者和下游。 第三步查消费者自身：业务处理耗时是否增加，线程池是否满，批量大小是否不合理，是否频繁 Rebalance，是否 GC，是否有异常消息卡住，是否同步调用外部接口变慢。 第四步查下游：数据库、Redis、HTTP 服务、ES、HBase 等是否限流或响应变慢。很多 Kafka 堆积本质是下游写不动。 第五步查分区和倾斜：某些 Partition lag 特别高，可能是 key 分布不均，热点 key 落到同一个 Partition。 处理手段包括临时扩容消费者、增加分区、提高批处理能力、异步化下游写入、限流削峰、隔离异常消息、优化业务逻辑、热点 key 拆分。紧急情况下可以先保住主链路，把坏消息转入死信，后续补偿。 可以作为临时手段，但要先看 Partition 数。如果消费者数已经等于或超过 Partition 数，继续加消费者没有效果。如果瓶颈在下游 DB，加消费者反而会把压力打到下游，导致整体更慢。扩容前要确认瓶颈在消费者 CPU/处理能力，而不是 Partition 数或下游。 技术上可以，但业务上风险很高。跳 offset 等于放弃一段消息，适合日志、埋点这类可容忍丢失的场景，或已经有离线补偿方案。订单、支付、库存类场景不能随便跳，应该做死信隔离、补偿消费或人工处理。

## 20. Kafka 常见核心参数有哪些？

- [ ] Producer、Consumer、Broker 常见调优参数有哪些？
  答案：Producer 常看 `acks`、`retries`、`batch.size`、`linger.ms`、`compression.type`、`buffer.memory`、`enable.idempotence`；Consumer 常看 `max.poll.records`、`max.poll.interval.ms`、`session.timeout.ms`、`heartbeat.interval.ms`、`fetch.min.bytes`；Broker 常看副本数、`min.insync.replicas`、retention、segment、网络和 IO 线程参数。 参数没有银弹，要围绕吞吐、延迟、可靠性三者取舍。 如果目标是高可靠，Producer 关注 `acks=all`、`enable.idempotence=true`、`retries`、`delivery.timeout.ms`；Broker 关注副本数、`min.insync.replicas`、ISR 监控；Consumer 关注手动提交和幂等。 如果目标是高吞吐，Producer 关注 `batch.size`、`linger.ms`、`compression.type`、`buffer.memory`、`max.in.flight.requests.per.connection`；Consumer 关注 `fetch.min.bytes`、`fetch.max.wait.ms`、`max.poll.records`；Broker 关注磁盘、网络线程、页缓存和分区均衡。 如果目标是低延迟，`linger.ms` 不能太大，批量大小要平衡，Consumer 单批处理不能太重，下游写入要低延迟，Broker 端要避免磁盘和网络排队。 它表示单连接上未确认请求的最大数量。早期未开启幂等时，如果该值大于 1，前一个 batch 失败重试、后一个 batch 先成功，可能导致同 Partition 内乱序。开启幂等后 Kafka 会用序列号保证重试顺序，但生产上仍要结合版本和可靠性要求谨慎配置。 不是。`linger.ms` 越大，Producer 等待凑 batch 的时间越长，吞吐可能提高，但端到端延迟也会增加。日志采集可以适当增大；支付、风控等低延迟场景要更保守。

## 21. Kafka 和 RocketMQ、RabbitMQ 有什么区别？

- [ ] 不同 MQ 的适用场景怎么选？
  答案：Kafka 偏日志流和大吞吐场景，适合埋点、日志、流计算；RocketMQ 对事务消息、延迟消息、业务消息支持较强；RabbitMQ 路由模型灵活，适合传统企业消息和复杂路由，但吞吐通常不如 Kafka。 选型要结合吞吐、延迟、事务、顺序、运维和团队熟悉度。 Kafka 适合大吞吐日志流、埋点、数据管道、流计算和湖仓链路，优势是分区并行、顺序追加、生态成熟、与 Flink/Spark 集成好。RocketMQ 更偏业务消息，事务消息、延迟消息、顺序消息等能力比较贴近互联网业务场景。RabbitMQ 路由模型灵活，适合传统企业系统解耦、复杂路由和中小规模消息分发。 选型不能只说性能，要看消息语义、吞吐、延迟、事务、延迟消息、顺序要求、运维体系、团队经验和生态。比如日志流优先 Kafka；订单事务消息可能 RocketMQ 更顺手；复杂路由和协议兼容可能 RabbitMQ 更合适。 Kafka 原生不是以延迟消息为核心能力设计的。可以通过时间轮、多个延迟 Topic、消费者延迟判断、外部调度服务等方式实现，但复杂度和精确性要权衡。如果业务强依赖延迟消息，RocketMQ 这类原生支持更成熟的方案可能更合适。

## 22. Kafka 新旧架构中 ZooKeeper 和 KRaft 的区别是什么？

- [ ] Kafka 为什么引入 KRaft？
  答案：早期 Kafka 使用 ZooKeeper 保存元数据和做 Controller 选举；KRaft 使用 Kafka 自身的 Raft 元数据仲裁替代 ZooKeeper，减少外部依赖，提升元数据管理能力和运维简洁度。 实际回答时可说明公司版本不同，生产环境要看具体 Kafka 版本和部署模式。 早期 Kafka 使用 ZooKeeper 保存集群元数据、Broker 注册信息、Controller 选举等。Kafka 自身负责数据读写，ZooKeeper 负责协调。这个架构成熟，但引入了额外组件，元数据扩展和运维复杂度也更高。 KRaft 使用 Kafka 自己的 Raft quorum 管理元数据，Controller 节点通过 Raft 日志复制元数据变更，逐步替代 ZooKeeper。好处是减少外部依赖，统一运维模型，提升大规模 Partition 元数据管理能力。 不一定。要看 Kafka 版本、公司稳定性要求、运维经验和迁移成本。面试中可以说：新集群会优先评估 KRaft，存量集群迁移要做版本兼容、元数据迁移、回滚方案和压测，不会只因为架构更新就贸然迁移。
