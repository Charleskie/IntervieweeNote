package cn.meituan.wangsheng;
import java.util.*;
//import storm tuple packages
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

//import Spout interface packages
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;

//Create a class FakeLogReaderSpout which implement IRichSpout interface to access functionalities

public class FakeCallLogReaderSpout implements IRichSpout {
    //Create instance for SpoutOutputCollector which passes tuples to bolt.
    private SpoutOutputCollector collector;
    private boolean completed = false;

    //Create instance for TopologyContext which contains topology data.
    private TopologyContext context;

    //Create instance for Random class.
    private Random randomGenerator = new Random();
    private Integer idx = 0;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.context = context;
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        if(this.idx <= 1000) {
            List<String> mobileNumbers = new ArrayList<String>();
            mobileNumbers.add("13531158704");
            mobileNumbers.add("15959268027");
            mobileNumbers.add("15671677958");
            mobileNumbers.add("15671567026");
            mobileNumbers.add("13250523092");

            Integer localIdx = 0;
            while(localIdx++ < 100 && this.idx++ < 1000) {
                String fromMobileNumber = mobileNumbers.get(randomGenerator.nextInt(5));
                String toMobileNumber = mobileNumbers.get(randomGenerator.nextInt(5));

                while(fromMobileNumber == toMobileNumber) {
                    toMobileNumber = mobileNumbers.get(randomGenerator.nextInt(5));
                }

                Integer duration = randomGenerator.nextInt(60);
                this.collector.emit(new Values(fromMobileNumber, toMobileNumber, duration));
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("from", "to", "duration"));
    }

    //Override all the interface methods
    @Override
    public void close() {}

    public boolean isDistributed() {
        return false;
    }

    @Override
    public void activate() {}

    @Override
    public void deactivate() {}

    @Override
    public void ack(Object msgId) {}

    @Override
    public void fail(Object msgId) {}

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}