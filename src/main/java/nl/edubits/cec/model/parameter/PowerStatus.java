package nl.edubits.cec.model.parameter;

import java.util.Arrays;

public enum PowerStatus implements Parameter {
	ON(0x00),
	STANDBY(0x01),
	IN_TRANSITION_TO_ON(0x02),
	IN_TRANSITION_TO_STANDBY(0x03),
	UNKNOWN(-1);

	private final int id;

	PowerStatus(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	public static PowerStatus powerStatusById(int id) {
		return Arrays.stream(PowerStatus.values())
				 	 .filter(status -> status.id == id)
				 	 .findFirst()
				 	 .orElse(UNKNOWN);
	}
}
