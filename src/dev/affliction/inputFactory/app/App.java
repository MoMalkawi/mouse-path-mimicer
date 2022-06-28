package dev.affliction.inputFactory.app;

import dev.affliction.inputFactory.components.mouse.Mouse;
import dev.affliction.inputFactory.components.recorders.ClickRecorder;
import dev.affliction.inputFactory.components.recorders.PathRecorder;
import dev.affliction.inputFactory.data.Clicks;
import dev.affliction.inputFactory.data.Paths;
import dev.affliction.inputFactory.utils.ReaderWriter;
import dev.affliction.inputFactory.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class App extends JPanel {

    private RecordingGround rg;

    public boolean recording = false;

    private JComboBox<String> dataType = new JComboBox<>(new String[]
            {"Regular Moves", "Slower Angular Moves", "Rapid Moves",
                    "Regular-Noisy Drags", "Rapid Drags",
                    "Fast-Clicks", "Regular Clicks", "Slower-Draggy Clicks"});

    private JList<String> dataTypeContents;

    private static JFrame f;

    private JTextField targetX;
    private JTextField targetY;

    private JTextField startX;
    private JTextField startY;

    public void start() {
        SwingUtilities.invokeLater(() -> {
            f = new JFrame("Recording Ground");
            f.setSize(1400, 950);
            f.setContentPane(this);
            f.setVisible(true);
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });
    }

    public App() {
        //setSize(f.getWidth(),f.getHeight());
        setLayout(new BorderLayout());

        JPanel toolsColumn = new JPanel();
        toolsColumn.setLayout(new GridLayout(20,1));
        toolsColumn.setOpaque(true);
        toolsColumn.setBackground(Color.DARK_GRAY);
        toolsColumn.setBorder(BorderFactory.createMatteBorder(0,0,0,2,Color.CYAN));
        JButton start = new JButton("Start");
        start.addActionListener(e -> startRec());
        JButton stop = new JButton("Stop");
        stop.addActionListener(e -> stopRec());
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> refresh());
        JButton removeSelected = new JButton("Remove Selected");
        removeSelected.addActionListener(e -> removeSelected());
        JLabel sxlabel = new JLabel("Start X:");
        startX = new JTextField();
        sxlabel.setForeground(Color.CYAN);
        JLabel sylabel = new JLabel("Start Y:");
        startY = new JTextField();
        sylabel.setForeground(Color.CYAN);
        JLabel xlabel = new JLabel("Target X:");
        targetX = new JTextField();
        xlabel.setForeground(Color.CYAN);
        JLabel ylabel = new JLabel("Target Y:");
        targetY = new JTextField();
        ylabel.setForeground(Color.CYAN);
        JButton playSelected = new JButton("Play Selected");
        playSelected.addActionListener(e -> playSelected());
        JButton save = new JButton("Save");
        save.addActionListener(e -> save());
        JButton saveAll = new JButton("Save All");
        saveAll.addActionListener(e -> saveAll());
        JButton load = new JButton("Load");
        load.addActionListener(e -> load());

        dataTypeContents = new JList<>();
        dataTypeContents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(dataTypeContents);

        dataType.addActionListener(e-> fillList());

        toolsColumn.add(start);
        toolsColumn.add(stop);
        toolsColumn.add(refresh);
        toolsColumn.add(removeSelected);
        toolsColumn.add(sxlabel);
        toolsColumn.add(startX);
        toolsColumn.add(sylabel);
        toolsColumn.add(startY);
        toolsColumn.add(xlabel);
        toolsColumn.add(targetX);
        toolsColumn.add(ylabel);
        toolsColumn.add(targetY);
        toolsColumn.add(playSelected);
        toolsColumn.add(dataType);
        toolsColumn.add(save);
        toolsColumn.add(saveAll);
        toolsColumn.add(load);

        add(toolsColumn,BorderLayout.WEST);
        add(scrollPane,BorderLayout.EAST);
        rg = new RecordingGround(this);
        add(rg,BorderLayout.CENTER);

    }

    public void startRec() {
        rg.shuffleTarget();
        recording = true;
    }

    public void stopRec() {
        recording = false;
        Paths.pointsList = null;
        Paths.longestDistanceFromOrigin = 0;
        Clicks.drags = null;
    }

    public void refresh() {
        fillList();
    }

    public void fillList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        switch ((String)dataType.getSelectedItem()) {
            case "Regular Moves":
                for(double hp : Paths.STANDARD_MOVEMENTS.values())
                    model.addElement(String.valueOf(hp));
                break;
            case "Slower Angular Moves":
                for(double hp : Paths.SLOWER_ANGULAR_MOVEMENTS.values())
                    model.addElement(String.valueOf(hp));
                break;
            case "Rapid Moves":
                for(double hp : Paths.RAPID_MOVEMENTS.values())
                    model.addElement(String.valueOf(hp));
                break;
            case "Regular-Noisy Drags":
                for(double hp : Paths.STANDARD_NOISY_DRAGS.values())
                    model.addElement(String.valueOf(hp));
                break;
            case "Rapid Drags":
                for(double hp : Paths.RAPID_DRAGS.values())
                    model.addElement(String.valueOf(hp));
                break;
            case "Fast-Clicks":
                rg.target.setLocation(-50,-50);
                for(double click : Clicks.RAPID_CLICKS.values())
                    model.addElement(String.valueOf(click));
                break;
            case "Regular Clicks":
                rg.target.setLocation(-50,-50);
                for(double click : Clicks.REGULAR_CLICKS.values())
                    model.addElement(String.valueOf(click));
                break;
            case "Slower-Draggy Clicks":
                rg.target.setLocation(-50,-50);
                for(double click : Clicks.SLOWER_DRAGGY_CLICKS.values())
                    model.addElement(String.valueOf(click));
                break;
        }
        dataTypeContents.setModel(model);
    }

    public void removeSelected() { //TODO: find a way to delete by index
        int index = dataTypeContents.getSelectedIndex();
        if(index >= 0) {
            switch ((String)dataType.getSelectedItem()) {
                case "Regular Moves":
                    if(Paths.STANDARD_MOVEMENTS.size() > index)
                        Paths.STANDARD_MOVEMENTS.remove(Paths.STANDARD_MOVEMENTS.keySet().toArray()[index]);
                    break;
                case "Slower Angular Moves":
                    if(Paths.SLOWER_ANGULAR_MOVEMENTS.size() > index)
                        Paths.SLOWER_ANGULAR_MOVEMENTS.remove(Paths.SLOWER_ANGULAR_MOVEMENTS.keySet().toArray()[index]);
                    break;
                case "Rapid Moves":
                    if(Paths.RAPID_MOVEMENTS.size() > index)
                        Paths.RAPID_MOVEMENTS.remove(Paths.RAPID_MOVEMENTS.keySet().toArray()[index]);
                    break;
                case "Regular-Noisy Drags":
                    if(Paths.STANDARD_NOISY_DRAGS.size() > index)
                        Paths.STANDARD_NOISY_DRAGS.remove(Paths.STANDARD_NOISY_DRAGS.keySet().toArray()[index]);
                    break;
                case "Rapid Drags":
                    if(Paths.RAPID_DRAGS.size() > index)
                        Paths.RAPID_DRAGS.remove(Paths.RAPID_DRAGS.keySet().toArray()[index]);
                    break;
                case "Fast-Clicks":
                    if(Clicks.RAPID_CLICKS.size() > index)
                        Clicks.RAPID_CLICKS.remove(Clicks.RAPID_CLICKS.keySet().toArray()[index]);
                    break;
                case "Regular Clicks":
                    if(Clicks.REGULAR_CLICKS.size() > index)
                        Clicks.REGULAR_CLICKS.remove(Clicks.REGULAR_CLICKS.keySet().toArray()[index]);
                    break;
                case "Slower-Draggy Clicks":
                    if(Clicks.SLOWER_DRAGGY_CLICKS.size() > index)
                        Clicks.SLOWER_DRAGGY_CLICKS.remove(Clicks.SLOWER_DRAGGY_CLICKS.keySet().toArray()[index]);
                    break;
            }
        }
        refresh();
    }

    public void playSelected() {
        new Thread(() -> {
            int index = dataTypeContents.getSelectedIndex();
            if(index >= 0) {
                int sX = Integer.parseInt(startX.getText());
                int sY = Integer.parseInt(startY.getText());
                int tX = Integer.parseInt(targetX.getText());
                int tY = Integer.parseInt(targetY.getText());
                Mouse.event(sX,sY,System.currentTimeMillis(),MouseEvent.MOUSE_MOVED);
                switch ((String)dataType.getSelectedItem()) {
                    case "Regular Moves":
                        if(Paths.STANDARD_MOVEMENTS.size() > index)
                            PathRecorder.play((int[]) Paths.STANDARD_MOVEMENTS.keySet().toArray()[index], new Point(tX, tY),
                                    new Point(sX,sY),
                                    MouseEvent.MOUSE_MOVED);
                        break;
                    case "Slower Angular Moves":
                        if(Paths.SLOWER_ANGULAR_MOVEMENTS.size() > index)
                            PathRecorder.play((int[]) Paths.SLOWER_ANGULAR_MOVEMENTS.keySet().toArray()[index], new Point(tX, tY),
                                    new Point(sX,sY),
                                    MouseEvent.MOUSE_MOVED);
                        break;
                    case "Rapid Moves":
                        if(Paths.RAPID_MOVEMENTS.size() > index)
                            PathRecorder.play((int[]) Paths.RAPID_MOVEMENTS.keySet().toArray()[index], new Point(tX, tY),
                                    new Point(sX,sY),
                                    MouseEvent.MOUSE_MOVED);
                        break;
                    case "Regular-Noisy Drags":
                        if(Paths.STANDARD_NOISY_DRAGS.size() > index) {
                            Mouse.event(Mouse.location.x,Mouse.location.y, System.currentTimeMillis(),
                                    1, 1, MouseEvent.MOUSE_PRESSED);
                            PathRecorder.play((int[]) Paths.STANDARD_NOISY_DRAGS.keySet().toArray()[index], new Point(tX, tY),
                                    new Point(sX,sY),
                                    MouseEvent.MOUSE_DRAGGED);
                            Mouse.event(Mouse.location.x,Mouse.location.y,System.currentTimeMillis(),
                                    1,1,MouseEvent.MOUSE_RELEASED);
                        }
                        break;
                    case "Rapid Drags":
                        if(Paths.RAPID_DRAGS.size() > index) {
                            Mouse.event(Mouse.location.x,Mouse.location.y, System.currentTimeMillis(),
                                    1, 1, MouseEvent.MOUSE_PRESSED);
                            PathRecorder.play((int[]) Paths.RAPID_DRAGS.keySet().toArray()[index], new Point(tX, tY),
                                    new Point(sX,sY),
                                    MouseEvent.MOUSE_DRAGGED);
                            Mouse.event(Mouse.location.x,Mouse.location.y,System.currentTimeMillis(),
                                    1,1,MouseEvent.MOUSE_RELEASED);
                        }
                        break;
                    case "Fast-Clicks":
                        if(Clicks.RAPID_CLICKS.size() > index)
                            ClickRecorder.playClick((int[]) Clicks.RAPID_CLICKS.keySet().toArray()[index],
                                    sX, sY, Mouse.LEFT_BUTTON);
                        break;
                    case "Regular Clicks":
                        if(Clicks.REGULAR_CLICKS.size() > index) {
                            for(int[] ar : Clicks.REGULAR_CLICKS.keySet()) {
                                System.out.println(Arrays.toString(ar));
                                System.out.println("\n");
                            }
                            ClickRecorder.playClick((int[]) Clicks.REGULAR_CLICKS.keySet().toArray()[index],
                                    sX, sY, Mouse.LEFT_BUTTON);
                        }
                        break;
                    case "Slower-Draggy Clicks":
                        if(Clicks.SLOWER_DRAGGY_CLICKS.size() > index)
                            ClickRecorder.playClick((int[]) Clicks.SLOWER_DRAGGY_CLICKS.keySet().toArray()[index],
                                    sX,sY, Mouse.LEFT_BUTTON);
                        break;
                }
            }
        }).start();
    }

    public void save() {
        int index = dataTypeContents.getSelectedIndex();
        switch ((String)dataType.getSelectedItem()) {
            case "Regular Moves":
                int[] ar = (int[]) Paths.STANDARD_MOVEMENTS.keySet().toArray()[index];
                double num = Paths.STANDARD_MOVEMENTS.get(ar);
                ReaderWriter.write("regular_moves/"+Utils.assignLoc(num),String.valueOf(num),ar);
                break;
            case "Slower Angular Moves":
                ar = (int[]) Paths.SLOWER_ANGULAR_MOVEMENTS.keySet().toArray()[index];
                num = Paths.SLOWER_ANGULAR_MOVEMENTS.get(ar);
                ReaderWriter.write("slower_angular_moves/"+Utils.assignLoc(num),String.valueOf(num),ar);
                break;
            case "Rapid Moves":
                ar = (int[]) Paths.RAPID_MOVEMENTS.keySet().toArray()[index];
                num = Paths.RAPID_MOVEMENTS.get(ar);
                ReaderWriter.write("rapid_moves/"+Utils.assignLoc(num),String.valueOf(num),ar);
                break;
            case "Fast-Clicks":
                ar = (int[]) Clicks.RAPID_CLICKS.keySet().toArray()[index];
                int numm = Clicks.RAPID_CLICKS.get(ar);
                ReaderWriter.write("rapid_clicks/",String.valueOf(numm),ar);
                break;
            case "Regular Clicks":
                ar = (int[]) Clicks.REGULAR_CLICKS.keySet().toArray()[index];
                numm = Clicks.REGULAR_CLICKS.get(ar);
                ReaderWriter.write("regular_clicks/",String.valueOf(numm),ar);
                break;
            case "Slower-Draggy Clicks":
                ar = (int[]) Clicks.SLOWER_DRAGGY_CLICKS.keySet().toArray()[index];
                numm = Clicks.SLOWER_DRAGGY_CLICKS.get(ar);
                ReaderWriter.write("slower_draggy_clicks/",String.valueOf(numm),ar);
                break;
        }
    }

    public void saveAll() {
        switch ((String)dataType.getSelectedItem()) {
            case "Regular Moves":
                for(int[] data : Paths.STANDARD_MOVEMENTS.keySet()) {
                    double num = Paths.STANDARD_MOVEMENTS.get(data);
                    ReaderWriter.write("regular_moves/" + Utils.assignLoc(num), String.valueOf(num), data);
                }
                break;
            case "Slower Angular Moves":
                for(int[] data : Paths.SLOWER_ANGULAR_MOVEMENTS.keySet()) {
                    double num = Paths.SLOWER_ANGULAR_MOVEMENTS.get(data);
                    ReaderWriter.write("slower_angular_moves/" + Utils.assignLoc(num), String.valueOf(num), data);
                }
                break;
            case "Rapid Moves":
                for(int[] data : Paths.RAPID_MOVEMENTS.keySet()) {
                    double num = Paths.RAPID_MOVEMENTS.get(data);
                    ReaderWriter.write("rapid_moves/" + Utils.assignLoc(num), String.valueOf(num), data);
                }
                break;
            case "Fast-Clicks":
                for(int[] data : Clicks.RAPID_CLICKS.keySet()) {
                    int num = Clicks.RAPID_CLICKS.get(data);
                    ReaderWriter.write("rapid_clicks/", String.valueOf(num), data);
                }
                break;
            case "Regular Clicks":
                for(int[] data : Clicks.REGULAR_CLICKS.keySet()) {
                    int num = Clicks.REGULAR_CLICKS.get(data);
                    ReaderWriter.write("regular_clicks/", String.valueOf(num), data);
                }
                break;
            case "Slower-Draggy Clicks":
                for(int[] data : Clicks.SLOWER_DRAGGY_CLICKS.keySet()) {
                    int num = Clicks.SLOWER_DRAGGY_CLICKS.get(data);
                    ReaderWriter.write("slower_draggy_clicks/", String.valueOf(num), data);
                }
                break;
        }
    }

    public void load() {

    }

    public JComboBox<String> getDataType() {
        return dataType;
    }

    public RecordingGround getRg() {
        return rg;
    }
}


