package GUI;

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
import java.util.Map;

public class GUI extends JFrame {
    mxGraph graph;
    // ely 3liha el buttons
    JPanel mainPane;
    //ely 7nrsm 3liha
    JPanel panelComp;

    public static final Color GREY = new Color(204, 204, 204);
    public static final  Color c = new Color(255, 153, 204);
    public static final Color c2 = new Color(153, 204, 255 );
    public static final Color c3 = new Color(251, 251, 251);

    JButton addVertexBtn;
    JButton startBtn;
    JButton removeGraphBtn;
    JLabel WarningLabel;
    JTextArea display;
    JTextField txt;
    public String output = "";
   // LinkedList<Object> redo;
    public GUI() {
        super("Signal-Flow Graph");

        initComponents();

        graph = new mxGraph(){};

        //graph.setAllowDanglingEdges(false);
        graph.getModel().beginUpdate();
        try
        {
            addvertex();
        }
        finally
        {
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
                        mxICell edge =(mxICell) evt.getProperties().get("edge");
                        //let the gain of the edge by default 1
                        edge.setValue("1");
                    }
                }

        );
        //Sets the new label for a cell
        graph.addListener(mxEvent.LABEL_CHANGED, (sender, evt) -> {
                    mxICell vertex =(mxICell) evt.getProperties().get("vertex");
                }

        );
        mainPane.add(graphComponent);
        setEdgeStyle();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initComponents() {

        getContentPane().setLayout(new BorderLayout());
        mainPane = new JPanel();
        mainPane.setBackground(c3);
        panelComp = new JPanel();
        panelComp.setLayout(null);
        panelComp.setBackground(GREY);
        getContentPane().add(panelComp, BorderLayout.CENTER);
        getContentPane().add(mainPane, BorderLayout.EAST);
        //redo = new LinkedList<Object>();
        //BUTTONS
        Font boldFont = new Font("SansSerif", Font.BOLD, 16);
        //FOR ADDING A VERTEX
        addVertexBtn = new JButton("Add Node");
        addVertexBtn.setBounds(40,105,100, 40);
        addVertexBtn.setBackground(c);
        addVertexBtn.setForeground(Color.gray);
        addVertexBtn.setFont(boldFont);
        addVertexBtn.setBorder(BorderFactory.createEmptyBorder());
        addVertexBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                addvertex();
            }
        });

        //FOR REMOVING THE WHOLE GRAPH
        removeGraphBtn = new JButton("Clear");
        removeGraphBtn.setBounds(230,105,100,40);
        removeGraphBtn.setBackground(c);
        removeGraphBtn.setForeground(Color.gray);
        removeGraphBtn.setFont(boldFont);
        removeGraphBtn.setBorder(BorderFactory.createEmptyBorder());
        removeGraphBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
                graph.getModel().beginUpdate();
                graph.getModel().endUpdate();
                addvertex();
            }
        });

        //FOR STARTING CALCULATION
        startBtn = new JButton("Calculate");
        startBtn.setBounds(40,250,100,40);
        startBtn.setBackground(c);
        startBtn.setForeground(Color.gray);
        startBtn.setFont(boldFont);
        startBtn.setBorder(BorderFactory.createEmptyBorder());
        startBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(checkEdgesValue() && checkNodesNames() && textExists()){
                    //startCalculation();
                }
            }
        });
       ///////////////////////////////////////////EXit//////////////////////////////////////
        boldFont = new Font("SansSerif", Font.BOLD, 14);

        //FOR EXIT BUTTON:
        JButton exitBtn = new JButton("Exit");
        exitBtn.setBounds(360,33,80,30);
        exitBtn.setBackground(c2);
        exitBtn.setForeground(Color.BLACK);
        exitBtn.setFont(boldFont);
        exitBtn.setBorder(BorderFactory.createEmptyBorder());
        exitBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        ///////////////////////////////////*****NOT  YET******/////////////////////////
        //FOR warning label
        WarningLabel=new JLabel();
        WarningLabel.setBounds(30,260,350,50);
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
        boldFont = new Font("Monospaced",  Font.BOLD, 30);
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
        display.setBackground(Color.white);
        boldFont = new Font("SansSerif", Font.BOLD, 17);
        display.setForeground(c3);
        display.setFont(boldFont);
        display.setBorder(new EmptyBorder(50,20,0,0));//top,left,bottom,right
        JScrollPane scroll = new JScrollPane(display);
        scroll.setBounds(20, 350, 400, 430);
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
        txt = new JTextField();
        txt.setBounds(230, 200, 150, 25);
        txt.setFont(boldFont);
////////////////////////////////////////////////////////////
        panelComp.add(txtlbl);
        panelComp.add(txt);
        panelComp.add(scroll);
        //panelComp.add(userGuide);
        panelComp.add(lbl);
        panelComp.add(lb);
        panelComp.add(exitBtn);
        //panelComp.add(redoBtn);
        //panelComp.add(undoBtn);
        panelComp.add(addVertexBtn);
        //ana 3mlt comment
        panelComp.add(startBtn);
        panelComp.add(removeGraphBtn);
        panelComp.add(WarningLabel);
    }
    //////////////////////////////////////////////////VERTEX////////////////////////////////////////////
    public void resetGraph(){
        //code here
        graph.refresh();
    }
    public void addvertex(){
        //30 30
        Object v = graph.insertVertex(graph.getDefaultParent(), null,"Text" , 30, 30, 55, 55);
        mxICell ver = (mxICell) v;
        setVertexStyle(ver, "#33ccff");
        resetGraph();
    }
    //STYLES FOR VERTICES
    private void setVertexStyle(final mxICell vertex, final String colorstr) {
        String targetStyle = vertex.getStyle();
        targetStyle = mxStyleUtils.removeAllStylenames(targetStyle);
        targetStyle = mxStyleUtils.setStyle(targetStyle , mxConstants.STYLE_STROKECOLOR, "black" );
        targetStyle = mxStyleUtils.setStyle(targetStyle, mxConstants.STYLE_FILLCOLOR, colorstr);
        targetStyle = mxStyleUtils.setStyle(targetStyle, mxConstants.STYLE_FONTCOLOR, "black");
        targetStyle = mxStyleUtils.setStyle(targetStyle, mxConstants.STYLE_FONTSIZE, "10");
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



/////////////////////////////NOT Yet Checks//////////////////////////////////////////
    public boolean checkEdgesValue(){
        Object[] list = graph.getChildEdges(graph.getDefaultParent());
        for(int i=0; i< list.length; i++){
            if(!isInteger((String) graph.getModel().getValue(list[i]),10)){
                WarningLabel.setForeground(new Color(225, 5, 108));
                return false;
            }
        }
        WarningLabel.setForeground(GREY);
        return true;
    }


    public  boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    public boolean checkNodesNames(){
        Object[] list = graph.getChildVertices(graph.getDefaultParent());
        for(int i=0;i<list.length-1;i++){
            String first=(String)((mxICell)list[i]).getValue();
            for (int j=i+1;j<list.length;j++){
                String second =(String)((mxICell)list[j]).getValue();
                if(first.equalsIgnoreCase(second)){
                    WarningLabel.setForeground(new Color(225, 5, 108));
                    return false;
                }
            }
        }
        return true;
    }
    public boolean textExists()
    {
        Object[] list = graph.getChildVertices(graph.getDefaultParent());
        for(int i=0; i<list.length; i++)
        {
            mxICell ver = (mxICell) list[i];
            String name = (String) ver.getValue();
            if((txt.getText()).equals(name))
            {
                WarningLabel.setForeground(GREY);
                return true;
            }
        }
        WarningLabel.setForeground(new Color(225, 5, 108));
        return false;
    }
}

