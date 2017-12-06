public class UC {

	private String id;
	private String name;
	private String acron;

	public UC() {
		id='';
		name='';
		acron='';
	}

	public UC(String id,String name,String acron) {
		this.id = id;
		this.name = name;
		this.acron = acron;
	}

	public UC(UC uc) {
		this.id = uc.getID();
		this.name = uc.getName();
		this.acron = uc.getAcron();
	}

	public getID() {
		return this.id;
	}

	public getName() {
		return this.name;
	}

	public getAcron() {
		return this.acron;
	}

	public void setID(String id) {
        this.id = id;
	}

	public void setName(String name) {
        this.name = name;
	}

	public void setAcron(String acron) {
        this.acron = acron;
	}

	public UC clone() {
       return new UC(this);
	}

	public boolean equals(Object obj){
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    UC u = (UC) obj;
    return u.getID().equals(id)
                        && u.getName().equals(name)
                        && u.getAcron().equals(acron)
    }
} 
