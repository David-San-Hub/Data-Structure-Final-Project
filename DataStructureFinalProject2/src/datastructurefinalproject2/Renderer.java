package datastructurefinalproject2;

import java.awt.Graphics;
import javax.swing.JPanel;

public class Renderer extends JPanel { //this class is for rendering that helps buffering
    
    private static final long serialVersionUID = 1L; //this is for declaring "static final serialVersionUID"
    
    @Override //method that retain super.paintComponent
    protected void paintComponent(Graphics g) {
        
            super.paintComponent(g); //draw something on JPanel other than drawing the background color

            DataStructureFinalProject2.FlappyBird.repaint(g); //performs a request to erase and perform redraw of the component after a small delay in time
	}
}