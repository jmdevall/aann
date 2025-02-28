package jmdevall.aann;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;
import jmdevall.aann.thinkresponseextractor.ThinkResponseExtractor;
import lombok.Data;


@Data
public class PerceptronAgent {

	private WindowConversation conversation;

	private static final String ACTIVATION_FUNCTION_PROMPT=
			"Given the following information, generate a concise and relevant response.\n";
	
	// Depending on the type of transformation you want to apply to the output.
	// The activation function should act as a 'directive' for the LLM to process the input in a structured or specific way."
	// others posibilities may be for example ...
			//"Analyze the following information and summarize the main idea";
			//"Filter and prioritize the most relevant elements of the following input."
	
	private ThinkResponseExtractor thinkResponseExtractor;

	private String weights;

	public PerceptronAgent(ChatLanguageModel llm, String weights, ThinkResponseExtractor thinkResponseExtractor) {
		this.thinkResponseExtractor=thinkResponseExtractor;

		this.conversation=WindowConversation.newConversation(llm);
		this.weights=weights;
		conversation.add(PromptTemplate.from(GENERAL_AGENT_SYSTEM_PROMPT).apply(this.weights).toSystemMessage());
		conversation.startNewRound();
	}
	
	int round=0;
	
	private static final String GENERAL_AGENT_SYSTEM_PROMPT="""
	You are a generic LLM Agent learning as a neuron do, acting as a perceptron.
	You recibe a serie of inputs and generte 1 output
	You are part of a neural network where each neuron is another agent
	
	The weights of the perceptron acts as your current "prompt".
	Like a perceptron do, you learn by adjusting you weights (the prompt), your mission as an agent is to adapt the prompt in each training iteration to ensure it provides the best possible response to all the inputs you receive.

	The best prompt is the one that is able to generate all the different expected responses from the inputs you receive. You will need to revise 
	Your prompt should store the instructions and information you consider relevant, such as instructions, guides, or learned deduced information.
		
	The user will instruct you to work in two modes.
	In **feedforward mode**, you will recibe a serie of inputs and you simply apply the current prompt to the inputs to generate a response, as a LLM will normally do
	In **backpropagation mode**, you will receibe the "correct answer" should have been, and you will be asked to adapt the weights (your current prompt) to. You will need to review the history of the conversation with the user.  

	Your current prompt:
	{{it}}
    """;

	public String forward(List<String> inputs) {

		StringBuilder combinedInstructions=new StringBuilder();
		combinedInstructions.append("Feedforward mode! "+ACTIVATION_FUNCTION_PROMPT);

	    for(int i=0;i<inputs.size();i++) {
	    	combinedInstructions.append("\n#INPUT #"+i+"\n");
	        combinedInstructions.append(inputs.get(i));
	    }
	    combinedInstructions.append("\nCurrent prompt:\n"+this.weights);
	    conversation.add(UserMessage.from(combinedInstructions.toString()));
	    
	    Response<AiMessage> response=generateConversation();
	    conversation.add(response.content());
	    return thinkResponseExtractor.extractResponse(response.content().text());
	}
	
	
	public List<String> train(List<String> inputs, String expectedOutput) {

		// Generar la salida actual
	    String actualOutput = forward(inputs);

	    return backpropage(inputs, expectedOutput);
	}


	/**
	 * Given a list of inputs and a expected output, 
	 * @param inputs
	 * @param expectedOutput
	 * @return
	 */
	public List<String> backpropage(List<String> inputs, String expectedOutput) {
		conversation.add(PromptTemplate.from("""
	    		Backpropagation mode!
	    		You interpreted some inputs and instructions, and combined both to generate a response, but the **expected response** should have been the following instead:
	    		
	    		{{expectedoutput}}
	    		
	    		# YOUR MISSION: Adapt/transform the value of the "current prompt":
	    		
	    		{{currentprompt}}

	    		# CONSIDERATIONS

	    		Ensure the new prompt is optimized for generating the new expected response and the previous expected responses in previous training rounds

	    		# IMPORTANT:
		    		
	    		Provide only the new prompt; nothing else.
	    		""").apply(Map.of(
	    				"expectedoutput",expectedOutput,
	    				"currentprompt",this.weights
	    				))
	    		.toUserMessage());

		    Response<AiMessage> response= generateConversation();
		    conversation.add(response.content());
		    String feedback = thinkResponseExtractor.extractResponse(response.content().text());
		    this.weights=feedback;
		    this.conversation.startNewRound();
		    
		    System.out.println("####### prompt="+this.weights);
		    
		    ArrayList<String> ret=new ArrayList<String>();
		    for(int i=0;i<inputs.size();i++) {
			    WindowConversation conversationObtainShouldBeInput=conversation.clone();
			    conversationObtainShouldBeInput.add(PromptTemplate.from(
			    	   """
			    	   Given the input number #{{numinput}}:
			    	   
			    	   {{input}}
			    	   
			    	   
			    	   ----
			    	   Modify or generate the 'input' such that when applied to the corrected prompt, it yields the expected result, as defined in our previous conversation.   
			    	  """)
			    	  .apply(Map.of("numinput",i,
			    			  "input",inputs.get(i))).toUserMessage());
			    Response<AiMessage> newInput=conversationObtainShouldBeInput.generateConversation();
			    String expectedInput = thinkResponseExtractor.extractResponse(newInput.content().text());
				ret.add(expectedInput);
		    }
		    return ret;
	}
	
	

	private Response<AiMessage> generateConversation() {
		return this.conversation.generateConversation();
	}
	
}
