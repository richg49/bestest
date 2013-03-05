package hu.promarkvf.besttest;

public class User {

	private String name;
	private String rname;
	private String key;
	private Long dbid;

	/**
	 * @param name
	 * @param rname
	 * @param key
	 * @param dbid
	 */
	public User(String name, String rname, String key, Long dbid) {
		super();
		this.name = name;
		this.rname = rname;
		this.key = key;
		this.dbid = dbid;
	}

	public User() {
		super();
		this.name = "";
		this.rname = "";
		this.key = "";
		this.dbid = (long) 0;
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

	public Long getDbid() {
		return dbid;
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

	public void setDbid(Long dbid) {
		this.dbid = dbid;
	}

}
