package biblioteca.model;

/**
 * @author Fabricio Souza.
 */
public class Aluno extends Pessoa {
    private int codigo;
    private int turma;
    private String turmaDesc;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getTurma() {
        return turma;
    }

    public void setTurma(int turma) {
        this.turma = turma;
    }

    public String getTurmaDesc() {
        return turmaDesc;
    }

    public void setTurmaDesc(String turma) {
        this.turmaDesc = turma;
    }
}