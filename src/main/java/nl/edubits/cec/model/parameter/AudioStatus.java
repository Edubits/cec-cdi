package nl.edubits.cec.model.parameter;

import java.util.Arrays;

public enum AudioStatus implements Parameter {
	MUTE_OFF(0x00),
	MUTE_ON(0x01),
	UNKNOWN(-1);

	private final int id;

	AudioStatus(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	public static AudioStatus audioStatusById(int id) {
		return Arrays.stream(AudioStatus.values())
				.filter(status -> status.id == id)
				.findFirst()
				.orElse(UNKNOWN);
	}
}
