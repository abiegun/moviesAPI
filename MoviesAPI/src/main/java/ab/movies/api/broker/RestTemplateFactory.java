package ab.movies.api.broker;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

public class RestTemplateFactory {

	private static final Logger LOG = Logger.getLogger(RestTemplateFactory.class.getName());

	private static Map<String, RestTemplate> map = new HashMap<>();

	public static synchronized RestTemplate getRestTemplate(String name) {
		RestTemplate result = map.get(name);
		if (result == null) {
			result = new RestTemplate();
			map.put(name, result);
			LOG.info("RestTemplate " + name + " created");
		}

		return result;
	}
}
