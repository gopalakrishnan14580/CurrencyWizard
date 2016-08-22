package sdi.com.currencywizard.model;
 
public class Country {


	/*"symbol": "$",
			"name": "US Dollar",
			"symbol_native": "$",
			"decimal_digits": 2,
			"rounding": 0,
			"code": "USD",
			"name_plural": "US dollars"
	*/
	public String symbol;
	public String name;
	public String symbol_native;
	public String decimal_digits;
	public String rounding;
	public String code;
	public String name_plural;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol_native() {
		return symbol_native;
	}

	public void setSymbol_native(String symbol_native) {
		this.symbol_native = symbol_native;
	}

	public String getDecimal_digits() {
		return decimal_digits;
	}

	public void setDecimal_digits(String decimal_digits) {
		this.decimal_digits = decimal_digits;
	}

	public String getRounding() {
		return rounding;
	}

	public void setRounding(String rounding) {
		this.rounding = rounding;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName_plural() {
		return name_plural;
	}

	public void setName_plural(String name_plural) {
		this.name_plural = name_plural;
	}
}