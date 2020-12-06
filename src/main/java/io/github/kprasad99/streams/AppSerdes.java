package io.github.kprasad99.streams;

import io.github.kprasad99.streams.proto.Department;
import io.github.kprasad99.streams.proto.DepartmentData;
import io.github.kprasad99.streams.proto.Employee;
import io.github.kprasad99.streams.protobuf.serialization.ProtobufDeserializer;
import io.github.kprasad99.streams.protobuf.serialization.ProtobufSerializer;
import org.apache.kafka.common.serialization.Serdes;

public class AppSerdes {
    public static final class DepartmentSerde extends Serdes.WrapperSerde<Department> {
        public DepartmentSerde() {
            super(new ProtobufSerializer<>(), new ProtobufDeserializer<>(Department.class));
        }
    }

    public static final class EmployeeSerde extends Serdes.WrapperSerde<Employee> {
        public EmployeeSerde() {
            super(new ProtobufSerializer<>(), new ProtobufDeserializer<>(Employee.class));
        }
    }

    public static final class DepartmentDataSerde extends Serdes.WrapperSerde<DepartmentData> {
        public DepartmentDataSerde() {
            super(new ProtobufSerializer<>(), new ProtobufDeserializer<>(DepartmentData.class));
        }
    }
    
    public static EmployeeSerde employee() {
    	return new EmployeeSerde();
    }
}
