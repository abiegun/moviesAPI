package ab.movies.api.broker;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ab.movies.api.data.MovieComments;
import ab.movies.api.data.MovieDetails;
import ab.movies.api.data.MovieInfo;
import ab.movies.api.data.RestResultData;

@Scope(value = "singleton")
@Component
public class DataProducer {

	private static final Logger LOG = Logger.getLogger(DataProducer.class.getName());

	private ExecutorService executor;
	private RESTServiceConsumer<MovieDetails> movieDetails;
	private RESTServiceConsumer<MovieComments[]> movieComments;

	public DataProducer() {
		try {
			executor = Executors.newFixedThreadPool(10);
			movieDetails = new RESTServiceConsumer<MovieDetails>(MovieDetails.class,
					"http://localhost:8080/movie_details?id={id}", "LFU");
			movieComments = new RESTServiceConsumer<MovieComments[]>(MovieComments[].class,
					"http://localhost:8080/movie_comments?id={id}", "FIFO");
			LOG.info("== DataProducer initialized properly ===");
		} catch (Exception ex) {
			LOG.error("DataProducer initialization error", ex);
		}
	}

	public RestResultData<MovieInfo> getMovieInfo(String id) throws InterruptedException, ExecutionException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getMovieInfo(" + id + ")...");
		}
		Future<RestResultData<MovieDetails>> details = executor.submit(() -> movieDetails.getCached(id));
		Future<RestResultData<MovieComments[]>> comments = executor.submit(() -> movieComments.getFailover(id));

		MovieInfo info = new MovieInfo(details.get(), comments.get());
		RestResultData<MovieInfo> result = new RestResultData<>(info);

		if (LOG.isDebugEnabled()) {
			LOG.debug("getMovieInfo(" + id + "): " + result);
		}
		return result;
	}
}
