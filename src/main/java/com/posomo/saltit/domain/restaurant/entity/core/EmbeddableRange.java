package com.posomo.saltit.domain.restaurant.entity.core;

import jakarta.persistence.Embeddable;

@Embeddable
public class EmbeddableRange<T> {
    private T from;
    private T to;
}
