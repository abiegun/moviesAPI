package ab.movies.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ab.movies.api.data.MovieInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoviesApiApplicationTests {

	@Autowired
    private FacadeControler controller;
	
	@Autowired
	MockedRemoteServices mockedServices;
	
	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();
		assertThat(mockedServices).isNotNull();
	}

	@Test
	public void movieService() throws InterruptedException, ExecutionException {
		String id = "ABC";
		MovieInfo mi = controller.movieInfo("ABC");
		assertThat(mi).isNotNull();
		assertThat(mi.getMovieDetails()).isNotNull();
		assertThat(mi.getMovieDetails().getId()).isEqualTo(id);
	}
	
}
