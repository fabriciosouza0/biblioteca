package biblioteca.controller;

import biblioteca.dao.AlunosDAO;
import biblioteca.dao.LivroDAO;
import biblioteca.dao.ProfessoresDAO;
import biblioteca.model.Aluno;
import biblioteca.model.Livro;
import biblioteca.model.Locar;
import biblioteca.model.Professor;
import biblioteca.util.FieldValidate;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * @author Fabricio Souza.
 */
public class FXMLCadLocarController implements Initializable {

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private ObservableList<Professor> obPlist;
    private ObservableList<Aluno> obAlist;
    private ObservableList<Livro> obLVlist;
    private Thread th;
    private final JFXSpinner spinner = new JFXSpinner();

    private Locar locar = new Locar();
    private Professor professor = new Professor();
    private final ProfessoresDAO professorDao = new ProfessoresDAO();
    private Aluno aluno = new Aluno();
    private final AlunosDAO alunoDao = new AlunosDAO();
    private Livro livro = new Livro();
    private final LivroDAO livroDao = new LivroDAO();

    @FXML
    private TableView<Livro> tbLivro;
    @FXML
    private TableColumn<Livro, String> tbcLivro;
    @FXML
    private TableColumn<Livro, String> tbcCodigo;
    @FXML
    private TableColumn<Livro, Long> tbcQtd;
    @FXML
    private TableColumn<Livro, String> tbcAutor;
    @FXML
    private TableView<Aluno> tbAluno;
    @FXML
    private TableColumn<Aluno, String> tbcNomeAl;
    @FXML
    private TableColumn<Aluno, String> tbcCpfAl;
    @FXML
    private TableColumn<Aluno, String> tbcTurma;
    @FXML
    private TableView<Professor> tbProfessor;
    @FXML
    private TableColumn<Professor, String> tbcCpfPf;
    @FXML
    private TableColumn<Professor, String> tbcNomePf;
    @FXML
    private JFXComboBox<String> cbPesquisa;
    @FXML
    private JFXComboBox<Livro> cbLivro;
    @FXML
    private JFXComboBox<Aluno> cbAluno;
    @FXML
    private JFXComboBox<Professor> cbProfessor;

    @FXML
    private JFXTextField tfPesquisa;
    @FXML
    private JFXTextField tfDiasParaDevolucao;
    @FXML
    private JFXRadioButton rdAluno;
    @FXML
    private JFXRadioButton rdProfessor;
    private final ToggleGroup slAlunoProfessor = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tbProfessor.setVisible(false);
        tbAluno.setVisible(false);
        slAlunoProfessor.getToggles().add(rdAluno);
        slAlunoProfessor.getToggles().add(rdProfessor);
        showTableViewLivro(1);
        //Carregamento dos itens das combobox.
        converteComboBox();
        livroDao.pesquisar(livro).forEach((li) -> {
            cbLivro.getItems().add(li);
        });
        alunoDao.getStudents(aluno).forEach((al) -> {
            cbAluno.getItems().add(al);
        });
        professorDao.readerPf(professor).forEach((pf) -> {
            cbProfessor.getItems().add(pf);
        });

        cbPesquisa.getItems().add("Livros");
        cbPesquisa.setValue("Livros");
        cbPesquisa.setOnAction((ActionEvent event) -> {
            if (cbPesquisa.getValue() != null)
                modoDePesquisa(cbPesquisa.getValue());
        });
        spinner.setMinHeight(40);
        spinner.setMaxHeight(40);
        spinner.setMinWidth(40);
        spinner.setMaxWidth(40);

        tbLivro.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewLivro(newValue));
        tbAluno.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewAluno(newValue));
        tbProfessor.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewProfessor(newValue));

        addItem();
        mask();
    }

    public Locar getLocar() {
        return this.locar;
    }

    public void setLocar(Locar l, int num) {
        this.locar = l;
        if (num == 1) {

        } else {

        }
    }

    @FXML
    public void handleButtonConfirmar() throws ParseException {
        if (validaEntradaDeDados()) {
            Date data = new Date();
            buttonConfirmarClicked = true;
            locar.setDataDeLocaCao(data);
            locar.setDias(Integer.parseInt(tfDiasParaDevolucao.getText()));
            locar.setDataParaDevolucao(locar.getDataDeLocaCao(), locar.getDias());
            locar.setCodigoLivro(cbLivro.getValue().getCodigo());
            if (rdProfessor.isSelected()) {
                locar.setCodigoLocatario(cbProfessor.getValue().getCpf());
            } else if (rdAluno.isSelected()) {
                locar.setCodigoLocatario(cbAluno.getValue().getCpf());
            }
            dialogStage.close();
        }
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    public void pesquisar() {
        if (!th.isAlive()) {
            String strParam = cbPesquisa.getValue();
            if (strParam != null) {
                if (tbLivro.isVisible()) {
                    livro.setTitulo(tfPesquisa.getText());
                    showTableViewLivro(2);
                }
                if (tbAluno.isVisible()) {
                    aluno.setNome(tfPesquisa.getText());
                    showTableViewAluno(2);
                }
                if (tbProfessor.isVisible()) {
                    professor.setNome(tfPesquisa.getText());
                    showTableViewProfessor(2);
                }
            }
        }
    }

    public void modoDePesquisa(String value) {
        switch (value) {
            case "Livros":
                showTableViewLivro(1);
                if (!tbLivro.isVisible()) {
                    tbLivro.setVisible(true);
                }
                if (tbAluno.isVisible()) {
                    tbAluno.setVisible(false);
                }
                if (tbProfessor.isVisible()) {
                    tbProfessor.setVisible(false);
                }
                break;
            case "Alunos":
                showTableViewAluno(1);
                if (!tbAluno.isVisible()) {
                    tbAluno.setVisible(true);
                }
                if (tbLivro.isVisible()) {
                    tbLivro.setVisible(false);
                }
                if (tbProfessor.isVisible()) {
                    tbProfessor.setVisible(false);
                }
                break;
            case "Professores":
                showTableViewProfessor(1);
                if (!tbProfessor.isVisible()) {
                    tbProfessor.setVisible(true);
                }
                if (tbLivro.isVisible()) {
                    tbLivro.setVisible(false);
                }
                if (tbAluno.isVisible()) {
                    tbAluno.setVisible(false);
                }
                break;
        }
    }

    @FXML
    public void selected() {
        if (rdAluno.isSelected()) {
            if (!cbAluno.isVisible()) {
                cbAluno.setVisible(true);
            }
            if (cbProfessor.isVisible()) {
                cbProfessor.setVisible(false);
            }
        } else if (rdProfessor.isSelected()) {
            if (!cbProfessor.isVisible()) {
                cbProfessor.setVisible(true);
            }
            if (cbAluno.isVisible()) {
                cbAluno.setVisible(false);
            }
        }
        addItem();
    }

    private void addItem() {
        if (rdAluno.isSelected()) {
            cbPesquisa.getItems().add("Alunos");
            if (cbPesquisa.getItems().contains("Professores")) {
                cbPesquisa.getItems().remove("Professores");
            }
        } else if (rdProfessor.isSelected()) {
            cbPesquisa.getItems().add("Professores");
            if (cbPesquisa.getItems().contains("Alunos")) {
                cbPesquisa.getItems().remove("Alunos");
            }
        }
    }

    //Chamada da classe biblioteca.util.Validate.
    public void mask() {
        //Metodo que mascara o field text.
        FieldValidate.noNumbers(tfPesquisa);
        FieldValidate.noCharacter(tfDiasParaDevolucao, 2);
    }

    //Preenche a table de Professores.
    public void showTableViewProfessor(int num) {
        if (!tbProfessor.getItems().isEmpty()) {
            tbProfessor.getItems().clear();
        }
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    tbProfessor.setPlaceholder(spinner);
                });
                tbcCpfPf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
                tbcNomePf.setCellValueFactory(new PropertyValueFactory<>("nome"));
                if (num == 1) {
                    obPlist = FXCollections.observableArrayList(professorDao.readerPf(professor));
                } else if (num == 2) {
                    obPlist = FXCollections.observableArrayList(professorDao.pesquisaEspecifica(professor));
                }
                tbProfessor.setItems(obPlist);
                Platform.runLater(() -> {
                    tbProfessor.setPlaceholder(new Label("Nenhum registro encontrado"));
                });
                return null;
            }
        };
        th = new Thread(task);
        th.start();
    }

    //Preenche a table de Alunos.
    public void showTableViewAluno(int num) {
        if (!tbAluno.getItems().isEmpty()) {
            tbAluno.getItems().clear();
        }
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    tbAluno.setPlaceholder(spinner);
                });
                tbcCpfAl.setCellValueFactory(new PropertyValueFactory<>("cpf"));
                tbcNomeAl.setCellValueFactory(new PropertyValueFactory<>("nome"));
                tbcTurma.setCellValueFactory(new PropertyValueFactory<>("turma_Desc"));
                if (num == 1) {
                    obAlist = FXCollections.observableArrayList(alunoDao.getStudents(aluno));
                } else if (num == 2) {
                    obAlist = FXCollections.observableArrayList(alunoDao.findStudent(aluno));
                }
                tbAluno.setItems(obAlist);
                Platform.runLater(() -> {
                    tbAluno.setPlaceholder(new Label("Nenhum registro encontrado"));
                });
                return null;
            }
        };
        th = new Thread(task);
        th.start();
    }

    //Preenche a table de livros.
    public void showTableViewLivro(int num) {
        if (!tbLivro.getItems().isEmpty()) {
            tbLivro.getItems().clear();
        }

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    tbLivro.setPlaceholder(spinner);
                });
                tbcCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
                tbcQtd.setCellValueFactory(new PropertyValueFactory<>("qtd"));
                tbcLivro.setCellValueFactory(new PropertyValueFactory<>("titulo"));
                tbcAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
                if (num == 1) {
                    obLVlist = FXCollections.observableArrayList(livroDao.pesquisar(livro));
                } else if (num == 2) {
                    obLVlist = FXCollections.observableArrayList(livroDao.pesquisaEspecifica(livro));
                }

                Platform.runLater(() -> {
                    tbLivro.setPlaceholder(new Label("Nenhum registro encontrado"));
                });

                if (obLVlist.isEmpty()) return null;

                FilteredList<Livro> filteredList = new FilteredList<>(obLVlist, livro -> livro.getQtd() > 0);

                if (filteredList.isEmpty()) {
                    Platform.runLater(() -> {
                        tbLivro.setPlaceholder(new Label("Nenhum registro encontrado"));
                    });
                    return null;
                }

                tbLivro.setItems(filteredList);
                return null;
            }
        };
        th = new Thread(task);
        th.start();
    }

    //Validação dos dados.
    private boolean validaEntradaDeDados() {
        String errorMenssage = "";
        if (cbLivro.getValue() == null) {
            errorMenssage = "Livro inválido";
            this.cbLivro.requestFocus();
        } else if (cbAluno.getValue() == null && cbAluno.isVisible()) {
            errorMenssage = "Aluno inválido";
            this.cbAluno.requestFocus();
        } else if (cbProfessor.getValue() == null && cbProfessor.isVisible()) {
            errorMenssage = "Professor inválido";
            this.cbProfessor.requestFocus();
        } else if (tfDiasParaDevolucao.getText().equals("") || tfDiasParaDevolucao.getText().startsWith(" ")) {
            errorMenssage = "Prazo para devolução inválido";
            this.tfDiasParaDevolucao.clear();
            this.tfDiasParaDevolucao.requestFocus();
        }

        if (errorMenssage.isEmpty()) return true;

        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/biblioteca/util/style/imgs/icon.png"));
        alert.setTitle("Erro na locação");
        alert.setHeaderText("Campos inválidos, por favor, corrija...");
        alert.setContentText(errorMenssage);
        alert.show();

        return false;
    }

    public void selecionarItemTableViewLivro(Livro newValue) {
        Livro livro = tbLivro.getSelectionModel().getSelectedItem();
        if (livro != null)
            cbLivro.getItems().stream().filter((l) -> (livro.getCodigo() == l.getCodigo())).forEachOrdered((l) -> {
                cbLivro.setValue(l);
            });
    }

    public void selecionarItemTableViewAluno(Aluno newValue) {
        Aluno aluno = tbAluno.getSelectionModel().getSelectedItem();
        if (aluno != null)
            cbAluno.getItems().stream().filter((a) -> (aluno.getCpf().equals(a.getCpf()))).forEachOrdered((a) -> {
                cbAluno.setValue(a);
            });
    }

    public void selecionarItemTableViewProfessor(Professor newValue) {
        Professor professor = tbProfessor.getSelectionModel().getSelectedItem();
        if (professor != null)
            cbProfessor.getItems().stream().filter((p) -> (professor.getCpf().equals(p.getCpf()))).forEachOrdered((p) -> {
                cbProfessor.setValue(p);
            });
    }

    //Faz a conversão e mostra apenas o que interessa para o usuario.
    private void converteComboBox() {
        cbLivro.setConverter(new StringConverter<Livro>() {
            @Override
            public String toString(Livro livro) {
                if (livro != null) {
                    return livro.getTitulo();
                }
                return null;
            }

            @Override
            public Livro fromString(String string) {
                return null;
            }
        });
        cbAluno.setConverter(new StringConverter<Aluno>() {
            @Override
            public String toString(Aluno aluno) {
                if (aluno != null) {
                    return aluno.getNome();
                }
                return null;
            }

            @Override
            public Aluno fromString(String string) {
                return null;
            }
        });
        cbProfessor.setConverter(new StringConverter<Professor>() {
            @Override
            public String toString(Professor professor) {
                if (professor != null) {
                    return professor.getNome();
                }
                return null;
            }

            @Override
            public Professor fromString(String string) {
                return null;
            }
        });
    }

}