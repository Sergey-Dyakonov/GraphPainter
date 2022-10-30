package com.university.view;

import com.university.controller.GraphController;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UI extends JFrame {
    private final static int WIDTH = 800;
    private final static int HEIGHT = 600;
    private JButton paintButton;
    private JSpinner verticesNumSpinner;
    private JPanel mainPanel;
    private JLabel colorsNumLabel;
    private JLabel colorsNumber;
    private JLabel enterGraphLabel;
    private JLabel spinnerLabel;
    private JPanel paintGraphPanel;
    private JPanel paintPanel;
    private JPanel actionPanel;
    private JScrollPane scrollPane;
    private JPanel innerScrollPanel;
    private JPanel panel1;
    private JPanel bigPanel;
    private JButton resetButton;
    private JButton ex1Btn;
    private JButton ex2Btn;
    private GraphController controller;
    private List<List<JTextField>> vertices;

    public UI(GraphController graphController) {
        controller = graphController;
        setContentPane(mainPanel);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
        }
        paintButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int[][] matrix = new int[vertices.size()][vertices.size()];
                for (int i = 0; i < vertices.size(); i++) {
                    List<JTextField> fields = vertices.get(i);
                    for (int j = 0; j < vertices.size(); j++) {
                        matrix[i][j] = Integer.parseInt(fields.get(j).getText());
                    }
                }
                controller.processMatrix(matrix);
                paintColoredGraph(controller.getGraphModel(), controller.getVertexColors());
                colorsNumber.setText(String.valueOf(controller.getColorsNumber()));
            }
        });
        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                vertices = new ArrayList<>();
                innerScrollPanel.removeAll();
                paintPanel.removeAll();
                colorsNumber.setText("");
                verticesNumSpinner.setValue(0);
                revalidate();
                repaint();
            }
        });
        verticesNumSpinner.addChangeListener(e -> {
            innerScrollPanel.removeAll();
            vertices = new ArrayList<>();
            int verticesNumber = (int) verticesNumSpinner.getValue();
            for (int i = 0; i < verticesNumber; i++) {
                JPanel panelWithRow = getConfiguredTextPanel();
                List<JTextField> row = new ArrayList<>();
                for (int j = 0; j < verticesNumber; j++) {
                    JTextField field = getConfiguredTextFiled();
                    panelWithRow.add(field);
                    row.add(field);
                }
                vertices.add(row);
                innerScrollPanel.add(panelWithRow);
            }
            revalidate();
        });
        ex1Btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int[][] matrix = controller.getFirstGraphMatrix();
                verticesNumSpinner.setValue(matrix.length);
                fillFieldsWithMatrix(matrix);
            }
        });
        ex2Btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int[][] matrix = controller.getSecondGraphMatrix();
                verticesNumSpinner.setValue(matrix.length);
                fillFieldsWithMatrix(matrix);
            }
        });
    }

    public void showMainFrame() {
        this.setTitle("Graph vertices painter");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setVisible(true);
    }

    private void createUIComponents() {
        innerScrollPanel = new JPanel(new GridLayout(0, 1));
        scrollPane = new JScrollPane(innerScrollPanel);

        paintPanel = new JPanel(new BorderLayout());
        paintPanel.setBorder(new EtchedBorder());
    }

    private JTextField getConfiguredTextFiled() {
        JTextField field = new JTextField();
        field.setHorizontalAlignment(SwingConstants.CENTER);
        field.setPreferredSize(new Dimension(20, 20));
        field.setMinimumSize(new Dimension(20, 20));
        field.setFont(new Font("Arial", Font.BOLD, 16));
        return field;
    }

    private JPanel getConfiguredTextPanel() {
        JPanel panelWithRow = new JPanel();
        panelWithRow.setBorder(new EtchedBorder());
        panelWithRow.setPreferredSize(new Dimension(60, 30));
        panelWithRow.setMinimumSize(new Dimension(60, 30));
        panelWithRow.setLayout(new GridLayout(1, 0));
        return panelWithRow;
    }

    private void fillFieldsWithMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                vertices.get(i).get(j).setText(String.valueOf(matrix[i][j]));
            }
        }
    }

    private void paintColoredGraph(Graph<Integer, String> g, Map<Integer, Color> vertexColor) {
        CircleLayout<Integer, String> layout = new CircleLayout<>(g);
        layout.setSize(new Dimension(300, 300));
        BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<>(layout);
        vv.setPreferredSize(new Dimension(400, 300));
        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor::get);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        paintPanel.add(vv);
    }

}
