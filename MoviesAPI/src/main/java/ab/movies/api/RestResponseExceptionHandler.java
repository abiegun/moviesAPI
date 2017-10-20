package ab.movies.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
		return new ResponseEntity<Object>("You are not authorized to enter here. ["+ex+"]", new HttpHeaders(), HttpStatus.FORBIDDEN);
	}
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAllOtherException(Exception ex, WebRequest request) {
		return new ResponseEntity<Object>("Unexpected server error. ["+ex+"]", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}}
