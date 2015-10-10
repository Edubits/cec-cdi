package nl.edubits.cec.model.parameter;

public enum RawParameter implements Parameter {

	RAW(0),
	UNKNOWN(-1);

	private final int id;

	RawParameter(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

}
