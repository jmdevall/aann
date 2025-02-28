package jmdevall.aann;

import java.util.ArrayList;
import java.util.List;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import lombok.Getter;

/**
 * This class represents a conversation window. The LLM has a context size limit. With this class, 
 * we control the initiation of new "conversation rounds" in such a way that if a critical point is reached, older rounds are deleted.
 */
public class WindowConversation {
	
	private ChatLanguageModel llm;
	private List<Round> rounds=new ArrayList<Round>();
	private int maxTokens=25000;
	
	public static WindowConversation newConversation(ChatLanguageModel llm) {
		WindowConversation c=new WindowConversation();
		c.llm=llm;
		c.rounds.add(Round.newFijo());
		return c;
	}
	
	public WindowConversation clone() {
		WindowConversation c=new WindowConversation();
		c.llm=this.llm;
		c.rounds=new ArrayList<Round>(this.rounds);
		return c;
	}
	
	public ChatLanguageModel getLlm() {
		return this.llm;
	}
	
	private Round getCurrentRound() {
		return this.rounds.get(this.rounds.size()-1);
	}
	
	public void startNewRound() {
		purge();
		this.rounds.add(Round.newDescartable());
	}
	
	public void add(ChatMessage chatMessage){
		this.getCurrentRound().add(chatMessage);
	}
	
	Response<AiMessage> generateConversation(){
		
		List<ChatMessage> messages=new ArrayList<>();
		
		for(Round r:rounds) {
			messages.addAll(r.getMessages());
		}
		
		Response<AiMessage> response=this.llm.generate(messages);
		this.getCurrentRound().addTokens(response.tokenUsage().totalTokenCount());
		return response;
	}
	
	private void removeFirstRoundDescartable() {
		for(int r=0;r<rounds.size();r++) {
			if(rounds.get(r).isDescartable()) {
				this.rounds.remove(r);
				return;
			}
		}
	}
	
	private void purge() {
		while(this.totalTokenCount()>maxTokens) {
			System.out.println("llegando al limite de ventana, se borran una ronda");
			removeFirstRoundDescartable();
		}
	}
	
	private int totalTokenCount() {
		int suma=0;
		for(Round r:rounds) {
			suma+=r.getTokens();
		}
		return suma;
	}
	
	
	@Getter
	private static final class Round{
		public static Round newDescartable() {
			Round r=new Round();
			r.descartable=true;
			return r;
		}

		public static Round newFijo() {
			Round r=new Round();
			r.descartable=false;
			return r;
		}

		private boolean descartable;
		private int tokens=0;
		private List<ChatMessage> messages=new ArrayList<>();
		
		public void addTokens(int numTokens){
			this.tokens+=numTokens;
		}
		
		public void add(ChatMessage chatMessage) {
			this.messages.add(chatMessage);
		}
	}
}
