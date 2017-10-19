package ab.movies.api.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class MovieInfo {
	private MovieDetails movieDetails;
	private MovieComments[] movieComments;
}
