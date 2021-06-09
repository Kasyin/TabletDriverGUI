import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

abstract class FieldsDocumentListener implements DocumentListener {

    @Override
    public void removeUpdate(DocumentEvent e) { }

    @Override
    public void changedUpdate(DocumentEvent e) { }
} 
