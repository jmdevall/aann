package jmdevall.aann;

import java.util.ArrayList;
import java.util.List;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;

public class InputUnificator {
	ChatLanguageModel llm;
	
	InputUnificator(ChatLanguageModel llm){
		this.llm=llm;
	}
	
	private static final String SYSTEM_PROMPT="""
You are an information consolidation expert. You will receive a list of inputs, each provided by a different agent. Your mission is to unify these inputs into a single, coherent output that reflects the majority consensus. The final output should be precise, concise, and summarize the most relevant information from the majority of the inputs. Ensure that the output is free of redundancies and irrelevant details.

# Inputs:

    [Input 1 from Agent 1]
    [Input 2 from Agent 2]
    [Input 3 from Agent 3]
    [Input 4 from Agent 4]
    [Input 5 from Agent 5]
    [Input 6 from Agent 6]

# Task:

    Analyze each input to identify the key points and information.
    Determine the majority consensus among the inputs.
    Eliminate any redundant or conflicting information.
    Summarize the majority consensus into a single, precise, and concise output.
    Ensure the output captures the most relevant information from the majority of the inputs.

# Output:

[Your unified, precise, concise, and information-rich output]

# Example:

Inputs:

@@Input 1:
The meeting will be held at 10 AM in Conference Room A. Agenda includes project updates and budget review.

@@Input 2:    
The conference is scheduled for 10 AM in Room A. We will discuss project status and financial planning.

@@Input 3:        
The meeting is at 10 AM in Conference Room B. Topics include project progress and budget allocation.

@@Input 4:        
The conference is at 10 AM in Room A. Key points: project updates and financial review.

@@Input 5:        
Meeting time is 10 AM in Conference Room A. Focus on project progress and budget review.

@@Input 6:        
The conference is at 10 AM in Room A. Discuss project updates and financial planning.

# Output:

"The meeting will be held at 10 AM in Conference Room A. The agenda includes project updates and budget review."

Please ensure your output adheres to the guidelines provided.
""";
	
	
	private static final String REQUEST_PROMPT="""
	Inputs:
	
	{{it}}
	
	# Remember your Task, and think step by step:

    Analyze each input to identify the key points and information.
    Determine the majority consensus among the inputs.
    Eliminate any redundant or conflicting information.
    Summarize the majority consensus into a single, precise, and concise output.
    Ensure the output captures the most relevant information from the majority of the inputs.
	""";
	
	public String unifica(List<String> inputs) {


		List<ChatMessage> messages=new ArrayList<ChatMessage>();
		
		messages.add(SystemMessage.from(SYSTEM_PROMPT));
		
		messages.add(
				PromptTemplate.from(REQUEST_PROMPT)
				.apply(this.allInputsString(inputs))
				.toUserMessage()
				);
		Response<AiMessage> response=llm.generate(messages);
		messages.add(response.content());
		messages.add(UserMessage.from("Please now give me only the final output and nothing else"));
		Response<AiMessage> responsefo=llm.generate(messages);
		return responsefo.content().text();
		
	}
	
	private String allInputsString(List<String> inputs) {
		StringBuilder sb=new StringBuilder();
		
		for(int i=0;i<inputs.size();i++) {
			sb.append("@@Input "+(i+1)+":\n");
			sb.append(inputs.get(i));
			sb.append("\n\n");
		}
		return sb.toString();
	
	}
}
