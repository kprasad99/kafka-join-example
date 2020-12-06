package io.github.kprasad99.streams.protobuf.serialization;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import com.google.protobuf.MessageLite;

public class ProtobufDeserializer<T extends MessageLite> implements Deserializer<T> {

	private static final String PROTO_METHOD_NAME = "parseFrom";

	private final Class<T> clazz;

	private final ConcurrentHashMap<String, Class<?>> registeredClazz;

	public ProtobufDeserializer() {
		this(null);
	}

	public ProtobufDeserializer(@Nullable Class<T> clazz) {
		this.clazz = clazz;
		this.registeredClazz = new ConcurrentHashMap<>();
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(String topic, byte[] data) {
		try {
			if (clazz == null) {
				throw new InstantiationException("No target type provided");
			}
			Method m = ReflectionUtils.findMethod(clazz, PROTO_METHOD_NAME, byte[].class);
			if (null == m) {
				throw new NoSuchMethodException(String
						.format("The message class [%s] must have a parseFrom(byte[] bytes) method", clazz.getName()));
			}
			return (T) m.invoke(null, data);
		} catch (IllegalAccessException | InstantiationException | NoSuchMethodException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new SerializationException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(String topic, Headers headers, byte[] data) {

		try {

			Method m = null;
			if (clazz != null) {
				m = ReflectionUtils.findMethod(clazz, PROTO_METHOD_NAME, byte[].class);
			} else {
				Header header = headers.lastHeader("com.google.protobuf.type");
				if (header != null) {
					String type = new String(header.value());
					if (!registeredClazz.containsKey(type)) {
						registeredClazz.put(type, ClassUtils.forName(type, getClass().getClassLoader()));
					}
					Class<?> clazzOne = registeredClazz.get(type);
					if (!MessageLite.class.isAssignableFrom(clazzOne)) {
						throw new InstantiationException(String
								.format("The provided [%s] is not a subtype of MessageLite.class", clazzOne.getName()));
					}
					m = ReflectionUtils.findMethod(clazzOne, PROTO_METHOD_NAME, byte[].class);
					if (null == m) {
						throw new NoSuchMethodException(
								String.format("The message class [%s] must have a parseFrom(byte[] bytes) method",
										clazzOne.getName()));
					}
				} else {
					throw new ClassNotFoundException(
							"Could not determine class, neither provided while instatiating Deserializer nor in kafka message header");
				}
			}
			return (T) m.invoke(null, data);
		} catch (IllegalAccessException | ClassNotFoundException | LinkageError | InstantiationException
				| NoSuchMethodException | IllegalArgumentException | InvocationTargetException e) {
			throw new SerializationException(e);
		}
	}

}
