package nl.edubits.cec.model;

import java.util.Arrays;

public enum Device {
	TV(0x0),
	RECORDER1(0x1),
	RECORDER2(0x2),
	TUNER1(0x3),
	PLAYER1(0x4),
	AUDIO(0x5),
	TUNER2(0x6),
	TUNER3(0x7),
	PLAYER2(0x8),
	RECORDER3(0x9),
	TUNER4(0xA),
	PLAYER3(0xB),
	RESERVED1(0xC),
	RESERVED2(0xD),
	FREE(0xE),
	BROADCAST(-1, 0xF),
	UNREGISTERED(0xF, -1),
	UNKNOWN(-1);

	private int initiatorAddress;
	private int destinationAddress;

	Device(int address) {
		this(address, address);
	}

	Device(int initiatorAddress, int destinationAddress) {
		this.initiatorAddress = initiatorAddress;
		this.destinationAddress = destinationAddress;
	}

	public int getInitiatorAddress() {
		return initiatorAddress;
	}

	public int getDestinationAddress() {
		return destinationAddress;
	}

	public static Device deviceByInitiatorAddress(int initiatorAddress) {
		return Arrays.stream(Device.values())
					 .filter(device -> device.initiatorAddress == initiatorAddress)
					 .findFirst()
					 .orElse(UNKNOWN);
	}

	public static Device deviceByDestinationAddress(int destinationAddress) {
		return Arrays.stream(Device.values())
					 .filter(device -> device.destinationAddress == destinationAddress)
					 .findFirst()
					 .orElse(UNKNOWN);
	}
}
