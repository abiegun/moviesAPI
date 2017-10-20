package ab.movies.api.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieInfo  implements Serializable {
	private static final long serialVersionUID = 1L;
	private RestResultData<MovieDetails> movieDetails;
	private RestResultData<MovieComments[]> movieComments;
}
