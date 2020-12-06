package io.github.kprasad99.streams.protobuf.serialization;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import com.google.protobuf.MessageLite;

public class ProtobufSerializer<T extends MessageLite> implements Serializer<T> {

	@Override
	public byte[] serialize(String topic, T data) {
		return data == null ? null : data.toByteArray();
	}

	@Override
	public byte[] serialize(String topic, Headers headers, T data) {
		if (data != null) {
			headers.add("com.google.protobuf.type", data.getClass().getName().getBytes());
		}
		return Serializer.super.serialize(topic, headers, data);
	}

}
