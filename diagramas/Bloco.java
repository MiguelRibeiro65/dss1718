public class Bloco {
	
	private String id; 
	private DayOfWeek dia;
	private LocalTime inicio;
	private LocalTime fim;

	public Bloco() {
		id='';
		dia=null;
		inicio=null;
		fim=null;
	}

	public Bloco(String id,DayOfWeek dia,LocalTime inicio,LocalTime fim) {
		this.id = id;
		this.dia = dia;
		this.inicio = inicio;
		this.fim = fim; 
	}

	public Bloco(Bloco b) {
		this.id = b.getID();
		this.dia = b.getDia();
		this.inicio = b.getInicio();
		this.fim = b.getFim();
	}


	public String getID() {
		return this.dia;
	}

	public DayOfWeek getDia() {
		return this.dia;
	}

	public LocalTime getInicio() {
		return this.inicio;
	}

	public LocalTime getFim() {
		return this.fim;
	}

	public void setID(String id) {
		this.id = id;
	}

	public void setDia(DayOfWeek dia) {
		this.dia = dia;
	}

	public void setInicio(LocalTime inicio) {
		this.dia = dia;
	}

	public void setFim(DayOfWeek fim) {
		this.fim = fim;
	}

	public UC clone() {
       return new Bloco(this);
	}

	public boolean equals(Object obj){
    	
    	if (obj == this) return true;
    	if (obj == null || obj.getClass() != this.getClass()) return false;
    	Bloco b = (UC) obj;
    	return b.getID().equals(id)
                        && b.getDia().equals(dia)
                        && b.getInicio().equals(inicio)
    					&& b.getFim().equals(fim);
    }

}
