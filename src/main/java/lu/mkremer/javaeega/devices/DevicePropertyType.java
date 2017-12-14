package lu.mkremer.javaeega.devices;

import java.io.Serializable;

public enum DevicePropertyType implements Serializable{
	STRING("Text", "^.+$"),
	BOOLEAN("Yes/No", "^(Yes|yes|No|no)$"),
	NUMBER("Number", "^[\\-]?[0-9]+$"),
	UNUMBER("Positive number", "^[0-9]+$"),
	FLOAT("Floating point number", "^[\\-]?[0-9]+(,[0-9]+)?$"),
	UFLOAT("Positive floating point number", "^[0-9]+(,[0-9]+)?$"),
	SERIAL_NUMER("Serial number", "^([0-9a-zA-Z]+[-])*([0-9a-zA-Z]+)$"),
	MAC_ADDRESS("MAC Address", "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
	
	private final String description, regex;
	
	private DevicePropertyType(String description, String regex) {
		this.description = description;
		this.regex = regex;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getRegEx() {
		return regex;
	}
}
