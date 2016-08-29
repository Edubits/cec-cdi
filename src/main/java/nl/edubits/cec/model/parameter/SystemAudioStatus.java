package nl.edubits.cec.model.parameter;

import java.util.Arrays;

public enum SystemAudioStatus implements Parameter {
	OFF(0x00),
	ON(0x01),
	UNKNOWN(-1);

	private final int id;

	SystemAudioStatus(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	public static SystemAudioStatus systemAudioStatusById(int id) {
		return Arrays.stream(SystemAudioStatus.values())
				.filter(status -> status.id == id)
				.findFirst()
				.orElse(UNKNOWN);
	}
}
