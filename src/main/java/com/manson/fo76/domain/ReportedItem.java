package com.manson.fo76.domain;

import com.manson.domain.itemextractor.ItemResponse;
import java.util.Date;
import lombok.Data;

@Data
public class ReportedItem {

    private ItemResponse item;
    private String reason;
    private Date date = new Date();

}
