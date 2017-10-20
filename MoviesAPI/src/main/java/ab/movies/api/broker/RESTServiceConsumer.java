package ab.movies.api.broker;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ab.movies.api.data.RestResultData;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

public class RESTServiceConsumer<T> {

	private static CacheManager cacheManager = CacheManager.newInstance();

	private String serviceUrl;
	private Cache cache;
	private Class<T> genericType;

	private RestTemplate restTemplate;

	RESTServiceConsumer(Class<T> type, String url, String cachePolicy) {
		this.serviceUrl = url;
		genericType = type;
		cacheManager.addCache(url);
		cache = cacheManager.getCache(url);
		restTemplate = RestTemplateFactory.getRestTemplate(type.getName());
		CacheConfiguration config = cache.getCacheConfiguration();
		config.setMemoryStoreEvictionPolicy(cachePolicy);
		config.setMaxEntriesLocalHeap(10000);

	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	@Bean
	public RestResultData<T> get(String id) {
		RestResultData<T> result;
		try {
			T data = (T) restTemplate.getForObject(serviceUrl, genericType, id);
			result = new RestResultData<>(data);
		} catch (Exception ex) {
			result = new RestResultData<>(null);
			result.setException(ex);
		} 
		if (result.getData()!=null) {
			cache.put(new Element(id, result.clone("CACHED")));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public RestResultData<T> getFailover(String id) {
		try {
			return get(id);
		} catch (RestClientException ex) {
			Element element = cache.get(id);
			if (element != null) {
				return (RestResultData<T>) element.getObjectValue();
			}
			RestResultData<T> result = new RestResultData<>(null);
			result.setException(ex);
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	public RestResultData<T> getCached(String id) {
		Element element = cache.get(id);
		if (element != null) {
			return (RestResultData<T>) element.getObjectValue();
		}
		return get(id);
	}
}
