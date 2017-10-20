package ab.movies.api.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor()
public class MovieComments implements Serializable {
	private static final long serialVersionUID = 1L;
	private String movieId;
	private String message;
	private String username;
	private long requestId;
}
