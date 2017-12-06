public class Turno {

	private String id;
	private int tipo; // 1 teorico 2 pratico
	private Bloco data;

	public Turno() {
		id='';
		tipo=0;
		data=null;
	}

	public Turno(String id,int tipo,Bloco data) {
		this.id = id;
		this.tipo = tipo;
		this.data = data; 
	}

	public Turno(Turno t) {
		this.id = t.getID();
		this.tipo = t.getTipo();
		this.data = t.getData();
	}


	public String getID() {
		return this.id;
	}

	public int getTipo() {
		return this.tipo;
	}

	public Bloco getData() {
		return this.data.clone();
	}

	public void setID(String id) {
		this.id = id;
	} 

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public void setData(Bloco b) {
		this.data = b;
	}

	public Turno clone() {
       return new Turno(this);
	}

	public boolean equals(Object obj){
    	
    	if (obj == this) return true;
    	if (obj == null || obj.getClass() != this.getClass()) return false;
    	Turno t = (Turno) obj;
    	return t.getID().equals(id)
                        && t.getTipo().equals(tipo)
                        && b.getData().equals(data);
    }


}