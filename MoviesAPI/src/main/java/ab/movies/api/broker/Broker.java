package ab.movies.api.broker;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ab.movies.api.data.MovieComments;
import ab.movies.api.data.MovieDetails;
import ab.movies.api.data.MovieInfo;

public class Broker {
	
	private static final Broker singleton = new Broker();
	
	private ExecutorService executor;
	private RESTConsumerService<MovieDetails> movieDetails;
	private RESTConsumerService<MovieComments[]> movieComments;
	
	private Broker() {
		try {
			executor = Executors.newFixedThreadPool(10);
			movieDetails = new RESTConsumerService<MovieDetails>(MovieDetails.class,
					"http://localhost:8080/movie_details?id={id}", "LFU");
			movieComments = new RESTConsumerService<MovieComments[]>(MovieComments[].class, 
					"http://localhost:8080/movie_comments?id={id}", "FIFO");
		} catch(Exception ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
	}
	
	public static Broker getInstance() {
		return singleton;
	}

	public MovieInfo getMovieInfo(String id) throws InterruptedException, ExecutionException {
		
		Future<MovieDetails> details = executor.submit(()->movieDetails.getCached(id));
		Future<MovieComments[]> comments = executor.submit(()->movieComments.getFailover(id));

		MovieInfo result = new MovieInfo(details.get(), comments.get());
		return result;
	}
}
