package ab.movies.api.broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

public class RESTConsumerService<T> {

	private static CacheManager cacheManager = CacheManager.newInstance();

	private String url;
	private Class<T> type;
	private Cache cache;
	
	private RestTemplate restTemplate = new RestTemplate();

	RESTConsumerService(Class<T> type, String url, String cachePolicy) {
		this.url = url;
		this.type = type;
		cacheManager.addCache(url);
		cache = cacheManager.getCache(url);
		CacheConfiguration config = cache.getCacheConfiguration();
		config.setMemoryStoreEvictionPolicy(cachePolicy);
		config.setMaxEntriesLocalHeap(10000);
	}

	@Bean
	public T get(String id) {
		T result = (T) restTemplate.getForObject(url, type, id);
		cache.put(new Element(id, result));
		return result;
	}

	@SuppressWarnings("unchecked")
	public T getFailover(String id) {
		try {
			return get(id);
		} catch (RestClientException ex) {
			Element element = cache.get(id);
			if (element != null) {
				return (T) element.getObjectValue();
			}
			throw new RestClientException("No data in cache", ex);
		}
	}

	@SuppressWarnings("unchecked")
	public T getCached(String id) {
		Element element = cache.get(id);
		if (element != null) {
			return (T) element.getObjectValue();
		}
		return get(id);
	}
}
