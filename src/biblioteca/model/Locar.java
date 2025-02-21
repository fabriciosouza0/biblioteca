package biblioteca.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Fabricio Souza.
 */
public class Locar {
    private long codigo;
    private String livro;
    private long codigoLivro;
    private String cdd;
    private int codigoCdd;
    private String locatario;
    private Date dataParaDevolucao;
    private String atrasado;
    private final SimpleDateFormat sdfBR = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat sdfUSA = new SimpleDateFormat("yyyy-MM-dd");
    private String strDataParaDevolucao;
    private String strDataParaDevolucaoA;
    private Date dataDeLocaCao;
    private String strDataDeLocaCao;
    private String strDataDeLocacaoA;
    private String codigoLocatario;
    private long dias;

    public String getStrDataParaDevolucaoA() {
        return strDataParaDevolucaoA;
    }

    public void setStrDataParaDevolucaoA(String strDataParaDevolucaoA) {
        this.strDataParaDevolucaoA = strDataParaDevolucaoA;
    }

    public String getStrDataDeLocacaoA() {
        return strDataDeLocacaoA;
    }

    public void setStrDataDeLocacaoA(String strDataDeLocacaoA) {
        this.strDataDeLocacaoA = strDataDeLocacaoA;
    }

    public String getStrDataParaDevolucao() {
        return strDataParaDevolucao;
    }

    public void setStrDataParaDevolucao(String strDataParaDevolucao) {
        this.strDataParaDevolucao = strDataParaDevolucao;
    }

    public String getStrDataDeLocaCao() {
        return strDataDeLocaCao;
    }

    public void setStrDataDeLocaCao(String strDataDeLocaCao) {
        this.strDataDeLocaCao = strDataDeLocaCao;
    }

    public long getDias() {
        return dias;
    }

    public void setDias(long dias) {
        if (dias >= 0) {
            this.dias = dias;
        } else {
            this.dias = 0;
        }
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getLivro() {
        return livro;
    }

    public void setLivro(String livro) {
        this.livro = livro;
    }

    public long getCodigoLivro() {
        return codigoLivro;
    }

    public void setCodigoLivro(long codigoLivro) {
        this.codigoLivro = codigoLivro;
    }

    public String getCdd() {
        return cdd;
    }

    public void setCdd(String cdd) {
        this.cdd = cdd;
    }

    public int getCodigoCdd() {
        return codigoCdd;
    }

    public void setCodigoCdd(int codigoCdd) {
        this.codigoCdd = codigoCdd;
    }

    public String getLocatario() {
        return locatario;
    }

    public void setLocatario(String locatario) {
        this.locatario = locatario;
    }

    public Date getDataParaDevolucao() {
        return dataParaDevolucao;
    }

    public void setDataParaDevolucao(Date dataParaDevolucao, long time) {
        Calendar c = Calendar.getInstance();
        String strDate = sdfBR.format(dataParaDevolucao);
        String strAuxD;
        String strAuxM;
        int day = Integer.parseInt(strDate.substring(0, 2)),
                mount = Integer.parseInt(strDate.substring(3, 5)),
                year = Integer.parseInt(strDate.substring(6));
        long newDays = day + time;
        if (newDays > c.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            newDays -= c.getActualMaximum(Calendar.DAY_OF_MONTH);
            mount++;
        }
        if (day <= 9) {
            strAuxD = "0";
        } else {
            strAuxD = "";
        }
        if (mount <= 9) {
            strAuxM = "0";
        } else {
            strAuxM = "";
        }
        this.dataParaDevolucao = dataParaDevolucao;
        try {
            this.dataParaDevolucao = sdfBR.parse(strAuxD + newDays + "/" + strAuxM + mount + "/" + year);
        } catch (ParseException e) {
            System.out.println("Erro ao calcular dias para devolução: " + e.getMessage());
        }
        setStrDataParaDevolucao(sdfBR.format(this.dataParaDevolucao));
        setStrDataParaDevolucaoA(sdfUSA.format(this.dataParaDevolucao));
    }

    public Date getDataDeLocaCao() {
        return dataDeLocaCao;
    }

    public void setDataDeLocaCao(Date dataDeLocaCao) {
        this.dataDeLocaCao = dataDeLocaCao;
        setStrDataDeLocaCao(sdfBR.format(this.dataDeLocaCao));
        setStrDataDeLocacaoA(sdfUSA.format(this.dataDeLocaCao));
    }

    public String getCodigoLocatario() {
        return codigoLocatario;
    }

    public void setCodigoLocatario(String codigoLocatario) {
        this.codigoLocatario = codigoLocatario;
    }

    public String getAtrasado() {
        return atrasado;
    }

    public void setAtrasado(String atrasado) {
        this.atrasado = atrasado;
    }
}