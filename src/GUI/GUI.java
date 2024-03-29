package GUI;

import Calculations.Edge;
import Calculations.GraphFlow;
import Calculations.TransferFunction;
import Calculations.Vertex;
import Loops.LoopDetection;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxStyleUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

public class GUI extends JFrame {
    mxGraph graph;
    // ely 3liha el buttons
    JPanel main;
    //ely 7nrsm 3liha
    JPanel panel;
    GraphFlow finalGraph;
    TransferFunction tf = new TransferFunction();
    public static final Color GREY = new Color(204, 204, 204);
    public static final Color c = new Color(255, 163, 26);
    public static final Color c2 = new Color(153, 204, 255);
    public static final Color c3 = new Color(251, 251, 251);
    JLabel WarningLabel;
    JTextArea display;
    JTextField outputNode;
    public String output = "";

    // LinkedList<Object> redo;
    public GUI() {
        super("Signal-Flow Graph");

        initComponents();

        graph = new mxGraph() {
        };

        //graph.setAllowDanglingEdges(false);
        graph.getModel().beginUpdate();
        try {
            addvertex();
        } finally {
            graph.getModel().endUpdate();
        }
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setConnectable(true);
        graphComponent.getViewport().setOpaque(true);
        graphComponent.getViewport().setBackground(c3);
        //setting dimension of the drawing board
        graph.getModel().setGeometry(graph.getDefaultParent(), new mxGeometry(990, 750, 0, 0));
        graph.setMinimumGraphSize(new mxRectangle(990, 750, 0, 0));
        graph.setCollapseToPreferredSize(true);
        graph.setVertexLabelsMovable(false);
        graph.setEdgeLabelsMovable(false);
        graph.setAllowLoops(true);
        //deh 3l4an el edges connected
        graph.addListener(mxEvent.CELL_CONNECTED, (sender, evt) -> {

                    if (!(boolean) evt.getProperties().get("source")) {
                        mxICell edge = (mxICell) evt.getProperties().get("edge");
                        //let the gain of the edge by default 1
                        edge.setValue("1");
                    }
                }
        );
        //Sets the new label for a cell
        graph.addListener(mxEvent.LABEL_CHANGED, (sender, evt) -> {
                    mxICell vertex = (mxICell) evt.getProperties().get("vertex");
                }

        );
        main.add(graphComponent);
        setEdgeStyle();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initComponents() {

        getContentPane().setLayout(new BorderLayout());
        main = new JPanel();
        main.setBackground(c3);
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(GREY);
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(main, BorderLayout.EAST);
        //redo = new LinkedList<Object>();

        //BUTTONS
        Font boldFont = new Font("SansSerif", Font.BOLD, 16);
        //FOR ADDING A VERTEX
        JButton addBtn = new JButton("Add Node");
        addBtn.setBounds(40, 105, 100, 40);
        addBtn.setBackground(c);
        addBtn.setForeground(Color.black);
        addBtn.setFont(boldFont);
        addBtn.setBorder(BorderFactory.createEmptyBorder());
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addvertex();
            }
        });

        //FOR STARTING CALCULATION
        JButton startBtn = new JButton("Calculate");
        startBtn.setBounds(40, 250, 100, 40);
        startBtn.setBackground(c);
        startBtn.setForeground(Color.black);
        startBtn.setFont(boldFont);
        startBtn.setBorder(BorderFactory.createEmptyBorder());
        startBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkEdgesValue() && isRedundantNode() && textExists()) {
                    startCalculation();
                    startBtn.setEnabled(false);
                    //finalGraph.debugGraph();
                }
            }
        });

        //FOR REMOVING THE WHOLE GRAPH
        JButton clearBtn = new JButton("Clear");
        clearBtn.setBounds(230, 105, 100, 40);
        clearBtn.setBackground(c);
        clearBtn.setForeground(Color.black);
        clearBtn.setFont(boldFont);
        clearBtn.setBorder(BorderFactory.createEmptyBorder());
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clear();
                startBtn.setEnabled(true);
            }
        });


        ///////////////////////////////////////////EXit//////////////////////////////////////
        boldFont = new Font("SansSerif", Font.BOLD, 14);

        //FOR EXIT BUTTON:
        JButton exitBtn = new JButton("Exit ");
        exitBtn.setBounds(360, 33, 80, 30);
        exitBtn.setBackground(c2);
        exitBtn.setForeground(Color.BLACK);
        exitBtn.setFont(boldFont);
        exitBtn.setBorder(BorderFactory.createEmptyBorder());
        exitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        /////////////////////////////////////////////////////////////////
        //FOR warning label
        WarningLabel = new JLabel();
        WarningLabel.setBounds(100, 298, 350, 50);
        WarningLabel.setForeground(GREY);
        boldFont = new Font("SansSerif", Font.BOLD, 16);
        WarningLabel.setFont(boldFont);
        WarningLabel.setText("Make sure your entry is not wrong!");
///////////////////////////////////////////////////////////////////////
        //FOR  TITLE
        JLabel lbl = new JLabel();
        lbl.setBounds(15, 15, 360, 60);
        lbl.setForeground(Color.BLACK);
        lbl.setText("Signal Flow App");
        boldFont = new Font("Monospaced", Font.BOLD, 30);
        lbl.setFont(boldFont);
///////////////////////////////////////////////////////////////////////////////
        //label for input :
        JLabel lb = new JLabel();
        lb.setBounds(20, 310, 100, 20);
        lb.setForeground(Color.black);
        lb.setText("Results: ");
        boldFont = new Font("SansSerif", Font.BOLD, 20);
        lb.setFont(boldFont);
        //////////////////////////////////////
        //input :
        display = new JTextArea();
        display.setText(output);
        //as it is the results can't edit in this area
        display.setEditable(false); // set textArea non-editable
        //display.setBackground(new Color(176, 175, 179 ));
        display.setBackground(Color.WHITE);
        boldFont = new Font("SansSerif", Font.BOLD, 17);
        display.setForeground(Color.BLACK);
        display.setFont(boldFont);
        display.setBorder(new EmptyBorder(50, 20, 0, 0));//top,left,bottom,right
        JScrollPane scroll = new JScrollPane(display);
        scroll.setBounds(20, 350, 300, 500);
        scroll.setSize(400, 500);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        /////////////////////////////////////////////////////////////////////////
        //output node text:
        boldFont = new Font("SansSerif", Font.BOLD, 16);
        JLabel txtlbl = new JLabel();
        txtlbl.setBounds(30, 200, 180, 20);
        txtlbl.setForeground(Color.BLACK);
        txtlbl.setText("Output Node Name:");
        txtlbl.setFont(boldFont);
        outputNode = new JTextField();
        outputNode.setBounds(230, 200, 150, 25);
        outputNode.setFont(boldFont);
////////////////////////////////////////////////////////////
        panel.add(txtlbl);
        panel.add(outputNode);
        panel.add(scroll);
        //panelComp.add(userGuide);
        panel.add(lbl);
        panel.add(lb);
        panel.add(exitBtn);
        //panelComp.add(redoBtn);
        //panelComp.add(undoBtn);
        panel.add(addBtn);
        //ana 3mlt comment
        panel.add(startBtn);
        panel.add(clearBtn);
        panel.add(WarningLabel);
    }

    //////////////////////////////////////////////////VERTEX////////////////////////////////////////////
    public void resetGraph() {
        //code here
        graph.refresh();
    }

    public void addvertex() {
        Object v = graph.insertVertex(graph.getDefaultParent(), null, "Name", 30, 30, 55, 55);
        mxICell ver = (mxICell) v;
        setVertexStyle(ver, "#33ccff");
        resetGraph();
    }

    void clear() {
        graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
        graph.getModel().beginUpdate();
        graph.getModel().endUpdate();
        output = "";
        this.display.setText(this.output);
        addvertex();
    }

    //STYLES FOR VERTICES
    private void setVertexStyle(final mxICell vertex, final String colorstr) {
        String targetStyle = vertex.getStyle();
        targetStyle = mxStyleUtils.removeAllStylenames(targetStyle);
        targetStyle = mxStyleUtils.setStyle(targetStyle, mxConstants.STYLE_STROKECOLOR, "black");
        targetStyle = mxStyleUtils.setStyle(targetStyle, mxConstants.STYLE_FILLCOLOR, colorstr);
        targetStyle = mxStyleUtils.setStyle(targetStyle, mxConstants.STYLE_FONTCOLOR, "black");
        targetStyle = mxStyleUtils.setStyle(targetStyle, mxConstants.STYLE_FONTSIZE, "12");
        targetStyle = mxStyleUtils.setStyle(targetStyle, mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        vertex.setStyle(targetStyle);
    }

    private void setEdgeStyle() {
        final mxStylesheet foo = graph.getStylesheet();
        final Map<String, Object> edgeMap = foo.getDefaultEdgeStyle();
        edgeMap.put(mxConstants.STYLE_ROUNDED, true);
        edgeMap.put(mxConstants.STYLE_STROKECOLOR, "black");
        edgeMap.put(mxConstants.STYLE_FONTCOLOR, "white");
        edgeMap.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "black");
        edgeMap.put(mxConstants.STYLE_FONTSIZE, "15");
        edgeMap.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ORTHOGONAL);
        foo.setDefaultEdgeStyle(edgeMap);
        graph.setStylesheet(foo);
    }

    /////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        GUI frame = new GUI();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setVisible(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void startCalculation() {
        Object[] list = graph.getChildVertices(graph.getDefaultParent());
        LinkedList vertices = new LinkedList(Arrays.asList(list));
        finalGraph = new GraphFlow(list.length);
        this.output += "Number of Nodes = " + list.length + "\n";
        for (int i = 0; i < list.length; i++) {
            mxICell vertex = (mxICell) list[i];
            Object[] edges = graph.getEdges(vertex, graph.getDefaultParent(), false, true, true);
            for (int j = 0; j < edges.length; j++) {
                String source = (String) vertex.getValue();
                Vertex sourceNode = new Vertex(i, source);
                if (source.equals(outputNode.getText()))
                    sourceNode.setOutput(true);
                String destination = (String) (((mxICell) edges[j]).getTerminal(false)).getValue();

                int index = vertices.indexOf(((mxICell) edges[j]).getTerminal(false));

                Vertex destinationNode = new Vertex(index, destination);
                if (destination.equals(outputNode.getText()))
                    destinationNode.setOutput(true);
                //this.output += "source-" + sourceNode.getName() + "dest: " + destinationNode.getName() + " weight: " + Integer.valueOf((String) graph.getModel().getValue(edges[j])) + "\n";
                finalGraph.addEgde(sourceNode, destinationNode, Integer.valueOf((String) graph.getModel().getValue(edges[j])));

            }

        }
        calculateOutput();
        this.display.setText(this.output);

    }

    public void calculateOutput() {
        finalGraph.findForwardPaths();
        this.output += finalGraph.getForwardPathsOutput();
        Vector<Vector<Edge>> loops;
        LoopDetection loopDetection = new LoopDetection(finalGraph);
        loops = loopDetection.getLoops();
        //this.output += loopDetection.getDeltaIString();
        Vector<Vector<Vector<Edge>>> nonTouchingEdges = loopDetection.getNonTouchingEdges();
        for (int i = 0; i < loops.size(); i++) {
            this.output += "L" + (i + 1) + " = ";
            for (int j = 0; j < loops.get(i).size(); j++) {
                this.output += loops.get(i).get(j).getSource().getName() + " --> ";
                if (j == loops.get(i).size() - 1)
                    this.output += loops.get(i).get(j).getDestination().getName() + " = " + loopDetection.loopsGain.get(i) + "\n";
            }
        }


        for (int i = 0; i < nonTouchingEdges.size(); i++) {
            this.output += (i + 2) + " nonTouchingLoops: \n";
            for (int j = 0; j < nonTouchingEdges.get(i).size(); j++) {
                this.output += loopDetection.loopTouch[i][j] + " = " + loopDetection.nonTouchingGains.get(i).get(j) + "\n";
            }
        }
        this.output += loopDetection.getDeltaIString();
        Double res = tf.deltaTotal(loopDetection.loopsGain, loopDetection.nonTouchingGains);
        this.output += "The Transfer Function = " + tf.TransFunction(res, loopDetection.deltaI, finalGraph.gain) + "\n";
        System.out.println("done");

    }


    /////////////////////////////NOT Yet Checks//////////////////////////////////////////
    public boolean checkEdgesValue() {
        Object[] list = graph.getChildEdges(graph.getDefaultParent());
        for (int i = 0; i < list.length; i++) {
            if (!checkInteger((String) graph.getModel().getValue(list[i]))) {
                WarningLabel.setForeground(Color.red);
                return false;
            }
        }
        WarningLabel.setForeground(GREY);
        return true;
    }


    public boolean checkInteger(String s) {
        if (s.isEmpty()) return false;
        //if the user enter -ve sign only
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            // if(Character.digit(s.charAt(i),10) < 0) return false;
            if (Character.digit(s.charAt(i), 10) < 0) return false;

        }
        return true;
    }

    public boolean isRedundantNode() {
        Object[] list = graph.getChildVertices(graph.getDefaultParent());
        for (int i = 0; i < list.length - 1; i++) {
            String first = (String) ((mxICell) list[i]).getValue();
            for (int j = i + 1; j < list.length; j++) {
                String second = (String) ((mxICell) list[j]).getValue();
                if (first.equalsIgnoreCase(second)) {
                    WarningLabel.setForeground(Color.red);
                    return false;
                }
            }
        }
        return true;
    }

    public boolean textExists() {
        Object[] list = graph.getChildVertices(graph.getDefaultParent());
        for (int i = 0; i < list.length; i++) {
            mxICell ver = (mxICell) list[i];
            String name = (String) ver.getValue();
            if ((outputNode.getText()).equals(name)) {
                WarningLabel.setForeground(GREY);
                return true;
            }
        }
        WarningLabel.setForeground(Color.red);
        return false;
    }
}
