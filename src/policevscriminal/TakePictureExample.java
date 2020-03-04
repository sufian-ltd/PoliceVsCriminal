/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policevscriminal;

import com.github.sarxos.webcam.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Example of how to take single picture.
 *
 * @author SUFIAN
 */
public class TakePictureExample {

    //public static void main(String[] args) throws IOException {
    PlayerInformation information=new PlayerInformation();
    public String pic(int img)
    {
        String pictureName="src/policevscriminal/test"+img+".png";
        JFrame window = new JFrame();

        JPanel contain = new JPanel();
        contain.setSize(200, 200);
        if(!Webcam.isAutoOpenMode())
            Webcam.setAutoOpenMode(true);
        Dimension[] nonStandardResolutions = new Dimension[]{
            WebcamResolution.VGA.getSize(),
            WebcamResolution.VGA.getSize(),
            new Dimension(200, 200),
            new Dimension(200, 200),};

        final Webcam webcam = Webcam.getDefault();
        System.out.println("Webcam: " + webcam.getName());
        webcam.setCustomViewSizes(nonStandardResolutions);
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        contain.add(panel);

        JButton b = new JButton("   Capture   ");
        window.add(b,BorderLayout.NORTH);
        //contain.add(b);
        b.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage image = webcam.getImage();
                try {
                    ImageIO.write(image, "png", new File(pictureName));
                    
                    JOptionPane.showMessageDialog(null, "Image captured");
                    webcam.close();
                    window.setVisible(false);
                    return;
                    
                    //System.exit(0);
                } catch (IOException ex) {
                    Logger.getLogger(TakePictureExample.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        window.add(contain);

        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        return "test"+img+".png";
//               
    }
    
    
}
