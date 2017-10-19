package ab.movies.api;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ab.movies.api.data.MovieComments;
import ab.movies.api.data.MovieDetails;

@RestController
public class MockedRemoteServices {
	private static final Logger LOG = Logger.getLogger(MockedRemoteServices.class.getName());
	private static int requestId;

	@RequestMapping("/movie_details")
	public MovieDetails movieDetails(@RequestParam(value = "id", defaultValue = "No ID") String id) {
		LOG.info("MOCKED: movieDetails(" + id + ")");
		return new MovieDetails(id, "Mocked Title-" + id, "Mocked Description-" + id, requestId++);
	}

	@RequestMapping("/movie_comments")
	public MovieComments[] movieComments(@RequestParam(value = "id", defaultValue = "No ID") String id) {
		LOG.info("MOCKED: movieComments(" + id + ")");
		return new MovieComments[] { new MovieComments(id, "Mocked message-" + id, "Mocked user-" + id, requestId++),
				new MovieComments(id, "Mocked message2-" + id, "Mocked user2-" + id, requestId++) };
	}

}
