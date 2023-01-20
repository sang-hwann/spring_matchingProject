package com.project.matchingsystem.domain;

public enum ItemTransactionStatusEnum {

    FOR_SALE{
        @Override
        public String toString() {
            return "FOR_SALE";
        }
    },
    SOLD_OUT{
        @Override
        public String toString() {
            return "SOLD_OUT";
        }
    };

}
