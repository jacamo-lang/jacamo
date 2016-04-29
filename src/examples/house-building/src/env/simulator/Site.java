package simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class Site implements HousePart {

    public void draw(Dimension size, Graphics2D g){
        g.setColor(Color.GREEN);
        g.fillRect(0, size.height-200, size.width,100);
        g.setColor(Color.BLACK);
        g.drawString("Site OK", 20, size.height-185);
    }

}
