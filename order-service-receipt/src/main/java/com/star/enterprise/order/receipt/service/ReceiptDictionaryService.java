package com.star.enterprise.order.receipt.service;

import com.star.enterprise.order.base.utils.EnumSerializeUtils;
import com.star.enterprise.order.receipt.constants.ReceiptActionEnum;
import com.star.enterprise.order.receipt.constants.ReceiptStatusEnum;
import com.star.enterprise.order.receipt.constants.ReceiptTypeEnum;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2022/12/21
 */
@Service
public class ReceiptDictionaryService {

    public Map<String, String> buildReceiptTypeDictionary() {
        return EnumSerializeUtils.toMap(ReceiptTypeEnum.values());
    }


    public Map<String, String> buildReceiptStatusDictionary() {
        return EnumSerializeUtils.toMap(ReceiptStatusEnum.values());
    }


    public Map<String, String> buildReceiptActionDictionary() {
        return EnumSerializeUtils.toMap(ReceiptActionEnum.values());
    }

}
