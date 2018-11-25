package imgFile;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class JNotePad extends JFrame {
    private JMenuItem menuOpen;
    private JMenuItem menuSave;
    private JMenuItem menuSaveAs;
    private JMenuItem menuClose;

    private JMenu editMenu;
    private JMenuItem menuCut;
    private JMenuItem menuCopy;
    private JMenuItem menuPaste;

    private JMenuItem menuAbout;
    
    private JTextArea textArea;
    
    private JPopupMenu popUpMenu;
    
    private JLabel stateBar;
    
    private JFileChooser fileChooser;
    
    public JNotePad() {
        super("�s�W��r�ɮ�");
        setUpUIComponent();
        setUpEventListener();
        setVisible(true);
    }
    
    private void setUpUIComponent() {
        setSize(640, 480);
        
        // ���C
        JMenuBar menuBar = new JMenuBar();
        
        // �]�m�u�ɮסv���
        JMenu fileMenu = new JMenu("�ɮ�");
        menuOpen = new JMenuItem("�}������");
        // �ֳt��]�m
        menuOpen.setAccelerator(
                    KeyStroke.getKeyStroke(
                            KeyEvent.VK_O, 
                            InputEvent.CTRL_MASK));
        menuSave = new JMenuItem("�x�s�ɮ�");
        menuSave.setAccelerator(
                    KeyStroke.getKeyStroke(
                            KeyEvent.VK_S, 
                            InputEvent.CTRL_MASK));
        menuSaveAs = new JMenuItem("�t�s�s��");

        menuClose = new JMenuItem("����");
        menuClose.setAccelerator(
                    KeyStroke.getKeyStroke(
                            KeyEvent.VK_Q, 
                            InputEvent.CTRL_MASK));
        
        fileMenu.add(menuOpen);
        fileMenu.addSeparator(); // ���j�u
        fileMenu.add(menuSave);
        fileMenu.add(menuSaveAs);        
        fileMenu.addSeparator(); // ���j�u
        fileMenu.add(menuClose);
        
        // �]�m�u�s��v���        
        editMenu = new JMenu("�s��");
        menuCut = new JMenuItem("�ŤU");
        menuCut.setAccelerator(
                    KeyStroke.getKeyStroke(KeyEvent.VK_X, 
                            InputEvent.CTRL_MASK));
        menuCopy = new JMenuItem("�ƻs");
        menuCopy.setAccelerator(
                    KeyStroke.getKeyStroke(KeyEvent.VK_C, 
                            InputEvent.CTRL_MASK));
        menuPaste = new JMenuItem("�K�W");
        menuPaste.setAccelerator(
                    KeyStroke.getKeyStroke(KeyEvent.VK_V, 
                            InputEvent.CTRL_MASK));
        editMenu.add(menuCut);
        editMenu.add(menuCopy);
        editMenu.add(menuPaste);
        
        // �]�m�u����v���        
        JMenu aboutMenu = new JMenu("����");
        menuAbout = new JMenuItem("����JNotePad");
        aboutMenu.add(menuAbout);
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(aboutMenu);
        
        setJMenuBar(menuBar);
        
        // ��r�s��ϰ�
        textArea = new JTextArea();
        textArea.setFont(new Font("�ө���", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        JScrollPane panel = new JScrollPane(textArea,
          ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
          ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        Container contentPane = getContentPane();
        contentPane.add(panel, BorderLayout.CENTER);  
        
        // ���A�C
        stateBar = new JLabel("���ק�");
        stateBar.setHorizontalAlignment(SwingConstants.LEFT); 
        stateBar.setBorder(
                BorderFactory.createEtchedBorder());
        contentPane.add(stateBar, BorderLayout.SOUTH);
        
        popUpMenu = editMenu.getPopupMenu();
        
        fileChooser = new JFileChooser();
    }
    
    private void setUpEventListener() {
        // ���U���������s�ƥ�B�z
        addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) { 
                    closeFile();
                }
            }
        );
        
        // ��� - �}������
        menuOpen.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openFile();
                }
            }
        );

        // ��� - �x�s�ɮ�
        menuSave.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveFile();
                }
            }
        );

        // ��� - �t�s�s��
        menuSaveAs.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveFileAs();
                }
            }
        );

        // ��� - �����ɮ�
        menuClose.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    closeFile();
                }
            }
        );

        // ��� - �ŤU
        menuCut.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cut();
                }
            }
        );

        // ��� - �ƻs
        menuCopy.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    copy();
                }
            }
        );

        // ��� - �K�W
        menuPaste.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    paste();
                }
            }
        );
        
        // ��� - ����
        menuAbout.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // ��ܹ�ܤ��
                    JOptionPane.showOptionDialog(null, 
                        "�{���W��:\n    JNotePad \n" + 
                        "�{���]�p:\n    �}����\n" + 
                        "²��:\n    �@��²�檺��r�s�边\n" + 
                        "    �i�@���禬Java����@��H\n" +
                        "    �w����ͤU����s��y\n\n" +
                        "http://caterpillar.onlyfun.net/",
                        "����JNotePad",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null, null, null);
                }
            }
        );      
        
        // �s�����L�ƥ�
        textArea.addKeyListener(
            new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    processTextArea();
                }
            }
        );

        // �s��Ϸƹ��ƥ�
        textArea.addMouseListener(
            new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON3)
                        popUpMenu.show(editMenu, e.getX(), e.getY());
                }
                
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON1)
                        popUpMenu.setVisible(false);
                }
            }
        );        
    }

    private void openFile() {
        if(isCurrentFileSaved()) { // ���O�_���x�s���A
            open(); // �}������
        }
        else {
            // ��ܹ�ܤ��
            int option = JOptionPane.showConfirmDialog(
                    null, "�ɮפw�ק�A�O�_�x�s�H",
                    "�x�s�ɮסH", JOptionPane.YES_NO_OPTION, 
                    JOptionPane.WARNING_MESSAGE, null);
            switch(option) { 
                // �T�{�ɮ��x�s
                case JOptionPane.YES_OPTION:
                    saveFile(); // �x�s�ɮ�
                     break;
               // ����ɮ��x�s
                case JOptionPane.NO_OPTION:
                    open();
                    break;
            }
        }
    }

    private void open() {
        // fileChooser �O JFileChooser �����
        // ����ɮ׿������ܤ��
        int option = fileChooser.showDialog(null, null);
        
        // �ϥΪ̫��U�T�{��
        if(option == JFileChooser.APPROVE_OPTION) {
            try {
                // �}�ҿ�����ɮ�
                BufferedReader buf = 
                    new BufferedReader(
                       new FileReader(
                         fileChooser.getSelectedFile()));

                // �]�w�����D
                setTitle(fileChooser.getSelectedFile().toString());
                // �M���e�@�����
                textArea.setText("");
                // �]�w���A�C
                stateBar.setText("���ק�");
                // ���o�t�ά̪ۨ�����r��
                String lineSeparator = System.getProperty("line.separator");
                // Ū���ɮרê��[�ܤ�r�s���
                String text;
                while((text = buf.readLine()) != null) {
                    textArea.append(text);
                    textArea.append(lineSeparator);
                }

                buf.close();
            }   
            catch(IOException e) {
                JOptionPane.showMessageDialog(null, e.toString(),
                            "�}���ɮץ���", JOptionPane.ERROR_MESSAGE);
            }
        }        
    }

    private boolean isCurrentFileSaved() {
        if(stateBar.getText().equals("�w�ק�")) {
            return false;
        }
        else {
            return true;
        }
    }

    private void saveFile() {
        // �q���D�C���o�ɮצW��
        File file = new File(getTitle());

        // �Y���w���ɮפ��s�b
        if(!file.exists()) {
            // ����t�s�s��
            saveFileAs();
        }
        else {
            try {
                // �}�ҫ��w���ɮ�
                BufferedWriter buf = 
                    new BufferedWriter(
                            new FileWriter(file));
                // �N��r�s��Ϫ���r�g�J�ɮ�
                buf.write(textArea.getText());
                buf.close();
                // �]�w���A�C�����ק�
                stateBar.setText("���ק�");
            }
            catch(IOException e) {
                JOptionPane.showMessageDialog(null, e.toString(),
                                "�g�J�ɮץ���", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFileAs() {
        // ����ɮ׹�ܤ��
        int option = fileChooser.showDialog(null, null);

        // �p�G�T�{����ɮ�
        if(option == JFileChooser.APPROVE_OPTION) {
            // ���o��ܪ��ɮ�
            File file = fileChooser.getSelectedFile();
            
            // �b���D�C�W�]�w�ɮצW��
            setTitle(file.toString());
            
            try {
                // �إ��ɮ�
                file.createNewFile();
                // �i���ɮ��x�s
                saveFile();
            }
            catch(IOException e) {
                JOptionPane.showMessageDialog(null, e.toString(),
                            "�L�k�إ߷s��", JOptionPane.ERROR_MESSAGE);
            }
        }   
    }
    
    private void closeFile() {
        // �O�_�w�x�s���
        if(isCurrentFileSaved()) {
            // ��������귽�A�ӫ������{��
            dispose();
        }
        else {
            int option = JOptionPane.showConfirmDialog(
                    null, "�ɮפw�ק�A�O�_�x�s�H",
                    "�x�s�ɮסH", JOptionPane.YES_NO_OPTION, 
                    JOptionPane.WARNING_MESSAGE, null);

            switch(option) {
                case JOptionPane.YES_OPTION:
                    saveFile();
                    break;
                case JOptionPane.NO_OPTION:
                    dispose();
            }
        }
    }
    
    private void cut() {
        textArea.cut();
        stateBar.setText("�w�ק�");
        popUpMenu.setVisible(false);
    }

    private void copy() {
        textArea.copy();
        popUpMenu.setVisible(false);    
    }

    private void paste() {
        textArea.paste();
        stateBar.setText("�w�ק�");
        popUpMenu.setVisible(false);
    }

    private void processTextArea() {
        stateBar.setText("�w�ק�");
    }
    
    public static void main(String[] args) {
        new JNotePad();
    }
}
