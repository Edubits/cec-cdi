package nl.edubits.cec.model.parameter;

public enum MenuState implements Parameter {
	ACTIVATE(0),
	DEACTIVATE(1),
	QUERY(2);

	private final int id;

	MenuState(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

}
