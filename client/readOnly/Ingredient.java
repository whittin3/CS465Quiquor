package client.readOnly;

/**
 * User: Neal Eric
 * Date: 11/10/13
 */

/**
 * PLEASE DO NOT CHANGE AS IT IS A REQUIRED CLASS TO INTERACT WITH THE BARTENDER
 */
public class Ingredient {
	private final String name;
	private final double density; // In g/mL
	private final boolean carbonated;

	public Ingredient(String name) {
		this(name, 1, false);
	}

	public Ingredient(String name, double density, boolean carbonated) {
		this.name = name;
		this.density = density;
		this.carbonated = carbonated;
	}

	public String getName() {
		return name;
	}

	public double getDensity() {
		return density;
	}

	public boolean isCarbonated() {
		return carbonated;
	}

	// volume is in mL
	public double getMassFromVolume(double volume) {
		return volume * density;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (o.getClass() != getClass())
			return false;

		return ((Ingredient) o).name.equals(name) &&
				((Ingredient) o).density == density &&
				((Ingredient) o).carbonated == carbonated;
	}
}
