package ab.movies.api;

import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ab.movies.api.broker.Broker;
import ab.movies.api.data.MovieInfo;

@RestController
public class FacadeControler {
	
	private static final Logger LOG = Logger.getLogger(FacadeControler.class.getName());
	
	@RequestMapping("/")
    public String test() {
        return "Use /movie?id=NN"
        		+"<BR>update_details"
        		+"<BR>update_comment";
    }

	@RequestMapping("/movie")
    public MovieInfo movieInfo(@RequestParam(value="id", defaultValue="") String id) throws InterruptedException, ExecutionException {
		LOG.info("movieInfo("+id+")");
		return Broker.getInstance().getMovieInfo(id);
    }
	@RequestMapping(value="/update_details", method = RequestMethod.POST)
    public String updateDetails(@RequestParam(value="id", defaultValue="") String id) {
		LOG.info("NOT IMPLEMENTED: updateDetails("+id+")");
        return "NOT IMPLEMENTED";
    }

	@RequestMapping(value="/update_comment", method = RequestMethod.POST)
    public String updateComment(@RequestParam(value="id", defaultValue="") String id) {
		LOG.info("NOT IMPLEMENTED: updateComment("+id+")");
        return "NOT IMPLEMENTED";
    }
	
}
