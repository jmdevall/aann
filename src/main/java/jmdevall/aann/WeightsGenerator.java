package jmdevall.aann;

import java.util.ArrayList;
import java.util.List;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import jmdevall.aann.thinkresponseextractor.ThinkResponseExtractor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class WeightsGenerator {
	
	private ChatLanguageModel llm;
	private ThinkResponseExtractor thinkResponseExtractor;

	public String generateRandomWeight() {
		String system = "generate a random prompt for a llm. Only respond with just the prompt. Do not add somethink like 'here is the prompt'";
		String randomweight = generateAndPruneThinking(system);
		log.debug("new random weight "+randomweight);
		return randomweight;
	}
	
	public List<String> generateRandomWeights(int size){
		List<String> weights=new ArrayList<>();
		for(int i=0;i<size;i++) {
			weights.add(generateRandomWeight());
		}
		
		return weights;
	}

	private String generateAndPruneThinking(String prompt) {
		String airesponse=this.llm.generate(SystemMessage.from(prompt)).content().text();
		return this.thinkResponseExtractor.extractResponse(airesponse);
	}
}
