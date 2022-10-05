package com.rafaelhosaka.shareme.visibility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Visibility {
    private VisibilityType type;
    private List<String> allowedIds;
}
