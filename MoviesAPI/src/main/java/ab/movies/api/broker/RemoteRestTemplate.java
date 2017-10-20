package ab.movies.api.broker;

import org.springframework.web.client.RestTemplate;

public class RemoteRestTemplate extends RestTemplate {

	private static RemoteRestTemplate singleton = new RemoteRestTemplate();
	
	private RemoteRestTemplate() {
		super();
	}

	public static RemoteRestTemplate getInstance() {
		return singleton;
	}

}
