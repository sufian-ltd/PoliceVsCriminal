/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policevscriminal;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javazoom.jl.player.Player;

/**
 *
 * @author SUFIAN
 */
public class BoardJFrame extends javax.swing.JFrame implements ActionListener
{

    /**
     * Creates new form PlayerInfoJFrame
     */
    JButton[] button=new JButton[4];
    JTextField[] field=new JTextField[4];
    JPanel panel[]=new JPanel[4];
    ArrayList<Integer> cardIndex=new ArrayList<Integer>(4);
    ArrayList<Integer> nameIndex=new ArrayList<Integer>(4);
    String playerNames[]=new String[4];
    String card[]={"বাবু","ডাকাত","চোর","পুলিশ"};
    String emptyPlayerNames[]={"১ম কম্পিউটার","২য় কম্পিউটার","৩য় কম্পিউটার","৪র্থ কম্পিউটার"};
    int policeTask=2;
    Color []colors={Color.ORANGE,Color.RED,Color.BLUE,Color.BLUE};
    boolean yes=false;
    int score[]={0,0,0,0};
    int point[]={12,8,4,10};
    int police;
    String insertDate="";
    /*database connection*/
    Connection con=null;
    PreparedStatement pstmt=null;
    Icon []icon=new ImageIcon[4];
    boolean snd=true;
    public BoardJFrame() {
        initComponents();
        getContentPane().setBackground(Color.BLACK);
        initializeComponent();
        for(int i=0;i<4;i++)
        { 
            cardIndex.add(i);     
            nameIndex.add(i);
        }
        dateCalculation();
        
        openDB();
        playSound("C:\\Users\\pavilion\\Desktop\\PoliceVsCriminal\\src\\policevscriminal\\toStartShuffleButtonClick.mp3");
    }
    public void addImageFromUser(Icon ic[])
    {
        icon=ic;
    }
    public void enableSound(boolean s)
    {
        snd=s;
    }
    private void openDB() {
        Connection c;
        Statement stmt;
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:insertscore.db");


            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS scoretable ("
                    
                    + "name VARCHAR( 100 ) NOT NULL ,"
                    + "score VARCHAR( 100 ) NOT NULL ,"
                    + "date VARCHAR( 50 ) NOT NULL"
                    + ")";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
            System.out.println("created");
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "ERROR:" + e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void insertData()
    {
        for(int i=0;i<4;i++)
        {
            Connection c;
            PreparedStatement stmt;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:insertscore.db");

                stmt = c.prepareStatement("INSERT INTO scoretable (name,score,date)"
                        + "VALUES (?,?,?)");
                stmt.setString(1, playerNames[i]);
                stmt.setString(2, score[i]+"");
                stmt.setString(3, insertDate);
                stmt.executeUpdate();
                stmt.close();
                c.commit();
                c.close();
                System.out.println("inserted");
                JOptionPane.showMessageDialog(null, "Record successfully inserted", "Database Status", JOptionPane.PLAIN_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR:" + e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void dateCalculation()
    {
        
        Calendar cal;
        cal = Calendar.getInstance();
        SimpleDateFormat sdf;   
        sdf = new SimpleDateFormat();
        insertDate=sdf.format(cal.getTime());

    }
    public void initializeComponent()
    {
        button[0]=firstPlayerButton;
        button[1]=secondPlayerButton;
        button[2]=thirdPlayerButton;
        button[3]=fourthPlayerButton;
        field[0]=firstPlayerTextField;
        field[1]=secondPlayerTextField;
        field[2]=thirdPlayerTextField;
        field[3]=fourthPlayerTextField;
        panel[0]=firstPanel;
        panel[1]=secondPanel;
        panel[2]=thirdPanel;
        panel[3]=fourthPanel;
        for(int i=0;i<4;i++)
            button[i].setBackground(Color.BLACK);
        
    }
    public void initializeNameAndPicture(String str[])
    {
        playerNames=str;
        for(int i=0;i<4;i++)
        {
            if(playerNames[i].isEmpty())
            {
                playerNames[i]=emptyPlayerNames[i]+" :";
            }
        }
    }
    public void setNamePicture()
    {      
        
        for(int i=0;i<4;i++)
        {
            panel[i].setBackground(colors[nameIndex.get(1)]);
            field[i].setBackground(Color.BLACK);
            field[i].setForeground(Color.WHITE);
        }        
        police();
        
        for(int i=0;i<4;i++)
        {
            score[nameIndex.get(i)]+=point[cardIndex.get(i)];
            button[i].setIcon(icon[nameIndex.get(i)]);
            if(cardIndex.get(i)==1 || cardIndex.get(i)==2)
            {
                field[i].setText(playerNames[nameIndex.get(i)]+" এর কার্ড "+"ক্রিমিনাল");  
                panel[i].setBackground(Color.RED);
                field[i].setForeground(Color.red);
                field[i].setBackground(Color.black);
                
            }            
            else
            {
                field[i].setText(playerNames[nameIndex.get(i)]+" এর কার্ড "+card[cardIndex.get(i)]);
                panel[i].setBackground(colors[cardIndex.get(i)]);
                if(cardIndex.get(i)==3)
                    police=nameIndex.get(i);
            }
            if(cardIndex.get(i)==3 && playerNames[nameIndex.get(i)].contains("System "))
            {
                Random random=new Random();
                int value=1+random.nextInt(1);
                if(value==policeTask)
                {
                    resultTextArea.setText("\n\n   "+emptyPlayerNames[nameIndex.get(i)]+"  সঠিকভাবে ক্রিমিনাল\n\n   ধরতে পেরেছেন.....\n\n   পুণরায় কার্ঢ বাটন \n\n   ক্লিক করুন");
                }
                else
                {
                    resultTextArea.setText("\n\n   "+emptyPlayerNames[nameIndex.get(i)]+"  সঠিকভাবে ক্রিমিনাল\n\n   ধরতে ব্যর্থ হয়েছে.....\n\n   পুণরায় কার্ঢ বাটন \n\n   ক্লিক করুন");
                }
                yes=false;                
            }
            button[i].addActionListener(this);
        }
    }
    public void actionPerformed(ActionEvent e)
    {
        if(yes)
        {
            for(int i=0;i<4;i++)
            {
                if(e.getSource()==button[i])
                {
                    if( cardIndex.get(i)==policeTask)
                    {
                        resultTextArea.setText("\n\n   আপনি সঠিকভাবে \n\n   ক্রিমিনাল ধরতে \n\n   পেরেছেন");
                        playSound("C:\\Users\\pavilion\\Desktop\\PoliceVsCriminal\\src\\policevscriminal\\successToFind.mp3");
                        score[nameIndex.get(i)]-=point[nameIndex.get(i)];
                    }
                    else if(cardIndex.get(i)!=0 && cardIndex.get(i)!=3)
                    {
                        resultTextArea.setText("\n\n    আপনি সঠিকভাবে \n\n    ক্রিমিনাল  ধরতে \n\n    ব্যর্থ হয়েছেন");
                        playSound("C:\\Users\\pavilion\\Desktop\\PoliceVsCriminal\\src\\policevscriminal\\failToFind.mp3");
                        score[police]-=10;
                    }
                    else
                    {
                        resultTextArea.setText("\n\n   আপনি ভুল বাটনে\n\n   ক্লিক করেছেন ");
                        playSound("C:\\Users\\pavilion\\Desktop\\PoliceVsCriminal\\src\\policevscriminal\\wrongButtonClick.mp3");
                        //playSound("C:\\Users\\AKASH\\Desktop\\Police Vs Criminal\\PoliceVsCriminal\\src\\policevscriminal\\wrongButtonClick.mp3");
                    }
                    yes=false;
                    break;
                }
            } 
        }
    }
    public void police()
    {
        resultTextArea.setText("\n\n   এখন আপনার \n\n"+"    "+card[policeTask]+" কে\n\n    ধরতে হবে");
        if(policeTask==1)
        {
            playSound("C:\\Users\\pavilion\\Desktop\\PoliceVsCriminal\\src\\policevscriminal\\findDakath.mp3");
        }
            // playSound("C:\\Users\\AKASH\\Desktop\\Police Vs Criminal\\PoliceVsCriminal\\src\\policevscriminal\\findDakath.mp3");
        else if(policeTask==2)
        {
            playSound("C:\\Users\\pavilion\\Desktop\\PoliceVsCriminal\\src\\policevscriminal\\findChor.mp3");
        } 
            //playSound("C:\\Users\\AKASH\\Desktop\\Police Vs Criminal\\PoliceVsCriminal\\src\\policevscriminal\\findChor.mp3");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        secondPanel = new javax.swing.JPanel();
        secondPlayerButton = new javax.swing.JButton();
        secondPlayerTextField = new javax.swing.JTextField();
        thirdPanel = new javax.swing.JPanel();
        thirdPlayerTextField = new javax.swing.JTextField();
        thirdPlayerButton = new javax.swing.JButton();
        fourthPanel = new javax.swing.JPanel();
        fourthPlayerButton = new javax.swing.JButton();
        fourthPlayerTextField = new javax.swing.JTextField();
        firstPanel = new javax.swing.JPanel();
        firstPlayerButton = new javax.swing.JButton();
        firstPlayerTextField = new javax.swing.JTextField();
        shuffleButton = new javax.swing.JButton();
        endGameButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("পুলিশ বনাম চোর");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusTraversalPolicyProvider(true);
        setMinimumSize(null);
        setResizable(false);

        secondPanel.setBackground(new java.awt.Color(28, 8, 111));
        secondPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 4, true));
        secondPanel.setPreferredSize(new java.awt.Dimension(448, 285));
        secondPanel.setRequestFocusEnabled(false);

        secondPlayerButton.setAutoscrolls(true);
        secondPlayerButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.LOG_OPTION);
        secondPlayerButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        secondPlayerButton.setIconTextGap(0);
        secondPlayerButton.setRequestFocusEnabled(false);

        secondPlayerTextField.setEditable(false);
        secondPlayerTextField.setFont(new java.awt.Font("SutonnyOMJ", 1, 24)); // NOI18N
        secondPlayerTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        secondPlayerTextField.setText("************");
        secondPlayerTextField.setRequestFocusEnabled(false);

        javax.swing.GroupLayout secondPanelLayout = new javax.swing.GroupLayout(secondPanel);
        secondPanel.setLayout(secondPanelLayout);
        secondPanelLayout.setHorizontalGroup(
            secondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(secondPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(secondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(secondPlayerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(secondPlayerTextField))
                .addContainerGap())
        );
        secondPanelLayout.setVerticalGroup(
            secondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(secondPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(secondPlayerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(secondPlayerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        thirdPanel.setBackground(new java.awt.Color(0, 102, 0));
        thirdPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 4, true));
        thirdPanel.setPreferredSize(new java.awt.Dimension(448, 285));

        thirdPlayerTextField.setEditable(false);
        thirdPlayerTextField.setFont(new java.awt.Font("SutonnyOMJ", 1, 24)); // NOI18N
        thirdPlayerTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        thirdPlayerTextField.setText("************");
        thirdPlayerTextField.setRequestFocusEnabled(false);

        thirdPlayerButton.setToolTipText("");
        thirdPlayerButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.LOG_OPTION);
        thirdPlayerButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        thirdPlayerButton.setRequestFocusEnabled(false);

        javax.swing.GroupLayout thirdPanelLayout = new javax.swing.GroupLayout(thirdPanel);
        thirdPanel.setLayout(thirdPanelLayout);
        thirdPanelLayout.setHorizontalGroup(
            thirdPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(thirdPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(thirdPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(thirdPlayerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(thirdPlayerTextField))
                .addContainerGap())
        );
        thirdPanelLayout.setVerticalGroup(
            thirdPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(thirdPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(thirdPlayerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(thirdPlayerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        fourthPanel.setBackground(new java.awt.Color(255, 102, 0));
        fourthPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 4, true));
        fourthPanel.setPreferredSize(new java.awt.Dimension(448, 285));
        fourthPanel.setRequestFocusEnabled(false);

        fourthPlayerButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.LOG_OPTION);
        fourthPlayerButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fourthPlayerButton.setRequestFocusEnabled(false);

        fourthPlayerTextField.setEditable(false);
        fourthPlayerTextField.setFont(new java.awt.Font("SutonnyOMJ", 1, 24)); // NOI18N
        fourthPlayerTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fourthPlayerTextField.setText("**********");
        fourthPlayerTextField.setRequestFocusEnabled(false);

        javax.swing.GroupLayout fourthPanelLayout = new javax.swing.GroupLayout(fourthPanel);
        fourthPanel.setLayout(fourthPanelLayout);
        fourthPanelLayout.setHorizontalGroup(
            fourthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fourthPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fourthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fourthPlayerTextField)
                    .addComponent(fourthPlayerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        fourthPanelLayout.setVerticalGroup(
            fourthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fourthPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fourthPlayerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fourthPlayerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        firstPanel.setBackground(new java.awt.Color(153, 0, 153));
        firstPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 4, true));
        firstPanel.setPreferredSize(new java.awt.Dimension(448, 285));

        firstPlayerButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.LOG_OPTION);
        firstPlayerButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        firstPlayerButton.setRequestFocusEnabled(false);

        firstPlayerTextField.setEditable(false);
        firstPlayerTextField.setFont(new java.awt.Font("SutonnyOMJ", 1, 24)); // NOI18N
        firstPlayerTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        firstPlayerTextField.setText("************");
        firstPlayerTextField.setRequestFocusEnabled(false);

        javax.swing.GroupLayout firstPanelLayout = new javax.swing.GroupLayout(firstPanel);
        firstPanel.setLayout(firstPanelLayout);
        firstPanelLayout.setHorizontalGroup(
            firstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(firstPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(firstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(firstPlayerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(firstPlayerTextField))
                .addContainerGap())
        );
        firstPanelLayout.setVerticalGroup(
            firstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(firstPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(firstPlayerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(firstPlayerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        firstPlayerButton.getAccessibleContext().setAccessibleParent(firstPlayerButton);

        shuffleButton.setBackground(new java.awt.Color(204, 0, 0));
        shuffleButton.setFont(new java.awt.Font("SutonnyOMJ", 1, 36)); // NOI18N
        shuffleButton.setForeground(new java.awt.Color(255, 255, 255));
        shuffleButton.setText("কার্ড চালুন");
        shuffleButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
        shuffleButton.setBorderPainted(false);
        shuffleButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.BUFFERED_OPTION);
        shuffleButton.setRequestFocusEnabled(false);
        shuffleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shuffleButtonActionPerformed(evt);
            }
        });

        endGameButton.setBackground(new java.awt.Color(204, 0, 0));
        endGameButton.setFont(new java.awt.Font("SutonnyOMJ", 1, 36)); // NOI18N
        endGameButton.setForeground(new java.awt.Color(255, 255, 255));
        endGameButton.setText("শেষ করুন");
        endGameButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
        endGameButton.setBorderPainted(false);
        endGameButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        endGameButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.BUFFERED_OPTION);
        endGameButton.setFocusable(false);
        endGameButton.setRequestFocusEnabled(false);
        endGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endGameButtonActionPerformed(evt);
            }
        });

        resultTextArea.setBackground(new java.awt.Color(0, 0, 51));
        resultTextArea.setColumns(20);
        resultTextArea.setFont(new java.awt.Font("SutonnyOMJ", 1, 28)); // NOI18N
        resultTextArea.setForeground(new java.awt.Color(255, 255, 255));
        resultTextArea.setRows(5);
        resultTextArea.setText("\n\n   খেলা শুরু করতে\n\n   কার্ড বাটন চাপুন\n");
        jScrollPane1.setViewportView(resultTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(firstPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(thirdPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(secondPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fourthPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(shuffleButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(endGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(firstPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(secondPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(thirdPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                            .addComponent(fourthPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(shuffleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(endGameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void shuffleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shuffleButtonActionPerformed
        // TODO add your handling code here:
        if(!yes)
        {
            Collections.shuffle(cardIndex);
            Collections.shuffle(nameIndex);
            //initComponents();
            yes=true;
            if(policeTask==1)
                policeTask=2;
            else
                policeTask=1;
            setNamePicture();
        }
        else
        {
            resultTextArea.setText("\n\n\n   দয়া করে প্রথমে\n\n\n   ক্রিমিনালকে সিলেক্ট \n\n\n   করুন তারপরে \n\n\n   ক্লিক করুন ");
            //playSound("C:\\Users\\AKASH\\Desktop\\Police Vs Criminal\\PoliceVsCriminal\\src\\policevscriminal\\againShuffleButtonClick.mp3");
            playSound("C:\\Users\\pavilion\\Desktop\\PoliceVsCriminal\\src\\policevscriminal\\againShuffleButtonClick.mp3");
        }
    }//GEN-LAST:event_shuffleButtonActionPerformed

    private void endGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endGameButtonActionPerformed
        // TODO add your handling code here:
        for(int i=0;i<4;i++)
        {
            if(score[i]<0)
                score[i]=0;
            System.out.println(score[i]);
        }
        PreviousResultJFrame previousResultJFrame=new PreviousResultJFrame();
        previousResultJFrame.openDB();
        previousResultJFrame.insertData(playerNames, score, insertDate);
        LastJFrame frame=new LastJFrame();
        frame.ini(playerNames,score,icon);
        frame.setCom();
        frame.setVisible(true);
    }//GEN-LAST:event_endGameButtonActionPerformed
    
    public void playSound(String url)
    {
        try
        {
            File f=new File(url);
            FileInputStream fis=new FileInputStream(f);
            BufferedInputStream bis=new BufferedInputStream(fis);
            Player player=new Player(bis);
            player.play();
        }
        catch(Exception ex)
        {
            
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BoardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BoardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BoardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BoardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BoardJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton endGameButton;
    private javax.swing.JPanel firstPanel;
    private javax.swing.JButton firstPlayerButton;
    private javax.swing.JTextField firstPlayerTextField;
    private javax.swing.JPanel fourthPanel;
    private javax.swing.JButton fourthPlayerButton;
    private javax.swing.JTextField fourthPlayerTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea resultTextArea;
    private javax.swing.JPanel secondPanel;
    private javax.swing.JButton secondPlayerButton;
    private javax.swing.JTextField secondPlayerTextField;
    private javax.swing.JButton shuffleButton;
    private javax.swing.JPanel thirdPanel;
    private javax.swing.JButton thirdPlayerButton;
    private javax.swing.JTextField thirdPlayerTextField;
    // End of variables declaration//GEN-END:variables
}
