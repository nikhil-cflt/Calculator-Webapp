package calculator;

import io.confluent.rest.RestConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class CalculatorConfiguration extends RestConfig {
    private static final ConfigDef config;

    static {
        config = baseConfigDef();
    }

    public CalculatorConfiguration(Map<String, String> props) {
        super(config, props);
    }
}
