package ab.movies.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import ab.movies.api.broker.RemoteRestTemplate;
import ab.movies.api.data.MovieInfo;
import ab.movies.api.data.RestResultData;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoviesApiApplicationTests {

	private MockRestServiceServer mockServer;

	@Autowired
	private FacadeControler controller;

	@Before
	public void setUp() {
		mockServer = MockRestServiceServer.createServer(RemoteRestTemplate.getInstance());
	}

	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();
	}

	private String expectedDetails(String id) {
		return "{\"id\":\"" + id + "\",\"title\":\"Mocked Title-" + id + "\",\"description\":\"Mocked Description-" + id
				+ "\",\"requestId\":2}";
	}

	private String expectedComments(String id) {
		return "[{\"id\":\"" + id + "\",\"message\":\"Mocked message-" + id + "\",\"username\":\"Mocked user-" + id
				+ "\",\"requestId\":0},{\"id\":\"" + id + "\",\"message\":\"Mocked message2-" + id
				+ "\",\"username\":\"Mocked user2-" + id + "\",\"requestId\":1}]";
	}

	@Test
	public void movieService() throws InterruptedException, ExecutionException, URISyntaxException {
		String id = "movieService";
		mockServer.expect(requestTo("http://localhost:8080/movie_details?id=" + id)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(expectedDetails(id), MediaType.APPLICATION_JSON));
		mockServer.expect(requestTo("http://localhost:8080/movie_comments?id=" + id)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(expectedComments(id), MediaType.APPLICATION_JSON));

		RestResultData<MovieInfo> mi = controller.movieInfo(id);
		System.out.println(mi);
		assertThat(mi).isNotNull();
		assertThat(mi.getData()).isNotNull();
		assertThat(mi.getData().getMovieDetails()).isNotNull();
		assertThat(mi.getData().getMovieComments().getState()).isEqualTo("LIVE");
		assertThat(mi.getData().getMovieDetails().getData()).isNotNull();
		assertThat(mi.getData().getMovieDetails().getData().getId()).isEqualTo(id);

	}

	@Test
	public void cacheingPolitics() throws InterruptedException, ExecutionException {
		String id = "movieService";
		mockServer.reset();
		mockServer.expect(requestTo("http://localhost:8080/movie_comments?id=" + id)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(expectedComments(id), MediaType.APPLICATION_JSON));
		mockServer.expect(requestTo("http://localhost:8080/movie_details?id=" + id)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(expectedDetails(id), MediaType.APPLICATION_JSON));

		controller.movieInfo(id);
		mockServer.reset();
		mockServer.expect(requestTo("http://localhost:8080/movie_comments?id=" + id)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(expectedComments(id), MediaType.APPLICATION_JSON));
		mockServer.expect(requestTo("http://localhost:8080/movie_details?id=" + id)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(expectedDetails(id), MediaType.APPLICATION_JSON));

		RestResultData<MovieInfo> mi = controller.movieInfo(id);
		assertThat(mi.getData().getMovieDetails().getState()).isEqualTo("CACHED");
		assertThat(mi.getData().getMovieComments().getState()).isEqualTo("LIVE");
	}

	@Test
	public void commentsFailover() throws InterruptedException, ExecutionException {
		String id = "commentsFailover";
		mockServer.reset();
		mockServer.expect(requestTo("http://localhost:8080/movie_comments?id=" + id)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(expectedComments(id), MediaType.APPLICATION_JSON));
		mockServer.expect(requestTo("http://localhost:8080/movie_details?id=" + id)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(expectedDetails(id), MediaType.APPLICATION_JSON));

		controller.movieInfo(id);

		mockServer.reset();
		mockServer.expect(requestTo("http://localhost:8080/movie_comments?id=" + id)).andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());
		mockServer.expect(requestTo("http://localhost:8080/movie_details?id=" + id)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(expectedDetails(id), MediaType.APPLICATION_JSON));

		RestResultData<MovieInfo> mi = controller.movieInfo(id);
		assertThat(mi.getData().getMovieComments().getState()).isEqualTo("CACHE");
	}

}
