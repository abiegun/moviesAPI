package ab.movies.api;

import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ab.movies.api.broker.DataProducer;
import ab.movies.api.data.MovieInfo;
import ab.movies.api.data.RestResultData;

@RestController
public class FacadeControler {

	private static final Logger LOG = Logger.getLogger(FacadeControler.class.getName());

	@RequestMapping(value = { "/", "/help" })
	public String help() {
		LOG.info("/help)");
		return "<BR>/movie?id=NN<BR>/update_details" + "<BR><BR>Users" + "<BR>admin/admin" + "<BR>user/user"
				+ "<BR>user2/user2"
				+ "<BS>If NoComments is a part of id, there will be no comments, if NoDetails a part of id, there will be no details";
	}

	@RequestMapping("/movie")
	public RestResultData<MovieInfo> movieInfo(@RequestParam(value = "id", defaultValue = "") String id)
			throws InterruptedException, ExecutionException {
		LOG.info("/movie?id=" + id);
		return DataProducer.getInstance().getMovieInfo(id);
	}

	@RequestMapping(value = "/update_details", method = RequestMethod.GET)
	public String updateDetails(@RequestParam(value = "id", defaultValue = "") String id) {
		LOG.info("/pdate_details  NOT IMPLEMENTED");
		return "NOT IMPLEMENTED";
	}

	@RequestMapping(value = "/update_comment", method = RequestMethod.GET)
	public String updateComment(@RequestParam(value = "id", defaultValue = "") String id) {
		LOG.info("/update_comment  NOT IMPLEMENTED");
		return "NOT IMPLEMENTED";
	}

	@ExceptionHandler(Exception.class)
	public RestResultData<String> handleIOException(Exception ex) {
		RestResultData<String> result = new RestResultData<>("Unexpected error");
		result.setException(ex);
		return result;
	}
}
