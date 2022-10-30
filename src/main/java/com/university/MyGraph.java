package com.university;


import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import lombok.SneakyThrows;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyGraph {
    @SneakyThrows
    public static void main(String[] args) {
        DefaultDirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);

        String x1 = "x1";
        String x2 = "x2";
        String x3 = "x3";

        g.addVertex(x1);
        g.addVertex(x2);
        g.addVertex(x3);

        DefaultEdge edge = g.addEdge(x1, x2);
        g.addEdge(x2, x3);
        g.addEdge(x3, x1);

        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(g);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());


        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("src/main/resources/graph.png");
        ImageIO.write(image, "PNG", imgFile);

        int VERTEX_SIZE = 7;
        int[][] graph = {
                {1, 1, 1, 0, 0, 0, 1},
                {1, 1, 1, 1, 0, 0, 0},
                {1, 1, 1, 1, 0, 1, 1},
                {0, 1, 1, 1, 1, 0, 1},
                {0, 0, 0, 1, 1, 1, 0},
                {0, 0, 1, 0, 1, 1, 0},
                {1, 0, 1, 1, 0, 0, 1}};
        Map<Integer, Color> vertexColor = new HashMap<>();
        Map<Color, ArrayList<Integer>> coloredVertex = new HashMap<>();
        for (int i = 0; i < VERTEX_SIZE; i++) {
            if (vertexColor.get(i) != null) {
                break;
            }
            Color color = getRandomColor();
            vertexColor.put(i, color);
            for (int j = 0; j < VERTEX_SIZE; j++) {
                if (graph[i][j] == 0 && graph[j][i] == 0 && vertexColor.get(j) == null) {
                    vertexColor.put(j, color);
                    for (int k = 0; k < VERTEX_SIZE; k++) {
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
    }

    private static Color getRandomColor() {
        return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }
}