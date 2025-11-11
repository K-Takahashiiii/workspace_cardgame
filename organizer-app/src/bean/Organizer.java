package bean;

	//beanのクラスはすべてこの型に従って作ってください！！！！！！！

import java.io.Serializable;

public class Organizer implements Serializable {

    private static final long serialVersionUID = 1L;

    private String	 id;
    private String name;
    private String pass;
    private String tellNum;

    // 引数なしコンストラクタ（JavaBeanの基本）
    public Organizer() {
    }

    // 全部入りコンストラクタ（便利なので残してOK）
    public Organizer(String id, String name, String pass, String tellNum) {
        this.id = id;
        this.name = name;
        this.pass = pass;
        this.tellNum = tellNum;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getTellNum() {
		return tellNum;
	}

	public void setTellNum(String tellNum) {
		this.tellNum = tellNum;
	}


}
