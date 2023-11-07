import java.awt.*;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JPanel;

public class guiStart 
{
    public guiEmpty frame;
    public JButton start;
    public JPanel buttonPanel;

    public guiStart(guiEmpty e) throws IOException 
    {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.frame = e;
        this.start = new JButton("Inizia una nuova partita");
        this.addButton(this.start, "s");
        this.frame.setVisible(true);
    }

    public void addButton(JButton b, String d)
    {
        buttonPanel.add(b);
        if(d.equals("n"))
            this.frame.add(buttonPanel, BorderLayout.NORTH);
        else if(d.equals("e"))
            this.frame.add(buttonPanel, BorderLayout.EAST);
        else if(d.equals("w"))
            this.frame.add(buttonPanel, BorderLayout.WEST);
        else if(d.equals("s"))
            this.frame.add(buttonPanel, BorderLayout.SOUTH);
    }

}
