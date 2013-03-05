package hu.promarkvf.besttest;

public class Inforec {
	private String id;
	private long user_id;
	private String name;
	private String rname;
	private String key;
	private String leiras;
	private String datum;

	public Inforec(String id, long user_id, String name, String rname, String key, String leiras, String datum) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.name = name;
		this.rname = rname;
		this.key = key;
		this.leiras = leiras;
		this.datum = datum;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getRname() {
		return rname;
	}

	public String getKey() {
		return key;
	}

	public String getLeiras() {
		return leiras;
	}

	public String getDatum() {
		return datum;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setLeiras(String leiras) {
		this.leiras = leiras;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

}
