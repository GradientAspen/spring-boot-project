package com.project.springbootproject.dto;

import java.math.BigDecimal;

public record BookSearchParameters(String[] authors, String[] description) {
}
