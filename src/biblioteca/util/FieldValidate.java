package biblioteca.util;

import java.util.function.UnaryOperator;
import javafx.scene.control.TextFormatter;
import com.jfoenix.controls.JFXTextField;

/**
 * @author Fabricio SOuza.
 */
public class FieldValidate {
    public static JFXTextField noCharacter(JFXTextField txf, int length) {
        UnaryOperator<TextFormatter.Change> filter = (TextFormatter.Change t) -> {
            if (t.isReplaced()) {
                if (t.getControlText().matches("^0-9")) {
                    t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));
                }
            }

            if (t.isAdded()) {
                if (t.getText().matches("[^0-9]")) {
                    t.setText("");
                } else if (t.getControlNewText().length() > length) {
                    t.setText("");
                }
            }

            return t;
        };

        txf.setTextFormatter(new TextFormatter<>(filter));
        return txf;
    }

    public static JFXTextField noNumbers(JFXTextField txf) {
        UnaryOperator<TextFormatter.Change> filter = (TextFormatter.Change t) -> {
            if (t.isReplaced()) {
                if (t.getControlText().matches("[^a-zA-Z\\s]")) {
                    t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));
                }
            }

            if (t.isAdded()) {
                t.setText(t.getText().toUpperCase());
                if (t.getText().matches("[^a-zA-Z\\s]")) {
                    t.setText("");
                } else if (t.getControlNewText().length() > 45) {
                    t.setText("");
                }
                if (t.getControlNewText().startsWith(" ")) {
                    t.setText("");
                }
            }

            return t;
        };

        txf.setTextFormatter(new TextFormatter<>(filter));
        return txf;
    }
}