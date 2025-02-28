package jmdevall.aann;

import java.util.HashMap;
import java.util.Map;

import com.exasol.parquetio.data.Row;

public class TrainingData {
	private Map<String,String> values;
	
	public TrainingData(Row row) {
		this.values=new HashMap<String,String>();
		
		for(String fieldName:row.getFieldNames()) {
			values.put(fieldName, convToString(row.getValue(fieldName)));
		}
	}
	
	private String convToString(Object value) {
		if(value==null) {
			return "";
		}
		return value.toString();
	}

	public String getSolution() {
		return this.values.get("solution");
	}
	
	public String getThinking() {
		return this.values.get("thinking_trajectories");
	}
	
	public String getQuestion() {
		return this.values.get("question");
	}
	
	public String getAttempt() {
		return this.values.get("attempt");
	}

}
