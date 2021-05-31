package com.ian.payment.payoneer.network;

import com.ian.payment.payoneer.model.ListResult;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface  PaymentMethodApiService {

    @GET("listresult.json")
    Observable<ListResult> getPayMethods();
}
