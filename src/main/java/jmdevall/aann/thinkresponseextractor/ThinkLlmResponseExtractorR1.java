package jmdevall.aann.thinkresponseextractor;

public class ThinkLlmResponseExtractorR1 implements ThinkResponseExtractor {
    @Override
	public String extractResponse(String input) {
        int startTagIndex = input.indexOf("<think>");
        int endTagIndex = input.indexOf("</think>");
        
        if (startTagIndex == -1 || endTagIndex == -1) {
            return input; // Return original string if tags are not found
        }
        
        // Extract the part after the end tag
        String responsePart = input.substring(endTagIndex + 8).trim();
        return responsePart;
    }
}
