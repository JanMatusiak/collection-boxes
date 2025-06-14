package com.sii.collection_boxes.dto;

import com.sii.collection_boxes.entity.CollectionBox;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class CollectionBoxesStateDTO {
    private boolean empty;
    private boolean assigned;

    public CollectionBoxesStateDTO(){}

    public CollectionBoxesStateDTO(boolean empty, boolean assigned){
        this.empty = empty;
        this.assigned = assigned;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public static List<CollectionBoxesStateDTO> toDTO(@NotNull List<CollectionBox> boxes){
        return boxes.stream()
                .map(box -> new CollectionBoxesStateDTO(box.isEmpty(), box.isAssigned()))
                .collect(Collectors.toList());
    }
}
