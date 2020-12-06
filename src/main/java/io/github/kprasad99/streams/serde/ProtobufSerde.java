package io.github.kprasad99.streams.serde;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.google.protobuf.MessageLite;

import io.github.kprasad99.streams.protobuf.serialization.ProtobufDeserializer;
import io.github.kprasad99.streams.protobuf.serialization.ProtobufSerializer;

public class ProtobufSerde<T extends MessageLite> implements Serde<T> {

	private final Serializer<T> serializer;
	private final Deserializer<T> deserializer;

	public ProtobufSerde() {
		serializer = new ProtobufSerializer<>();
		deserializer = new ProtobufDeserializer<>();
	}

	public ProtobufSerde(Class<T> clazz) {
		serializer = new ProtobufSerializer<>();
		deserializer = new ProtobufDeserializer<>(clazz);
	}

	@Override
	public Serializer<T> serializer() {
		return serializer;
	}

	@Override
	public Deserializer<T> deserializer() {
		return deserializer;
	}

}
