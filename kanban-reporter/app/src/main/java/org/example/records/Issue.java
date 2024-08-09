package org.example.records;

import java.util.ArrayList;

public record Issue(
    String id,
    String title,
    String start,
    String end,
    ArrayList<String> assignees
) {}
