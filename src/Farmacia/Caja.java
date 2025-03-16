package Farmacia;

public class Caja

{
    int idcaja, valor;
    String formato;

    public Caja(int idcaja, String formato, int valor) {
        this.idcaja = idcaja;
        this.formato = formato;
        this.valor = valor;
    }

    public int getIdcaja() {
        return idcaja;
    }

    public void setIdcaja(int idcaja) {
        this.idcaja = idcaja;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}
