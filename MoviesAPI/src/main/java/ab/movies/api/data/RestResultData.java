package ab.movies.api.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class RestResultData<T>  implements Serializable {
	private static final long serialVersionUID = 1L;
	private static long nextRequestId;
	private T data;
	private String state;
	private String error;
	private long requestId;
	
	public RestResultData(T data) {
		this.data = data;
		this.state = "LIVE";
		this.requestId = ++nextRequestId;
	}
	
	@SuppressWarnings("unchecked")
	public RestResultData<T> clone(String newState) {
		RestResultData<T> result;
		try {
			result = (RestResultData<T>)super.clone();
		} catch(CloneNotSupportedException ex) {
			result = new RestResultData<>(data, newState, error, requestId);
		}
		result.setState(newState);
		return result;
	}

	public void setException(Exception ex) {
		setError("["+ex.getClass().getName()+"] "+ex.getLocalizedMessage());
	}
}
