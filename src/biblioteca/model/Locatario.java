package biblioteca.model;

/**
 * @author Fabricio Souza.
 */
public class Locatario extends Pessoa {
    private int codigoAluno;
    private int codigoProfessor;

    public int getCodigoAluno() {
        return codigoAluno;
    }

    public void setCodigoAluno(int codigoAluno) {
        this.codigoAluno = codigoAluno;
    }

    public int getCodigoProfessor() {
        return codigoProfessor;
    }

    public void setCodigoProfessor(int codigoProfessor) {
        this.codigoProfessor = codigoProfessor;
    }
}