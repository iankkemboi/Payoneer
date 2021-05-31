package com.ian.payment.payoneer.repository;

import com.ian.payment.payoneer.model.ListResult;
import com.ian.payment.payoneer.network.PaymentMethodApiService;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class PayMethodsRepo {

    private final PaymentMethodApiService apiService;

    @Inject
    public PayMethodsRepo( PaymentMethodApiService apiService) {

        this.apiService = apiService;
    }


    public Observable<ListResult> getPayMethods(){
        return apiService.getPayMethods();
    }

}
