package IM.Data.Kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ProducerClient {
	private final Properties _properties;

	public ProducerClient(Properties properties) {
		this._properties = properties;
	}

	public Properties getProperties() {
		return _properties;
	}

	public void sendMessage(Object msg) {
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(_properties);
		String msgjson= com.alibaba.fastjson.JSON.toJSONString(msg);
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(_properties.getProperty("topic"),msgjson);
		producer.send(record);
		producer.close();
	}
}
