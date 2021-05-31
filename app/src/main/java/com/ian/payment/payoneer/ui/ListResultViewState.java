package com.ian.payment.payoneer.ui;


import com.ian.payment.payoneer.model.ListResult;

public class ListResultViewState extends BaseViewState<ListResult> {
    private ListResultViewState(ListResult data, int currentState, Throwable error) {
        this.data = data;
        this.error = error;
        this.currentState = currentState;
    }

    public static ListResultViewState ERROR_STATE = new ListResultViewState(null, State.FAILED.value, new Throwable());
    public static ListResultViewState LOADING_STATE = new ListResultViewState(null, State.LOADING.value, null);
    public static ListResultViewState EMPTY_STATE = new ListResultViewState(null, State.EMPTY.value, null);
    public static ListResultViewState SUCCESS_STATE = new ListResultViewState(new ListResult(), State.SUCCESS.value, null);

}

