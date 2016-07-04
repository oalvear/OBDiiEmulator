/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.grc.gui.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Joystick extends JPanel {

    private static final long serialVersionUID = 1L;
    //Maximum value for full horiz or vert position where centered is 0:
    private int joyOutputRange;
    private float joySize;     //joystick icon size
    private float joyWidth, joyHeight;
    private float joyCenterX, joyCenterY;  //Joystick displayed Center
    //Display positions for text feedback values:
    private float curJoyAngle;    //Current joystick angle
    private float curJoySize;     //Current joystick size
    private boolean isMouseTracking;
    private boolean leftMouseButton;
    private int mouseX, mouseY;
    private final Stroke lineStroke = new BasicStroke(20, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND);
    private final Point position;

    private JoystickHandler handler;
    
    public Joystick(){
        position = new Point();
    }
    public Joystick(final int joyOutputRange, final int joySize, JoystickHandler handler) {        
        this();
        this.handler = handler;
        this.joyOutputRange = joyOutputRange;
        this.joySize = joySize;
        joyWidth = joySize;
        joyHeight = joyWidth;
        setPreferredSize(new Dimension((int) joyWidth + 20, (int) joyHeight + 20));
        joyCenterX = getPreferredSize().width / 2;
        joyCenterY = getPreferredSize().height / 2;
        this.joySize = joyWidth / 2;
        //setBackground(new Color(226, 226, 226));
        
        //setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        
        MouseAdapter mouseAdapter = new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                leftMouseButton = SwingUtilities.isLeftMouseButton(e);
                mouseCheck(e.getX(),e.getY());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mouseCheck(e.getX(),e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int dx = (int)joyCenterX;
                int dy = (int)joyCenterY;
                mouseCheck(dx, dy);
            }
            
            

        };
        addMouseMotionListener(mouseAdapter);
        addMouseListener(mouseAdapter);
    }

    private void mouseCheck(int mx, int my) {
        mouseX = mx;
        mouseY = my;
        float dx = mouseX - joyCenterX;
        float dy = mouseY - joyCenterY;
        isMouseTracking = leftMouseButton;
        if (isMouseTracking) {
            curJoyAngle = (float) Math.atan2(dy, dx);
            curJoySize = (float) Point.distance(mouseX, mouseY,
                    joyCenterX, joyCenterY);
        } else {
            curJoySize = 0;
        }
        if (curJoySize > joySize) {
            curJoySize = joySize;
        }
        position.x = (int) (joyOutputRange * (Math.cos(curJoyAngle)
                * curJoySize) / joySize);
        position.y = (int) (joyOutputRange * (-(Math.sin(curJoyAngle)
                * curJoySize) / joySize));
        handler.moveEvent(position);
        SwingUtilities.getRoot(Joystick.this).repaint();
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Image img = Toolkit.getDefaultToolkit().getImage(Joystick.class.getResource("images/base.png"));
        g2.drawImage(img, (int) (joyCenterX - joyWidth / 2), 
                (int) (joyCenterY - joyHeight / 2), (int) joyWidth, (int) joyHeight, this);
        Graphics2D g3 = (Graphics2D) g2.create();
        g3.translate(joyCenterX, joyCenterY);
        g3.rotate(curJoyAngle);
        //g3.setColor(new Color(68,68,133));
        g3.setColor(Color.GRAY);
        g3.setStroke(lineStroke);
        g3.drawLine(0, 0, (int) curJoySize, 0);
        g3.drawImage(img, (int) curJoySize -20, -20, 40, 40, this);
        g3.dispose();
    }


}
