package com.manson.fo76.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ItemContext {

    private boolean priceCheck = false;
    private boolean shortenResponse = false;
    private boolean fed76Enhance = false;
}
