package com.university;

import com.google.common.base.Objects;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyGraph {

    public static void main(String[] args) {
        int[][] graph = {
                {1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
                {1, 1, 0, 0, 1, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 0, 0, 0, 0, 0},
                {1, 0, 0, 1, 1, 1, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 0, 1, 0},
                {0, 0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 0, 0, 1},
                {0, 0, 0, 0, 0, 1, 0, 1, 1, 1},
                {0, 0, 0, 0, 1, 0, 0, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 1, 1, 1, 1}};
        int[][] initialGraph = graph.clone();
        Map<Integer, Color> vertexColor = new HashMap<>();
        Map<Color, ArrayList<Integer>> coloredVertex = new HashMap<>();
        for (int i = 0; i < graph.length; i++) {
            if (vertexColor.get(i) != null) {
                break;
            }
            Color color = getRandomColor();
            vertexColor.put(i, color);
            for (int j = 0; j < graph.length; j++) {
                if (graph[i][j] == 0 && j != i + 1 && vertexColor.get(j) == null) {
                    vertexColor.put(j, color);
                    for (int k = 0; k < graph.length; k++) {
                        if (graph[j][k] == 1) {
                            graph[i][k] = graph[j][k];
                        }
                    }
                }
            }
        }
        vertexColor.forEach((vertex, color) -> {
            coloredVertex.putIfAbsent(color, new ArrayList<>());
            coloredVertex.get(color).add(vertex);
        });
        paintColoredGraph(vertexColor, initialGraph);
    }

    private static void paintColoredGraph(Map<Integer, Color> vertexColor, int[][] graph) {
        Graph<Integer, String> g = new SparseMultigraph<>();

        vertexColor.forEach((vertex, color) -> {
            g.addVertex(vertex);
        });

        List<Edge> edges = new ArrayList<>();
        matrixToEdges(graph, edges);
        edges.forEach(edge -> g.addEdge(edge.toString(), edge.getStartVertex(), edge.getEndVertex()));
        CircleLayout<Integer, String> layout = new CircleLayout<>(g);
        layout.setSize(new Dimension(300, 300));
        BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<>(layout);
        vv.setPreferredSize(new Dimension(400, 300));
        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor::get);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }

    private static void matrixToEdges(int[][] graph, List<Edge> edges) {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (i != j && graph[i][j] != 0 && graph[j][i] != 0 && !edges.contains(new Edge(i, j))) {
                    edges.add(new Edge(i, j));
                }
            }
        }
    }

    private static Color getRandomColor() {
        return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class Edge {
        private int startVertex;
        private int endVertex;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return startVertex == edge.startVertex && endVertex == edge.endVertex ||
                    startVertex == edge.endVertex && endVertex == edge.startVertex;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(startVertex, endVertex);
        }
    }
}