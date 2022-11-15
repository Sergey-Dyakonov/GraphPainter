package com.university.controller;

import com.university.model.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleGraphController implements GraphController {
    private Map<Integer, Color> vertexColor = new HashMap<>();
    private Map<Color, ArrayList<Integer>> coloredVertex = new HashMap<>();
    private int[][] initialMatrix;

    @Override
    public int[][] getFirstGraphMatrix() {
        return new int[][]{
                {1, 1, 1, 0, 0, 0, 1},
                {1, 1, 1, 1, 0, 0, 0},
                {1, 1, 1, 1, 0, 1, 1},
                {0, 1, 1, 1, 1, 0, 1},
                {0, 0, 0, 1, 1, 1, 0},
                {0, 0, 1, 0, 1, 1, 0},
                {1, 0, 1, 1, 0, 0, 1}};
    }

    @Override
    public int[][] getSecondGraphMatrix() {
        return new int[][]{
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
    }

    @Override
    public void processMatrix(int[][] matrix) {
        initialMatrix = matrix.clone();
        vertexColor = new HashMap<>();
        coloredVertex = new HashMap<>();

        for (int i = 0; i < matrix.length; i++) {
            if (vertexColor.get(i) != null) {
                continue;
            }
            Color color = getRandomColor();
            vertexColor.put(i, color);
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == 0 && vertexColor.get(j) == null) {
                    vertexColor.put(j, color);
                    for (int k = 0; k < matrix.length; k++) {
                        if (matrix[j][k] == 1) {
                            matrix[i][k] = matrix[j][k];
                        }
                    }
                }
            }
        }

        vertexColor.forEach((vertex, color) -> {
            coloredVertex.putIfAbsent(color, new ArrayList<>());
            coloredVertex.get(color).add(vertex);
        });
    }

    public Graph<Integer, String> getGraphModel() {
        Graph<Integer, String> g = new SparseMultigraph<>();

        vertexColor.forEach((vertex, color) -> {
            g.addVertex(vertex);
        });

        matrixToEdges(initialMatrix)
                .forEach(edge -> g.addEdge(edge.toString(), edge.getStartVertex(), edge.getEndVertex()));
        return g;
    }

    @Override
    public Map<Integer, Color> getVertexColors() {
        return vertexColor;
    }

    @Override
    public int getColorsNumber() {
        return coloredVertex.size();
    }

    private Color getRandomColor() {
        return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    private List<Edge> matrixToEdges(int[][] graph) {
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (i != j && graph[i][j] != 0 && graph[j][i] != 0 && !edges.contains(new Edge(i, j))) {
                    edges.add(new Edge(i, j));
                }
            }
        }
        return edges;
    }
}
