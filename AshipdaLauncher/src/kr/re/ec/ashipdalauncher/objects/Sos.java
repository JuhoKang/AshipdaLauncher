package kr.re.ec.ashipdalauncher.objects;

public class Sos {
	
	private int id;
	private String Name;
	private String PhoneNumber;
	
	public Sos(){
		
	}
	
	public Sos(String Name, String PhoneNumber)
	{
		this.Name = Name;
		this.PhoneNumber = PhoneNumber;
	}
	
	public Sos(int id, String Name, String PhoneNumber)
	{
		this.id = id;
		this.Name = Name;
		this.PhoneNumber = PhoneNumber;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(String PhoneNumber) {
		this.PhoneNumber = PhoneNumber;
	}
	
}
