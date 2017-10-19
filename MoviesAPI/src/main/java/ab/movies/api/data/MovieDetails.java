/**
 * Movies API task 
 * @author arek
 *
 */
package ab.movies.api.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class MovieDetails implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String description;
	private long requestId;
}
