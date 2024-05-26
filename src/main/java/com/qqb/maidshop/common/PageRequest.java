package com.qqb.maidshop.common;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class PageRequest {

    private int pageNum = 1;

    private int pageSize = 5;
}
