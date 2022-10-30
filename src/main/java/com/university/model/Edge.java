package com.university.model;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Edge {
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