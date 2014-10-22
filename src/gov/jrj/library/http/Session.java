package gov.jrj.library.http;

import android.os.Handler;

public class Session {

	private static Session instance = null;

	private Session() {
	}

	public static void setInstance(Session instance) {
		Session.instance = instance;
	}

	public synchronized static Session getInstance() {
		if (instance == null) {
			instance = new Session();
		}
		synchronized (instance) {
			return instance;
		}

	}

	private Handler handler;

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	private boolean checkedUpdate;

	private boolean chechedProductUpdate;

	public boolean isProductUpdate() {
		return chechedProductUpdate;
	}

	public void setCheckedProduceUpdate(boolean chechedProductUpdate) {
		this.chechedProductUpdate = chechedProductUpdate;
	}

	public boolean isCheckedUpdate() {
		return checkedUpdate;
	}

	public void setCheckedUpdate(boolean checkedUpdate) {
		this.checkedUpdate = checkedUpdate;
	}

	private boolean newMessage;

	public boolean isNewMessage() {
		return newMessage;
	}

	public void setNewMessage(boolean newMessage) {
		this.newMessage = newMessage;
	}

}
