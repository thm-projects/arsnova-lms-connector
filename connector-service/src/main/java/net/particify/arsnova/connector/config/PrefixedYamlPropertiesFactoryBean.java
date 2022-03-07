package net.particify.arsnova.connector.config;

import java.util.Properties;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.CollectionFactory;

/**
 * This {@link YamlPropertiesFactoryBean} removes a prefix from properties. This allows the use of a single, top-level
 * YAML root node without need to include it when referencing properties by name.
 *
 * @author Daniel Gerhardt
 */
public class PrefixedYamlPropertiesFactoryBean extends YamlPropertiesFactoryBean {
	private static final String PREFIX = "connector";

	@Override
	protected Properties createProperties() {
		final Properties result = CollectionFactory.createStringAdaptingProperties();
		process((properties, map) -> properties.forEach((k, v) -> {
			if (k.toString().startsWith(PREFIX + ".")) {
				result.put(k.toString().substring(PREFIX.length() + 1), v);
			}
		}));

		return result;
	}
}
