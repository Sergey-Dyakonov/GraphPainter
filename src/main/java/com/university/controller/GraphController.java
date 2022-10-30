package com.university.controller;

import edu.uci.ics.jung.graph.Graph;

import java.awt.*;
import java.util.Map;

public interface GraphController {
    int[][] getFirstGraphMatrix();

    int[][] getSecondGraphMatrix();

    void processMatrix(int[][] matrix);

    Graph<Integer, String> getGraphModel();

    Map<Integer, Color> getVertexColors();

    int getColorsNumber();
}
