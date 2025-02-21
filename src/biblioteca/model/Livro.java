package biblioteca.model;

/**
 * @author Fabricio Souza.
 */
public class Livro {
    private long codigo;
    private String titulo;
    private String autor;
    private String CDD;
    private long qtd;
    private long codigoAutor;
    private long codigoCDD;

    public long getCodigoAutor() {
        return codigoAutor;
    }

    public void setCodigoAutor(long codigoAutor) {
        this.codigoAutor = codigoAutor;
    }

    public long getCodigoCDD() {
        return codigoCDD;
    }

    public void setCodigoCDD(long codigoCDD) {
        this.codigoCDD = codigoCDD;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCDD() {
        return CDD;
    }

    public void setCDD(String CDD) {
        this.CDD = CDD;
    }

    public long getQtd() {
        return qtd;
    }

    public void setQtd(long qtd) {
        this.qtd = qtd;
    }
}