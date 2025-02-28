package jmdevall.aann.thinkresponseextractor;

public class ThinkResponseExtractorDoNothing implements ThinkResponseExtractor{

	@Override
	public String extractResponse(String input) {
		return input;
	}

}
