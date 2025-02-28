package jmdevall.aann;

import java.time.Duration;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jmdevall.aann.thinkresponseextractor.ThinkLlmResponseExtractorR1;
import jmdevall.aann.thinkresponseextractor.ThinkResponseExtractor;
import jmdevall.aann.thinkresponseextractor.ThinkResponseExtractorDoNothing;

public class SetupUtil {
	
	private static final String OPENAI_BASE_URL="http://localhost:8081/v1";
	private static final String FULL_PARQUET_FILE_PATH = "/your-fullpath-to..../train-00000-of-00001.parquet";
	
	public static ChatLanguageModel getLlm() {
		ChatLanguageModel model = OpenAiChatModel.builder()
	    		.apiKey("foo")
	            .baseUrl(OPENAI_BASE_URL)
	            .maxTokens(30000)
	            .timeout(Duration.ofHours(5))
	            .build();
		return model;
	}
	
	public static ThinkResponseExtractor getThinkResponseExtractor() {
		
		// SELECT the one you like depending on the model you use:....
				return new ThinkResponseExtractorDoNothing();
				//return new ThinkLlmResponseExtractorR1();
	}
	
	public static final TrainingDataLoader getTrainingDataLoader() {
		return new TrainingDataLoader(FULL_PARQUET_FILE_PATH);
	}
	
	public static WeightsGenerator getWeightsGenerator() {
		return new WeightsGenerator(getLlm(), getThinkResponseExtractor());
	}
	
	public static InputUnificator getInputUnificator() {
		return new InputUnificator(getLlm());
	}

	public static NetworkLayerFactory getNetworkLayerFactory() {
		return new NetworkLayerFactory(getLlm(), getWeightsGenerator(), getInputUnificator(),getThinkResponseExtractor());
	}
	
	public static PerceptronAgent newPerceptronAgent() {
		return new PerceptronAgent(getLlm(),getWeightsGenerator().generateRandomWeight(),getThinkResponseExtractor());
	}
}
