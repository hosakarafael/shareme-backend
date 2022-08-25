package com.rafaelhosaka.shareme.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguagePreference {
    private String shortName;
    private String longName;
}
