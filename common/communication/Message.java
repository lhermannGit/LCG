package communication;

import java.util.List;

public class Message {
	
	private List<Action> actions;
	
	public void addAction(Action action) {
		actions.add(action);
	}
	
	public List<Action> getAction() {
		return actions;
	}
}
