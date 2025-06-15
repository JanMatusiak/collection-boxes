package com.sii.collection_boxes.dto;

import com.sii.collection_boxes.entity.CollectionBox;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public record CollectionBoxesStateDTO(boolean empty, boolean assigned) {
    public static List<CollectionBoxesStateDTO> toDTO(@NotNull List<CollectionBox> boxes){
        return boxes.stream()
                .map(box -> new CollectionBoxesStateDTO(box.isEmpty(), box.isAssigned()))
                .collect(Collectors.toList());
    }
}
