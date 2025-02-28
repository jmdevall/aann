package jmdevall.aann;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import jmdevall.aann.thinkresponseextractor.ThinkLlmResponseExtractorR1;

public class ThinkLlmResponseExtractorR1Test {

	private static final String EXAMPLE_RESPONSE="""
			 <think>
             (texto de pensamiento )
             </think>
             respuesta del llm
			""";
	@Test
	public void extractResponse() {
		assertEquals("respuesta del llm",new ThinkLlmResponseExtractorR1().extractResponse(EXAMPLE_RESPONSE));
	}
}
